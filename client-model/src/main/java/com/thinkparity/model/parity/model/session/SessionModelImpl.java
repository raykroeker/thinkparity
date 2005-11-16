/*
 * Mar 7, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.util.Collection;
import java.util.Vector;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.ParityErrorTranslator;
import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.events.PresenceEvent;
import com.thinkparity.model.parity.api.events.PresenceListener;
import com.thinkparity.model.parity.api.events.SessionListener;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.document.Document;
import com.thinkparity.model.parity.model.document.DocumentAction;
import com.thinkparity.model.parity.model.document.DocumentActionData;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.document.XMPPDocument;
import com.thinkparity.model.xmpp.user.User;

/**
 * SessionModelImpl
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class SessionModelImpl extends AbstractModelImpl {

	/**
	 * List of all of the registered parity presence listeners.
	 * @see SessionModelImpl#presenceListenersLock
	 */
	private static final Collection<PresenceListener> presenceListeners;

	/**
	 * Lock used to synchronize the collection access.
	 * @see SessionModelImpl#presenceListeners
	 */
	private static final Object presenceListenersLock;

	/**
	 * List of all of the registered parity session listeners.
	 * @see SessionModelImpl#sessionListenersLock
	 */
	private static final Collection<SessionListener> sessionListeners;

	/**
	 * Lock used to synchronize the collection access.
	 * @see SessionModelImpl#sessionListeners
	 */
	private static final Object sessionListenersLock;

	/**
	 * Helper wrapper class for xmpp calls.
	 * @see SessionModelImpl#xmppHelperLock
	 */
	private static final SessionModelXMPPHelper xmppHelper;

	/**
	 * Helper wrapper's synchronization lock.
	 * @see SessionModelImpl#xmppHelper
	 */
	private static final Object xmppHelperLock;

	static {
		// create the presence listener list & sync lock
		presenceListeners = new Vector<PresenceListener>(3);
		presenceListenersLock = new Object();
		// create the session listener list & sync lock
		sessionListeners = new Vector<SessionListener>(3);
		sessionListenersLock = new Object();
		// create the xmpp helper
		xmppHelper = new SessionModelXMPPHelper();
		xmppHelperLock = new Object();
	}

	/**
	 * Handle the event generated by xmppExtensionListenerImpl.  Here we create
	 * a new document based upon the document version.
	 * 
	 * @param xmppDocument
	 *            The xmpp document that has been received.
	 */
	static void notifyDocumentReceived(final XMPPDocument xmppDocument) {
		final DocumentModel documentModel = DocumentModel.getModel();
		try { documentModel.receive(xmppDocument); }
		catch(ParityException px) {}
	}

	/**
	 * Notify all of the registered presence listeners that a user has requested
	 * visiblity into their presence.
	 * 
	 * @param user
	 *            The requesting user.
	 */
	static void notifyPresenceRequested(final User user) {
		synchronized(SessionModelImpl.presenceListenersLock) {
			for(PresenceListener listener : SessionModelImpl.presenceListeners) {
				listener.presenceRequested(new PresenceEvent(user));
			}
		}
	}

	/**
	 * Notify all of the registered session listeners that the session has been
	 * established.
	 *
	 */
	static void notifySessionEstablished() {
		synchronized(SessionModelImpl.sessionListenersLock) {
			for(SessionListener listener : SessionModelImpl.sessionListeners) {
				listener.sessionEstablished();
			}
		}
	}

	/**
	 * Notify all of the registered session listeners that the session has been
	 * terminated.
	 *
	 */
	static void notifySessionTerminated() {
		synchronized(SessionModelImpl.sessionListenersLock) {
			for(SessionListener listener : SessionModelImpl.sessionListeners) {
				listener.sessionTerminated();
			}
		}
	}

	/**
	 * Notify all of the registered session listeners that the session has been
	 * terminated due to an error.
	 * 
	 * @param x
	 *            The cause of the session termination.
	 */
	static void notifySessionTerminated(final Exception x) {
		synchronized(SessionModelImpl.sessionListenersLock) {
			for(SessionListener listener : SessionModelImpl.sessionListeners) {
				listener.sessionTerminated(x);
			}
		}
	}

	/**
	 * Create a SessionModelImpl
	 * @param workspace
	 */
	SessionModelImpl(final Workspace workspace) { super(workspace); }

	/**
	 * Accept a presence request from user.
	 * 
	 * @param user
	 *            The user to accept the request from.
	 * @throws ParityException
	 */
	void acceptPresence(final User user) throws ParityException {
		synchronized(xmppHelperLock) {
			try { xmppHelper.acceptPresence(user); }
			catch(SmackException sx) {
				logger.error("acceptPresence(User)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("acceptPresence(User)", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Add a presence listener to the session.
	 * 
	 * @param presenceListener
	 *            The presence listener to add.
	 */
	void addListener(final PresenceListener presenceListener) {
		Assert.assertNotNull("Cannot register a null presence listener.",
				presenceListener);
		synchronized(SessionModelImpl.presenceListenersLock) {
			Assert.assertTrue("Cannot re-register the same presence listener.",
					!SessionModelImpl.presenceListeners.contains(presenceListener));
			SessionModelImpl.presenceListeners.add(presenceListener);
		}
	}

	/**
	 * Add a session listener to the session.
	 * 
	 * @param sessionListener
	 *            The session listener to add.
	 */
	void addListener(final SessionListener sessionListener) {
		Assert.assertNotNull("Cannot register a null session listener.",
				sessionListener);
		Assert.assertTrue("Cannot re-register the same session listener.",
				!sessionListeners.contains(sessionListener));
		sessionListeners.add(sessionListener);
	}

	/**
	 * Add a roster entry for the user. This will send a presence request to
	 * user.
	 * 
	 * @param user
	 *            The user to add to the roster.
	 * @throws ParityException
	 */
	void addRosterEntry(final User user) throws ParityException {
		synchronized(xmppHelperLock) {
			try { xmppHelper.addRosterEntry(user); }
			catch(SmackException sx) {
				logger.error("addRosterEntry(User)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("addRosterEntry(User)", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Deny a presence request from user.
	 * 
	 * @param user
	 *            The user to deny.
	 * @throws ParityException
	 */
	void denyPresence(final User user) throws ParityException {
		synchronized(xmppHelperLock) {
			assertIsLoggedIn("denyPresence", xmppHelper);
			try { xmppHelper.denyPresence(user); }
			catch(SmackException sx) {
				logger.error("denyPresence(User)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("denyPresence(User)", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Obtain a list of roster entries.
	 * 
	 * @return The list of roster entries.
	 * @throws ParityException
	 */
	Collection<User> getRosterEntries() throws ParityException {
		synchronized(xmppHelperLock) {
			assertIsLoggedIn("getRosterEntries", xmppHelper);
			try { return xmppHelper.getRosterEntries(); }
			catch(SmackException sx) {
				logger.error("getRosterEntries()", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("getRosterEntries()", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Determine whether or not a user is logged in.
	 * 
	 * @return True if the user is logged in, false otherwise.
	 */
	Boolean isLoggedIn() {
		synchronized(xmppHelperLock) { return xmppHelper.isLoggedIn(); }
	}

	/**
	 * Establish a new xmpp session.
	 * 
	 * @param username
	 *            The login.
	 * @param password
	 *            The login password.
	 * @throws ParityException
	 */
	void login(final String username, final String password) throws ParityException {
		final String host = preferences.getServerHost();
		final Integer port = preferences.getServerPort();
		synchronized(xmppHelperLock) {
			try { xmppHelper.login(host, port, username, password); }
			catch(SmackException sx) {
				logger.error("login(String,String)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("login(String,String)", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Terminate the current session.
	 * @throws ParityException
	 */
	void logout() throws ParityException {
		synchronized(xmppHelperLock) {
			assertIsLoggedIn("logout", xmppHelper);
			try { xmppHelper.logout(); }
			catch(SmackException sx) {
				logger.error("logout()", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("logout()", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Remove a session listener from the session.
	 * 
	 * @param sessionListener
	 */
	void removeListener(final SessionListener sessionListener) {
		Assert.assertNotNull("Cannot remove a null session listener.",
				sessionListener);
		if(sessionListeners.contains(sessionListener))
			sessionListeners.remove(sessionListener);
	}

	/**
	 * Send a document to a list of parity users. The document is converted from
	 * a parity object into an xmpp document in order to send it, then each user
	 * is sent the document.
	 * 
	 * @param users
	 *            The list of parity users to send to.
	 * @param document
	 *            The document to send.
	 * @throws ParityException
	 */
	void send(final Collection<User> users, final Document document)
			throws ParityException {
		synchronized(xmppHelperLock) {
			assertIsLoggedIn("send(Collection<User>,Document)", xmppHelper);
			try {
				// create a new version (updating local content into the
				// document content metadata) then send.
				final DocumentModel documentModel = getDocumentModel();
				documentModel.createVersion(
						document, DocumentAction.SEND, createSendDocumentActionData());
				xmppHelper.send(
						users, XMPPDocument.create(
								document, documentModel.getContent(document)));
			}
			catch(SmackException sx) {
				logger.error("send(Collection<User>,Document)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("send(Collection<User>,Document)", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	private DocumentActionData createSendDocumentActionData() {
		return new DocumentActionData();
	}

	/**
	 * Send a message to a list of parity users.
	 * 
	 * @param users
	 *            The list of parity users to send to.
	 * @param message
	 *            The message to send.
	 * @throws ParityException
	 */
	void send(final Collection<User> users, final String message)
			throws ParityException {
		synchronized(xmppHelperLock) {
			assertIsLoggedIn("send(Collection<User>,String)", xmppHelper);
			try { xmppHelper.send(users, message); }
			catch(SmackException sx) {
				logger.error("send(Collection<User>,String)", sx);
				throw ParityErrorTranslator.translate(sx);
			}
			catch(RuntimeException rx) {
				logger.error("send(Collection<User>,String)", rx);
				throw ParityErrorTranslator.translate(rx);
			}
		}
	}

	/**
	 * Update a roster entry for the currently logged in user.
	 * 
	 * @param user
	 *            The roster entry to update.
	 * @throws ParityException
	 */
	void updateRosterEntry(final User user) throws ParityException {
		synchronized(xmppHelperLock) {
			assertIsLoggedIn("updateRosterEntry", xmppHelper);
			xmppHelper.updateRosterEntry(user);
		}
	}

	/**
	 * Assert that the user is currently logged in.
	 * 
	 * @param message
	 *            Message to display in the assertion.
	 * @param xmppHelper
	 *            A handle to the xmpp helper in order to determine logged in
	 *            status.
	 */
	private void assertIsLoggedIn(final String message,
			final SessionModelXMPPHelper xmppHelper) {
		Assert.assertTrue(message, xmppHelper.isLoggedIn());
	}
}
