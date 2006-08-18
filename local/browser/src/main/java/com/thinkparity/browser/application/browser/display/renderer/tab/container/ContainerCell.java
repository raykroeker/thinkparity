/**
 * Created On: 13-Jul-06 1:19:09 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.renderer.tab.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import com.thinkparity.browser.Constants.InsetFactors;
import com.thinkparity.browser.application.browser.BrowserConstants;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentIcon;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentImage;
import com.thinkparity.browser.application.browser.display.avatar.main.border.DocumentDefault;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.platform.Platform.Connection;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.action.container.CreateDraft;

import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.container.Container;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainerCell extends Container implements TabCell  {

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

    /** A flag indicating whether or not the container is closed. */
    private Boolean closed = Boolean.FALSE;

    /** A flag indicating the expand\collapse status. */
    private Boolean expanded = Boolean.FALSE;
    
    /** An image cache. */
    private final MainCellImageCache imageCache;
    
    /** A flag indicating whether or not the user is the key holder. */
    private Boolean keyHolder = Boolean.FALSE; 
    
    /** A popup menu item factory. */
    private final PopupItemFactory popupItemFactory;
    
    /** A flag indicating whether or not the cell has been seen. */
    private Boolean seen = Boolean.FALSE;
    
    /** A flag indicating whether or not the cell is urgent. */
    private Boolean urgent = Boolean.FALSE;

    /** Create CellContainer. */
    public ContainerCell(final Container container) {
        super();
        setCreatedBy(container.getCreatedBy());
        setCreatedOn(container.getCreatedOn());
        add(container.getFlags());
        setUniqueId(container.getUniqueId());
        setName(container.getName());
        setUpdatedBy(container.getUpdatedBy());
        setUpdatedOn(container.getUpdatedOn());
        setId(container.getId());
        setLocalDraft(container.isSetLocalDraft());
        setRemoteInfo(container.getRemoteInfo());
        setState(container.getState());
        this.closed = getState() == ArtifactState.CLOSED;
        this.imageCache = new MainCellImageCache();
        this.keyHolder = contains(ArtifactFlag.KEY);
        this.popupItemFactory = PopupItemFactory.getInstance();
        this.seen = contains(ArtifactFlag.SEEN);
        this.urgent = Boolean.FALSE;
    }

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
        if(isClosed()) { return imageCache.read(DocumentImage.BG_CLOSED); }
        else if(isUrgent()) { return imageCache.read(DocumentImage.BG_URGENT); }
        else { return imageCache.read(DocumentImage.BG_DEFAULT); }
    }
    
    /**
     * Obtain the background image for a selected cell.
     * 
     * 
     * @return A buffered image.
     */
    public BufferedImage getBackgroundSelected() {
        if(isClosed()) { return imageCache.read(DocumentImage.BG_SEL_CLOSED); }
        else if(isUrgent()) { return imageCache.read(DocumentImage.BG_URGENT); }
        else { return imageCache.read(DocumentImage.BG_SEL_DEFAULT); }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getBorder()
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
        return null;
        //if(isKeyHolder()) { return imageCache.read(DocumentIcon.INFO_IS_KEYHOLDER); }
        //else { return imageCache.read(DocumentIcon.INFO_IS_NOT_KEYHOLDER); }
    }

    /**
     * Obtain the document cell's key requests.
     * 
     * @return The document cell's key requests.
     */
    //public List<KeyRequest> getKeyRequests() { return keyRequests; }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getNodeIcon()
     * 
     */
    public ImageIcon getNodeIcon() {
        return imageCache.read(DocumentIcon.NODE_DEFAULT);
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getNodeIconSelected()
     * 
     */
    public ImageIcon getNodeIconSelected() {
        if(isExpanded()) { return imageCache.read(DocumentIcon.NODE_SEL_EXPANDED); }
        else { return imageCache.read(DocumentIcon.NODE_SEL_DEFAULT); }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getParent()
     * 
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
        if(isSeen()) { return BrowserConstants.Fonts.DefaultFont; }
        else { return BrowserConstants.Fonts.DefaultFontBold; }
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getTextForeground()
     * 
     */
    public Color getTextForeground() {
        if(isClosed()) { return TEXT_FG_CLOSED; }
        else { return TEXT_FG; }
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
     * Determine whether or not the container is closed.
     * 
     * @return True if the container is closed; false otherwise.
     */
    public Boolean isClosed() { return closed; }    

    /**
     * Determine whether or not the cell is expanded.
     * 
     * @return True if the cell is expanded.
     */
    public Boolean isExpanded() { return expanded; }
    
    /**
     * Determine whether or not the user is the container's key holder.
     * 
     * @return True if the user is the key holder.
     */
    public Boolean isKeyHolder() { return keyHolder; }
    
    /**
     * Determine whether or not the container cell has been seen.
     * 
     * @return True if the container has been seen; false otherwise.
     */
    public Boolean isSeen() { return seen; }

    /**
     * Determine whether or not the container is urgent.
     * 
     * @return True if the container is urgent.
     */
    public Boolean isUrgent() { return urgent; }

    /**
     * Set the expanded flag.
     * 
     * @param expanded
     *            The expanded flag.
     */
    public void setExpanded(final Boolean expanded) { this.expanded = expanded; }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(final Connection connection, final Component invoker, final MouseEvent e, final int x, final int y) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();

        if (!isSetDraft()) {
            final Data createDraftData = new Data(1);
            createDraftData.set(CreateDraft.DataKey.CONTAINER_ID, getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_CREATE_DRAFT, createDraftData));        
        }
        jPopupMenu.show(invoker, x, y);
    }
}
