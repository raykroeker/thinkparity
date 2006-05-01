/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.application.browser;

import com.thinkparity.model.parity.api.events.*;
import com.thinkparity.model.parity.model.message.system.SystemMessage;

/**
 * The browser's event dispatcher.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class EventDispatcher {

	/** The browser application. */
	protected final Browser browser;

	/** The document listener. */
	private DocumentListener documentListener;

	/** The parity session key event listener. */
	private KeyListener sessionKeyListener;

	/** The parity session event listener.*/
	private SessionListener sessionSessionListener;

	/** The parity system message event listener. */
	private SystemMessageListener systemMessageListener;

	/**
     * Create an EventDispatcher.
     * 
     * @param browser
     *            The browser application.
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
		browser.getDocumentModel().removeListener(documentListener);
		documentListener = null;
		
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
		documentListener = createDocumentListener();
		browser.getDocumentModel().addListener(documentListener);

		sessionKeyListener = createSessionModelKeyListener();
		browser.getSessionModel().addListener(sessionKeyListener);

		sessionSessionListener = createSessionModelSessionListener();
		browser.getSessionModel().addListener(sessionSessionListener);

		systemMessageListener = createSystemMessageListener();
		browser.getSystemMessageModel().addListener(systemMessageListener);
	}

    /**
     * Create a document listener.
     * 
     * @return A document listener.
     */
	private DocumentListener createDocumentListener() {
		return new DocumentAdapter() {
            public void confirmationReceived(final DocumentEvent e) {
                if(e.isRemote()) {
                    browser.fireDocumentConfirmationReceived(e.getDocument().getId());
                }
            }
            public void documentClosed(final DocumentEvent e) {
                if(e.isRemote()) {
                    browser.getArtifactModel().removeFlagSeen(e.getDocument().getId());
                    browser.fireDocumentUpdated(e.getDocument().getId());
                }
            }
            public void documentCreated(final DocumentEvent e) {
                if(e.isRemote()) {
                    browser.fireDocumentCreated(e.getDocument().getId(), Boolean.TRUE);
                }
            }
            public void documentUpdated(final DocumentEvent e) {
                if(e.isRemote()) {
                    browser.fireDocumentUpdated(e.getDocument().getId(), Boolean.TRUE);
                }
            }
            public void keyRequestAccepted(final DocumentEvent e) {
                if(e.isRemote()) {
                    browser.fireDocumentUpdated(e.getDocument().getId(), e.isRemote());
                }
            }
            public void keyRequestDeclined(final DocumentEvent e) {
                if(e.isRemote()) {
                    browser.fireDocumentUpdated(e.getDocument().getId(), e.isRemote());
                }
            }
            public void keyRequested(final DocumentEvent e) {
                if(e.isRemote()) {
                    browser.fireDocumentUpdated(e.getDocument().getId(), e.isRemote());
                }

            }
		};
	}

	private KeyListener createSessionModelKeyListener() {
		return new KeyListener() {
			public void keyRequestAccepted(final KeyEvent e) {
				browser.fireDocumentUpdated(e.getArtifactId(), Boolean.TRUE);
			}
			public void keyRequestDenied(final KeyEvent e) {
				browser.fireDocumentUpdated(e.getArtifactId(), Boolean.TRUE);
			}
			public void keyRequested(final KeyEvent e) {
				browser.fireDocumentUpdated(e.getArtifactId(), Boolean.TRUE);
			}
		};
	}

    /**
     * Create a session listener.
     * 
     * @return A session listener.
     */
	private SessionListener createSessionModelSessionListener() {
		return new SessionListener() {
			public void sessionEstablished() { browser.fireSessionEstablished(); }
			public void sessionTerminated() { browser.fireSessionTerminated(); }
			public void sessionTerminated(final Throwable cause) {
				sessionTerminated();
			}
		};
	}

	private SystemMessageListener createSystemMessageListener() {
		return new SystemMessageListener() {
			public void systemMessageCreated(
					final SystemMessageEvent systemMessageEvent) {
				browser.fireSystemMessageCreated(((SystemMessage) systemMessageEvent.getSource()).getId());
			}
		};
	}
}
