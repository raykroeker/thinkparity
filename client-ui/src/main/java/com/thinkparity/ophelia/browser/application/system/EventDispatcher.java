/*
 * Mar 18, 2006
 */
package com.thinkparity.ophelia.browser.application.system;

import com.thinkparity.ophelia.model.events.ContactAdapter;
import com.thinkparity.ophelia.model.events.ContactListener;
import com.thinkparity.ophelia.model.events.ContainerAdapter;
import com.thinkparity.ophelia.model.events.ContainerEvent;
import com.thinkparity.ophelia.model.events.ContainerListener;
import com.thinkparity.ophelia.model.events.DocumentAdapter;
import com.thinkparity.ophelia.model.events.DocumentListener;
import com.thinkparity.ophelia.model.events.SessionListener;

/**
 * The system application's event dispatcher.  
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class EventDispatcher {

	/** A thinkParity <code>ContainerListener</code>. */
    private ContainerListener containerListener;

    /** A thinkParity <code>ContactListener</code>. */
    private ContactListener contactListener;

    /** A thinkParity document listener. */
	private DocumentListener documentListener;

	/** A thinkParity session listener. */
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

        sessionListener = createSessionListener();
        systemApplication.getSessionModel().addListener(sessionListener);
	}

    /**
     * Create a thinkParity contact listener.
     * 
     * @return A <code>ContactListener</code>.
     */
    private ContactListener createContactListener() {
        return new ContactAdapter() {};
    }

	private ContainerListener createContainerListener() {
        return new ContainerAdapter() {
            @Override
            public void draftPublished(final ContainerEvent e) {
                if (e.isRemote())
                    systemApplication.fireContainerDraftPublished(e);
            }
            @Override
            public void teamMemberAdded(final ContainerEvent e) {
                if (e.isRemote())
                    systemApplication.fireContainerTeamMemberAdded(e);
            }
            @Override
            public void teamMemberRemoved(final ContainerEvent e) {
                if (e.isRemote())
                    systemApplication.fireContainerTeamMemberRemoved(e);
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
     * Create a session listener.
     * 
     * @return A <code>SessionListener</code>.
     */
    private SessionListener createSessionListener() {
        return new SessionListener() {
            public void sessionEstablished() {
                systemApplication.fireConnectionOnline();
            }
            public void sessionTerminated() {
                systemApplication.fireConnectionOffline();
            }
            public void sessionTerminated(final Throwable t) {
                sessionTerminated();
            }
        };
    }
}
