/*
 * Jan 30, 2006
 */
package com.thinkparity.browser.platform.action.session;

import javax.swing.Icon;

import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class RequestKey extends AbstractAction {

	public enum DataKey { ARTIFACT_ID }

	private static final Icon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.SESSION_REQUEST_KEY;
		NAME = "Request Key";
	}

	/**
	 * Create a RequestKey.
	 * 
	 */
	public RequestKey() { super("Session.RequestKey", ID, NAME, ICON); }

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(Data data) throws Exception {
		final Long artifactId = (Long) data.get(DataKey.ARTIFACT_ID);
		Assert.assertNotNull("Cannot request key for null artifact id.", artifactId);
		getSessionModel().sendKeyRequest(artifactId);
	}
}
