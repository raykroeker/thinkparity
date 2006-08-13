/*
 * Mar 21, 2006
 */
package com.thinkparity.browser.platform.action.artifact;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ApplyFlagSeen extends AbstractAction {

	/** Create ApplyFlagSeen. */
	public ApplyFlagSeen(final Browser browser) {
		super(ActionId.ARTIFACT_APPLY_FLAG_SEEN);
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
		final Long artifactId = (Long) data.get(DataKey.ARTIFACT_ID);
		getArtifactModel().applyFlagSeen(artifactId);
	}

	/** Data keys. */
	public enum DataKey { ARTIFACT_ID }
}
