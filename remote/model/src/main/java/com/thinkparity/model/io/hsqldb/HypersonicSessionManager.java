/*
 * Feb 8, 2006
 */
package com.thinkparity.model.io.hsqldb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HypersonicSessionManager {

	/** A map of the session to the session's caller. */
    private static final Map<HypersonicSession, Object> sessionCallers;

    /** A list of all open sessions. */
	private static final Vector<HypersonicSession> sessions;

	static {
		sessions = new Vector<HypersonicSession>();
        sessionCallers = new HashMap<HypersonicSession, Object>();
		HypersonicUtil.registerDriver();
	}

	/**
	 * Open a new database session.
	 * 
	 * @return The new database session.
	 */
	public static HypersonicSession openSession(
            final HypersonicConnectionProvider connectionProvider,
            final StackTraceElement caller) {
		synchronized (sessions) {
            final HypersonicSession session = new HypersonicSession(connectionProvider.getConnection());
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
	static void close(final HypersonicSession session) {
        synchronized(sessions) {
            sessions.remove(session);
            sessionCallers.remove(session);
        }
    }

    /**
     * Obtain the creator of a session.
     * 
     * @param session
     *            A session.
     * @return The stack trace element of the caller.
     */
    static StackTraceElement getSessionCaller(final HypersonicSession session) {
        return (StackTraceElement) sessionCallers.get(session);
    }

    /**
     * Obtain the list of sessions.
     * 
     * @return A list of sessions.
     */
    static List<HypersonicSession> getSessions() {
        return sessions;
    }

	/** Create SessionManager. */
	private HypersonicSessionManager() { super(); }
}
