/*
 * Created On:  7-Aug-07 2:04:06 PM
 */
package com.thinkparity.desdemona.model.io.jta;

import java.sql.SQLException;

import javax.naming.NamingException;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.util.xapool.XADataSource;
import com.thinkparity.codebase.model.util.xapool.XADataSourceConfiguration;
import com.thinkparity.codebase.model.util.xapool.XADataSourcePool;
import com.thinkparity.codebase.model.util.xapool.XADataSourceConfiguration.Key;

import org.objectweb.carol.util.configuration.ConfigurationException;

/**
 * <b>Title:</b>thinkParity Desdemona Model Transaction Service<br>
 * <b>Description:</b>A transaction service.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class TransactionService {

    /** A singleton instance. */
    private static final TransactionService SINGLETON;

    static {
        SINGLETON = new TransactionService();
    }

    /**
     * Obtain an instance of the transaction service.
     * 
     * @return A <code>TransactionService</code>.
     */
    public static TransactionService getInstance() {
        return SINGLETON;
    }

    /** A data source. */
    private javax.sql.DataSource dataSource;

    /** A transaction manager. */
    private final TransactionManager manager;

    /** A flag indicating whether we are running. */
    private boolean running;

    /**
     * Create TransactionManager.
     *
     */
    private TransactionService() {
        super();
        this.manager = new TransactionManager();
        this.running = false;
    }

    /**
     * Start the transaction service.
     * 
     */
    public void start() {
        synchronized (this) {
            startImpl();
        }
    }

    /**
     * Stop the transaction service.
     * 
     */
    public void stop() {
        synchronized (this) {
            stopImpl();
        }
    }

    /**
     * Obtain a transaction data source.
     * 
     * @return A <code>DataSource</code>.
     */
    javax.sql.DataSource getDataSource() {
        if (running) {
            return dataSource;
        } else {
            throw new IllegalStateException("Transaction service has not been started.");
        }
    }

    /**
     * Obtain a transaction manager.
     * 
     * @return A <code>TransactionManager</code>.
     */
    TransactionManager getTransactionManager() {
        if (running) {
            return manager;
        } else {
            throw new IllegalStateException("Transaction service is not running.");
        }
    }

    /**
     * Create a new data source pool for use within the transaction manager.
     * 
     * @return A <code>XADataSourcePool</code>.
     * @throws SQLException
     */
    private XADataSourcePool newDataSource() throws SQLException {
        final XADataSourceConfiguration xaDataSourceConfiguration = new XADataSourceConfiguration();
        xaDataSourceConfiguration.setProperty(Key.DRIVER, System.getProperty("thinkparity.datasource-driver"));
        xaDataSourceConfiguration.setProperty(Key.PASSWORD, System.getProperty("thinkparity.datasource-password")); 
        xaDataSourceConfiguration.setProperty(Key.URL, System.getProperty("thinkparity.datasource-url")); 
        xaDataSourceConfiguration.setProperty(Key.USER, System.getProperty("thinkparity.datasource-user"));
        return new XADataSourcePool(new XADataSource(xaDataSourceConfiguration));
    }

    /**
     * The start implementation.
     * 
     */
    private void startImpl() {
        if (running) {
            throw new IllegalStateException("Transaction service has already been started.");
        }
        try {
            manager.start();
            dataSource = newDataSource();
            manager.bind((XADataSourcePool) dataSource);
            running = true;
        } catch (final ConfigurationException cx) {
            throw new ThinkParityException(cx);
        } catch (final NamingException nx) {
            throw new ThinkParityException(nx);
        } catch (final SQLException sqlx) {
            throw new ThinkParityException(sqlx);
        }
    }

    /**
     * The stop implementation.
     * 
     */
    private void stopImpl() {
        if (running) {
            try {
                manager.stop();
            } finally {
                running = false;
            }
        } else {
            throw new IllegalStateException("Transaction service has not been started.");
        }
    }
}
