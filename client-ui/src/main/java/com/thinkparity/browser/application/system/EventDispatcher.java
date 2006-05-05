/*
 * Mar 18, 2006
 */
package com.thinkparity.browser.application.system;

import com.thinkparity.model.parity.api.events.DocumentAdapter;
import com.thinkparity.model.parity.api.events.DocumentEvent;
import com.thinkparity.model.parity.api.events.DocumentListener;
import com.thinkparity.model.parity.api.events.SessionListener;
import com.thinkparity.model.parity.api.events.SystemMessageEvent;
import com.thinkparity.model.parity.api.events.SystemMessageListener;
import com.thinkparity.model.parity.model.message.system.SystemMessage;

/**
 * The system application's event dispatcher.  
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class EventDispatcher {

	/** The document listener. */
	private DocumentListener documentListener;

	/** The application. */
	private final SystemApplication systemApplication;

    /** The session listener. */
    private SessionListener sessionListener;

	/** The system message listener. */
	private SystemMessageListener systemMessageListener;

	/**
	 * Create an EventDispatcher.
	 * 
	 * @param sysApp
	 *            The system application.
	 */
	EventDispatcher(final SystemApplication systemApplication) {
		super();
		this.systemApplication = systemApplication;
	}

    /**
     * End the event dispatcher. This will remove the document and system
     * message listeners.
     * 
     */
	void end() {
		systemApplication.getDocumentModel().removeListener(documentListener);
		documentListener = null;

        systemApplication.getSessionModel().removeListener(sessionListener);
        sessionListener = null;

		systemApplication.getSystemMessageModel().removeListener(systemMessageListener);
		systemMessageListener = null;
	}

    /**
     * Start the event dispatcher. This registers a document and system message
     * listener.
     * 
     */
	void start() {
		documentListener = createDocumentListener();
		systemApplication.getDocumentModel().addListener(documentListener);

        sessionListener = createSessionListener();
        systemApplication.getSessionModel().addListener(sessionListener);

		systemMessageListener = createSystemMessageListener();
		systemApplication.getSystemMessageModel().addListener(systemMessageListener);
	}

	/**
	 * Create an update listener for the document model.
	 * 
	 * @return The creation listener.
	 */
	private DocumentListener createDocumentListener() {
		return new DocumentAdapter() {
            public void documentClosed(final DocumentEvent e) {
                if(e.isRemote())
                    systemApplication.fireDocumentClosed(e.getDocument());
            }
            public void documentCreated(final DocumentEvent e) {
                if(e.isRemote())
                    systemApplication.fireDocumentCreated(e.getDocument());
            }
            public void documentUpdated(final DocumentEvent e) {
                if(e.isRemote())
                    systemApplication.fireDocumentUpdated(e.getDocument());
			}
            public void keyRequestAccepted(final DocumentEvent e) {
                if(e.isRemote())
                    systemApplication.fireDocumentKeyRequestAccepted(e.getUser(), e.getDocument());
            }
            public void keyRequestDeclined(final DocumentEvent e) {
                if(e.isRemote())
                    systemApplication.fireDocumentKeyRequestDeclined(e.getUser(), e.getDocument());
            }
            public void keyRequested(final DocumentEvent e) {
                if(e.isRemote())
                    systemApplication.fireDocumentKeyRequested(e.getUser(), e.getDocument());
            }
            public void teamMemberAdded(final DocumentEvent e) {
                if(e.isRemote()) {
                    systemApplication.fireDocumentTeamMemberAdded(e.getUser(), e.getDocument());
                }
            }
		};
	}

    private SessionListener createSessionListener() {
        return new SessionListener() {
            public void sessionEstablished() {
                systemApplication.fireSessionEstablished();
            }
            public void sessionTerminated() {
                systemApplication.fireSessionTerminated();
            }
            public void sessionTerminated(final Throwable t) {
                systemApplication.fireSessionTerminated();
            }
        };
    }

	/**
	 * Create a system message listener.
	 * 
	 * @return The system message listener.
	 */
	private SystemMessageListener createSystemMessageListener() {
		return new SystemMessageListener() {
			public void systemMessageCreated(
					final SystemMessageEvent systemMessageEvent) {
				systemApplication.fireSystemMessageCreated((SystemMessage) systemMessageEvent.getSource());
			}
		};
	}
}
