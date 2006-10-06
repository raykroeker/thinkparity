/**
 * Created On: 13-Jul-06 1:19:09 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Calendar;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.container.Container;

import com.thinkparity.ophelia.browser.application.browser.BrowserConstants;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.TabCellImage;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCacheTest.TabCellIconTest;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.container.*;
import com.thinkparity.ophelia.browser.platform.plugin.Plugin;
import com.thinkparity.ophelia.browser.platform.plugin.PluginId;
import com.thinkparity.ophelia.browser.platform.plugin.PluginRegistry;
import com.thinkparity.ophelia.browser.platform.plugin.extension.ActionExtension;
import com.thinkparity.ophelia.model.container.ContainerDraft;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContainerCell extends DefaultTabCell {
    
    /** A flag indicating whether or not the user is the key holder. */
    private Boolean keyHolder = Boolean.FALSE;

    /** A flag indicating whether or not the cell has been seen. */
    private Boolean seen = Boolean.FALSE;

    /** A flag indicating whether or not the cell is urgent. */
    private Boolean urgent = Boolean.FALSE;
    
    /** The draft owner. */
    private String draftOwner = null;
    
    /** A flag indicating if this cell is the first in a group. */
    private Boolean firstInGroup = Boolean.FALSE;
    
    /** The container associated with this cell. */
    private Container container;
    
    /** Create CellContainer. */
    public ContainerCell(final Container container, final ContainerDraft containerDraft) {
        super();
        this.container = new Container();
        this.container.setCreatedBy(container.getCreatedBy());
        this.container.setCreatedOn(container.getCreatedOn());
        this.container.setDraft(container.isDraft());
        this.container.setId(container.getId());
        this.container.setLocalDraft(container.isLocalDraft());
        this.container.setName(container.getName());
        this.container.setRemoteInfo(container.getRemoteInfo());
        this.container.setState(container.getState());
        this.container.setUniqueId(container.getUniqueId());
        this.container.setUpdatedBy(container.getUpdatedBy());
        this.container.setUpdatedOn(container.getUpdatedOn());
        this.container.add(container.getFlags());
        this.keyHolder = this.container.contains(ArtifactFlag.KEY);
        this.seen = this.container.contains(ArtifactFlag.SEEN);
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
    public boolean equals(final Object obj) {
        if (null != obj && obj instanceof ContainerCell) {
            return ((ContainerCell) obj).container.equals(container);
        }
        return false;
    }
    
    /**
     * Get the container Id.
     * 
     * @return The container id.
     */
    public Long getId() {
        return container.getId();
    }
    
    /**
     * Get the container name.
     * 
     * @return The container name.
     */
    public String getName() {
        return container.getName();
    }
    
    /**
     * Obtain the update date.
     * 
     * @return The updated date.
     */
    public Calendar getUpdatedOn() {
        return container.getUpdatedOn();
    }
    
    /**
     * Determine whether or not there exists a draft.
     * 
     * @return True if there exists a draft; false otherwise.
     */
    public Boolean isDraft() {
        return container.isDraft();
    }
    
    /** 
     * Determine whether or not the draft is local.
     * 
     * @return True if the draft is local; false otherwise.
     */
    public Boolean isLocalDraft() {
        return container.isLocalDraft();
    }

    /**
     * Obtain the background image for a cell.
     * 
     * @return A buffered image.
     */
    public BufferedImage getBackground() {
        if(isUrgent()) { return imageCache.read(TabCellImage.BG_URGENT); }
        else { return imageCache.read(TabCellImage.BG_DEFAULT); }
    }

    /**
     * Obtain the background image for a selected cell.
     * 
     * 
     * @return A buffered image.
     */
    public BufferedImage getBackgroundSelected() {
        if(isUrgent()) { return imageCache.read(TabCellImage.BG_URGENT); }
        else { return imageCache.read(TabCellImage.BG_SEL_DEFAULT); }
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIcon()
     */
    public ImageIcon getNodeIcon() {
        return null;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIconSelected()
     */
    public ImageIcon getNodeIconSelected() {
        return null;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getSecondNodeIcon()
     */
    public ImageIcon getSecondNodeIcon() {
        return imageCacheTest.read(TabCellIconTest.CONTAINER); 
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell#getTextNoClipping(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     */
    @Override
    public String getTextNoClipping(final TextGroup textGroup) {
        switch (textGroup) {
        case EAST:
            return draftOwner;
        case WEST:
            return container.getName();
        default:
            throw Assert.createUnreachable("UNKNOWN TEXT GROUP");
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextFont(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     *
     */
    public Font getTextFont(TextGroup textGroup) {
        if (isSeen()) { return BrowserConstants.Fonts.DefaultFont; }
        else { return BrowserConstants.Fonts.DefaultFontBold; }
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
    public int hashCode() { return container.hashCode(); }
    
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(java.awt.Component, java.awt.event.MouseEvent, MouseEvent)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        
        if(connection == Connection.ONLINE) {
            Boolean needSeparator = Boolean.FALSE;
            if (container.isDraft() && container.isLocalDraft()) {
                final Data publishData = new Data(1);
                publishData.set(Publish.DataKey.CONTAINER_ID, container.getId());
                jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PUBLISH, publishData));
                
                final Data deleteData = new Data(1);
                deleteData.set(DeleteDraft.DataKey.CONTAINER_ID, container.getId());
                jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_DELETE_DRAFT, deleteData));
                needSeparator = Boolean.TRUE;
            }
            
            if (!container.isDraft()) {
                final Data createDraftData = new Data(1);
                createDraftData.set(CreateDraft.DataKey.CONTAINER_ID, container.getId());
                jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_CREATE_DRAFT, createDraftData));  
                needSeparator = Boolean.TRUE;
            } 
            
            if (needSeparator) {
                jPopupMenu.addSeparator(); 
            }
        } 
        
        // TODO remove this code section, it is here for debug only.
        if(connection != Connection.ONLINE) {
            if (container.isDraft() && container.isLocalDraft()) {
                final Data publishData = new Data(1);
                publishData.set(Publish.DataKey.CONTAINER_ID, container.getId());
                jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PUBLISH, publishData));
            }
        }                  
        
        if (container.isDraft() && container.isLocalDraft()) {
            final Data addDocumentData = new Data(2);
            addDocumentData.set(AddDocument.DataKey.CONTAINER_ID, container.getId());
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
            final PluginRegistry pluginRegistry = new PluginRegistry();
            final Plugin archivePlugin = pluginRegistry.getPlugin(PluginId.ARCHIVE);
            if (null != archivePlugin) {
                final ActionExtension archiveExtension =
                    pluginRegistry.getActionExtension(PluginId.ARCHIVE, "ArchiveAction");
                jPopupMenu.add(popupItemFactory.createPopupItem(
                        archiveExtension, container));
            }
        }              

        final Data deleteData = new Data(1);
        deleteData.set(Delete.DataKey.CONTAINER_ID, container.getId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_DELETE, deleteData));
        
        jPopupMenu.addSeparator();
        
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_EXPORT, Data.emptyData()));
        if (container.isDraft() && container.isLocalDraft()) {
            final Data printData = new Data(1);
            printData.set(PrintDraft.DataKey.CONTAINER_ID, container.getId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PRINT_DRAFT, printData));
        } else {
            final Data printData = new Data(1);
            printData.set(PrintDraft.DataKey.CONTAINER_ID, container.getId());
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
            final JMenuItem idJMenuItem = new JMenuItem("Id - " + container.getId());
            idJMenuItem.putClientProperty("COPY_ME", container.getId());
            idJMenuItem.addActionListener(debugActionListener);

            final JMenuItem uidJMenuItem = new JMenuItem("Unique id - " + container.getUniqueId());
            uidJMenuItem.putClientProperty("COPY_ME", container.getUniqueId());
            uidJMenuItem.addActionListener(debugActionListener);

            jPopupMenu.addSeparator();
            jPopupMenu.add(idJMenuItem);
            jPopupMenu.add(uidJMenuItem);
        }
        jPopupMenu.show(invoker, e.getX(), e.getY());
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isChildren()
     */
    public Boolean isChildren() {
        return Boolean.TRUE;
    } 
}
