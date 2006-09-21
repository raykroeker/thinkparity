/*
 * Created On: Sep 1, 2006 10:59:14 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.archive;

import java.awt.Component;
import java.awt.event.MouseEvent;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.artifact.Artifact;

import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ArchiveCell extends DefaultTabCell {

    /** The archived artifact. */
    private Artifact artifact;

    public ArchiveCell() {
        super();
    }

    /**
     * Obtain the artifact
     *
     * @return The Artifact.
     */
    public Artifact getArtifact() {
        return artifact;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getParent()
     */
    public TabCell getParent() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getText(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     *
     */
    public String getText(TextGroup textGroup) {
        if (textGroup == TextGroup.MAIN_TEXT) {
            return artifact.getName();
        } else {
            return null;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_0;
    }

    /**
     * Set artifact.
     *
     * @param artifact The Artifact.
     */
    public void setArtifact(final Artifact artifact) {
        this.artifact = artifact;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        throw Assert.createNotYetImplemented("ArchiveCell#triggerPopup");
    }
}
