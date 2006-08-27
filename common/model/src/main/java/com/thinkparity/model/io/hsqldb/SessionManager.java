/*
 * Feb 8, 2006
 */
package com.thinkparity.model.io.hsqldb;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import com.thinkparity.codebase.StackUtil;

import com.thinkparity.model.parity.model.io.md.MetaData;
import com.thinkparity.model.parity.model.io.md.MetaDataType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SessionManager {

	/** A map of the session to the session's caller. */
    private static final Map<Session, Object> sessionCallers;

    /** A list of all open sessions. */
	private static final Vector<Session> sessions;

	private static final String SQL_GET_META_DATA =
		"select VALUE from META_DATA where META_DATA_TYPE_ID=? and KEY=?";

	static {
		sessions = new Vector<Session>();
        sessionCallers = new HashMap<Session, Object>();
		HypersonicUtil.registerDriver();
		HypersonicUtil.setInitialProperties();
	}

    /**
	 * Obtain a named property from the database.
	 * 
	 * @param property
	 *            The property.
	 * @return The property value; or null if no such property exists.
	 * @throws HypersonicException
	 */
	public static String getMetaDataString(final MetaData metaData)
			throws HypersonicException {
		final Session session = SessionManager.openSession(StackUtil.getExecutionPoint());
		try {
			session.prepareStatement(SQL_GET_META_DATA);
			session.setTypeAsInteger(1, MetaDataType.STRING);
			session.setMetaDataAsString(2, metaData);
			session.executeQuery();
			if(session.nextResult()) { return session.getString("VALUE"); }
			else { return null; }
		}
		finally { session.close(); }
	}

	/**
	 * Open a new database session.
	 * 
	 * @return The new database session.
	 */
	public static Session openSession(final StackTraceElement caller) {
		synchronized(sessions) {
            final Session session = new Session(HypersonicUtil.createConnection());
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
	static void close(final Session session) {
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
    static StackTraceElement getSessionCaller(final Session session) {
        return (StackTraceElement) sessionCallers.get(session);
    }

    /**
     * Obtain the list of sessions.
     * 
     * @return A list of sessions.
     */
    static List<Session> getSessions() {
        return sessions;
    }

	/** Create SessionManager. */
	private SessionManager() { super(); }
}
