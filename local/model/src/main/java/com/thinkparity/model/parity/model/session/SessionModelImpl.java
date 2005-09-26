/*
 * Mar 7, 2005
 */
package com.thinkparity.model.parity.model.session;

import java.util.Collection;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.assertion.Assert;


import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.api.document.Document;
import com.thinkparity.model.parity.api.document.DocumentApi;
import com.thinkparity.model.parity.api.document.DocumentVersion;
import com.thinkparity.model.parity.api.events.PresenceEvent;
import com.thinkparity.model.parity.api.events.PresenceListener;
import com.thinkparity.model.parity.api.events.SessionListener;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.workspace.Preferences;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.parity.util.LoggerFactory;
import com.thinkparity.model.parity.util.prefs.ParityPrefs;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.XMPPSession;
import com.thinkparity.model.xmpp.XMPPSessionFactory;
import com.thinkparity.model.xmpp.events.XMPPExtensionListener;
import com.thinkparity.model.xmpp.events.XMPPPresenceListener;
import com.thinkparity.model.xmpp.events.XMPPSessionListener;
import com.thinkparity.model.xmpp.user.User;

/**
 * SessionModelImpl
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class SessionModelImpl extends AbstractModelImpl {

	/**
	 * Handle to an internal logger.
	 */
	private static final Logger logger =
		LoggerFactory.createInstance(SessionModelImpl.class);

	/**
	 * List of all of the registered parity presence listeners.
	 */
	private static final Collection<PresenceListener> presenceListeners;

	/**
	 * Lock used to synchronize the collection access.
	 */
	private static final Object presenceListenersLock;

	/**
	 * List of all of the registered parity session listeners.
	 */
	private static final Collection<SessionListener> sessionListeners;

	/**
	 * Lock used to synchronize the collection access.
	 */
	private static final Object sessionListenersLock;

	// initialize the listeners
	static {
		presenceListeners = new Vector<PresenceListener>(3);
		presenceListenersLock = new Object();
		sessionListeners = new Vector<SessionListener>(3);
		sessionListenersLock = new Object();
	}

	/**
	 * XMPPExtensionListenerImpl
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class XMPPExtensionListenerImpl implements XMPPExtensionListener {
		/**
		 * @see com.thinkparity.model.xmpp.events.XMPPExtensionListener#documentReceived(com.thinkparity.model.parity.api.document.DocumentVersion)
		 */
		public void documentReceived(final DocumentVersion documentVersion) {			
			doNotifyDocumentReceived(documentVersion);
		}
	}

	/**
	 * XMPPPresenceListenerImpl
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class XMPPPresenceListenerImpl implements XMPPPresenceListener {
		/**
		 * @see com.thinkparity.model.xmpp.events.XMPPPresenceListener#presenceRequested(com.thinkparity.model.xmpp.user.User)
		 */
		public void presenceRequested(final User xmppUser) {
			doNotifyPresenceRequested(xmppUser);
		}
	}
	
	/**
	 * XMPPSessionListener
	 * This class is responsible for listening to the xmpp's session events and
	 * translating them into parity session events.
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class XMPPSessionListenerImpl implements XMPPSessionListener {
		/**
		 * @see com.thinkparity.model.xmpp.events.XMPPSessionListener#sessionEstablished()
		 */
		public void sessionEstablished() { doNotifySessionEstablished(); }
		/**
		 * @see com.thinkparity.model.xmpp.events.XMPPSessionListener#sessionTerminated()
		 */
		public void sessionTerminated() { doNotifySessionTerminated(); }
		/**
		 * @see com.thinkparity.model.xmpp.events.XMPPSessionListener#sessionTerminated(java.lang.Exception)
		 */
		public void sessionTerminated(Exception x) { doNotifySessionTerminated(x); }
	}

	/**
	 * Handle to the document model's api.
	 */
	private DocumentApi documentModel;

	private final Preferences preferences;

	private final Workspace workspace;

	/**
	 * Listens for extensions events generated by the xmpp library.
	 */
	private XMPPExtensionListenerImpl xmppExtensionListenerImpl;

	/**
	 * Instance of the class used to listen for xmpp presence events.
	 */
	private XMPPPresenceListenerImpl xmppPresenceListenerImpl;

	/**
	 * This reference should be the only reference to the Smack session within
	 * the client application.
	 */
	private XMPPSession xmppSession;

	/**
	 * Instance of the class used to listen for xmpp sesion events.
	 */
	private XMPPSessionListenerImpl xmppSessionListenerImpl;

	/**
	 * Create a SessionModelImpl
	 * @deprecated
	 */
	SessionModelImpl() { 
		super();
		initImpl();
		this.workspace = null;
		this.preferences = null;
	}

	SessionModelImpl(final Workspace workspace) {
		super();
		initImpl();
		this.workspace = workspace;
		this.preferences = workspace.getPreferences();
	}

	private void debug(final String variableName, final Object variableValue) {
		debug("", variableName, variableValue);
	}

	private void debug(final String context, final String variableName, final Object variableValue) {
		final StringBuffer message = new StringBuffer(context)
			.append("[").append(variableName).append("]")
			.append(variableValue);
		logger.debug(message);
	}

	/**
	 * Handle the event generated by xmppExtensionListenerImpl.
	 * 
	 * @param documentVersion
	 *            <code>com.thinkparity.model.parity.api.document.DocumentVersion</code>
	 */
	private void doNotifyDocumentReceived(final DocumentVersion documentVersion) {
		try {
			final Document document =
				documentModel.createDocument(documentVersion);
		}
		catch(ParityException px) {
			error("Could not receive document.", px);
		}
	}

	/**
	 * Notify all of the presence listeners that the user's presence has been
	 * requested.
	 * @param user
	 */
	private void doNotifyPresenceRequested(final User user) {
		synchronized(SessionModelImpl.presenceListenersLock) {
			for(PresenceListener listener : SessionModelImpl.presenceListeners) {
				listener.presenceRequested(new PresenceEvent(user));
			}
		}
	}

	/**
	 * Notify all of the session listeners that the session has been
	 * established.
	 */
	private void doNotifySessionEstablished() {
		synchronized(SessionModelImpl.sessionListenersLock) {
			for(SessionListener listener : SessionModelImpl.sessionListeners) {
				listener.sessionEstablished();
			}
		}
	}

	/**
	 * Notify all of the session listeners that the session has been terminated.
	 */
	private void doNotifySessionTerminated() {
		synchronized(SessionModelImpl.sessionListenersLock) {
			for(SessionListener listener : SessionModelImpl.sessionListeners) {
				listener.sessionTerminated();
			}
		}
	}

	/**
	 * Notify all of the session listeners that the session has been terminated
	 * due to an unknown cause.
	 * @param x <code>java.lang.Exception</code>
	 */
	private void doNotifySessionTerminated(final Exception x) {
		synchronized(SessionModelImpl.sessionListenersLock) {
			for(SessionListener listener : SessionModelImpl.sessionListeners) {
				listener.sessionTerminated(x);
			}
		}
	}

	private ParityException error(final String message, final Throwable cause) {
		logger.error(message, cause);
		return new ParityException(message);
	}

	/**
	 * @deprecated
	 */
	private void initImpl() {
		this.xmppSession = XMPPSessionFactory.getSession();

		this.xmppPresenceListenerImpl = new XMPPPresenceListenerImpl();
		this.xmppSession.addListener(xmppPresenceListenerImpl);

		this.xmppSessionListenerImpl = new XMPPSessionListenerImpl();
		this.xmppSession.addListener(this.xmppSessionListenerImpl);

		this.xmppExtensionListenerImpl = new XMPPExtensionListenerImpl();
		this.xmppSession.addListener(this.xmppExtensionListenerImpl);
		this.documentModel = DocumentApi.getModel();
	}

	void acceptPresence(final User xmppUser) throws ParityException {
		try { xmppSession.acceptPresence(xmppUser); }
		catch(SmackException sx) {
			throw error("Could not accept presence.", sx);
		}
	}

	void addListener(final PresenceListener presenceListener) {
		Assert.assertNotNull("Cannot register a null presence listener.",
				presenceListener);
		synchronized(SessionModelImpl.presenceListenersLock) {
			Assert.assertTrue("Cannot re-register the same presence listener.",
					!SessionModelImpl.presenceListeners.contains(presenceListener));
			SessionModelImpl.presenceListeners.add(presenceListener);
		}
	}

	void addListener(final SessionListener sessionListener) {
		Assert.assertNotNull("Cannot register a null session listener.",
				sessionListener);
		Assert.assertTrue("Cannot re-register the same session listener.",
				!sessionListeners.contains(sessionListener));
		sessionListeners.add(sessionListener);
	}

	void addRosterEntry(final User xmppUser)
			throws ParityException {
		try { xmppSession.addRosterEntry(xmppUser); }
		catch(SmackException sx) {
			throw error("Could not add roster entry.", sx);
		}
	}

	void debugRoster() {
		xmppSession.debugRoster();
	}

	void denyPresence(final User xmppUser) throws ParityException {}

	Collection<User> getRosterEntries() throws ParityException {
		Assert.assertTrue(
			"Cannot obtain roster entries if the session is not established.",
			isLoggedIn());
		try { return xmppSession.getRosterEntries(); }
		catch(SmackException sx) {
			throw error("Could not obtain the user's roster entries.", sx);
		}
	}

	Boolean isLoggedIn() { return xmppSession.isLoggedIn(); }

	void login(final String host, final Integer port, final String username,
			final String password) throws ParityException {
		try {
			xmppSession.login(host, port, username, password);
			ParityPrefs.setUsername(username);
		}
		catch(SmackException sx) {
			throw error("Could not establish the user's parity session.", sx);
		}
	}

	void login(final String username, final String password) throws ParityException {
		login(preferences.getServerHost(), preferences.getServerPort(),
				username, password);
	}

	void logout() throws ParityException {
		Assert.assertTrue("Cannot logout if the session is not established.",
				isLoggedIn());
		try { xmppSession.logout(); }
		catch(SmackException sx) {
			throw error("Could not terminate the user's parity session.", sx);
		}
	}

	void removeListener(final SessionListener sessionListener) {
		Assert.assertNotNull("Cannot remove a null session listener.",
				sessionListener);
		if(sessionListeners.contains(sessionListener))
			sessionListeners.remove(sessionListener);
	}

	void send(final User user, final DocumentVersion documentVersion)
			throws ParityException {
		debug("user", user);
		debug("document", documentVersion);
		try { xmppSession.send(user, documentVersion); }
		catch(SmackException sx) { throw error("Could not send document.", sx); }
	}
}
