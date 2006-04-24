/*
 * Mar 18, 2006
 */
package com.thinkparity.browser.application.system;

import com.thinkparity.model.parity.api.events.DocumentAdapter;
import com.thinkparity.model.parity.api.events.DocumentEvent;
import com.thinkparity.model.parity.api.events.DocumentListener;
import com.thinkparity.model.parity.api.events.SystemMessageEvent;
import com.thinkparity.model.parity.api.events.SystemMessageListener;
import com.thinkparity.model.parity.model.message.system.SystemMessage;

/**
 * The system application's event dispatcher.  
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class EventDispatcher {

	/** The document listener. */
	private DocumentListener documentListener;

	/** The system message listener. */
	private SystemMessageListener systemMessageListener;

	/** The application. */
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
		sysApp.getDocumentModel().removeListener(documentListener);
		documentListener = null;

		sysApp.getSystemMessageModel().removeListener(systemMessageListener);
		systemMessageListener = null;
	}

	void start() {
		documentListener = createDocumentListener();
		sysApp.getDocumentModel().addListener(documentListener);

		systemMessageListener = createSystemMessageListener();
		sysApp.getSystemMessageModel().addListener(systemMessageListener);
	}

	/**
	 * Create an update listener for the document model.
	 * 
	 * @return The creation listener.
	 */
	private DocumentListener createDocumentListener() {
		return new DocumentAdapter() {
			public void documentUpdated(final DocumentEvent e) {
                if(e.isRemote()) { sysApp.notifyReceived(e.getDocument()); }
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
