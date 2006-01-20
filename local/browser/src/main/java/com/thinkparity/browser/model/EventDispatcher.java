/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.model;

import com.thinkparity.browser.javax.swing.browser.Controller;

import com.thinkparity.model.parity.api.events.*;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.project.ProjectModel;
import com.thinkparity.model.parity.model.session.SessionModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class EventDispatcher {

	/**
	 * Singleton instance of the event dispatcher.
	 * 
	 */
	private static final EventDispatcher singleton;

	static { singleton = new EventDispatcher(); }

	/**
	 * Obtain the singleton instance of the event dispatcher.
	 * 
	 * @return The event dispatcher.
	 */
	public static EventDispatcher getInstance() { return singleton; }

	/**
	 * Handle to the model factory.
	 * 
	 */
	protected final ModelFactory modelFactory = ModelFactory.getInstance();

	/**
	 * Handle to the document model api.
	 * 
	 */
	private DocumentModel documentModel;

	/**
	 * Flag indicating whether the event dispatcher has been initialized.
	 * 
	 */
	private boolean isInitialized;

	/**
	 * Handle to the project model api.
	 * 
	 */
	private ProjectModel projectModel;

	/**
	 * Handle to the session model api.
	 * 
	 */
	private SessionModel sessionModel;

	/**
	 * Main ui controller.
	 * 
	 */
	private Controller controller;

	/**
	 * Create an EventDispatcher.
	 * 
	 */
	private EventDispatcher() {
		super();
		this.isInitialized = false;
	}

	/**
	 * Initialize the model event dispatcher for the ui.
	 *
	 */
	public void initialize() {
		if(!isInitialized) {
			controller = Controller.getInstance();

			documentModel = modelFactory.getDocumentModel(getClass());
			projectModel = modelFactory.getProjectModel(getClass());
			sessionModel = modelFactory.getSessionModel(getClass());
	
			documentModel.addListener(createDocumentModelCreationListener());
			documentModel.addListener(createDocumentModelUpdateListener());
			projectModel.addListener(createProjectModelCreationListener());
			projectModel.addListener(createProjectModelUpdateListener());
			sessionModel.addListener(createSessionModelPresenceListener());
			sessionModel.addListener(createSessionModelKeyListener());
			sessionModel.addListener(createSessionModelSessionListener());
			isInitialized = true;
		}
	}

	private CreationListener createDocumentModelCreationListener() {
		return new CreationListener() {
			public void objectCreated(final CreationEvent e) {
				controller.refreshDocumentList(false);
			}
			public void objectReceived(final CreationEvent e) {
				controller.refreshDocumentList(false);
			}
			public void objectVersionCreated(final VersionCreationEvent e) {
				controller.refreshDocumentList(false);
			}
			public void objectVersionReceived(VersionCreationEvent e) {
				controller.refreshDocumentList(false);
			}
		};
	}

	private UpdateListener createDocumentModelUpdateListener() {
		return new UpdateListener() {
			public void objectDeleted(final DeleteEvent e) {
				controller.refreshDocumentList(false);
			}
			public void objectReceived(final UpdateEvent e) {
				controller.refreshDocumentList(false);
			}
			public void objectUpdated(final UpdateEvent e) {
				controller.refreshDocumentList(false);
			}
		};
	}

	private CreationListener createProjectModelCreationListener() {
		return new CreationListener() {
			public void objectCreated(final CreationEvent e) {}
			public void objectReceived(final CreationEvent e) {}
			public void objectVersionCreated(final VersionCreationEvent e) {}
			public void objectVersionReceived(final VersionCreationEvent e) {}
		};
	}

	private UpdateListener createProjectModelUpdateListener() {
		return new UpdateListener() {
			public void objectDeleted(final DeleteEvent e) {}
			public void objectReceived(final UpdateEvent e) {}
			public void objectUpdated(final UpdateEvent e) {}
		};
	}

	private KeyListener createSessionModelKeyListener() {
		return new KeyListener() {
			public void keyRequestAccepted(final KeyEvent e) {}
			public void keyRequestDenied(final KeyEvent e) {}
			public void keyRequested(final KeyEvent e) {}
		};
	}

	private PresenceListener createSessionModelPresenceListener() {
		return new PresenceListener() {
			public void presenceRequested(final PresenceEvent e) {}
		};
	}

	private SessionListener createSessionModelSessionListener() {
		return new SessionListener() {
			public void sessionEstablished() {
				controller.set(NetworkStatus.ONLINE);
			}
			public void sessionTerminated() {
				controller.set(NetworkStatus.OFFLINE);
			}
			public void sessionTerminated(final Throwable cause) {
				sessionTerminated();
			}
		};
	}
}
