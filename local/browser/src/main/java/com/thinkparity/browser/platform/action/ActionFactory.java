/*
 * Jan 10, 2006
 */
package com.thinkparity.browser.platform.action;

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
		case ARTIFACT_ACCEPT_KEY_REQUEST:
			return new com.thinkparity.browser.platform.action.artifact.AcceptKeyRequest();
		case ARTIFACT_APPLY_FLAG_SEEN:
			return new com.thinkparity.browser.platform.action.artifact.ApplyFlagSeen();
		case ARTIFACT_DECLINE_KEY_REQUEST:
			return new com.thinkparity.browser.platform.action.artifact.DeclineKeyRequest();
		case ARTIFACT_DECLINE_ALL_KEY_REQUESTS:
			return new com.thinkparity.browser.platform.action.artifact.DeclineAllKeyRequests();
		case DOCUMENT_CLOSE:
			return new com.thinkparity.browser.platform.action.document.Close();
		case DOCUMENT_CREATE:
			return new com.thinkparity.browser.platform.action.document.Create();
		case DOCUMENT_DELETE:
			return new com.thinkparity.browser.platform.action.document.Delete();
		case DOCUMENT_OPEN:
			return new com.thinkparity.browser.platform.action.document.Open();
		case DOCUMENT_OPEN_VERSION:
			return new com.thinkparity.browser.platform.action.document.OpenVersion();
		case SESSION_ACCEPT_INVITATION:
			return new com.thinkparity.browser.platform.action.session.AcceptInvitation();
		case SESSION_DECLINE_INVITATION:
			return new com.thinkparity.browser.platform.action.session.DeclineInvitation();
		case SESSION_ADD_CONTACT:
			return new com.thinkparity.browser.platform.action.session.AddContact();
		case SESSION_REQUEST_KEY:
			return new com.thinkparity.browser.platform.action.session.RequestKey();
		case SYSTEM_MESSAGE_DELETE:
			return new com.thinkparity.browser.platform.action.system.message.DeleteSystemMessage();
		default: throw Assert.createUnreachable("Unable to create action [" + actionId + "].");
		}
	}
}
