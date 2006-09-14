/*
 * Feb 8, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class SessionManager {

    /** A hypersonic connection provider. */
    private HypersonicConnectionProvider connectionProvider;

	/** A map of the session to the session's caller. */
    private final Map<Session, Object> sessionCallers;

	/** A list of all open sessions. */
	private final Vector<Session> sessions;

    /** Create SessionManager. */
	protected SessionManager() {
        super();
        sessions = new Vector<Session>();
        sessionCallers = new HashMap<Session, Object>();
	}

    /**
	 * Open a new database session.
	 * 
	 * @return The new database session.
	 */
	public Session openSession(final StackTraceElement caller) {
		synchronized (sessions) {
            final Session session = new Session(this, connectionProvider.getConnection());
            sessions.add(session);
            sessionCallers.put(session, caller);
            return session;
        }
	}

    /**
	 * Close the session.
	 * 
	 * @param session
	 *            The session to close.
	 */
	protected void close(final Session session) {
        synchronized (sessions) {
            sessions.remove(session);
            sessionCallers.remove(session);
        }
    }

    /**
     * Obtain the connectionProvider
     *
     * @return The HypersonicConnectionProvider.
     */
    protected HypersonicConnectionProvider getConnectionProvider() {
        return connectionProvider;
    }

	/**
     * Obtain the creator of a session.
     * 
     * @param session
     *            A session.
     * @return The stack trace element of the caller.
     */
    protected StackTraceElement getSessionCaller(final Session session) {
        return (StackTraceElement) sessionCallers.get(session);
    }

    /**
     * Obtain a list of the sessions.
     * 
     * @return A <code>List&lt;Session&gt;</code>.
     */
    protected List<Session> getSessions() {
        return Collections.unmodifiableList(sessions);
    }

    /**
     * Set connectionProvider.
     *
     * @param connectionProvider The HypersonicConnectionProvider.
     */
    protected void setConnectionProvider(
            final HypersonicConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }
}
