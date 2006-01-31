/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.model;

import com.thinkparity.browser.Controller;

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
	private static EventDispatcher singleton;

	/**
	 * Obtain the singleton instance of the event dispatcher.
	 * 
	 * @param The
	 *            main controller.
	 * @return The event dispatcher.
	 */
	public static EventDispatcher getInstance() {
		if(null == singleton) { singleton = new EventDispatcher(); }
		return singleton;
	}

	/**
	 * Handle to the model factory.
	 * 
	 */
	protected final ModelFactory modelFactory = ModelFactory.getInstance();

	/**
	 * Main controller.
	 * 
	 */
	protected Controller controller;

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
			public void objectCreated(final CreationEvent e) {}
			public void objectReceived(final CreationEvent e) {
				controller.reloadMainBrowserAvatar();
			}
			public void objectVersionCreated(final VersionCreationEvent e) {}
			public void objectVersionReceived(VersionCreationEvent e) {}
		};
	}

	private UpdateListener createDocumentModelUpdateListener() {
		return new UpdateListener() {
			public void objectDeleted(final DeleteEvent e) {}
			public void objectReceived(final UpdateEvent e) {}
			public void objectUpdated(final UpdateEvent e) {
				controller.reloadMainBrowserAvatar();
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
			public void sessionEstablished() {}
			public void sessionTerminated() {}
			public void sessionTerminated(final Throwable cause) {
				sessionTerminated();
			}
		};
	}
}
