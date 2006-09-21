/**
 * Created On: 21-Jun-2006 3:08:54 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.swing.border.BottomBorder;
import com.thinkparity.codebase.swing.border.TopBorder;

import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.ContactIcon;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentImage;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCellRenderer;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.Delete;
import com.thinkparity.ophelia.browser.platform.action.contact.Read;


/**
 * An extension of a contact that allows the {@link TabCellRenderer} to display
 * a parity contact.
 * 
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContactCell implements TabCell {

    /** The border for the bottom of the container cell. */
    private static final Border BORDER_BOTTOM;

    /** The border for the top of the first container cell. */
    private static final Border BORDER_TOP_0;

    /** The border for the top of the rest of the container cells. */
    private static final Border BORDER_TOP_N;

    /** The cell's text foreground color. */
    private static final Color TEXT_FG;

    /** Maximum length of cell's text. */
    private static final Integer TEXT_MAX_LENGTH;

    static {
        BORDER_BOTTOM = new BottomBorder(Colours.MAIN_CELL_DEFAULT_BORDER1);
        BORDER_TOP_0 = new TopBorder(Colours.MAIN_CELL_DEFAULT_BORDER1, new Insets(2,0,0,0));
        BORDER_TOP_N = new TopBorder(Color.WHITE);

        TEXT_FG = Color.BLACK;
        TEXT_MAX_LENGTH = 60;
    }

    /** An image cache. */
    private final MainCellImageCache imageCache;

    /** A popup item factory. */
    private final PopupItemFactory popupItemFactory;
    
    /** The contact associated with this cell. */
    private Contact contact;

    /**
     * Create a CellContact.
     */
    public ContactCell(final Contact contact) {        
        this.contact = new Contact();
        this.contact.setId(contact.getId());
        this.contact.setLocalId(contact.getLocalId());
        this.contact.setName(contact.getName());
        this.contact.setOrganization(contact.getOrganization());
        this.contact.addAllEmails(contact.getEmails());
        this.imageCache = new MainCellImageCache();
        this.popupItemFactory = PopupItemFactory.getInstance();
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        if (null != obj && obj instanceof ContactCell) {
            return ((ContactCell) obj).contact.equals(contact);
        }
        return false;
    }

    /**
     * Get the contact Id.
     * 
     * @return The contact id.
     */
    public JabberId getId() {
        return contact.getId();
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#fireSelection()
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getBorder(int)
     * 
     */
    public Border getBorder(final int index, final Boolean isFirstInGroup, final Boolean lastCell) {
        final Border topBorder;
        if (0 == index) {
            topBorder = BORDER_TOP_0;
        } else {
            topBorder = BORDER_TOP_N;
        }
        return BorderFactory.createCompoundBorder(topBorder, BORDER_BOTTOM);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIcon()
     * 
     */
    public ImageIcon getNodeIcon() {
        return imageCache.read(ContactIcon.NODE);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIconSelected()
     * 
     */
    public ImageIcon getNodeIconSelected() {
        return imageCache.read(ContactIcon.NODE_SELECTED);
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
            if(TEXT_MAX_LENGTH < contact.getName().length()) {
                return contact.getName().substring(0, TEXT_MAX_LENGTH - 1 - 3) + "...";
            }
            else {
                return contact.getName();
            }
        } else {
            return null;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextFont(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     *
     */
    public Font getTextFont(TextGroup textGroup) {
        return BrowserConstants.Fonts.DefaultFont;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextForeground(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     * 
     */
    public Color getTextForeground(TextGroup textGroup) {
        return TEXT_FG;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_0;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getToolTip()
     * 
     */
    public String getToolTip() {
        if(TEXT_MAX_LENGTH < contact.getName().length()) { return contact.getName(); }
        else { return null; }
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isFirstInGroup()
     * 
     */
    public Boolean isFirstInGroup() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#hashCode()
     * 
     */
    public int hashCode() { return contact.hashCode(); }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection,
     *      java.awt.Component, java.awt.event.MouseEvent, int, int)
     * 
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();

        final Data readData = new Data(1);
        readData.set(Read.DataKey.CONTACT_ID, contact.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_READ, readData));

        final Data deleteData = new Data(1);
        deleteData.set(Delete.DataKey.CONTACT_ID, contact.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_DELETE, deleteData));
        jPopupMenu.show(invoker, e.getX(), e.getY());
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerDoubleClickAction(com.thinkparity.ophelia.browser.application.browser.Browser)
     */
    public void triggerDoubleClickAction(Browser browser) {  
        browser.runReadContact(contact.getId());
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isExpanded()
     */
    public Boolean isExpanded() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#setExpanded(java.lang.Boolean)
     */
    public Boolean setExpanded(Boolean expand) {
        return Boolean.FALSE;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#setMouseOver(java.lang.Boolean)
     */
    public void setMouseOver(Boolean mouseOver) {
        return;        
    }
}
