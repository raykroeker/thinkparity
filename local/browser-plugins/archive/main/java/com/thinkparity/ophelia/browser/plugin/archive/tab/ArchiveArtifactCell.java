/*
 * Created On: Sep 1, 2006 10:59:14 AM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.artifact.Artifact;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class ArchiveArtifactCell extends ArchiveCell {

    /** The archived artifact. */
    private Artifact artifact;

    /**
     * Create ArchiveCell.
     * 
     */
    ArchiveArtifactCell() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell#getTextNoClipping(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     */
    @Override
    public String getTextNoClipping(final TextGroup textGroup) {
        switch (textGroup) {
        case EAST:
            return null;
        case WEST:
            return getArtifact().getName();
        default:
            throw Assert.createUnreachable("UNKNOWN TEXT GROUP");
        }
    }

    /**
     * Obtain the artifact
     *
     * @return The Artifact.
     */
    protected Artifact getArtifact() {
        return artifact;
    }

    /**
     * Obtain the artifact unique id.
     * 
     * @return A unique id <code>UUID</code>.
     */
    protected UUID getArtifactUniqueId() {
        return getArtifact().getUniqueId();
    }

    /**
     * Set artifact.
     *
     * @param artifact The Artifact.
     */
    protected void setArtifact(final Artifact artifact) {
        this.artifact = artifact;
    }
}
