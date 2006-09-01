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

	/** The browser application. */
	protected final Browser browser;
    
    /** A thinkParity contact interface listener. */
    private ContactListener contactListener;

	/** A thinkParity container interface listener. */
    private ContainerListener containerListener;

	/** A thinkParity session interface listener.*/
	private SessionListener sessionListener;

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
        browser.removeListener(contactListener);
        contactListener = null;

        browser.getContainerModel().removeListener(containerListener);
        containerListener = null;
        
		browser.getSessionModel().removeListener(sessionListener);
		sessionListener = null;
	}

	/**
	 * Start the event dispatcher.
	 *
	 */
	void start() {
        contactListener = createContactListener();
        browser.addListener(contactListener);

        containerListener = createContainerListener();
        browser.getContainerModel().addListener(containerListener);
        
		sessionListener = createSessionListener();
		browser.getSessionModel().addListener(sessionListener);
	}
    
    private ContactListener createContactListener() {
        return new ContactAdapter() {
            @Override
            public void outgoingInvitationAccepted(final ContactEvent e) {
                browser.fireOutgoingContactInvitationAccepted(e.getContact().getId(), e.getOutgoingInvitation().getId(), e.isRemote());
            }
            @Override
            public void incomingInvitationAccepted(final ContactEvent e) {
                browser.fireIncomingContactInvitationAccepted(e.getContact().getId(), e.getIncomingInvitation().getId(), e.isRemote());
            }
            @Override
            public void incomingInvitationCreated(final ContactEvent e) {
                browser.fireIncomingContactInvitationCreated(e.getIncomingInvitation().getId(), e.isRemote());
            }
            @Override
            public void incomingInvitationDeclined(final ContactEvent e) {
                browser.fireIncomingContactInvitationDeclined(e.getIncomingInvitation().getId(), e.isRemote());
            }
            @Override
            public void outgoingInvitationCreated(final ContactEvent e) {
                browser.fireOutgoingContactInvitationCreated(e.getOutgoingInvitation().getId(), e.isRemote());
            }
            @Override
            public void outgoingInvitationDeleted(final ContactEvent e) {
                browser.fireOutgoingContactInvitationDeleted(e.getOutgoingInvitation().getId(), e.isRemote());
            }
        };
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
    private ContainerListener createContainerListener() {
        return new ContainerAdapter() {
            @Override
            public void containerCreated(final ContainerEvent e) {
                browser.fireContainerCreated(e.getContainer().getId(), e.isRemote());
            }
            @Override
            public void containerDeleted(final ContainerEvent e) {
                browser.fireContainerDeleted(e.getContainer().getId(), Boolean.FALSE);
            }
            @Override
            public void documentAdded(final ContainerEvent e) {
                browser.fireContainerDocumentAdded(
                        e.getContainer().getId(), e.getDocument().getId());
            }
            @Override
            public void documentRemoved(final ContainerEvent e) {
                browser.fireContainerDocumentRemoved(e.getDocument().getId());
            }
            @Override
            public void draftPublished(final ContainerEvent e) {
                browser.fireContainerCreated(e.getContainer().getId(), e.isRemote());
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
	private SessionListener createSessionListener() {
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
}
