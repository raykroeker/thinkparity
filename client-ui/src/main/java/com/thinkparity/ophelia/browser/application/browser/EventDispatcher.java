/*
 * Jan 19, 2006
 */
package com.thinkparity.ophelia.browser.application.browser;

import com.thinkparity.ophelia.model.events.ContactAdapter;
import com.thinkparity.ophelia.model.events.ContactEvent;
import com.thinkparity.ophelia.model.events.ContactListener;
import com.thinkparity.ophelia.model.events.ContainerAdapter;
import com.thinkparity.ophelia.model.events.ContainerListener;
import com.thinkparity.ophelia.model.events.SessionAdapter;
import com.thinkparity.ophelia.model.events.SessionListener;

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
            public void contactDeleted(final ContactEvent e) {
                browser.fireContactDeleted(e.getContact().getId(), e.isRemote());
            }
            @Override
            public void contactUpdated(final ContactEvent e) {
                browser.fireContactUpdated(e.getContact().getId());
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
            public void incomingInvitationDeleted(final ContactEvent e) {
                browser.fireIncomingContactInvitationDeleted(e.getIncomingInvitation().getId(), e.isRemote());
            }
            @Override
            public void outgoingInvitationAccepted(final ContactEvent e) {
                browser.fireOutgoingContactInvitationAccepted(e.getContact().getId(), e.getOutgoingInvitation().getId(), e.isRemote());
            }
            @Override
            public void outgoingInvitationCreated(final ContactEvent e) {
                browser.fireOutgoingContactInvitationCreated(e.getOutgoingInvitation().getId(), e.isRemote());
            }
            @Override
            public void outgoingInvitationDeclined(final ContactEvent e) {
                browser.fireOutgoingContactInvitationDeclined(e.getOutgoingInvitation().getId(), e.isRemote());
            }
            @Override
            public void outgoingInvitationDeleted(final ContactEvent e) {
                browser.fireOutgoingContactInvitationDeleted(e.getOutgoingInvitation().getId(), e.isRemote());
            }
        };
    }

    /**
     * Create a container listener for the browser application.
     * 
     * @return A <code>ContainerListener</code>.
     */
    private ContainerListener createContainerListener() {
        return new ContainerAdapter() {};
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
