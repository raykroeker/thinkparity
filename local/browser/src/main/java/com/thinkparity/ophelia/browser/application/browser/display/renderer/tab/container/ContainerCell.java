/**
 * Created On: 13-Jul-06 1:19:09 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;


import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.swing.border.BottomBorder;
import com.thinkparity.codebase.swing.border.TopBorder;

import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.ContainerIcon;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentIcon;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentImage;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.container.AddDocument;
import com.thinkparity.ophelia.browser.platform.action.container.CreateDraft;
import com.thinkparity.ophelia.browser.platform.action.container.Delete;
import com.thinkparity.ophelia.browser.platform.action.container.DeleteDraft;
import com.thinkparity.ophelia.browser.platform.action.container.PrintDraft;
import com.thinkparity.ophelia.browser.platform.action.container.Publish;
import com.thinkparity.ophelia.model.container.ContainerDraft;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainerCell extends Container implements TabCell  {

    /** The border for the bottom of the container cell. */
    private static final Border BORDER_BOTTOM;

    /** The border for the top of the container cell. */
    private static final Border BORDER_TOP;
    
    /** The border for the top of a group of container cells. */
    private static final Border BORDER_GROUP_TOP;
    
    /** The border insets for the top of the container cell. */
    private static final Insets BORDER_TOP_INSETS;
    private static final Insets BORDER_GROUP_TOP_INSETS;

    /** The cell's text foreground color. */
    private static final Color TEXT_FG;

    /** The cell's text foreground colour for closed containers. */
    private static final Color TEXT_FG_CLOSED;

    /** Maximum length of a container cell's text. */
    private static final Integer TEXT_MAX_LENGTH;
    
    static {
        BORDER_TOP_INSETS = new Insets(2,0,0,0);  // Top, left, bottom, right  
        BORDER_GROUP_TOP_INSETS = new Insets(3,0,0,0);
        BORDER_BOTTOM = new BottomBorder(Colours.MAIN_CELL_DEFAULT_BORDER1);
        BORDER_TOP = new TopBorder(Colours.MAIN_CELL_DEFAULT_BORDER1, BORDER_TOP_INSETS);
        BORDER_GROUP_TOP = new TopBorder(Colours.MAIN_CELL_DEFAULT_BORDER_GROUP, 2, BORDER_GROUP_TOP_INSETS);

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
    
    /** The draft owner. */
    private String draftOwner = null;
    
    /** A flag indicating if this cell is the first in a group. */
    private Boolean firstInGroup = Boolean.FALSE;
    
    /** Create CellContainer. */
    public ContainerCell(final Container container, final ContainerDraft containerDraft) {
        super();
        setCreatedBy(container.getCreatedBy());
        setCreatedOn(container.getCreatedOn());
        setDraft(container.isDraft());
        setId(container.getId());
        setLocalDraft(container.isLocalDraft());
        setName(container.getName());
        setRemoteInfo(container.getRemoteInfo());
        setState(container.getState());
        setUniqueId(container.getUniqueId());
        setUpdatedBy(container.getUpdatedBy());
        setUpdatedOn(container.getUpdatedOn());
        add(container.getFlags());
        this.closed = getState() == ArtifactState.CLOSED;
        this.imageCache = new MainCellImageCache();
        this.keyHolder = contains(ArtifactFlag.KEY);
        this.popupItemFactory = PopupItemFactory.getInstance();
        this.seen = contains(ArtifactFlag.SEEN);
        this.urgent = Boolean.FALSE;
        if (null==containerDraft) {
            this.draftOwner = null;
        } else {
            this.draftOwner = containerDraft.getOwner().getName();
        }
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#equals(java.lang.Object)
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getBorder(int)
     * 
     */
    public Border getBorder(final int index, final Boolean isFirstInGroup, final Boolean lastCell) { 
        final Border topBorder;
        if (isFirstInGroup) {
            topBorder = BORDER_GROUP_TOP;
        } else {
            topBorder = BORDER_TOP;
        }
            
        if (lastCell) {
            return BorderFactory.createCompoundBorder(topBorder, BORDER_BOTTOM);
        } else {
            return topBorder;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIcon()
     * 
     */
    public ImageIcon getNodeIcon() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIconSelected()
     * 
     */
    public ImageIcon getNodeIconSelected() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getParent()
     * 
     */
    public TabCell getParent() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getText()
     * 
     */
    public String getText() {
        if(TEXT_MAX_LENGTH < getName().length()) {
            return getName().substring(0, TEXT_MAX_LENGTH - 1 - 3) + "...";
        }
        else { return getName(); }
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getSecondaryText()
     * 
     */
    public String getSecondaryText() {
        return draftOwner;
    }    

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextFont()
     * 
     */
    public Font getTextFont() {
        if(isSeen()) { return BrowserConstants.Fonts.DefaultFont; }
        else { return BrowserConstants.Fonts.DefaultFontBold; }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextForeground()
     * 
     */
    public Color getTextForeground() {
        if(isClosed()) { return TEXT_FG_CLOSED; }
        else { return TEXT_FG; }
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
        if(TEXT_MAX_LENGTH < getName().length()) { return getName(); }
        else { return null; }
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isFirstInGroup()
     * 
     */
    public Boolean isFirstInGroup() {
        return firstInGroup;
    }
    
    /**
     * Set the cell to be the first in the group.
     * 
     * @param firstInGroup
     *            The firstInGroup flag.
     */
    public void setFirstInGroup(Boolean firstInGroup) {
        this.firstInGroup = firstInGroup;
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#hashCode()
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(java.awt.Component, java.awt.event.MouseEvent, MouseEvent)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        
        if(connection == Connection.ONLINE) {
            Boolean needSeparator = Boolean.FALSE;
            if (isDraft() && isLocalDraft()) {
                final Data publishData = new Data(1);
                publishData.set(Publish.DataKey.CONTAINER_ID, getId());
                jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PUBLISH, publishData));
                
                final Data deleteData = new Data(1);
                deleteData.set(DeleteDraft.DataKey.CONTAINER_ID, getId());
                jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_DELETE_DRAFT, deleteData));
                needSeparator = Boolean.TRUE;
            }
            
            if (!isDraft()) {
                final Data createDraftData = new Data(1);
                createDraftData.set(CreateDraft.DataKey.CONTAINER_ID, getId());
                jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_CREATE_DRAFT, createDraftData));  
                needSeparator = Boolean.TRUE;
            } 
            
            if (needSeparator) {
                jPopupMenu.addSeparator(); 
            }
        }               
        
        if (isDraft() && isLocalDraft()) {
            final Data addDocumentData = new Data(2);
            addDocumentData.set(AddDocument.DataKey.CONTAINER_ID, getId());
            addDocumentData.set(AddDocument.DataKey.FILES, new File[0]);
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_ADD_DOCUMENT, addDocumentData));            
            jPopupMenu.addSeparator();
        }
        
        if(connection == Connection.ONLINE) {
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_SUBSCRIBE, Data.emptyData()));
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_UNSUBSCRIBE, Data.emptyData()));
            jPopupMenu.addSeparator();
        }
        
        // TODO should only be here if never published
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_RENAME, Data.emptyData()));       
        if(connection == Connection.ONLINE) {
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_ARCHIVE, Data.emptyData()));
        }              
        final Data deleteData = new Data(1);
        deleteData.set(Delete.DataKey.CONTAINER_ID, getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_DELETE, deleteData));
        
        jPopupMenu.addSeparator();
        
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_EXPORT, Data.emptyData()));
        if (isDraft() && isLocalDraft()) {
            final Data printData = new Data(1);
            printData.set(PrintDraft.DataKey.CONTAINER_ID, getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PRINT_DRAFT, printData));
        } else {
            final Data printData = new Data(1);
            printData.set(PrintDraft.DataKey.CONTAINER_ID, getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PRINT_VERSION, printData));
        }

        // include the container's id and unique id in the menu
        if(e.isShiftDown()) {
            final Clipboard systemClipboard =
                Toolkit.getDefaultToolkit().getSystemClipboard();
            final ActionListener debugActionListener = new ActionListener() {
                public void actionPerformed(final ActionEvent e) {
                    final StringSelection stringSelection =
                        new StringSelection(((JComponent) e.getSource()).getClientProperty("COPY_ME").toString());
                    systemClipboard.setContents(stringSelection, null);
                }
            };
            final JMenuItem idJMenuItem = new JMenuItem("Id - " + getId());
            idJMenuItem.putClientProperty("COPY_ME", getId());
            idJMenuItem.addActionListener(debugActionListener);

            final JMenuItem uidJMenuItem = new JMenuItem("Unique id - " + getUniqueId());
            uidJMenuItem.putClientProperty("COPY_ME", getUniqueId());
            uidJMenuItem.addActionListener(debugActionListener);

            jPopupMenu.addSeparator();
            jPopupMenu.add(idJMenuItem);
            jPopupMenu.add(uidJMenuItem);
        }
        jPopupMenu.show(invoker, e.getX(), e.getY());
    }
}
