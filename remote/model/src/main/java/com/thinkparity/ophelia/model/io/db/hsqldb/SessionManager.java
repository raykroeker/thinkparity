/*
 * Feb 8, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.sql.DataSource;

import com.thinkparity.codebase.StackUtil;

/**
 * <b>Title:</b>thinkParity Hypersonic Session Manager<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.3
 */
public final class SessionManager {

    /** A map of the session to the session's caller. */
    private static final Map<Session, Object> SESSION_CALLERS;

	/** A list of all open sessions. */
	private static final Vector<Session> SESSIONS;

	static {
        SESSIONS = new Vector<Session>();
        SESSION_CALLERS = new HashMap<Session, Object>();
    }

    /** A <code>DataSource</code>. */
    private final DataSource dataSource;

    /**
     * Create SessionManager.
     * 
     * @param dataSource
     *            A sql <code>DataSource</code>.
     */
	public SessionManager(final DataSource dataSource) {
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
    public StackTraceElement getSessionCaller(final Session session) {
        synchronized (SESSIONS) {
            return (StackTraceElement) SESSION_CALLERS.get(session);
        }
    }

	/**
     * Obtain a list of the sessions.
     * 
     * @return A <code>List&lt;Session&gt;</code>.
     */
    public List<Session> getSessions() {
        return Collections.unmodifiableList(SESSIONS);
    }

    /**
     * Open a new database session.
     * 
     * @return A <code>Session</code>.
     */
    public Session openSession() {
        return openSessionImpl(StackUtil.getCaller());
    }

    /**
     * Open a new database session.
     * 
     * @param caller
     *            A caller <code>StackTraceElement</code>.
     * @return A <code>Session</code>.
     */
	public Session openSession(final StackTraceElement caller) {
		return openSessionImpl(caller);
	}

    /**
	 * Close the session.
	 * 
	 * @param session
	 *            The session to close.
	 */
	protected void close(final Session session) {
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
    private Session openSessionImpl(final StackTraceElement caller) {
        synchronized (SESSIONS) {
            try {
                final Session session = new Session(this, dataSource.getConnection());
                SESSIONS.add(session);
                SESSION_CALLERS.put(session, caller);
                return session;
            } catch (final SQLException sqlx) {
                throw new HypersonicException(sqlx);
            }
        }
    }
}
