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
    
    /** The container listener. */
    private ContainerListener containerListener;

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
        browser.getContainerModel().removeListener(containerListener);
        containerListener = null;
        
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
        containerListener = createContainerListener();
        browser.getContainerModel().addListener(containerListener);
        
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
     * Create a container listener.
     * Some notes as of July 28, 2006...
     *   - Closing and reactivating a package won't be supported (in the GUI)
     *   - Adding and removing documents won't be a remote event since this will
     *     be done in a draft, and then show up when the package is published.
     *   - Deleting a package won't be a remote event since this can only be
     *     done in the draft, before the package is published for the first time.
     * 
     * @return A container listener.
     */
    // TO DO Need to add: archived, published, confirmation received, 3 key messages, 2 team messages
    private ContainerListener createContainerListener() {
        return new ContainerAdapter() {
            public void containerClosed(final ContainerEvent e) {
                if(e.isRemote()) {
                    browser.getArtifactModel().removeFlagSeen(e.getContainer().getId());
                    // Note we don't use "remote" because the effect of this would be
                    // to put the closed container at the top of the list.
                    browser.fireContainerUpdated(e.getContainer().getId());
                }                
            }
            public void containerReactivated(final ContainerEvent e) {
                if(e.isRemote()) {
                    browser.fireContainerUpdated(e.getContainer().getId(), Boolean.TRUE);
                }                
            }            
            public void containerCreated(final ContainerEvent e) {
                if(e.isRemote()) {
                    browser.fireContainerCreated(e.getContainer().getId(), Boolean.TRUE);
                }                
            }
            public void containerDeleted(final ContainerEvent e) {
                // This is never a remote event because it can only happen before the new package is published.
                browser.fireContainerDeleted(e.getContainer().getId(), Boolean.TRUE);
            }
            public void documentAdded(final ContainerEvent e) {
                // This is never a remote event. You would instead get an event when the package is published.
                browser.fireDocumentCreated(e.getContainer().getId(), e.getDocument().getId(), Boolean.TRUE);
            }
            public void documentRemoved(final ContainerEvent e) {
                // This is never a remote event. You would instead get an event when the package is published.
                browser.fireDocumentDeleted(e.getContainer().getId(), e.getDocument().getId(), Boolean.TRUE);
            }
            // TO DO the following don't exist, yet.
/*
            public void teamMemberAdded(final ContainerEvent e) {
                if(e.isRemote()) {
                    browser.fireContainerTeamMemberAdded(e.getContainer().getId());
                }
            }
            public void teamMemberRemoved(ContainerEvent e) {
                if(e.isRemote()) {
                    browser.fireContainerTeamMemberRemoved(e.getContainer().getId());
                }
            }
*/
        };     
    }

    /**
     * Create a document listener.
     * Some notes as of July 28, 2006...
     *  - Close is discontinued, replaced by the container event "containerClosed"
     *  - Reactivate is discontinued, replaced by the container event "containerReactivated"
     *  - Created is discontinued, replaced by the container event "documentAdded"
     *  - Deleted is discontinued, replaced by the container event "documentRemoved"
     *  - Archived is discontinued, will be replaced by a container event.
     *  - Published is discontinued, will be replaced by a container event.
     *  - Updated is discontinued.
     *  - teamMemberAdded is discontinued, will be replaced by a container event.
     *  - teamMemberRemoved is discontinued, will be replaced by a container event.
     *  - keyRequestAccepted is discontinued.
     *  - keyRequestDeclined is discontinued.
     *  - keyRequested is discontinued.
     * 
     * @return A document listener.
     */
    // TO DO Remove these events
	private DocumentListener createDocumentListener() {
		return new DocumentAdapter() {
/*
            public void confirmationReceived(final DocumentEvent e) {
                if(e.isRemote()) {
                    browser.fireDocumentConfirmationReceived(e.getDocument().getId());
                }
            }   
            public void keyRequestAccepted(final DocumentEvent e) {
                if(e.isRemote()) {
                    browser.fireDocumentUpdated(null, e.getDocument().getId(), e.isRemote());
                }
            }
            public void keyRequestDeclined(final DocumentEvent e) {
                if(e.isRemote()) {
                    browser.fireDocumentUpdated(null, e.getDocument().getId(), e.isRemote());
                }
            }
            public void keyRequested(final DocumentEvent e) {
                if(e.isRemote()) {
                    browser.fireDocumentUpdated(null, e.getDocument().getId(), e.isRemote());
                }
            }*/
		};
	}

	private KeyListener createSessionModelKeyListener() {
		return new KeyListener() {
			public void keyRequestAccepted(final KeyEvent e) {
				browser.fireContainerUpdated(e.getArtifactId(), Boolean.TRUE);
			}
			public void keyRequestDenied(final KeyEvent e) {
				browser.fireContainerUpdated(e.getArtifactId(), Boolean.TRUE);
			}
			public void keyRequested(final KeyEvent e) {
                browser.runKeyRequested(e.getArtifactId());
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
			public void sessionEstablished() { browser.fireConnectionOnline(); }
			public void sessionTerminated() { browser.fireConnectionOffline(); }
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
