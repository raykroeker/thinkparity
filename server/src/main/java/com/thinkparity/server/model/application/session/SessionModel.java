/*
 * Dec 2, 2005
 */
package com.thinkparity.desdemona.model.session;


import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.queue.QueueItem;


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
	 * Send the queue item to the logged in user.
	 * 
	 * @param queueItem
	 *            The queue item to send.
	 * @throws ParityServerModelException
	 */
	public void send(final QueueItem queueItem)
			throws ParityServerModelException {
		synchronized(implLock) { impl.send(queueItem); }
	}
}
