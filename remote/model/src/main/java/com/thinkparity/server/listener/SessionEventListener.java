/*
 * Dec 2, 2005
 */
package com.thinkparity.server.listener;

import java.util.Collection;

import org.apache.log4j.Logger;
import org.jivesoftware.messenger.Session;
import org.xmpp.packet.JID;

import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.queue.QueueItem;
import com.thinkparity.server.model.queue.QueueModel;
import com.thinkparity.server.model.session.SessionModel;
import com.thinkparity.server.org.apache.log4j.ServerLoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SessionEventListener implements org.jivesoftware.messenger.event.SessionEventListener {

	/**
	 * Handle to the parity model session.
	 */
	private com.thinkparity.server.model.session.Session modelSession;

	/**
	 * Create a SessionEventListener.
	 */
	public SessionEventListener() {
		super();
		this.logger = ServerLoggerFactory.getLogger(getClass());
	}

	/**
	 * @see org.jivesoftware.messenger.event.SessionEventListener#anonymousSessionCreated(org.jivesoftware.messenger.Session)
	 */
	public void anonymousSessionCreated(Session session) {}

	/**
	 * @see org.jivesoftware.messenger.event.SessionEventListener#anonymousSessionDestroyed(org.jivesoftware.messenger.Session)
	 */
	public void anonymousSessionDestroyed(Session session) {}

	/**
	 * @see org.jivesoftware.messenger.event.SessionEventListener#sessionCreated(org.jivesoftware.messenger.Session)
	 */
	public void sessionCreated(final Session session) {
		final QueueModel queueModel = getQueueModel(session);
		final SessionModel sessionModel = getSessionModel(session);
		try {
			final Collection<QueueItem> queueItems = queueModel.list();
			for(QueueItem queueItem : queueItems) {
				sessionModel.send(queueItem);
				queueModel.dequeue(queueItem);
			}
		}
		catch(final ParityServerModelException psmx) {
			logger.error("Could not process session created event.", psmx);
		}
	}

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * @see org.jivesoftware.messenger.event.SessionEventListener#sessionDestroyed(org.jivesoftware.messenger.Session)
	 */
	public void sessionDestroyed(Session session) {}

	/**
	 * Use the jive session to obtain a handle to the parity session.
	 * 
	 * @param session
	 *            The jive session.
	 * @return The parity session.
	 */
	private com.thinkparity.server.model.session.Session getModelSession(final Session session) {
		if(null == modelSession) {
			modelSession = new com.thinkparity.server.model.session.Session() {
				final JID jid = session.getAddress();
				public JID getJID() { return jid; }
			};
		}
		return modelSession;
	}

	/**
	 * Obtain a handle to the queue model api.
	 * 
	 * @param session
	 *            The jive session.
	 * @return The queue model api.
	 */
	private QueueModel getQueueModel(final Session session) {
		final QueueModel queueModel = QueueModel.getModel(getModelSession(session));
		return queueModel;
	}

	/**
	 * Obtain a handle to the session model api.
	 * 
	 * @param session
	 *            The jive session.
	 * @return The session model api.
	 */
	private SessionModel getSessionModel(final Session session) {
		final SessionModel sessionModel = SessionModel.getModel(getModelSession(session));
		return sessionModel;
	}
}
