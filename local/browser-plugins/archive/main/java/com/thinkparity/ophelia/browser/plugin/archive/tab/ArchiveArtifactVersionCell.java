/*
 * Created On: Sep 1, 2006 10:59:14 AM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import java.text.MessageFormat;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class ArchiveArtifactVersionCell extends ArchiveCell {

    /** The archived artifact version. */
    private ArtifactVersion version;

    /**
     * Create ArchiveCell.
     * 
     */
    ArchiveArtifactVersionCell() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell#getText(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     */
    @Override
    public String getText(final TextGroup textGroup) {
        return super.getText(textGroup);
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
            return MessageFormat.format("{0,date,MMM dd yyyy HH:mm}",
                    getArtifactVersion().getCreatedOn().getTime());
        default:
            throw Assert.createUnreachable("UNKNOWN TEXT GROUP");
        }
    }

    /**
     * Obtain the artifact
     *
     * @return The Artifact.
     */
    protected ArtifactVersion getArtifactVersion() {
        return version;
    }

    /**
     * Obtain the artifact version's version id.
     * 
     * @return A version id <code>Long</code>.
     */
    protected Long getArtifactVersionId() {
        return getArtifactVersion().getVersionId();
    }

    /**
     * Set artifact.
     *
     * @param artifact The Artifact.
     */
    protected void setArtifactVersion(final ArtifactVersion version) {
        this.version = version;
    }
}
