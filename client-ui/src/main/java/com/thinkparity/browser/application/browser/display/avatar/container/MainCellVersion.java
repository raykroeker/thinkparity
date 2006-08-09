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
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentIcon;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentImage;
import com.thinkparity.browser.application.browser.display.avatar.main.border.DocumentDefault;
import com.thinkparity.browser.platform.util.l10n.MainCellL18n;

import com.thinkparity.model.parity.model.container.ContainerVersion;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class MainCellVersion extends ContainerVersion implements
        MainCell {

    /** The container cell. */
    private final MainCellContainer container;
    
    /** The cell's text foreground color. */
    private static final Color TEXT_FG;

    /** The cell's text foreground colour for closed containers. */
    private static final Color TEXT_FG_CLOSED;

    /** Maximum length of a container cell's text. */
    private static final Integer TEXT_MAX_LENGTH;

    static {
        TEXT_FG = Color.BLACK;
        TEXT_FG_CLOSED = new Color(127, 131, 134, 255);

        TEXT_MAX_LENGTH = 60;
    }
    
    /** A flag indicating the expand\collapse status. */
    private Boolean expanded = Boolean.FALSE; 
    
    /** An image cache. */
    private final MainCellImageCache imageCache;

    /** The draft cell localization. */
    private final MainCellL18n localization;

    /** Create MainCellContainerVersion. */
    public MainCellVersion(final MainCellContainer container,
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
     * 
     */
    public boolean canImport() { return Boolean.FALSE; }

    /**
     * @see com.thinkparity.model.parity.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) { return super.equals(obj); }
    
    /**
     * Obtain the background image for a cell.
     * 
     * 
     * @return A buffered image.
     */
    public BufferedImage getBackground() {
        return imageCache.read(DocumentImage.BG_DEFAULT);
    }

    /**
     * Obtain the background image for a selected cell.
     * 
     * 
     * @return A buffered image.
     */
    public BufferedImage getBackgroundSelected() {
        return imageCache.read(DocumentImage.BG_SEL_DEFAULT);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBorder()
     * 
     */
    public Border getBorder() { return new DocumentDefault(); }

    /**
     * Obtain an info icon.
     * 
     * 
     * @return An image icon.
     */
    public ImageIcon getInfoIcon() { return null; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getNodeIcon()
     * 
     */
    public ImageIcon getNodeIcon() {
        return imageCache.read(DocumentIcon.NODE_DEFAULT);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getNodeIconSelected()
     * 
     */
    public ImageIcon getNodeIconSelected() {
        if(isExpanded()) { return imageCache.read(DocumentIcon.NODE_SEL_EXPANDED); }
        else { return imageCache.read(DocumentIcon.NODE_SEL_DEFAULT); }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getText()
     * 
     */
    public String getText() {
        return localization.getString("Text", new Object[] {getVersionId()});
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextFont()
     * 
     */
    public Font getTextFont() { return Fonts.DefaultFont; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextForeground()
     * 
     */
    public Color getTextForeground() { return TEXT_FG; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() { return 3.0F; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getToolTip()
     * 
     */
    public String getToolTip() {
        return null;
    }

    /**
     * @see com.thinkparity.model.parity.model.artifact.Artifact#hashCode()
     * 
     */
    @Override
    public int hashCode() { return super.hashCode(); }

    /**
     * Determine whether or not the cell is expanded.
     * 
     * @return True if the cell is expanded.
     */
    public Boolean isExpanded() { return expanded; }

    /**
     * Set the expanded flag.
     * 
     * @param expanded
     *            The expanded flag.
     */
    public void setExpanded(final Boolean expanded) { this.expanded = expanded; }
}
