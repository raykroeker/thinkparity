/**
 * Created On: 21-Jun-2006 3:08:54 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.avatar.contact;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCell;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellRenderer;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentIcon;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentImage;
import com.thinkparity.browser.application.browser.display.avatar.main.border.DocumentDefault;

import com.thinkparity.model.xmpp.contact.Contact;

/**
 * An extension of a contact that allows the {@link MainCellRenderer} to display
 * a parity contact.
 * 
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class CellContact extends Contact implements MainCell {
    
    /** The cell's text foreground color. */
    private static final Color TEXT_FG;

    /** Maximum length of a document cell's text. */
    private static final Integer TEXT_MAX_LENGTH;

    static {
        TEXT_FG = Color.BLACK;
        TEXT_MAX_LENGTH = 60;
    }

    /** A flag indicating the expand\collapse status. */
    private Boolean expanded = Boolean.FALSE;

    /** An image cache. */
    private final MainCellImageCache imageCache;

    /**
     * Create a CellContact.
     */
    public CellContact(final Contact c) {
        super();
        // Set contact details
        addAllEmails(c.getEmails());
        setId(c.getId());
        setLocalId(c.getLocalId());
        setName(c.getName());
        setOrganization(c.getOrganization());
        this.imageCache = new MainCellImageCache();
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#canImport()
     * 
     */
    public boolean canImport() { return false; }

    /**
     * @see com.thinkparity.model.parity.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) { return super.equals(obj); }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#fireSelection()
     * 
     */
    public void fireSelection() {}
    
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
    public ImageIcon getInfoIcon() {
        return imageCache.read(DocumentIcon.INFO_IS_KEYHOLDER);
    }

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
        if(TEXT_MAX_LENGTH < getName().length()) {
            return getName().substring(0, TEXT_MAX_LENGTH - 1 - 3) + "...";
        }
        else { return getName(); }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextFont()
     * 
     */
    public Font getTextFont() {
        return BrowserConstants.Fonts.DefaultFont;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextForeground()
     * 
     */
    public Color getTextForeground() {
        return TEXT_FG;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() { return 1.0F; }

    /**
     * @see com.thinkparity.browser.application.browser.display.avatar.main.MainCell#getToolTip()
     * 
     */
    public String getToolTip() {
        if(TEXT_MAX_LENGTH < getName().length()) { return getName(); }
        else { return null; }
    }

    /**
     * @see com.thinkparity.model.parity.model.artifact.Artifact#hashCode()
     * 
     */
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
