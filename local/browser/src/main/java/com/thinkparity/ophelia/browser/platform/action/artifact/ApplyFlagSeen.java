/*
 * Mar 21, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.artifact;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.artifact.ArtifactModel;

/**
 * <b>Title:</b>thinkParity Browser Apply Flag Seen<br>
 * <b>Description:</b>A browser action to apply the seen flag to an artifact.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ApplyFlagSeen extends AbstractBrowserAction {
    
    /** The browser application. */
    private final Browser browser;

    /**
     * Create ApplyFlagSeen.
     * 
     * @param browser
     *            The <code>Browser</code>.
     */
	public ApplyFlagSeen(final Browser browser) {
		super(ActionId.ARTIFACT_APPLY_FLAG_SEEN);
        this.browser = browser;
	}

	/**
	 * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
		final Long artifactId = (Long) data.get(DataKey.ARTIFACT_ID);
		final ArtifactModel artifactModel = getArtifactModel();
        if (!artifactModel.hasBeenSeen(artifactId)) {
		    artifactModel.applyFlagSeen(artifactId);
            browser.fireArtifactSeenFlagUpdated(artifactId);
        }
	}

	/** The data key. */
	public enum DataKey { ARTIFACT_ID }
}
