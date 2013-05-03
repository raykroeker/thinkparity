/*
 * Jan 19, 2006
 */
package com.thinkparity.ophelia.browser.application.browser;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.ophelia.model.events.*;

/**
 * The browser's event dispatcher.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class EventDispatcher {

	/** The browser application. */
	protected final Browser browser;
    
    /** A contact listener. */
    private ContactListener contactListener;

	/** A session listener.*/
	private SessionListener sessionListener;

	/** A profile listener. */
	private ProfileListener profileListener;

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

        profileListener = new ProfileAdapter() {
            @Override
            public void profileUpdated(final ProfileEvent e) {
                browser.reloadMenuBar();
            }
        };
        browser.addListener(profileListener);

		sessionListener = createSessionListener();
		browser.getSessionModel().addListener(sessionListener);
	}

    private ContactListener createContactListener() {
        return new ContactAdapter() {
            @Override
            public void contactCreated(final ContactEvent e) {
                syncContactTabContact(e);
            }
            @Override
            public void contactDeleted(final ContactEvent e) {
                syncContactTabContact(e);
            }
            @Override
            public void contactUpdated(final ContactEvent e) {
                syncContactTabContact(e);
            }
            @Override
            public void incomingEMailInvitationAccepted(final ContactEvent e) {
                syncContactTabIncomingEMailInvitation(e);
                syncContactTabContact(e);
            }
            @Override
            public void incomingEMailInvitationCreated(final ContactEvent e) {
                syncContactTabIncomingEMailInvitation(e);
            }
            @Override
            public void incomingEMailInvitationDeclined(final ContactEvent e) {
                syncContactTabIncomingEMailInvitation(e);
            }
            @Override
            public void incomingEMailInvitationDeleted(final ContactEvent e) {
                syncContactTabIncomingEMailInvitation(e);
            }
            @Override
            public void incomingUserInvitationAccepted(final ContactEvent e) {
                syncContactTabIncomingUserInvitation(e);
                syncContactTabContact(e);
            }
            @Override
            public void incomingUserInvitationCreated(final ContactEvent e) {
                syncContactTabIncomingUserInvitation(e);
            }
            @Override
            public void incomingUserInvitationDeclined(final ContactEvent e) {
                syncContactTabIncomingUserInvitation(e);
            }
            @Override
            public void incomingUserInvitationDeleted(final ContactEvent e) {
                syncContactTabIncomingUserInvitation(e);
            }
            @Override
            public void outgoingEMailInvitationAccepted(final ContactEvent e) {
                syncContactTabOutgoingEMailInvitation(e);
                syncContactTabContact(e);
            }
            @Override
            public void outgoingEMailInvitationCreated(final ContactEvent e) {
                syncContactTabOutgoingEMailInvitation(e);
            }
            @Override
            public void outgoingEMailInvitationDeclined(final ContactEvent e) {
                syncContactTabOutgoingEMailInvitation(e);
            }
            @Override
            public void outgoingEMailInvitationDeleted(final ContactEvent e) {
                syncContactTabOutgoingEMailInvitation(e);
            }
            @Override
            public void outgoingUserInvitationAccepted(final ContactEvent e) {
                syncContactTabOutgoingUserInvitation(e);
                syncContactTabContact(e);
            }
            @Override
            public void outgoingUserInvitationCreated(final ContactEvent e) {
                syncContactTabOutgoingUserInvitation(e);
            }
            @Override
            public void outgoingUserInvitationDeclined(final ContactEvent e) {
                syncContactTabOutgoingUserInvitation(e);
            }
            @Override
            public void outgoingUserInvitationDeleted(final ContactEvent e) {
                syncContactTabOutgoingUserInvitation(e);
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

    /**
     * Sync the contact tab contact.
     * 
     * @param e
     *            A <code>ContactEvent</code>.
     */
    private void syncContactTabContact(final ContactEvent e) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                browser.syncContactTabContact(e.getContact(),
                        e.isRemote());
            }
        });
    }

    /**
     * Sync the contact tab incoming email invitation.
     * 
     * @param e
     *            A <code>ContactEvent</code>.
     */
    private void syncContactTabIncomingEMailInvitation(final ContactEvent e) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                browser.syncContactTabIncomingEMailInvitation(e.getIncomingInvitation().getId(), e.isRemote());
            }
        });
    }

    /**
     * Sync the contact tab incoming user invitation.
     * 
     * @param e
     *            A <code>ContactEvent</code>.
     */
    private void syncContactTabIncomingUserInvitation(final ContactEvent e) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                browser.syncContactTabIncomingUserInvitation(e.getIncomingInvitation().getId(), e.isRemote());
            }
        });
    }

    /**
     * Sync the contact tab outgoing email invitation.
     * 
     * @param e
     *            A <code>ContactEvent</code>.
     */
    private void syncContactTabOutgoingEMailInvitation(final ContactEvent e) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                browser.syncContactTabOutgoingEMailInvitation(e.getOutgoingInvitation().getId(), e.isRemote());
            }
        });
    }

    /**
     * Sync the contact tab outgoing user invitation.
     * 
     * @param e
     *            A <code>ContactEvent</code>.
     */
    private void syncContactTabOutgoingUserInvitation(final ContactEvent e) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                browser.syncContactTabOutgoingUserInvitation(e.getOutgoingInvitation().getId(), e.isRemote());
            }
        });
    }
}
