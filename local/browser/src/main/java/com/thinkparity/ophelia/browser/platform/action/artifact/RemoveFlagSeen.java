/*
 * Mar 21, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.artifact;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.artifact.ArtifactType;

import com.thinkparity.ophelia.model.artifact.ArtifactModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class RemoveFlagSeen extends AbstractAction {
    
    /** The browser application. */
    private final Browser browser;

	/**
     * Create RemoveFlagSeen.
     * 
     * @param browser
     *            The <code>Browser</code>.
     */
	public RemoveFlagSeen(final Browser browser) {
		super(ActionId.ARTIFACT_REMOVE_FLAG_SEEN);
        this.browser = browser;
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
        
            // The "seen" flag does not impact the back end. Therefore it is OK to fire this event directly.
            final ArtifactType type = artifactModel.readType(artifactId);
            switch (type) {
            case CONTAINER:
                browser.fireContainerUpdated(artifactId, Boolean.FALSE);
                break;
            case DOCUMENT:
                browser.fireDocumentUpdated(artifactId, Boolean.FALSE);
                break;
            default:
                Assert.assertUnreachable("UNKNOWN ARTIFACT TYPE");
            }
        }
	}

	/** Data keys. */
	public enum DataKey { ARTIFACT_ID, ARTIFACT_TYPE }
}
