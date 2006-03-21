/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.application.browser;

import com.thinkparity.model.parity.api.events.*;

/**
 * The browser's event dispatcher.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class EventDispatcher {

	/**
	 * The browser.
	 * 
	 */
	protected final Browser browser;

	/**
	 * The parity document creation event listener.
	 * 
	 */
	private CreationListener documentCreationListener;

	/**
	 * The parity document update event listener.
	 * 
	 */
	private UpdateListener documentUpdateListener;

	/**
	 * The parity session key event listener.
	 * 
	 */
	private KeyListener sessionKeyListener;

	/**
	 * The parity session presence event listener.
	 * 
	 */
	private PresenceListener sessionPresenceListener;

	/**
	 * The parity session event listener.
	 * 
	 */
	private SessionListener sessionSessionListener;

	/**
	 * The parity system message event listener.
	 * 
	 */
	private SystemMessageListener systemMessageListener;

	/**
	 * Create an EventDispatcher.
	 * 
	 */
	EventDispatcher(final Browser browser) {
		super();
		this.browser = browser;
	}

	/**
	 * End the event dispatcher.
	 *
	 */
	void end() {
		browser.getDocumentModel().removeListener(documentCreationListener);
		documentCreationListener = null;
		
		browser.getDocumentModel().removeListener(documentUpdateListener);
		documentUpdateListener = null;

		browser.getSessionModel().removeListener(sessionPresenceListener);
		sessionPresenceListener = null;

		browser.getSessionModel().removeListener(sessionKeyListener);
		sessionKeyListener = null;

		browser.getSessionModel().removeListener(sessionSessionListener);
		sessionSessionListener = null;

		browser.getSystemMessageModel().removeListener(systemMessageListener);
		systemMessageListener = null;
	}

	/**
	 * Start the event dispatcher.
	 *
	 */
	void start() {
		documentCreationListener = createDocumentModelCreationListener();
		browser.getDocumentModel().addListener(documentCreationListener);
		
		documentUpdateListener = createDocumentModelUpdateListener();
		browser.getDocumentModel().addListener(documentUpdateListener);

		sessionPresenceListener = createSessionModelPresenceListener();
		browser.getSessionModel().addListener(sessionPresenceListener);

		sessionKeyListener = createSessionModelKeyListener();
		browser.getSessionModel().addListener(sessionKeyListener);

		sessionSessionListener = createSessionModelSessionListener();
		browser.getSessionModel().addListener(sessionSessionListener);

		systemMessageListener = createSystemMessageListener();
		browser.getSystemMessageModel().addListener(systemMessageListener);
	}

	private CreationListener createDocumentModelCreationListener() {
		return new CreationListener() {
			public void objectCreated(final CreationEvent e) {}
			public void objectVersionCreated(final VersionCreationEvent e) {}
		};
	}

	private UpdateListener createDocumentModelUpdateListener() {
		return new UpdateListener() {
			public void objectClosed(CloseEvent closeEvent) {
				browser.reloadMainList();
			}
			public void objectDeleted(final DeleteEvent e) {}
			public void objectReceived(final UpdateEvent e) {
				browser.reloadMainList();
				browser.reloadHistoryList();
			}
		};
	}

	private KeyListener createSessionModelKeyListener() {
		return new KeyListener() {
			public void keyRequestAccepted(final KeyEvent e) {
				browser.reloadMainList();
			}
			public void keyRequestDenied(final KeyEvent e) {
				browser.reloadMainList();
			}
			public void keyRequested(final KeyEvent e) {
				browser.reloadMainList();
			}
		};
	}

	private PresenceListener createSessionModelPresenceListener() {
		return new PresenceListener() {
			public void presenceRequested(final PresenceEvent e) {
				browser.reloadMainList();
			}
		};
	}

	private SessionListener createSessionModelSessionListener() {
		return new SessionListener() {
			public void sessionEstablished() {}
			public void sessionTerminated() {}
			public void sessionTerminated(final Throwable cause) {
				sessionTerminated();
			}
		};
	}

	private SystemMessageListener createSystemMessageListener() {
		return new SystemMessageListener() {
			public void systemMessageCreated(
					final SystemMessageEvent systemMessageEvent) {
				browser.reloadMainList();
			}
		};
	}
}
