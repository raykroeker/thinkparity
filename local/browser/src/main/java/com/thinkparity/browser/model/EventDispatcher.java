/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.model;

import com.thinkparity.browser.application.browser.Browser;

import com.thinkparity.model.parity.api.events.*;
import com.thinkparity.model.parity.model.document.DocumentModel;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;
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
	 * The browser.
	 * 
	 */
	protected Browser browser;

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
	 * Handle to the session model api.
	 * 
	 */
	private SessionModel sessionModel;

	/**
	 * Handle to the system message interface.
	 * 
	 */
	private SystemMessageModel systemMessageModel;

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

			documentModel = modelFactory.getDocumentModel(getClass());
			sessionModel = modelFactory.getSessionModel(getClass());
			systemMessageModel = modelFactory.getSystemMessageModel(getClass());
	
			documentModel.addListener(createDocumentModelCreationListener());
			documentModel.addListener(createDocumentModelUpdateListener());
			sessionModel.addListener(createSessionModelPresenceListener());
			sessionModel.addListener(createSessionModelKeyListener());
			sessionModel.addListener(createSessionModelSessionListener());
			systemMessageModel.addListener(createSystemMessageListener());

			isInitialized = true;
		}
	}

	/**
	 * Obtain the browser.
	 * 
	 * @return The browser.
	 */
	protected Browser getBrowser() {
		if(null == browser) {
			browser = Browser.getInstance();
		}
		return browser;
	}

	private CreationListener createDocumentModelCreationListener() {
		return new CreationListener() {
			public void objectCreated(final CreationEvent e) {}
			public void objectVersionCreated(final VersionCreationEvent e) {}
		};
	}

	private UpdateListener createDocumentModelUpdateListener() {
		return new UpdateListener() {
			public void objectClosed(CloseEvent closeEvent) {
				getBrowser().reloadMainList();
			}
			public void objectDeleted(final DeleteEvent e) {}
			public void objectReceived(final UpdateEvent e) {
				getBrowser().reloadMainList();
			}
		};
	}

	private KeyListener createSessionModelKeyListener() {
		return new KeyListener() {
			public void keyRequestAccepted(final KeyEvent e) {
				getBrowser().reloadMainList();
			}
			public void keyRequestDenied(final KeyEvent e) {
				getBrowser().reloadMainList();
			}
			public void keyRequested(final KeyEvent e) {
				getBrowser().reloadMainList();
			}
		};
	}

	private PresenceListener createSessionModelPresenceListener() {
		return new PresenceListener() {
			public void presenceRequested(final PresenceEvent e) {
				getBrowser().reloadMainList();
			}
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

	private SystemMessageListener createSystemMessageListener() {
		return new SystemMessageListener() {
			public void systemMessageCreated(
					final SystemMessageEvent systemMessageEvent) {
				getBrowser().reloadMainList();
			}
		};
	}
}
