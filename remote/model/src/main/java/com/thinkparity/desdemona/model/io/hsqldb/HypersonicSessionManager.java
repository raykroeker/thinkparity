/*
 * Feb 8, 2006
 */
package com.thinkparity.desdemona.model.io.hsqldb;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.DataSource;

import com.thinkparity.codebase.StackUtil;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HypersonicSessionManager {

    /** A map of the session to the session's caller. */
    private static final Map<HypersonicSession, Object> SESSION_CALLERS;

    /** A list of all open sessions. */
    private static final Vector<HypersonicSession> SESSIONS;

    static {
        SESSIONS = new Vector<HypersonicSession>();
        SESSION_CALLERS = new HashMap<HypersonicSession, Object>();
    }

    /** A <code>DataSource</code>. */
    private final DataSource dataSource;

    /**
     * Create SessionManager.
     * 
     * @param dataSource
     *            A sql <code>DataSource</code>.
     */
    public HypersonicSessionManager(final DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

    /**
     * Obtain the creator of a session.
     * 
     * @param session
     *            A session.
     * @return The stack trace element of the caller.
     */
    public StackTraceElement getSessionCaller(final HypersonicSession session) {
        synchronized (SESSIONS) {
            return (StackTraceElement) SESSION_CALLERS.get(session);
        }
    }

    /**
     * Obtain a list of the sessions.
     * 
     * @return A <code>List&lt;Session&gt;</code>.
     */
    public List<HypersonicSession> getSessions() {
        return Collections.unmodifiableList(SESSIONS);
    }

    /**
     * Open a new database session.
     * 
     * @return A <code>Session</code>.
     */
    public HypersonicSession openSession() {
        return openSessionImpl(StackUtil.getCaller());
    }

    /**
     * Open a new database session.
     * 
     * @param caller
     *            A caller <code>StackTraceElement</code>.
     * @return A <code>Session</code>.
     */
    public HypersonicSession openSession(final StackTraceElement caller) {
        return openSessionImpl(caller);
    }

    /**
     * Close the session.
     * 
     * @param session
     *            The session to close.
     */
    protected void close(final HypersonicSession session) {
        synchronized (SESSIONS) {
            SESSIONS.remove(session);
            SESSION_CALLERS.remove(session);
        }
    }

    /**
     * Open a new database session.
     * 
     * @param caller
     *            A caller <code>StackTraceElement</code>.
     * @return A <code>Session</code>.
     */
    private HypersonicSession openSessionImpl(final StackTraceElement caller) {
        synchronized (SESSIONS) {
            try {
                final HypersonicSession session = new HypersonicSession(this, dataSource.getConnection());
                SESSIONS.add(session);
                SESSION_CALLERS.put(session, caller);
                return session;
            } catch (final SQLException sqlx) {
                throw new HypersonicException(sqlx);
            }
        }
    }
}
