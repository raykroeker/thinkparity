/*
 * Created On: Sep 1, 2006 10:59:14 AM
 */
package com.thinkparity.ophelia.browser.plugin.archive.tab;

import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.browser.Constants.InsetFactors;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ArchiveContainerVersionCell extends ArchiveArtifactVersionCell {

    /**
     * Create ArchiveContainerVersionCell.
     * 
     */
    ArchiveContainerVersionCell() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell#getTextInsetFactor()
     */
    @Override
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_1;
    }

    /**
     * Obtain the artifact
     *
     * @return The Artifact.
     */
    ContainerVersion getContainerVersion() {
        return (ContainerVersion) getArtifactVersion();
    }

    /**
     * Set artifact.
     *
     * @param artifact The Artifact.
     */
    void setContainerVersion(final ContainerVersion version) {
        setArtifactVersion(version);
    }
}
