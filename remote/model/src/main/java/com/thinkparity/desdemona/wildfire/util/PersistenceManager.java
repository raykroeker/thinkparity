/*
 * Created On:  26-Mar-07 7:21:37 PM
 */
package com.thinkparity.desdemona.wildfire.util;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.util.xapool.XADataSource;
import com.thinkparity.codebase.model.util.xapool.XADataSourceConfiguration;
import com.thinkparity.codebase.model.util.xapool.XADataSourcePool;
import com.thinkparity.codebase.model.util.xapool.XADataSourceConfiguration.Key;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSessionManager;

/**
 * <b>Title:</b>thinkParity Desdemona Persistence Manager<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class PersistenceManager {

    /** A singleton instance of <code>PersistenceManager</code>. */
    private static PersistenceManager INSTANCE;

    /**
     * Obtain a persistence manager.
     * 
     * @return An instance of <code>PersistenceManager</code>.
     */
    public static PersistenceManager getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new PersistenceManager();
        }
        return INSTANCE;
    }

    /** The <code>DataSource</code>. */
    private DataSource dataSource;

    /** A <code>Log4JWrapper</code>. */
    private final Log4JWrapper logger;

    /** A <code>HypersonicSessionManager</code>. */
    private HypersonicSessionManager sessionManager;

    /**
     * Create PersistenceManager.
     *
     */
    private PersistenceManager() {
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
     * Start the persistence manager.
     * 
     */
    public void start() {
        final XADataSourceConfiguration conf = new XADataSourceConfiguration();
        conf.setProperty(Key.DRIVER, "org.apache.derby.jdbc.ClientDriver");
        conf.setProperty(Key.PASSWORD, System.getProperty("thinkparity.datasource-password")); 
        conf.setProperty(Key.URL, System.getProperty("thinkparity.datasource-url")); 
        conf.setProperty(Key.USER, System.getProperty("thinkparity.datasource-user"));
        try {
            dataSource = new XADataSourcePool(new XADataSource(conf));
            sessionManager = new HypersonicSessionManager(dataSource);
        } catch (final SQLException sqlx) {
            throw new ThinkParityException("Could not start persistence manager.", sqlx);
        }
    }

    /**
     * Stop the persistence manager.
     *
     */
    public void stop() {
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
        } catch (final Throwable t) {
            throw new ThinkParityException("Cannot stop persistence manager.", t);
        }
    }
}
