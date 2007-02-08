/*
 * Created On: Sep 11, 2006 7:41:22 PM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.util.List;

import javax.sql.DataSource;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.util.jta.Transaction;
import com.thinkparity.codebase.model.util.jta.TransactionManager;

import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceException;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class PersistenceManagerImpl {

    /** An <code>PersistenceManagerDataSource</code>. */
    private PersistenceManagerDataSource dataSource;

    /** An apache logger. */
    private final Log4JWrapper logger;

    /** A persistence <code>SessionManager</code>. */
    private SessionManager sessionManager;

    /** A <code>TransactionManager</code>. */
    private TransactionManager transactionManager;

    /** The <code>Workspace</code>. */
    private final Workspace workspace;

    /**
     * Create SessionManagerImpl.
     * 
     * @param workspace
     *            The workspace root directory <code>File</code>.
     */
    PersistenceManagerImpl(final WorkspaceImpl workspace) {
        super();
        this.logger = new Log4JWrapper();
        this.workspace = workspace;
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
        return transactionManager.getTransaction();
    }

    /**
     * Start the persistence manager.
     *
     */
    void start() {
        try {
            // create transaction manager
            transactionManager = TransactionManager.getInstance();
            transactionManager.start();
            // create datasource
            dataSource = new PersistenceManagerDataSource(workspace); 
            // bind the transaction manager to the data source
            transactionManager.bind(dataSource);
            sessionManager = new SessionManager(dataSource);

            final Transaction transaction = beginTransaction();
            try {
                new PersistenceMigrator(sessionManager).migrate();
                transaction.commit();
            } catch (final Throwable t) {
                transaction.rollback();
                throw t;
            }
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

        final Session session = sessionManager.openSession();
        try {
            session.execute("SHUTDOWN COMPACT");
        } finally {
            session.close();
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
