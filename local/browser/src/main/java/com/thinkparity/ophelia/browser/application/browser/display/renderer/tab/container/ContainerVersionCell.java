/*
 * Created On: Aug 3, 2006 6:42:00 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.User;

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

    /** The cell localization. */
    private final MainCellL18n localization;

    /** The parent <code>TabCell</code>. */
    private TabCell parent;
    
    /** The user who published this version. */
    private User publishedBy; 
    
    /** The <code>ContainerVersion</code>. */
    private ContainerVersion version;

    /** Create MainCellContainerVersion. */
    public ContainerVersionCell() {
        super();
        this.localization = new MainCellL18n("MainCellContainerVersion");
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        if (null != obj && obj instanceof ContainerVersionCell) {
            return ((ContainerVersionCell) obj).version.equals(version);
        }
        return false;
    }
    
    /**
     * Get the container artifact id.
     * 
     * @return The container artifact id.
     */
    public Long getArtifactId() {
        return version.getArtifactId();
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getParent()
     * 
     */
    public TabCell getParent() {
        return parent;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getSecondNodeIcon()
     */
    public ImageIcon getSecondNodeIcon() {
        return imageCacheTest.read(TabCellIconTest.VERSION); 
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_1;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextNoClipping(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     *
     */
    public String getTextNoClipping(final TextGroup textGroup) {
        final String textNoClipping;
        switch(textGroup) {
        case WEST:
            if (isToday(version.getCreatedOn().getTime())) {
                textNoClipping = localization.getString("TextToday", version.getCreatedOn().getTime(), publishedBy.getName());
            } else {
                textNoClipping =  localization.getString("Text", version.getCreatedOn().getTime(), publishedBy.getName()); 
            }            
            break;
        case EAST:
            textNoClipping =  null;
            break;
        default:
            throw Assert.createUnreachable("UNKNOWN TEXT GROUP");
        }
        return textNoClipping;
    }

    /**
     * Get the container version Id.
     * 
     * @return The container version id.
     */
    public Long getVersionId() {
        return version.getVersionId();
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#hashCode()
     * 
     */
    @Override
    public int hashCode() {
        return version.hashCode();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isChildren()
     */
    public Boolean isChildren() {
        return Boolean.TRUE;
    }
    
    /**
     * Set parent.
     *
     * @param parent The TabCell.
     */
    public void setParent(final TabCell parent) {
        this.parent = parent;
    }

    /**
     * Set publishedBy.
     *
     * @param publishedBy The User.
     */
    public void setPublishedBy(final User publishedBy) {
        this.publishedBy = publishedBy;
    }

    /**
     * Set version.
     *
     * @param version The ContainerVersion.
     */
    public void setVersion(final ContainerVersion version) {
        this.version = version;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();

        if (Connection.ONLINE == connection) {
            final Data shareData = new Data(2);
            shareData.set(Share.DataKey.CONTAINER_ID, version.getArtifactId());
            shareData.set(Share.DataKey.VERSION_ID, version.getVersionId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_SHARE, shareData));    
            jPopupMenu.addSeparator();
        }
        
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_EXPORT_VERSION, Data.emptyData()));
        final Data printData = new Data(2);
        printData.set(PrintVersion.DataKey.CONTAINER_ID, version.getArtifactId());
        printData.set(PrintVersion.DataKey.VERSION_ID, version.getVersionId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PRINT_VERSION, printData));
        jPopupMenu.show(invoker, e.getX(), e.getY());
    }  
    
    private Boolean isToday(Date date) {
        final StringBuffer dateStr = new StringBuffer();
        final StringBuffer todayStr = new StringBuffer();
        dateStr.append(MessageFormat.format("{0,date,yyyyMMMdd}", date));
        todayStr.append(MessageFormat.format("{0,date,yyyyMMMdd}", Calendar.getInstance().getTime()));
        return dateStr.toString().equals(todayStr.toString());
    }
}
