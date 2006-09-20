/**
 * Created On: 14-Sep-06 11:28:47 AM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.ContainerIcon;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentImage;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.Read;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainerVersionSentToUserCell implements TabCell {
    
    /** The cell's text foreground color. */
    private static final Color TEXT_FG;
    
    /** Maximum length of cell's text. */
    private static final Integer TEXT_MAX_LENGTH;

    static {      
        TEXT_FG = Color.BLACK;
        TEXT_MAX_LENGTH = 60;
    }

    /** An image cache. */
    private final MainCellImageCache imageCache;
    
    /** A popup menu item factory. */
    private final PopupItemFactory popupItemFactory;
    
    /** The user's parent cell. */
    private final ContainerVersionSentToCell versionSentTo;
    
    /** The user's parent version (grandparent cell). */
    private final ContainerVersionCell version;
    
    /** The user associated with this cell. */
    private User user;

    /** Create ContainerVersionSentToCell. */
    public ContainerVersionSentToUserCell(final ContainerVersionSentToCell versionSentTo, final User user) { 
        this.user = new User();
        this.user.setId(user.getId());
        this.user.setLocalId(user.getLocalId());
        this.user.setName(user.getName());
        this.user.setOrganization(user.getOrganization());
        this.user.setTitle(user.getTitle());
        this.imageCache = new MainCellImageCache();        
        this.popupItemFactory = PopupItemFactory.getInstance();
        this.versionSentTo = versionSentTo;
        this.version = (ContainerVersionCell) versionSentTo.getParent();
    }
    
    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        if (null != obj && obj instanceof ContainerVersionSentToUserCell) {           
            return ((ContainerVersionSentToUserCell) obj).user.equals(user) &&
                   ((ContainerVersionSentToUserCell) obj).version.equals(version);
        }
        return false;
    }
    
    /**
     * Get the user Id.
     * 
     * @return The user id.
     */
    public JabberId getId() {
        return user.getId();
    }

    /**
     * Obtain the background image for a cell.
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
        return getParent().getBorder(index, isFirstInGroup, lastCell);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIcon()
     * 
     */
    public ImageIcon getNodeIcon() {
        return imageCache.read(ContainerIcon.NODE_NOCHILDREN); 
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIconSelected()
     * 
     */
    public ImageIcon getNodeIconSelected() {
        return imageCache.read(ContainerIcon.NODE_NOCHILDREN); 
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getParent()
     * 
     */
    public TabCell getParent() {
        return versionSentTo;
    }
    
    /**
     * Get the text for the cell.
     * 
     * @return A string.
     */
    public String getFullText() {
        String text = new String(user.getName());
        if (user.isSetTitle() || user.isSetOrganization()) {
            text += "  (";
            if (user.isSetTitle()) {
                text += user.getTitle();
                if (user.isSetOrganization()) {
                    text += ", ";
                }
            }
            if (user.isSetOrganization()) {
                text += user.getOrganization();
            }
            text += ")";
        }
        return text;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getText()
     * 
     */
    public String getText() {
        if(TEXT_MAX_LENGTH < getFullText().length()) {
            return getFullText().substring(0, TEXT_MAX_LENGTH - 1 - 3) + "...";
        }
        else { return getFullText(); }
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getSecondaryText()
     * 
     */
    public String getSecondaryText() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextFont()
     * 
     */
    public Font getTextFont() { return Fonts.DefaultFont; }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextForeground()
     * 
     */
    public Color getTextForeground() { return TEXT_FG; }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_3;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getToolTip()
     * 
     */
    public String getToolTip() {
        if(TEXT_MAX_LENGTH < getFullText().length()) { return getFullText(); }
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
    @Override
    public int hashCode() {
        return toString().hashCode();
    }
    
    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
            .append(user.getId()).append("/")
            .append(version.getArtifactId()).append("/")
            .append(version.getVersionId())
            .toString();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();

        final Data readData = new Data(1);
        readData.set(Read.DataKey.CONTACT_ID, user.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_READ, readData));

        jPopupMenu.show(invoker, e.getX(), e.getY());
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerDoubleClickAction(com.thinkparity.ophelia.browser.application.browser.Browser)
     */
    public void triggerDoubleClickAction(Browser browser) {  
        browser.runReadContact(user.getId());
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
}
