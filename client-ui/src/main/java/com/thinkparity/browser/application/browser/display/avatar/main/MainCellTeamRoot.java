/*
 * Apr 12, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.border.Border;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentIcon;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.HistoryItemIcon;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.HistoryItemImage;
import com.thinkparity.browser.platform.util.l10n.MainCellL18n;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MainCellTeamRoot implements MainCell {

    /** The history item's document. */
    private final MainCellDocument document;

    /** Flag indicating whether or not the cell is expanded. */
    private Boolean expanded;

    /** An image cache. */
    private final MainCellImageCache imageCache;

    /** Localisation access. */
    private final MainCellL18n l18n;

    /** The size of the team. */
    private final Integer teamSize;

    /**
     * Create a MainCellHistoryItem.
     * 
     */
    public MainCellTeamRoot(final MainCellDocument document, final Integer teamSize) {
        super();
        this.document = document;
        this.expanded = Boolean.FALSE;
        this.imageCache = new MainCellImageCache();
        this.l18n = new MainCellL18n("TeamRoot");
        this.teamSize = teamSize;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#canImport()
     * 
     */
    public boolean canImport() { return false; }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        if(null != obj && obj instanceof MainCellTeamRoot) {
            return ((MainCellTeamRoot) obj).document.equals(document);
        }
        return false;
    }


    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBackground()
     * 
     */
    public BufferedImage getBackground() {
        if(document.isClosed()) { return imageCache.read(HistoryItemImage.BG_CLOSED); }
        else if(document.isUrgent()) { return imageCache.read(HistoryItemImage.BG_URGENT); }
        else { return imageCache.read(HistoryItemImage.BG_DEFAULT); }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBackgroundSelected()
     * 
     */
    public BufferedImage getBackgroundSelected() {
        if(document.isClosed()) { return imageCache.read(HistoryItemImage.BG_SEL_CLOSED); }
        else if(document.isUrgent()) { return imageCache.read(HistoryItemImage.BG_SEL_URGENT); }
        else { return imageCache.read(HistoryItemImage.BG_SEL_DEFAULT); }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBorder()
     * 
     */
    public Border getBorder() { return null; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getBorderSelected()
     *
     */
     public Border getBorderSelected() { return BorderFactory.createLineBorder(BrowserConstants.Colours.MAIN_CELL_DEFAULT_BORDER2); }

    /**
     * Obtain the document.
     * 
     * @return The main cell document.
     */
    public MainCellDocument getDocument() { return document; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getInfoIcon()
     * 
     */
    public ImageIcon getInfoIcon() { return imageCache.read(HistoryItemIcon.INFO_DEFAULT); }

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
        return l18n.getString("TeamRoot", new Object[] {teamSize});
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextFont()
     * 
     */
    public Font getTextFont() { return BrowserConstants.Fonts.SmallFont; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextForeground()
     * 
     */
    public Color getTextForeground() { return document.getTextForeground(); }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() { return 3.0F; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getToolTip()
     */
    public String getToolTip() { return null; }

    /**
     * @see java.lang.Object#hashCode()
     * 
     */
    public int hashCode() { return document.hashCode() & getText().hashCode(); }

    /**
     * Obtain the expanded
     *
     * @return The Boolean.
     */
    public Boolean isExpanded() { return expanded; }

    /**
     * Set expanded.
     *
     * @param expanded The Boolean.
     */
    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }
}
