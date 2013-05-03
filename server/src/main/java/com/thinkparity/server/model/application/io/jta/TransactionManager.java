/*
 * Created On:  7-Aug-07 2:04:06 PM
 */
package com.thinkparity.desdemona.model.io.jta;

import javax.naming.NamingException;
import javax.sql.DataSource;

import com.thinkparity.codebase.model.util.jta.Transaction;

import org.enhydra.jdbc.pool.StandardXAPoolDataSource;
import org.objectweb.carol.util.configuration.ConfigurationException;

/**
 * <b>Title:</b>thinkParity Desdemona Model Transaction Manager<br>
 * <b>Description:</b>A transaction manager.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class TransactionManager {

    /**
     * Obtain the transaction manager.
     * 
     * @return A <code>TransactionManager</code>.
     */
    public static TransactionManager getInstance() {
        return TransactionService.getInstance().getTransactionManager();
    }

    /**
     * Obtain the transaction service.
     * @return
     */
    private static TransactionService getService() {
        return TransactionService.getInstance();
    }

    /** A transaction manager. */
    private final com.thinkparity.codebase.model.util.jta.TransactionManager manager;

    /**
     * Create TransactionManager.
     *
     */
    TransactionManager() {
        super();
        this.manager =
            com.thinkparity.codebase.model.util.jta.TransactionManager.getInstance();
    }

    /**
     * Obtain a data source.
     * 
     * @return A <code>DataSource</code>.
     */
    public DataSource getDataSource() {
        return getService().getDataSource();
    }

    /**
     * Obtain a transaction.
     * 
     * @return A <code>Transaction</code>.
     */
    public Transaction getTransaction() {
        return manager.getTransaction();
    }

    /**
     * Bind the datasource to the manager.
     * 
     * @param dataSource
     *            A <code>StandartXAPoolDataSource</code>.
     */
    void bind(final StandardXAPoolDataSource dataSource) {
        manager.bind(dataSource);
    }

    /**
     * Start the manager.
     * 
     * @throws ConfigurationException
     * @throws NamingException
     */
    void start() throws ConfigurationException, NamingException {
        manager.start();
    }

    /**
     * Stop the manager.
     * 
     */
    void stop() {
        manager.stop();
    }
}
