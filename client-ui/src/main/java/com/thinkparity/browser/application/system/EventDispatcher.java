/*
 * Mar 18, 2006
 */
package com.thinkparity.browser.application.system;

import com.thinkparity.model.parity.api.events.CloseEvent;
import com.thinkparity.model.parity.api.events.DeleteEvent;
import com.thinkparity.model.parity.api.events.SystemMessageEvent;
import com.thinkparity.model.parity.api.events.SystemMessageListener;
import com.thinkparity.model.parity.api.events.UpdateEvent;
import com.thinkparity.model.parity.api.events.UpdateListener;
import com.thinkparity.model.parity.model.message.system.SystemMessage;

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
	 * Listens for new system messages.
	 * 
	 */
	private SystemMessageListener systemMessageListener;

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

		systemMessageListener = createSystemMessageListener();
		sysApp.getSystemMessageModel().addListener(systemMessageListener);
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
		};
	}

	/**
	 * Create a system message listener.
	 * 
	 * @return The system message listener.
	 */
	private SystemMessageListener createSystemMessageListener() {
		return new SystemMessageListener() {
			public void systemMessageCreated(
					final SystemMessageEvent systemMessageEvent) {
				sysApp.notifyReceived((SystemMessage) systemMessageEvent.getSource());
			}
		};
	}
}
