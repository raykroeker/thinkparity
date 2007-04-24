/*
 * Created On: Sep 11, 2006 7:41:22 PM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.util.jta.Transaction;
import com.thinkparity.codebase.model.util.jta.TransactionManager;
import com.thinkparity.codebase.model.util.xapool.XADataSource;
import com.thinkparity.codebase.model.util.xapool.XADataSourceConfiguration;
import com.thinkparity.codebase.model.util.xapool.XADataSourcePool;
import com.thinkparity.codebase.model.util.xapool.XADataSourceConfiguration.Key;

import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;
import com.thinkparity.ophelia.model.io.md.MetaDataType;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceException;

/**
 * <b>Title:</b>thinkParity OpheliaModel Persistence Manager Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class PersistenceManagerImpl {

    /** The user transaction timeout <code>int</code> in seconds. */
    private static final int XA_TIMEOUT;

    static {
        // TIMEOUT - PersistenceManagerImpl#<cinit> - 2H
        // TODO change the timeout of the transaction back to the default
        XA_TIMEOUT = 60 * 60 * 2;
    }

    /** A <code>DataSource</code>. */
    private DataSource dataSource;

    /** An apache logger. */
    private final Log4JWrapper logger;

    /** A persistence <code>SessionManager</code>. */
    private SessionManager sessionManager;

    /** A <code>TransactionManager</code>. */
    private TransactionManager transactionManager;

    /**
     * Create SessionManagerImpl.
     * 
     * @param workspace
     *            The <code>Workspace</code>.
     */
    PersistenceManagerImpl(final Workspace workspace) {
        super();
        this.logger = new Log4JWrapper();
    }

    /**
     * Initialize the persistence manager.
     *
     */
    void beginInitialize() {
        try {
            final Transaction transaction = beginTransaction();
            try {
                new PersistenceMigrator(sessionManager).migrate();
                transaction.commit();
            } catch (final Throwable t) {
                transaction.rollback();
                throw t;
            }
        } catch (final Throwable t) {
            throw new WorkspaceException("Cannot initialize persistence.", t);
        }
    }

    /**
     * Finish the initialization.
     *
     */
    void finishInitialize() {
        try {
            final Transaction transaction = beginTransaction();
            try {
                final Session session = sessionManager.openSession();
                try {
                    final String sql =
                        new StringBuffer("insert into META_DATA ")
                        .append("(META_DATA_TYPE_ID,META_DATA_KEY,META_DATA_VALUE) ")
                        .append("values(?,?,?)")
                        .toString();
                    session.prepareStatement(sql);
                    session.setTypeAsInteger(1, MetaDataType.BOOLEAN);
                    session.setString(2, "thinkparity.workspace-initialized");
                    session.setString(3, Boolean.TRUE.toString());
                    if (1 != session.executeUpdate())
                        throw new WorkspaceException("Cannot initialize persistence.");
                } finally {
                    session.close();
                }
                transaction.commit();
            } catch (final Throwable t) {
                transaction.rollback();
                throw t;
            }
        } catch (final Throwable t) {
            throw new WorkspaceException("Cannot initialize persistence.", t);
        }
    }

    /**
     * Obtain the data source.
     * 
     * @return The <code>DataSource</code>.
     */
    DataSource getDataSource() {
        return dataSource;
    }

    /**
     * Obtain a transaction.
     * 
     * @return A <code>Transaction</code>.
     */
    Transaction getTransaction() {
        final Transaction tx = transactionManager.getTransaction();
        try {
            tx.setTransactionTimeout(XA_TIMEOUT);
        } catch (final SystemException sx) {
            logger.logWarning(sx, "Could not set timeout for transaction.",
                    XA_TIMEOUT);
        }
        return tx;
    }

    /**
     * Open a database session.
     * 
     * @return A <code>Session</code>.
     */
    Session openSession() {
        return sessionManager.openSession();
    }

    /**
     * Start the persistence manager.
     *
     */
    void start() {
        try {
            // create datasource configuration
            final XADataSourceConfiguration xaDataSourceConfiguration = new XADataSourceConfiguration();
            xaDataSourceConfiguration.setProperty(Key.DRIVER, System.getProperty("thinkparity.datasource-driver"));
            xaDataSourceConfiguration.setProperty(Key.PASSWORD, System.getProperty("thinkparity.datasource-password")); 
            xaDataSourceConfiguration.setProperty(Key.URL, System.getProperty("thinkparity.datasource-url")); 
            xaDataSourceConfiguration.setProperty(Key.USER, System.getProperty("thinkparity.datasource-user"));
            dataSource = new XADataSourcePool(new XADataSource(xaDataSourceConfiguration));

            // create transaction manager
            transactionManager = TransactionManager.getInstance();
            transactionManager.start();
            // bind the transaction manager to the data source
            transactionManager.bind((XADataSourcePool) dataSource);
            sessionManager = new SessionManager(dataSource);
        } catch (final Throwable t) {
            throw new WorkspaceException("Cannot start persistence manager.", t);
        }
    }

    /**
     * Stop the persistence manager.
     *
     */
    void stop() {
        final List<Session> sessions = sessionManager.getSessions();
        if (0 < sessions.size())
            logger.logWarning("{0} abandoned database sessions.", sessions.size());
        for (final Session session : sessions) {
            logger.logWarning("Session abandoned by {0}.", sessionManager.getSessionCaller(session));
            session.close();
        }

        ((XADataSourcePool) dataSource).shutdown(true);
        try {
            ((XADataSourcePool) dataSource).getShutdownConnection();
        } catch (final SQLException sqlx) {
        } catch (final Throwable t) {
            throw new WorkspaceException("Cannot stop persistence manager.", t);
        }

        transactionManager.stop();
        transactionManager = null;
    }

    /**
     * Begin a new transaction.
     * 
     * @return A <code>Transaction</code>.
     */
    private Transaction beginTransaction() throws NotSupportedException,
            SystemException {
        final Transaction transaction = getTransaction();
        transaction.begin();
        return transaction;
    }
}
