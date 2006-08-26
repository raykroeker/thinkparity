/**
 * Created On: 21-Jun-2006 3:08:54 PM
 */
package com.thinkparity.browser.application.browser.display.renderer.tab.contact;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import com.thinkparity.browser.Constants.InsetFactors;
import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.ContactIcon;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentImage;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCellRenderer;
import com.thinkparity.browser.platform.Platform.Connection;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.action.contact.Delete;
import com.thinkparity.browser.platform.action.contact.Read;

import com.thinkparity.codebase.swing.border.BottomBorder;
import com.thinkparity.codebase.swing.border.TopBorder;

import com.thinkparity.model.xmpp.contact.Contact;

/**
 * An extension of a contact that allows the {@link TabCellRenderer} to display
 * a parity contact.
 * 
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContactCell extends Contact implements TabCell {
    /** The border for the bottom of the container cell. */
    private static final Border BORDER_BOTTOM;

    /** The border for the top of the first container cell. */
    private static final Border BORDER_TOP_0;

    /** The border for the top of the rest of the container cells. */
    private static final Border BORDER_TOP_N;

    /** The cell's text foreground color. */
    private static final Color TEXT_FG;

    /** Maximum length of a document cell's text. */
    private static final Integer TEXT_MAX_LENGTH;

    static {
        BORDER_BOTTOM = new BottomBorder(Colours.MAIN_CELL_DEFAULT_BORDER1);
        BORDER_TOP_0 = new TopBorder(Colours.MAIN_CELL_DEFAULT_BORDER1,2);
        BORDER_TOP_N = new TopBorder(Color.WHITE);

        TEXT_FG = Color.BLACK;
        TEXT_MAX_LENGTH = 60;
    }

    /** A flag indicating the expand\collapse status. */
    private Boolean expanded = Boolean.FALSE;

    /** An image cache. */
    private final MainCellImageCache imageCache;

    /** A popup item factory. */
    private final PopupItemFactory popupItemFactory;

    /**
     * Create a CellContact.
     */
    public ContactCell() {
        super();
        this.imageCache = new MainCellImageCache();
        this.popupItemFactory = PopupItemFactory.getInstance();
    }

    /**
     * @see com.thinkparity.model.parity.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) { return super.equals(obj); }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#fireSelection()
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
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getBorder(int)
     * 
     */
    public Border getBorder(final int index) {
        final Border topBorder;
        if (0 == index) {
            topBorder = BORDER_TOP_0;
        } else {
            topBorder = BORDER_TOP_N;
        }
        return BorderFactory.createCompoundBorder(topBorder, BORDER_BOTTOM);
    }

    /**
     * Obtain an info icon.
     * 
     * 
     * @return An image icon.
     */
    public ImageIcon getInfoIcon() {
        return imageCache.read(ContactIcon.INFO);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getNodeIcon()
     * 
     */
    public ImageIcon getNodeIcon() {
        return imageCache.read(ContactIcon.NODE);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getNodeIconSelected()
     * 
     */
    public ImageIcon getNodeIconSelected() {
        return imageCache.read(ContactIcon.NODE_SELECTED);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getParent()
     */
    public TabCell getParent() {
        return null;
    }
    
    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getText()
     * 
     */
    public String getText() {
        if(TEXT_MAX_LENGTH < getName().length()) {
            return getName().substring(0, TEXT_MAX_LENGTH - 1 - 3) + "...";
        }
        else { return getName(); }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getTextFont()
     * 
     */
    public Font getTextFont() {
        return BrowserConstants.Fonts.DefaultFont;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getTextForeground()
     * 
     */
    public Color getTextForeground() {
        return TEXT_FG;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_0;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getToolTip()
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

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.browser.platform.Platform.Connection,
     *      java.awt.Component, java.awt.event.MouseEvent, int, int)
     * 
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e, final int x,
            final int y) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();

        final Data readData = new Data(1);
        readData.set(Read.DataKey.CONTACT_ID, getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_READ, readData));

        final Data deleteData = new Data(1);
        deleteData.set(Delete.DataKey.CONTACT_ID, getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_DELETE, deleteData));
        jPopupMenu.show(invoker, x, y);
    }
}
