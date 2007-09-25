/*
 * Created On: Sep 11, 2006 7:41:22 PM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.model.Constants.DirectoryNames;
import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;
import com.thinkparity.ophelia.model.io.md.MetaDataType;
import com.thinkparity.ophelia.model.workspace.CannotLockException;
import com.thinkparity.ophelia.model.workspace.Transaction;
import com.thinkparity.ophelia.model.workspace.Workspace;
import com.thinkparity.ophelia.model.workspace.WorkspaceException;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * <b>Title:</b>thinkParity OpheliaModel Persistence Manager Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class PersistenceManagerImpl {

    /** A tread-local transaction. */
    private static final ThreadLocal<Transaction> transaction;

    static {
        transaction = new ThreadLocal<Transaction>();
    }

    /** A <code>DataSource</code>. */
    private DataSource dataSource;

    /** An apache logger. */
    private final Log4JWrapper logger;

    /** The persistence log <code>File</code>. */
    private final File persistenceLogFile;

    /** The persistence root directory <code>File</code>. */
    private final File persistenceRoot;

    /** A persistence <code>SessionManager</code>. */
    private SessionManager sessionManager;

    /**
     * Create SessionManagerImpl.
     * 
     * @param workspace
     *            The <code>Workspace</code>.
     */
    PersistenceManagerImpl(final Workspace workspace) {
        super();
        this.logger = new Log4JWrapper();
        this.persistenceLogFile = new File(
                workspace.getLogDirectory(), "thinkParity Derby.log");
        this.persistenceRoot = new File(
                workspace.getDataDirectory(), DirectoryNames.Workspace.Data.DB);
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
        return ((TransactionImpl) getTransaction()).getDataSource();
    }

    /**
     * Obtain a transaction.
     * 
     * @return A <code>Transaction</code>.
     */
    Transaction getTransaction() {
        final Transaction currentTransaction = transaction.get();
        if (null == currentTransaction) {
            return createTransaction();
        } else {
            return currentTransaction;
        }
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
    void start() throws CannotLockException {
        try {
            System.setProperty("derby.infolog.append", "true");
            System.setProperty("derby.storage.pageCacheSize", "64");
            System.setProperty("derby.stream.error.file", persistenceLogFile.getAbsolutePath());

            final String url = new StringBuilder("jdbc:derby:")
                .append(persistenceRoot.getAbsolutePath())
                .append(persistenceRoot.exists() ? "" : ";create=true")
                .toString();

            final BasicDataSource basicDataSource = new BasicDataSource();
            basicDataSource.setDefaultAutoCommit(false);
            basicDataSource.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
            basicDataSource.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
            basicDataSource.setInitialSize(5);
            basicDataSource.setMaxActive(5);
            basicDataSource.setPoolPreparedStatements(true);
            basicDataSource.setUrl(url);
basicDataSource.setAccessToUnderlyingConnectionAllowed(true);
            basicDataSource.setUsername("sa");

            dataSource = new DataSourceImpl(basicDataSource, "sa", null);

            sessionManager = new SessionManager(dataSource);

            // check to ensure the database is not locked
            if (isLocked()) {
                throw new CannotLockException(persistenceRoot.getAbsolutePath());
            }
        } catch (final CannotLockException clx) {
            throw clx;
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

        final String url = new StringBuilder("jdbc:derby:")
            .append(persistenceRoot.getAbsolutePath())
            .append(";shutdown=true")
            .toString();
        try {
            DriverManager.getConnection(url);
        } catch (final SQLException sqlx) {
        } catch (final Throwable t) {
            throw new WorkspaceException("Cannot stop persistence manager.", t);
        }
    }

    /**
     * Begin a new transaction.
     * 
     * @return A <code>Transaction</code>.
     */
    private Transaction beginTransaction() {
        final Transaction transaction = getTransaction();
        transaction.begin();
        return transaction;
    }

    /**
     * Create a transaction.
     * 
     * @return A <code>Transaction</code>.
     */
    private Transaction createTransaction() {
        final Transaction currentTransaction = new TransactionImpl(dataSource);
        transaction.set(currentTransaction);
        return currentTransaction;
    }

    /**
     * Determine whether or not the database is locked.
     * 
     * @return True if the database is locked.
     */
    private boolean isLocked() {
        Session session = null;
        try {
            session = sessionManager.openSession();
            return false;
        } catch (final HypersonicException hx) {
            /* if the db is locked merely opening a session (connection) will
             * throw an error
             * 
             * NOTE the xapool data-source prints the exception stack */
            return true;
        } finally {
            if (null != session) {
                session.close();
            }
        }
    }
}
