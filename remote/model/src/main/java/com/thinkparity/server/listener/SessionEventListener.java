/*
 * Dec 2, 2005
 */
package com.thinkparity.server.listener;

import org.jivesoftware.messenger.Session;
import org.xmpp.packet.JID;

import com.thinkparity.server.model.session.SessionModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SessionEventListener implements org.jivesoftware.messenger.event.SessionEventListener {

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
		final SessionModel sessionModel = SessionModel.getModel(new com.thinkparity.server.model.session.Session() {
			final JID jid = session.getAddress();
			public JID getJID() { return jid; }
		});
		sessionModel.sendQueuedMessages();
	}

	/**
	 * @see org.jivesoftware.messenger.event.SessionEventListener#sessionDestroyed(org.jivesoftware.messenger.Session)
	 */
	public void sessionDestroyed(Session session) {}

	/**
	 * Create a SessionEventListener.
	 */
	public SessionEventListener() { super(); }
}
