/*
 * Created On:  26-Mar-07 7:21:37 PM
 */
package com.thinkparity.desdemona.service.persistence;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

import javax.sql.DataSource;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.util.xapool.XADataSource;
import com.thinkparity.codebase.model.util.xapool.XADataSourceConfiguration;
import com.thinkparity.codebase.model.util.xapool.XADataSourcePool;
import com.thinkparity.codebase.model.util.xapool.XADataSourceConfiguration.Key;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSessionManager;

import com.thinkparity.desdemona.service.Service;

import com.thinkparity.desdemona.util.DateTimeProvider;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * <b>Title:</b>thinkParity Desdemona Persistence Manager<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class PersistenceService extends Service {

    /** A default archive schedule. */
    private static final Long DEFAULT_SCHEDULE;

    /** A singleton instance of <code>PersistenceManager</code>. */
    private static final PersistenceService SINGLETON;

    static {
        DEFAULT_SCHEDULE = 20L * 60L * 60L * 1000L; // SCHEDULE 20:00
        SINGLETON = new PersistenceService();
    }

    /**
     * Obtain a persistence manager.
     * 
     * @return An instance of <code>PersistenceManager</code>.
     */
    public static PersistenceService getInstance() {
        return SINGLETON;
    }

    /** The <code>DataSource</code>. */
    private DataSource dataSource;

    /** A data source. */
    private DataSource dataSource2;

    /** An derby archiver. */
    private DerbyArchiver derbyArchiver;

    /** A <code>Log4JWrapper</code>. */
    private final Log4JWrapper logger;

    /** An operational data source. */
    private DataSource operationalDataSource;

    /** A set of properties. */
    private Properties properties;

    /** A <code>HypersonicSessionManager</code>. */
    private HypersonicSessionManager sessionManager;

    /**
     * Create PersistenceManager.
     *
     */
    private PersistenceService() {
        super();
        this.logger = new Log4JWrapper();
    }

    /**
     * Obtain a data source.
     * 
     * @return A <code>DataSource</code>.
     */
    public DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Obtain a data source.
     * 
     * @return A <code>DataSource</code>.
     */
    public DataSource getDataSource2() {
        return dataSource2;
    }

    /**
     * Obtain an operational data source.
     * 
     * @return A <code>DataSource</code>.
     */
    public DataSource getOperationalDataSource() {
        return operationalDataSource;
    }

    /**
     * Start the persistence service.
     * 
     */
    public void start(final Properties properties) {
        this.properties = properties;

        derbyArchiver = new DerbyArchiver();
        startArchiveDelegate();

        final XADataSourceConfiguration conf = new XADataSourceConfiguration();
        conf.setProperty(Key.DRIVER, System.getProperty("thinkparity.app.datasource-driver"));
        conf.setProperty(Key.PASSWORD, System.getProperty("thinkparity.app.datasource-password")); 
        conf.setProperty(Key.URL, System.getProperty("thinkparity.app.datasource-url")); 
        conf.setProperty(Key.USER, System.getProperty("thinkparity.app.datasource-user"));
        try {
            dataSource = new XADataSourcePool(new XADataSource(conf));
            sessionManager = new HypersonicSessionManager(dataSource, Boolean.TRUE);
        } catch (final SQLException sqlx) {
            throw new ThinkParityException("Could not start persistence manager.", sqlx);
        }
        try {
            final HypersonicSession session = sessionManager.openSession();
            session.close();
        } catch (final HypersonicException hx) {
            throw new ThinkParityException("Database session could not be opened.", hx);
        }

        dataSource2 = newPooledDataSource();
        operationalDataSource = newOperationalDataSource();
    }

    /**
     * Stop the persistence manager.
     *
     */
    public void stop() {
        try {
            derbyArchiver.stop();
            derbyArchiver = null;

            final List<HypersonicSession> sessions = sessionManager.getSessions();
            if (0 < sessions.size())
                logger.logWarning("{0} abandoned database sessions.", sessions.size());
            for (final HypersonicSession session : sessions) {
                logger.logWarning("Session abandoned by {0}.", sessionManager.getSessionCaller(session));
                session.close();
            }
    
            ((XADataSourcePool) dataSource).shutdown(true);
            try {
                ((XADataSourcePool) dataSource).getShutdownConnection();
            } catch (final SQLException sqlx) {
                // the sqlx is thrown deliberately by derby on shutdown
            } catch (final Throwable t) {
                logger.logError(t, "An error occured stopping persistence.");
            }

            try {
                ((BasicDataSource) dataSource2).close();
            } catch (final SQLException sqlx) {
                logger.logError(sqlx, "Could not shut down data source pool.");
            }

            try {
                ((BasicDataSource) operationalDataSource).close();
            } catch (final SQLException sqlx) {
                logger.logError(sqlx, "Could not shut down operational data source pool.");
            }
        } finally {
            this.properties = null;
            this.dataSource = operationalDataSource = null;
            this.sessionManager = null;
        }
    }

    /**
     * Obtain the sleep property.
     * 
     * @param key
     *            A <code>String</code>.
     * @return A <code>Long</code>.
     */
    private List<Long> getSchedule(final String key) {
        final String tokens = properties.getProperty(key, String.valueOf(DEFAULT_SCHEDULE));
        final StringTokenizer tokenizer = new StringTokenizer(tokens, ",");
        final List<Long> schedule = new ArrayList<Long>(tokenizer.countTokens());
        while (tokenizer.hasMoreTokens()) {
            try {
                schedule.add(Long.valueOf(tokenizer.nextToken()));
            } catch (final NumberFormatException nfx) {}
        }
        if (schedule.isEmpty()) {
            schedule.add(Long.valueOf(DEFAULT_SCHEDULE));
        }
        return schedule;
    }

    /**
     * Instantiate a operational pooling source.
     * 
     * @return A <code>DataSource</code>.
     */
    private DataSource newOperationalDataSource() {
        final BasicDataSource bds = new BasicDataSource();
        bds.setDefaultAutoCommit(false);
        bds.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        bds.setDriverClassName(properties.getProperty("thinkparity.ops.datasource.driver"));
        bds.setInitialSize(Integer.valueOf(properties.getProperty("thinkparity.ops.datasource.poolinitialsize")));
        bds.setMaxActive(Integer.valueOf(properties.getProperty("thinkparity.ops.datasource.poolmaxactive")));
        bds.setPoolPreparedStatements(Boolean.valueOf(properties.getProperty("thinkparity.ops.datasource.poolpreparedstatements")));
        bds.setUrl(properties.getProperty("thinkparity.ops.datasource.url"));
        bds.setUsername(properties.getProperty("thinkparity.ops.datasource.user"));
        bds.setPassword(properties.getProperty("thinkparity.ops.datasource.password"));
        return bds;
    }

    /**
     * Instantiate an application pooling data source.
     * 
     * @return A <code>DataSource</code>.
     */
    private DataSource newPooledDataSource() {
        final BasicDataSource bds = new BasicDataSource();
        bds.setDefaultAutoCommit(false);
        bds.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        bds.setDriverClassName(properties.getProperty("thinkparity.app.datasource-driver"));
        bds.setInitialSize(Integer.valueOf(properties.getProperty("thinkparity.app.datasource-poolinitialsize")));
        bds.setMaxActive(Integer.valueOf(properties.getProperty("thinkparity.app.datasource-poolmaxactive")));
        bds.setPoolPreparedStatements(Boolean.valueOf(properties.getProperty("thinkparity.app.datasource.poolpreparedstatements")));
        bds.setUrl(properties.getProperty("thinkparity.app.datasource-url"));
        bds.setUsername(properties.getProperty("thinkparity.app.datasource-user"));
        bds.setPassword(properties.getProperty("thinkparity.app.datasource-password"));
        return bds;
    }

    /**
     * Instantiate a new timer task.
     * 
     * @return A <code>TimerTask</code>.
     */
    private TimerTask newTimerTask() {
        return new TimerTask() {
            /**
             * @see java.util.TimerTask#run()
             *
             */
            @Override
            public void run() {
                derbyArchiver.run();
            }
        };
    }

    /**
     * Start the archive delegate.
     * 
     */
    private void startArchiveDelegate() {
        final Timer timer = new Timer("TPS-DesdemonaModel-DerbyArchiver", true);
        final List<Long> schedule = getSchedule("thinkparity.persistence.derbyarchiverschedule");
        final Calendar midnight = DateTimeProvider.getCurrentDateTime();
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        final long midnightInMillis = midnight.getTimeInMillis();
        final long period = 24L * 60L * 60L * 1000L;
        long currentTimeInMillis;
        for (final long scheduleInMillis : schedule) {
            currentTimeInMillis = System.currentTimeMillis();
            if ((currentTimeInMillis - midnightInMillis) < scheduleInMillis) {
                /* schedule the task */
                timer.scheduleAtFixedRate(newTimerTask(),
                        scheduleInMillis - (currentTimeInMillis - midnightInMillis),
                        period);
            } else {
                /* schedule the task plus 24 hours */
                timer.scheduleAtFixedRate(newTimerTask(),
                        (scheduleInMillis - (currentTimeInMillis - midnightInMillis)) + period,
                        period);
            }
        }
    }
}
