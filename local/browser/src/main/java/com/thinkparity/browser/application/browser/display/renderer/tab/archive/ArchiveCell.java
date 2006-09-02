/*
 * Created On: Sep 1, 2006 10:59:14 AM
 */
package com.thinkparity.browser.application.browser.display.renderer.tab.archive;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.Constants.Colors;
import com.thinkparity.browser.Constants.InsetFactors;
import com.thinkparity.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.browser.application.browser.display.renderer.tab.DefaultTabCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.platform.Platform.Connection;

import com.thinkparity.model.parity.model.artifact.Artifact;

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
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getBackground()
     */
    public BufferedImage getBackground() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getBackgroundSelected()
     */
    public BufferedImage getBackgroundSelected() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getNodeIcon()
     */
    public ImageIcon getNodeIcon() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getNodeIconSelected()
     */
    public ImageIcon getNodeIconSelected() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getParent()
     */
    public TabCell getParent() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getSecondaryText()
     */
    public String getSecondaryText() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getText()
     */
    public String getText() {
        return artifact.getName();
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getTextFont()
     */
    public Font getTextFont() {
        return Fonts.DefaultFont;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getTextForeground()
     */
    public Color getTextForeground() {
        return Colors.Browser.TabCell.TEXT;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_0;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getToolTip()
     */
    public String getToolTip() {
        return getText();
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
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        throw Assert.createNotYetImplemented("ArchiveCell#triggerPopup");
    }
}
