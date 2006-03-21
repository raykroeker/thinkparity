/*
 * Mar 21, 2006
 */
package com.thinkparity.browser.platform.action.artifact;

import javax.swing.ImageIcon;

import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ApplyFlagSeen extends AbstractAction {

	private static final ImageIcon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.ARTIFACT_APPLY_FLAG_SEEN;
		NAME = "Apply Flag Seen";
	}

	/**
	 * Create a ApplyFlagSeen.
	 * 
	 */
	public ApplyFlagSeen() {
		super("ApplyFlagSeen", ID, NAME, ICON);
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final Long artifactId = (Long) data.get(DataKey.ARTIFACT_ID);
		getArtifactModel().applyFlagSeen(artifactId);
	}

	/**
	 * Keys for the invocation data.
	 * 
	 */
	public enum DataKey { ARTIFACT_ID }

}
