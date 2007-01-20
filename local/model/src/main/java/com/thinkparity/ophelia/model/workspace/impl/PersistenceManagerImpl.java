/*
 * Created On: Sep 11, 2006 7:41:22 PM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.io.File;
import java.util.List;

import javax.sql.DataSource;

import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.util.jta.Transaction;
import com.thinkparity.codebase.model.util.jta.TransactionManager;

import com.thinkparity.ophelia.model.Constants.DirectoryNames;
import com.thinkparity.ophelia.model.Constants.FileNames;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;
import com.thinkparity.ophelia.model.workspace.WorkspaceException;

import org.enhydra.jdbc.standard.StandardXADataSource;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class PersistenceManagerImpl {

    /** The data source driver name. */
    private static final String DS_DRIVER_NAME;

    /** The data source password. */
    private static final String DS_PASSWORD;

    /** The data source user. */
    private static final String DS_USER;

    static {
        DS_DRIVER_NAME = "org.hsqldb.jdbcDriver";
        DS_USER = "sa";
        DS_PASSWORD = "";
    }

    /** An enhydra <code>StandardXADataSource</code>. */
    private StandardXADataSource dataSource;

    /** The data source driver url. */
    private final String dsDriverURL;

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
     *            The workspace root directory <code>File</code>.
     */
    PersistenceManagerImpl(final WorkspaceImpl workspace) {
        super();
        this.dsDriverURL = new StringBuffer("jdbc:hsqldb:file:")
            .append(workspace.getDataDirectory().getAbsolutePath())
            .append(File.separator)
            .append(DirectoryNames.Workspace.Data.DB)
            .append(File.separator)
            .append(FileNames.Workspace.Data.DB)
            .append(Separator.SemiColon)
            .append("hsqldb.default_table_type")
            .append(Separator.Equals)
            .append("cached")
            .append(Separator.SemiColon)
            .append("sql.tx_no_multi_rewrite")
            .append(Separator.Equals)
            .append("TRUE")
            .toString();
        this.logger = new Log4JWrapper();
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
            dataSource = new StandardXADataSource(); 
            dataSource.setDriverName(DS_DRIVER_NAME);
            dataSource.setPassword(DS_PASSWORD);
            dataSource.setUrl(dsDriverURL);
            dataSource.setUser(DS_USER);
            // bind the transaction manager to the data source
            transactionManager.bind(dataSource);
            sessionManager = new SessionManager(dataSource);
            new PersistenceMigrator(sessionManager).migrate();
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
}
