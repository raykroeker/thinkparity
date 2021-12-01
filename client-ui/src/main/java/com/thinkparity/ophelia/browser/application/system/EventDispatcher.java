/*
 * Mar 18, 2006
 */
package com.thinkparity.ophelia.browser.application.system;

import com.thinkparity.ophelia.model.events.*;

/**
 * The system application's event dispatcher.  
 * 
 * @author raymond@raykroeker.com
 * @version 1.1
 */
class EventDispatcher {

	/** A thinkParity <code>ContactListener</code>. */
    private ContactListener contactListener;

    /** A thinkParity <code>ContainerListener</code>. */
    private ContainerListener containerListener;

    /** A thinkParity <code>DocumentListener</code>. */
	private DocumentListener documentListener;

    /** A thinkParity <code>MigratorListener</code>. */
    private MigratorListener migratorListener;

    /** A thinkParity <code>ProfileListener</code>. */
    private ProfileListener profileListener;

    /** A thinkParity <code>SessionListener</code>. */
    private SessionListener sessionListener;

    /** The <code>SystemApplication</code>. */
	private final SystemApplication systemApplication;

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
        systemApplication.getContactModel().removeListener(contactListener);
        contactListener = null;

        systemApplication.getContainerModel().removeListener(containerListener);
        containerListener = null;

		systemApplication.getDocumentModel().removeListener(documentListener);
		documentListener = null;

        systemApplication.getMigratorModel().removeListener(migratorListener);
        migratorListener = null;

        systemApplication.getProfileModel().removeListener(profileListener);
        profileListener = null;

        systemApplication.getSessionModel().removeListener(sessionListener);
        sessionListener = null;
	}

    /**
     * Start the event dispatcher. This registers a document and system message
     * listener.
     * 
     */
	void start() {
        contactListener = createContactListener();
        systemApplication.getContactModel().addListener(contactListener);

        containerListener = createContainerListener();
        systemApplication.getContainerModel().addListener(containerListener);

		documentListener = createDocumentListener();
		systemApplication.getDocumentModel().addListener(documentListener);
        
        migratorListener = createMigratorListener();
        systemApplication.getMigratorModel().addListener(migratorListener);

        profileListener = createProfileListener();
        systemApplication.getProfileModel().addListener(profileListener);

        sessionListener = createSessionListener();
        systemApplication.getSessionModel().addListener(sessionListener);
	}

    /**
     * Create a thinkParity contact listener.
     * 
     * @return A <code>ContactListener</code>.
     */
    private ContactListener createContactListener() {
        return new ContactAdapter() {
            @Override
            public void incomingEMailInvitationCreated(final ContactEvent e) {
                if (e.isRemote()) {
                    systemApplication.fireContactIncomingEMailInvitationCreated(e);                    
                }
            }
            @Override
            public void incomingUserInvitationCreated(final ContactEvent e) {
                if (e.isRemote()) {
                    systemApplication.fireContactIncomingUserInvitationCreated(e);                    
                }
            }
            @Override
            public void incomingEMailInvitationDeleted(final ContactEvent e) {
                if (e.isRemote())
                    systemApplication.fireContactIncomingEMailInvitationDeleted(e);
            }
            @Override
            public void incomingUserInvitationDeleted(final ContactEvent e) {
                if (e.isRemote())
                    systemApplication.fireContactIncomingUserInvitationDeleted(e);
            }
        };
    }

    private ContainerListener createContainerListener() {
        return new ContainerAdapter() {
            @Override
            public void containerPublished(final ContainerEvent e) {
                if (e.isRemote()) {
                    systemApplication.fireContainerPublished(e);
                }
            }
        };
    }

	/**
	 * Create an update listener for the document model.
	 * 
	 * @return The creation listener.
	 */
	private DocumentListener createDocumentListener() {
		return new DocumentAdapter() {};
	}

    /**
     * Create a migrator listener.
     * 
     * @return A <code>MigratorListener</code>.
     */
    private MigratorListener createMigratorListener() {
        return new MigratorAdapter() {
            @Override
            public void productReleaseInstalled(final MigratorEvent e) {
                systemApplication.fireProductReleaseInstalled(e);
            }
        };
    }

    /**
     * Create a profile listener.
     * 
     * @return A <code>ProfileListener</code>.
     */
    private ProfileListener createProfileListener() {
        return new ProfileAdapter() {
            @Override
            public void emailUpdated(ProfileEvent e) {
                systemApplication.fireEMailUpdated(e);
            }
            @Override
            public void emailVerified(ProfileEvent e) {
                systemApplication.fireEMailVerified(e);
            }
            @Override
            public void profileActivated(final ProfileEvent e) {
                systemApplication.fireProfileActivated(e);
            }
            @Override
            public void profilePassivated(final ProfileEvent e) {
                systemApplication.fireProfilePassivated(e);
            }
        };
    }

    /**
     * Create a session listener.
     * 
     * @return A <code>SessionListener</code>.
     */
    private SessionListener createSessionListener() {
        return new SessionAdapter() {
            @Override
            public void sessionEstablished() {
                systemApplication.fireConnectionOnline();
            }
            @Override
            public void sessionTerminated() {
                systemApplication.fireConnectionOffline();
            }
        };
    }
}
