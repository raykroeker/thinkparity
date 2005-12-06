/*
 * Dec 2, 2005
 */
package com.thinkparity.server.model.session;

import com.thinkparity.server.model.AbstractModel;
import com.thinkparity.server.model.ParityServerModelException;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SessionModel extends AbstractModel {

	/**
	 * Obtain a handle to the session model api.
	 * 
	 * @param session
	 *            The user session.
	 * @return The session model.
	 */
	public static SessionModel getModel(final Session session) {
		final SessionModel sessionModel = new SessionModel(session);
		return sessionModel;
	}

	/**
	 * Session model implementation.
	 */
	private final SessionModelImpl impl;

	/**
	 * Implementation synchronization lock.
	 */
	private final Object implLock;


	/**
	 * Create a SessionModel.
	 */
	private SessionModel(final Session session) {
		super();
		this.impl = new SessionModelImpl(session);
		this.implLock = new Object();
	}

	/**
	 * Send all queued messages for the user.
	 * 
	 * @throws ParityServerModelException
	 */
	public void sendQueuedMessages() throws ParityServerModelException {
		synchronized(implLock) { impl.sendQueuedMessages(); }
	}

}
