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

	/** The application. */
	private final SysApp sysApp;

	/** The system message listener. */
	private SystemMessageListener systemMessageListener;

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
            public void documentClosed(final DocumentEvent e) {
                if(e.isRemote())
                    sysApp.fireDocumentClosed(e.getDocument());
            }
            public void documentCreated(final DocumentEvent e) {
                if(e.isRemote())
                    sysApp.fireDocumentCreated(e.getDocument());
            }
            public void documentUpdated(final DocumentEvent e) {
                if(e.isRemote())
                    sysApp.fireDocumentUpdated(e.getDocument());
			}
            public void keyRequested(final DocumentEvent e) {
                if(e.isRemote())
                    sysApp.fireDocumentKeyRequested(null, e.getDocument());
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
				sysApp.fireSystemMessageCreated((SystemMessage) systemMessageEvent.getSource());
			}
		};
	}
}
