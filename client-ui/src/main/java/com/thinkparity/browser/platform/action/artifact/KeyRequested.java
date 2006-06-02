/*
 * Created On: Thu Jun 01 2006 16:37 PDT
 * $Id$
 */
package com.thinkparity.browser.platform.action.artifact;

import javax.swing.ImageIcon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * KeyRequested is run when the browser received the remote key request
 * event.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class KeyRequested extends AbstractAction {

	private static final ImageIcon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.ARTIFACT_ACCEPT_KEY_REQUEST;
		NAME = "KeyRequested";
	}

	/** The browser application. */
	private final Browser browser;

	/**
	 * Create KeyRequested..
	 * 
     * @param browser
     *      The browser application.
	 */
	public KeyRequested(final Browser browser) {
		super(NAME, ID, NAME, ICON);
		this.browser = browser;
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final Long artifactId = (Long) data.get(DataKey.ARTIFACT_ID);
		getArtifactModel().removeFlagSeen(artifactId);

		browser.fireDocumentUpdated(artifactId, Boolean.TRUE);
	}

	public enum DataKey { ARTIFACT_ID }
}
