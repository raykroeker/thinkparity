/*
 * Jan 10, 2006
 */
package com.thinkparity.browser.ui.action;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ActionFactory {

	private static final ActionFactory singleton;

	private static final Object singletonLock;

	static {
		singleton = new ActionFactory();
		singletonLock = new Object();
	}

	/**
	 * Create a new named action.
	 * 
	 * @param NAME
	 *            The action NAME.
	 * @return The action.
	 */
	public static AbstractAction createAction(final ActionId actionId) {
		synchronized(singletonLock) { return singleton.doCreateAction(actionId); }
	}

	/**
	 * Create a ActionFactory [Singleton, Factory]
	 * 
	 */
	private ActionFactory() { super(); }

	/**
	 * Create a new instance of a named action.
	 * 
	 * @param NAME
	 *            The action to create.
	 * @return A new instance of the action.
	 */
	private AbstractAction doCreateAction(final ActionId actionId) {
		switch(actionId) {
		case DOCUMENT_CLOSE:
			return new com.thinkparity.browser.ui.action.document.Close();
		case DOCUMENT_CREATE:
			return new com.thinkparity.browser.ui.action.document.Create();
		case DOCUMENT_DELETE:
			return new com.thinkparity.browser.ui.action.document.Delete();
		case DOCUMENT_OPEN:
			return new com.thinkparity.browser.ui.action.document.Open();
		case DOCUMENT_OPEN_VERSION:
			return new com.thinkparity.browser.ui.action.document.OpenVersion();
		default: throw Assert.createUnreachable("Unable to create action [" + actionId + "].");
		}
	}
}
