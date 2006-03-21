/*
 * Mar 18, 2006
 */
package com.thinkparity.browser.application.system;

import com.thinkparity.model.parity.api.events.CloseEvent;
import com.thinkparity.model.parity.api.events.DeleteEvent;
import com.thinkparity.model.parity.api.events.UpdateEvent;
import com.thinkparity.model.parity.api.events.UpdateListener;

/**
 * The system application's event dispatcher.  
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class EventDispatcher {

	/**
	 * Listens for document update events.
	 * 
	 */
	private UpdateListener documentUpdateListener;

	/**
	 * The system application.
	 * 
	 */
	private final SysApp sysApp;

	/**
	 * Create an EventDispatcher.
	 * 
	 * @param sysApp
	 *            The system application.
	 */
	EventDispatcher(final SysApp sysApp) {
		super();
		this.sysApp = sysApp;
	}

	void end() {
		sysApp.getDocumentModel().removeListener(documentUpdateListener);
		documentUpdateListener = null;
	}

	void start() {
		documentUpdateListener = createDocumentUpdateListener();
		sysApp.getDocumentModel().addListener(documentUpdateListener);
	}

	/**
	 * Create an update listener for the document model.
	 * 
	 * @return The creation listener.
	 */
	private UpdateListener createDocumentUpdateListener() {
		return new UpdateListener() {
			public void objectClosed(final CloseEvent closeEvent) {}
			public void objectDeleted(final DeleteEvent deleteEvent) {}
			public void objectReceived(final UpdateEvent updateEvent) {
				sysApp.notifyReceived(updateEvent.getSource());
			}
			public void objectUpdated(final UpdateEvent updateEvent) {}
		};
	}
}
