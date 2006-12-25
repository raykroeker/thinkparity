/*
 * Mar 21, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.artifact;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity Browser Remove Flag Seen<br>
 * <b>Description:</b>A browser action to remove the seen flag from an
 * artifact.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class RemoveFlagSeen extends AbstractAction {
    
	/**
     * Create RemoveFlagSeen.
     * 
     * @param browser
     *            The <code>Browser</code>.
     */
	public RemoveFlagSeen(final Browser browser) {
		super(ActionId.ARTIFACT_REMOVE_FLAG_SEEN);
	}

	/**
	 * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
		final Long artifactId = (Long) data.get(DataKey.ARTIFACT_ID);
		final ArtifactModel artifactModel = getArtifactModel();
        if (artifactModel.hasBeenSeen(artifactId)) {
		    artifactModel.removeFlagSeen(artifactId);
        }
	}

	/** The data key. */
	public enum DataKey { ARTIFACT_ID }
}
