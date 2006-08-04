/*
 * Created On: Aug 3, 2006 6:42:00 PM
 */
package com.thinkparity.browser.application.browser.display.avatar.container;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

import com.thinkparity.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCell;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.browser.platform.util.l10n.MainCellL18n;

import com.thinkparity.model.parity.model.container.ContainerVersion;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class MainCellContainerVersion extends ContainerVersion implements
        MainCell {

    /** The container cell. */
    private final CellContainer container;

    /** The cell image cache. */
    private final MainCellImageCache imageCache;

    /** Main cell localization. */
    private final MainCellL18n localization;

    /** Create MainCellContainerVersion. */
    public MainCellContainerVersion(final CellContainer container,
            final ContainerVersion containerVersion) {
        super();
        this.setArtifactId(containerVersion.getArtifactId());
        this.setArtifactType(containerVersion.getArtifactType());
        this.setArtifactUniqueId(containerVersion.getArtifactUniqueId());
        this.setCreatedBy(containerVersion.getCreatedBy());
        this.setCreatedOn(containerVersion.getCreatedOn());
        this.setName(containerVersion.getName());
        this.setUpdatedBy(containerVersion.getUpdatedBy());
        this.setUpdatedOn(containerVersion.getUpdatedOn());
        this.setVersionId(containerVersion.getVersionId());
        this.container = container;
        this.imageCache = new MainCellImageCache();
        this.localization = new MainCellL18n("MainCellContainerVersion");
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#canImport()
     */
    public boolean canImport() { return false; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBackground()
     */
    public BufferedImage getBackground() { return null; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBackgroundSelected()
     */
    public BufferedImage getBackgroundSelected() { return null; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBorder()
     */
    public Border getBorder() { return null; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getInfoIcon()
     */
    public ImageIcon getInfoIcon() { return null; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getNodeIcon()
     */
    public ImageIcon getNodeIcon() { return null; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getNodeIconSelected()
     */
    public ImageIcon getNodeIconSelected() { return null; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getText()
     */
    public String getText() { return localization.getString("", new Object[] {getVersionId()}); }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextFont()
     */
    public Font getTextFont() { return Fonts.DefaultFont; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextForeground()
     */
    public Color getTextForeground() { return null; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextInsetFactor()
     */
    public Float getTextInsetFactor() { return 3.0F; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getToolTip()
     */
    public String getToolTip() { return getText(); }
}
