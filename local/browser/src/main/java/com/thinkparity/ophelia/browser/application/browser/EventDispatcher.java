/*
 * Jan 19, 2006
 */
package com.thinkparity.ophelia.browser.application.browser;

import com.thinkparity.ophelia.model.events.ContactAdapter;
import com.thinkparity.ophelia.model.events.ContactEvent;
import com.thinkparity.ophelia.model.events.ContactListener;
import com.thinkparity.ophelia.model.events.ContainerAdapter;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.events.ContainerListener;
import com.thinkparity.ophelia.model.events.ProfileAdapter;
import com.thinkparity.ophelia.model.events.ProfileEvent;
import com.thinkparity.ophelia.model.events.ProfileListener;
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
    
    /** A thinkParity profile interface listener. */
    private ProfileListener profileListener;

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
        
        browser.removeListener(profileListener);
        profileListener = null;
        
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
        
        profileListener = createProfileListener();
        browser.addListener(profileListener);
        
		sessionListener = createSessionListener();
		browser.getSessionModel().addListener(sessionListener);
	}
    
    private ContactListener createContactListener() {
        return new ContactAdapter() {
            @Override
            public void contactDeleted(final ContactEvent e) {
                browser.syncContactTabContact(e.getContact().getId(), e.isRemote());
            }
            @Override
            public void contactUpdated(final ContactEvent e) {
                browser.syncContactTabContact(e.getContact().getId(), e.isRemote());
            }
            @Override
            public void incomingInvitationAccepted(final ContactEvent e) {
                browser.syncContactTabIncomingInvitation(e.getIncomingInvitation().getId(), e.isRemote());
                browser.syncContactTabContact(e.getContact().getId(), e.isRemote());
            }
            @Override
            public void incomingInvitationCreated(final ContactEvent e) {
                browser.syncContactTabIncomingInvitation(e.getIncomingInvitation().getId(), e.isRemote());
            }
            @Override
            public void incomingInvitationDeclined(final ContactEvent e) {
                browser.syncContactTabIncomingInvitation(e.getIncomingInvitation().getId(), e.isRemote());
            }
            @Override
            public void incomingInvitationDeleted(final ContactEvent e) {
                browser.syncContactTabIncomingInvitation(e.getIncomingInvitation().getId(), e.isRemote());
            }
            @Override
            public void outgoingEMailInvitationAccepted(final ContactEvent e) {
                browser.syncContactTabOutgoingEMailInvitation(e.getOutgoingInvitation().getId(), e.isRemote());
                browser.syncContactTabContact(e.getContact().getId(), e.isRemote());
            }
            @Override
            public void outgoingEMailInvitationCreated(final ContactEvent e) {
                browser.syncContactTabOutgoingEMailInvitation(e.getOutgoingInvitation().getId(), e.isRemote());
            }
            @Override
            public void outgoingEMailInvitationDeclined(final ContactEvent e) {
                browser.syncContactTabOutgoingEMailInvitation(e.getOutgoingInvitation().getId(), e.isRemote());
            }
            @Override
            public void outgoingEMailInvitationDeleted(final ContactEvent e) {
                browser.syncContactTabOutgoingEMailInvitation(e.getOutgoingInvitation().getId(), e.isRemote());
            }
            @Override
            public void outgoingUserInvitationAccepted(final ContactEvent e) {
                browser.syncContactTabOutgoingUserInvitation(e.getOutgoingInvitation().getId(), e.isRemote());
                browser.syncContactTabContact(e.getContact().getId(), e.isRemote());
            }
            @Override
            public void outgoingUserInvitationCreated(final ContactEvent e) {
                browser.syncContactTabOutgoingUserInvitation(e.getOutgoingInvitation().getId(), e.isRemote());
            }
            @Override
            public void outgoingUserInvitationDeclined(final ContactEvent e) {
                browser.syncContactTabOutgoingUserInvitation(e.getOutgoingInvitation().getId(), e.isRemote());
            }
            @Override
            public void outgoingUserInvitationDeleted(final ContactEvent e) {
                browser.syncContactTabOutgoingUserInvitation(e.getOutgoingInvitation().getId(), e.isRemote());
            }
        };
    }

    /**
     * Create a container listener for the browser application.
     * 
     * @return A <code>ContainerListener</code>.
     */
    private ContainerListener createContainerListener() {
        return new ContainerAdapter() {
            @Override
            public void containerRestored(final ContainerEvent e) {
                browser.fireContainerRestored(e);
            }
        };
    }
    
    /**
     * Create a profile listener for the browser application.
     * 
     * @return A <code>ProfileListener</code>.
     */
    private ProfileListener createProfileListener() {
        return new ProfileAdapter() {
            @Override
            public void emailAdded(final ProfileEvent e) {
                browser.syncContactTabProfile(e.isRemote());
            }
            @Override
            public void emailRemoved(final ProfileEvent e) {
                browser.syncContactTabProfile(e.isRemote());
            }
            @Override
            public void emailVerified(final ProfileEvent e) {
                browser.syncContactTabProfile(e.isRemote());
            }
            @Override
            public void passwordReset(final ProfileEvent e) {
                browser.syncContactTabProfile(e.isRemote());
            }
            @Override
            public void passwordUpdated(final ProfileEvent e) {
                browser.syncContactTabProfile(e.isRemote());
            }
            @Override
            public void profileUpdated(final ProfileEvent e) {
                browser.syncContactTabProfile(e.isRemote());
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
