/*
 * Created On: Aug 3, 2006 6:42:00 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;

import com.thinkparity.codebase.model.container.ContainerVersion;

import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCacheTest.TabCellIconTest;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.container.PrintVersion;
import com.thinkparity.ophelia.browser.platform.action.container.Share;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ContainerVersionCell extends DefaultTabCell {

    /** The container cell. */
    private final ContainerCell container;
    
    /** The draft cell localization. */
    private final MainCellL18n localization; 
    
    /** The container version associated with this cell. */
    private ContainerVersion containerVersion;

    /** Create MainCellContainerVersion. */
    public ContainerVersionCell(final ContainerCell container,
            final ContainerVersion containerVersion) {
        super();
        this.containerVersion = new ContainerVersion();
        this.containerVersion.setArtifactId(containerVersion.getArtifactId());
        this.containerVersion.setArtifactType(containerVersion.getArtifactType());
        this.containerVersion.setArtifactUniqueId(containerVersion.getArtifactUniqueId());
        this.containerVersion.setCreatedBy(containerVersion.getCreatedBy());
        this.containerVersion.setCreatedOn(containerVersion.getCreatedOn());
        this.containerVersion.setName(containerVersion.getName());
        this.containerVersion.setUpdatedBy(containerVersion.getUpdatedBy());
        this.containerVersion.setUpdatedOn(containerVersion.getUpdatedOn());
        this.containerVersion.setVersionId(containerVersion.getVersionId());
        this.container = container;
        this.localization = new MainCellL18n("MainCellContainerVersion");
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        if (null != obj && obj instanceof ContainerVersionCell) {
            return ((ContainerVersionCell) obj).containerVersion.equals(containerVersion);
        }
        return false;
    }
    
    /**
     * Get the container artifact id.
     * 
     * @return The container artifact id.
     */
    public Long getArtifactId() {
        return containerVersion.getArtifactId();
    }
    
    /**
     * Get the container version Id.
     * 
     * @return The container version id.
     */
    public Long getVersionId() {
        return containerVersion.getVersionId();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getParent()
     * 
     */
    public TabCell getParent() {
        return container;
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getSecondNodeIcon()
     */
    public ImageIcon getSecondNodeIcon() {
        if (isExpanded()) {
            return imageCacheTest.read(TabCellIconTest.FOLDER_OPEN); 
        } else {
            return imageCacheTest.read(TabCellIconTest.FOLDER_CLOSED); 
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextNoClipping(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     *
     */
    public String getTextNoClipping(TextGroup textGroup) {
        if (textGroup == TextGroup.WEST) {
            return localization.getString("Text", new Object[] {containerVersion.getVersionId()});
        } else {
            return "Publish Person";
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_1;
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#hashCode()
     * 
     */
    @Override
    public int hashCode() { return containerVersion.hashCode(); }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();

        if (Connection.ONLINE == connection) {
            final Data shareData = new Data(2);
            shareData.set(Share.DataKey.CONTAINER_ID, containerVersion.getArtifactId());
            shareData.set(Share.DataKey.VERSION_ID, containerVersion.getVersionId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_SHARE, shareData));    
            jPopupMenu.addSeparator();
        }
        
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_EXPORT_VERSION, Data.emptyData()));
        final Data printData = new Data(2);
        printData.set(PrintVersion.DataKey.CONTAINER_ID, containerVersion.getArtifactId());
        printData.set(PrintVersion.DataKey.VERSION_ID, containerVersion.getVersionId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PRINT_VERSION, printData));
        jPopupMenu.show(invoker, e.getX(), e.getY());
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isChildren()
     */
    public Boolean isChildren() {
        return Boolean.TRUE;
    }  
}
