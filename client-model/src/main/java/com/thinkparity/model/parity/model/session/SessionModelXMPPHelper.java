/*
 * Oct 9, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.io.File;
import java.util.Collection;
import java.util.UUID;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImplHelper;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.XMPPSession;
import com.thinkparity.model.xmpp.XMPPSessionFactory;
import com.thinkparity.model.xmpp.document.XMPPDocument;
import com.thinkparity.model.xmpp.events.XMPPExtensionListener;
import com.thinkparity.model.xmpp.events.XMPPPresenceListener;
import com.thinkparity.model.xmpp.events.XMPPSessionListener;
import com.thinkparity.model.xmpp.user.User;

/**
 * The session model xmpp helper is used as an intermediary between the parity
 * session model and the xmpp session implementation. It handles all of the
 * events generated by the xmpp implementation and passes them on to the session
 * for further analysis.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2
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
		this.xmppSession = XMPPSessionFactory.createSession();
		this.xmppExtensionListener = new XMPPExtensionListener() {
			public void documentReceived(final XMPPDocument xmppDocument) {
				handleDocumentReceived(xmppDocument);
			}
			public void keyRequestAccepted(final User user, final UUID artifactUUID) {
				handleKeyRequestAccepted(user, artifactUUID);
			}
			public void keyRequestDenied(final User user, final UUID artifactUUID) {
				handleKeyRequestDenied(user, artifactUUID);
			}
			public void keyRequested(final User user, final UUID artifactUUID) {
				handleKeyRequested(user, artifactUUID);
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

	Collection<User> getSubscriptions(final UUID artifactUniqueId)
			throws SmackException {
		return xmppSession.getArtifactSubscription(artifactUniqueId);
	}

	/**
	 * Obtain the user for the current session.
	 * 
	 * @return The user for the current session.
	 */
	User getUser() throws SmackException { return xmppSession.getUser(); }

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
	 * Send a message to a list of users.
	 * 
	 * @param users
	 *            The users to send the message to.
	 * @param message
	 *            The message to send.
	 * @throws SmackException
	 */
	void send(final Collection<User> users, final String message)
			throws SmackException {
		xmppSession.send(users, message);
	}

	/**
	 * Send a document to a list of users.
	 * 
	 * @param users
	 *            The users to send the document to.
	 * @param xmppDocument
	 *            The xmpp document to send.
	 * @throws SmackException
	 */
	void send(final Collection<User> users, final XMPPDocument xmppDocument)
			throws SmackException {
		xmppSession.send(users, xmppDocument);
	}

	/**
	 * Send a create packet to the parity server.
	 * 
	 * @param parityObjectUUID
	 *            The object unique id.
	 * @throws SmackException
	 */
	void sendCreate(final UUID parityObjectUUID) throws SmackException {
		xmppSession.sendCreate(parityObjectUUID);
	}

	/**
	 * Send a reqest for a document key to the parity server.
	 * 
	 * @param parityObjectUUID
	 *            The object unique id.
	 * @throws ParityException
	 */
	void sendKeyRequest(final UUID parityObjectUUID) throws SmackException {
		xmppSession.sendKeyRequest(parityObjectUUID);
	}

	/**
	 * Send the response to a document key request to the user (via the parity
	 * server).
	 * 
	 * @param artifactUUID
	 *            The document unique id.
	 * @param keyResponse
	 *            The response.
	 * @param user
	 *            The user.
	 * @throws SmackException
	 */
	void sendKeyResponse(final UUID artifactUUID, final KeyResponse keyResponse,
			final User user) throws SmackException {
		xmppSession.sendKeyResponse(artifactUUID, keyResponse, user);
	}

	/**
	 * Send the log file archive to the parity server.
	 * 
	 * @param logFileArchive
	 *            The log file archive.
	 * @param The
	 *            user to send the file to.
	 * @throws SmackException
	 */
	void sendLogFileArchive(final File logFileArchive, final User user)
			throws SmackException {
		xmppSession.sendLogFileArchive(logFileArchive, user);
	}

	/**
	 * Send a subscribe packet to the parity server.
	 * 
	 * @param parityObjectUUID
	 *            The object unique id.
	 * @throws SmackException
	 */
	void sendSubscribe(final UUID parityObjectUUID) throws SmackException {
		xmppSession.sendSubscribe(parityObjectUUID);
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
	 * @param xmppDocument
	 *            The xmpp document that has been received.
	 */
	private void handleDocumentReceived(final XMPPDocument xmppDocument) {
		SessionModelImpl.notifyDocumentReceived(xmppDocument);
	}

	/**
	 * Event handler for the extension listener's key request accepted event.
	 * 
	 * @param user
	 *            The user who accepted the request.
	 * @param artifactUUID
	 *            The artifact unique id being requested.
	 */
	private void handleKeyRequestAccepted(final User user, final UUID artifactUUID) {
		SessionModelImpl.notifyKeyRequestAccepted(artifactUUID, user);
	}

	/**
	 * Event handler for the extension listener's key request accepted event.
	 * 
	 * @param user
	 *            The user who accepted the request.
	 * @param artifactUUID
	 *            The artifact unique id being requested.
	 */
	private void handleKeyRequestDenied(final User user, final UUID artifactUUID) {
		SessionModelImpl.notifyKeyRequestDenied(artifactUUID, user);
	}

	/**
	 * Event handler for the extension listener's key requested event.
	 * 
	 * @param user
	 *            The user requesting the key.
	 * @param artifactUUID
	 *            The artifact unique id being requested.
	 */
	private void handleKeyRequested(final User user, final UUID artifactUUID) {
		SessionModelImpl.notifyKeyRequested(user, artifactUUID);
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
