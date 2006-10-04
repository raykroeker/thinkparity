/*
 * Created On: Sep 11, 2006 7:41:22 PM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.model.Constants.DirectoryNames;
import com.thinkparity.ophelia.model.Constants.FileNames;
import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicConnectionProvider;
import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class SessionManagerImpl extends SessionManager implements
        HypersonicConnectionProvider {

    /** The session manager connection info. */
    private static final Properties CONNECTION_INFO;

    static {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (final ClassNotFoundException cnfx) {
            throw new HypersonicException(cnfx);
        }

        CONNECTION_INFO = new Properties();
        CONNECTION_INFO.setProperty("user", "sa");
        CONNECTION_INFO.setProperty("password", "");
        CONNECTION_INFO.setProperty("hsqldb.default_table_type", "cached");
    }

    /** The database connection url. */
    private final String connectionURL;

    /** An apache logger. */
    private final Log4JWrapper logger;

    /**
     * Create SessionManagerImpl.
     * 
     * @param workspace
     *            The workspace root directory <code>File</code>.
     */
    SessionManagerImpl(final WorkspaceImpl workspace) {
        super();
        this.connectionURL = new StringBuffer("jdbc:hsqldb:file:")
            .append(workspace.getDataDirectory().getAbsolutePath())
            .append(File.separator)
            .append(DirectoryNames.Workspace.Data.DB)
            .append(File.separator)
            .append(FileNames.Workspace.Data.DB)
            .toString();
        this.logger = new Log4JWrapper();
    }

    /**
     * @see com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicConnectionProvider#getConnection()
     */
    public Connection getConnection() {
        try {
            final Connection connection =
                DriverManager.getConnection(connectionURL, CONNECTION_INFO);
            connection.setAutoCommit(false);
            return connection;
        } catch (final SQLException sqlx) {
            throw new HypersonicException(sqlx);
        }
    }

    /**
     * Restart the session manager.
     *
     */
    public void restart() {
        stop();
        start();
    }

    /**
     * Start the session manager.
     *
     */
    public void start() {
        setConnectionProvider(this);
        final Session session = openSession();
        try {
            final String[] sql = { "SET PROPERTY \"sql.tx_no_multi_rewrite\" TRUE" };
            session.execute(sql);
        } finally {
            session.close();
        }
    }

    /**
     * Stop the session manager.
     *
     */
    public void stop() {
        // close abandoned sessions
        final List<Session> sessions = getSessions();
        synchronized (sessions) {
            if (0 < sessions.size()) {
                logger.logWarning("{0} abandoned database sessions.", sessions.size());
            }
            StackTraceElement sessionCaller;
            for (final Session session : sessions) {
                sessionCaller = getSessionCaller(session);
                logger.logWarning("{0} - {1}.{2}({3}:{4})",
                                session.getId(),
                                sessionCaller.getClassName(),
                                sessionCaller.getMethodName(),
                                sessionCaller.getFileName(),
                                sessionCaller.getLineNumber());
                close(session);
            }
            final Session session = openSession();
            try {
                session.execute("SHUTDOWN COMPACT");
            } finally {
                session.close();
            }
        }
    }

    /**
     * Open a session.
     * 
     * @return A <code>Session</code>.
     */
    private Session openSession() {
        return openSession(StackUtil.getCaller());
    }
}
