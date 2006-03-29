/*
 * Jan 10, 2006
 */
package com.thinkparity.browser.platform.action;

import com.thinkparity.browser.application.browser.Browser;

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

	public static AbstractAction createAction(final ActionId actionId, final Browser browser) {
		synchronized(singletonLock) { return singleton.doCreateAction(actionId, browser); }
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
		case ARTIFACT_APPLY_FLAG_SEEN:
			return new com.thinkparity.browser.platform.action.artifact.ApplyFlagSeen();
		case DOCUMENT_CLOSE:
			return new com.thinkparity.browser.platform.action.document.Close();
		default:
			throw Assert.createUnreachable("Unable to create action [" + actionId + "].");
		}
	}

	/**
	 * Create an action for the browser application.
	 * 
	 * @param actionId
	 *            The action id.
	 * @param browser
	 *            The browser application.
	 * @return The action.
	 */
	private AbstractAction doCreateAction(final ActionId actionId, final Browser browser) {
		switch(actionId) {
		case ARTIFACT_ACCEPT_KEY_REQUEST:
			return new com.thinkparity.browser.platform.action.artifact.AcceptKeyRequest(browser);
		case ARTIFACT_DECLINE_KEY_REQUEST:
			return new com.thinkparity.browser.platform.action.artifact.DeclineKeyRequest(browser);
		case ARTIFACT_DECLINE_ALL_KEY_REQUESTS:
			return new com.thinkparity.browser.platform.action.artifact.DeclineAllKeyRequests(browser);
		case ARTIFACT_REQUEST_KEY:
			return new com.thinkparity.browser.platform.action.artifact.RequestKey(browser);
		case DOCUMENT_CREATE:
			return new com.thinkparity.browser.platform.action.document.Create(browser);
		case DOCUMENT_DELETE:
			return new com.thinkparity.browser.platform.action.document.Delete(browser);
		case DOCUMENT_OPEN:
			return new com.thinkparity.browser.platform.action.document.Open(browser);
		case DOCUMENT_OPEN_VERSION:
			return new com.thinkparity.browser.platform.action.document.OpenVersion(browser);
		case SESSION_DECLINE_INVITATION:
			return new com.thinkparity.browser.platform.action.session.DeclineInvitation(browser);
		case SYSTEM_MESSAGE_DELETE:
			return new com.thinkparity.browser.platform.action.system.message.DeleteSystemMessage(browser);
		case SESSION_ACCEPT_INVITATION:
			return new com.thinkparity.browser.platform.action.session.AcceptInvitation(browser);
		default:
			return doCreateAction(actionId);
		}
	}
}
