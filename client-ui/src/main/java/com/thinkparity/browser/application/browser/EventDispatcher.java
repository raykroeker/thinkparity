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
    
    /** A thinkParity container interface listener. */
    private ContainerListener containerListener;

	/** A thinkParity session interface listener.*/
	private SessionListener sessionSessionListener;

	/** A thinkParity system message interface listener. */
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
            @Override
            public void containerClosed(final ContainerEvent e) {
                if(e.isRemote()) {
                    browser.getArtifactModel().removeFlagSeen(e.getContainer().getId());
                    // Note we don't use "remote" because the effect of this would be
                    // to put the closed container at the top of the list.
                    browser.fireContainerUpdated(e.getContainer().getId());
                }                
            }
            @Override
            public void containerCreated(final ContainerEvent e) {
                if(e.isRemote()) {
                    browser.fireContainerCreated(e.getContainer().getId(), Boolean.TRUE);
                }                
            }            
            @Override
            public void containerDeleted(final ContainerEvent e) {
                // This is never a remote event because it can only happen before the new package is published.
                browser.fireContainerDeleted(e.getContainer().getId(), Boolean.TRUE);
            }
            @Override
            public void containerReactivated(final ContainerEvent e) {
                if(e.isRemote()) {
                    browser.fireContainerUpdated(e.getContainer().getId(), Boolean.TRUE);
                }                
            }
            @Override
            public void documentRemoved(final ContainerEvent e) {
                // This is never a remote event. You would instead get an event when the package is published.
                browser.fireDocumentDeleted(e.getContainer().getId(), e.getDocument().getId(), Boolean.TRUE);
            }
            @Override
            public void teamMemberAdded(final ContainerEvent e) {
                if(e.isRemote()) {
                    browser.fireContainerTeamMemberAdded(e.getContainer().getId());
                }
            }
            @Override
            public void teamMemberRemoved(ContainerEvent e) {
                if(e.isRemote()) {
                    browser.fireContainerTeamMemberRemoved(e.getContainer().getId());
                }
            }
        };     
    }

    /**
     * Create a session listener.
     * 
     * @return A session listener.
     */
	private SessionListener createSessionModelSessionListener() {
        return new SessionAdapter() {
            @Override
            public void sessionEstablished() { browser.fireConnectionOnline(); }
            @Override
            public void sessionTerminated() { browser.fireConnectionOffline(); }
            @Override
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
