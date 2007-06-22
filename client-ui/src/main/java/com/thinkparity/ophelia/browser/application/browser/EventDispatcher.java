/*
 * Jan 19, 2006
 */
package com.thinkparity.ophelia.browser.application.browser;

import com.thinkparity.ophelia.model.events.ContactAdapter;
import com.thinkparity.ophelia.model.events.ContactEvent;
import com.thinkparity.ophelia.model.events.ContactListener;
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

		sessionListener = createSessionListener();
		browser.getSessionModel().addListener(sessionListener);
	}
    
    private ContactListener createContactListener() {
        return new ContactAdapter() {
            @Override
            public void contactCreated(ContactEvent e) {
                browser.syncContactTabContact(e.getContact().getId(), e.isRemote());
            }
            @Override
            public void contactDeleted(final ContactEvent e) {
                browser.syncContactTabContact(e.getContact().getId(), e.isRemote());
            }
            @Override
            public void contactUpdated(final ContactEvent e) {
                browser.syncContactTabContact(e.getContact().getId(), e.isRemote());
            }
            @Override
            public void incomingEMailInvitationAccepted(final ContactEvent e) {
                browser.syncContactTabIncomingEMailInvitation(e.getIncomingInvitation().getId(), e.isRemote());
                browser.syncContactTabContact(e.getContact().getId(), e.isRemote());
            }
            @Override
            public void incomingEMailInvitationCreated(final ContactEvent e) {
                browser.syncContactTabIncomingEMailInvitation(e.getIncomingInvitation().getId(), e.isRemote());
            }
            @Override
            public void incomingEMailInvitationDeclined(final ContactEvent e) {
                browser.syncContactTabIncomingEMailInvitation(e.getIncomingInvitation().getId(), e.isRemote());
            }
            @Override
            public void incomingEMailInvitationDeleted(final ContactEvent e) {
                browser.syncContactTabIncomingEMailInvitation(e.getIncomingInvitation().getId(), e.isRemote());
            }
            @Override
            public void incomingUserInvitationAccepted(final ContactEvent e) {
                browser.syncContactTabIncomingUserInvitation(e.getIncomingInvitation().getId(), e.isRemote());
                browser.syncContactTabContact(e.getContact().getId(), e.isRemote());
            }
            @Override
            public void incomingUserInvitationCreated(final ContactEvent e) {
                browser.syncContactTabIncomingUserInvitation(e.getIncomingInvitation().getId(), e.isRemote());
            }
            @Override
            public void incomingUserInvitationDeclined(final ContactEvent e) {
                browser.syncContactTabIncomingUserInvitation(e.getIncomingInvitation().getId(), e.isRemote());
            }
            @Override
            public void incomingUserInvitationDeleted(final ContactEvent e) {
                browser.syncContactTabIncomingUserInvitation(e.getIncomingInvitation().getId(), e.isRemote());
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
     * Create a session listener.
     * 
     * @return A session listener.
     */
	private SessionListener createSessionListener() {
        return new SessionAdapter() {
            @Override
            public void sessionError(final Throwable cause) {
                browser.displayErrorDialog("SessionError", new Object[] {}, cause);
            }
        };
    }
}
