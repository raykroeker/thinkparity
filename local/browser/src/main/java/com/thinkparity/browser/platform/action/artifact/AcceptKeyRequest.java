/*
 * Feb 28, 2006
 */
package com.thinkparity.browser.platform.action.artifact;

import javax.swing.ImageIcon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AcceptKeyRequest extends AbstractAction {

	private static final ImageIcon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.ARTIFACT_ACCEPT_KEY_REQUEST;
		NAME = "Accept key request";
	}

	/**
	 * The browser application.
	 * 
	 */
	private final Browser browser;

	/**
	 * Create a AcceptKeyRequest.
	 * 
	 */
	public AcceptKeyRequest(final Browser browser) {
		super("AcceptKeyRequest", ID, NAME, ICON);
		this.browser = browser;
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final Long artifactId = (Long) data.get(DataKey.ARTIFACT_ID);
		final Long keyRequestId = (Long) data.get(DataKey.KEY_REQUEST_ID);
		getArtifactModel().acceptKeyRequest(keyRequestId);
		getArtifactModel().applyFlagSeen(artifactId);

		browser.fireDocumentUpdated(artifactId);
	}

	public enum DataKey { ARTIFACT_ID, KEY_REQUEST_ID }
}
