/*
 * Mar 21, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.artifact;

import com.thinkparity.codebase.model.artifact.ArtifactType;


import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ApplyFlagSeen extends AbstractAction {
    
    /** The browser application. */
    private final Browser browser;

	/** Create ApplyFlagSeen. */
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
        final ArtifactType artifactType = (ArtifactType) data.get(DataKey.ARTIFACT_TYPE);
        if (!getArtifactModel().hasBeenSeen(artifactId)) {
		    getArtifactModel().applyFlagSeen(artifactId);
        
            // The "seen" flag does not impact the back end. Therefore it is OK to fire this event directly.
            browser.fireArtifactFlagSeen(artifactId, artifactType, Boolean.FALSE);
        }
	}

	/** Data keys. */
	public enum DataKey { ARTIFACT_ID, ARTIFACT_TYPE }
}
