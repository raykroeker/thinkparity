/*
 * Oct 9, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.util.Collection;

import com.thinkparity.model.parity.api.document.DocumentVersion;
import com.thinkparity.model.parity.model.AbstractModelImplHelper;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.XMPPSession;
import com.thinkparity.model.xmpp.XMPPSessionFactory;
import com.thinkparity.model.xmpp.events.XMPPExtensionListener;
import com.thinkparity.model.xmpp.events.XMPPPresenceListener;
import com.thinkparity.model.xmpp.events.XMPPSessionListener;
import com.thinkparity.model.xmpp.user.User;

/**
 * SessionModelXMPPHelper
 * @author raykroeker@gmail.com
 * @version 1.0
 */
class SessionModelXMPPHelper extends AbstractModelImplHelper {

	/**
	 * XMPP Extension listener.
	 */
	private final XMPPExtensionListener xmppExtensionListener;

	/**
	 * XMPP Presence listener.
	 */
	private final XMPPPresenceListener xmppPresenceListener;

	/**
	 * XMPP session.
	 */
	private final XMPPSession xmppSession;

	/**
	 * XMPP Session listener.
	 */
	private final XMPPSessionListener xmppSessionListener;

	/**
	 * Create a SessionModelXMPPHelper
	 */
	SessionModelXMPPHelper() {
		super();
		this.xmppSession = XMPPSessionFactory.getSession();
		this.xmppExtensionListener = new XMPPExtensionListener() {
			public void documentReceived(final DocumentVersion documentVersion) {
				handleDocumentReceived(documentVersion);
			}
		};
		this.xmppPresenceListener = new XMPPPresenceListener() {
			public void presenceRequested(final User user) {
				handlePresenceRequested(user);
			}
		};
		this.xmppSessionListener = new XMPPSessionListener() {
			public void sessionEstablished() { handleSessionEstablished(); }
			public void sessionTerminated() { handleSessionTerminated(); }
			public void sessionTerminated(final Exception x) {
				handleSessionTerminated(x);
			}
		};

		xmppSession.addListener(xmppExtensionListener);
		xmppSession.addListener(xmppPresenceListener);
		xmppSession.addListener(xmppSessionListener);
	}

	/**
	 * Accept the user's presence request.
	 * 
	 * @param user
	 *            The user seeking visibility into the presence.
	 * @throws SmackException
	 * @see SessionModelXMPPHelper#addRosterEntry(User)
	 * @see SessionModelXMPPHelper#denyPresence(User)
	 */
	void acceptPresence(final User user) throws SmackException {
		xmppSession.acceptPresence(user);
	}

	/**
	 * Add a user to the roster. This will send a presence visibility request to
	 * the user.
	 * 
	 * @param user
	 *            The user to add to the roster.
	 * @throws SmackException
	 */
	void addRosterEntry(final User user) throws SmackException {
		xmppSession.addRosterEntry(user);
	}

	/**
	 * Deny the user's presence request.
	 * 
	 * @param user
	 *            The user seeking visibility into the presence.
	 * @throws SmackException
	 * @see SessionModelXMPPHelper#addRosterEntry(User)
	 * @see SessionModelXMPPHelper#acceptPresence(User)
	 */
	void denyPresence(final User user) throws SmackException {
		xmppSession.denyPresence(user);
	}

	/**
	 * Obtain a list of roster entries.
	 * 
	 * @return The list of roster entries.
	 * @throws SmackException
	 */
	Collection<User> getRosterEntries() throws SmackException {
		return xmppSession.getRosterEntries();
	}

	/**
	 * Determine if the user is logged in.
	 * 
	 * @return True if the user is logged in false, otherwise.
	 */
	Boolean isLoggedIn() { return xmppSession.isLoggedIn(); }

	/**
	 * Establish a new xmpp session for the user.
	 * 
	 * @param host
	 *            The server host.
	 * @param port
	 *            The server port.
	 * @param username
	 *            The login name.
	 * @param password
	 *            The login password.
	 * @throws SmackException
	 */
	void login(final String host, final Integer port, final String username,
			final String password) throws SmackException {
		xmppSession.login(host, port, username, password);
	}

	/**
	 * Terminate the existing session for the user.
	 * 
	 * @throws SmackException
	 */
	void logout() throws SmackException { xmppSession.logout(); }

	/**
	 * Send a document to a user.
	 * 
	 * @param user
	 *            The user to send the document to.
	 * @param documentVersion
	 *            The document to send.
	 * @throws SmackException
	 */
	void send(final User user, final DocumentVersion documentVersion)
			throws SmackException {
		xmppSession.send(user, documentVersion);
	}

	/**
	 * Update the information in the roster for a user.
	 * 
	 * @param user
	 *            The user for whom the information will be updated.
	 */
	void updateRosterEntry(final User user) {
		xmppSession.updateRosterEntry(user);
	}

	/**
	 * Event handler for the extension listener's document received event.
	 * 
	 * @param documentVersion
	 *            The document that is received.
	 */
	private void handleDocumentReceived(final DocumentVersion documentVersion) {
		SessionModelImpl.notifyDocumentReceived(documentVersion);
	}

	/**
	 * Event handler for the presence listener's presence requested event.
	 * 
	 * @param user
	 *            The user requesting presence visibility.
	 */
	private void handlePresenceRequested(final User user) {
		SessionModelImpl.notifyPresenceRequested(user);
	}

	/**
	 * Event handler for the session listener's session established event.
	 *
	 */
	private void handleSessionEstablished() {
		SessionModelImpl.notifySessionEstablished();
	}

	/**
	 * Event handler for the session listener's session termination event.
	 * 
	 */
	private void handleSessionTerminated() {
		SessionModelImpl.notifySessionTerminated();
	}

	/**
	 * Event handler for the sesion listener's session termination with error
	 * event.
	 * 
	 * @param x
	 *            The error.
	 */
	private void handleSessionTerminated(final Exception x) {
		SessionModelImpl.notifySessionTerminated(x);
	}
}
