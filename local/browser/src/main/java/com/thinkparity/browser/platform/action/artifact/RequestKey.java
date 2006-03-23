/*
 * Jan 30, 2006
 */
package com.thinkparity.browser.platform.action.artifact;

import javax.swing.Icon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class RequestKey extends AbstractAction {

	private static final Icon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.ARTIFACT_REQUEST_KEY;
		NAME = "Request Key";
	}

	/**
	 * The browser application.
	 * 
	 */
	private final Browser browser;

	/**
	 * Create a RequestKey.
	 * 
	 */
	public RequestKey(final Browser browser) {
		super("Session.RequestKey", ID, NAME, ICON);
		this.browser = browser;
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(Data data) throws Exception {
		final Long artifactId = (Long) data.get(DataKey.ARTIFACT_ID);
		getSessionModel().sendKeyRequest(artifactId);
		getArtifactModel().applyFlagSeen(artifactId);

		browser.fireDocumentUpdated(artifactId);
	}

	public enum DataKey { ARTIFACT_ID }
}
