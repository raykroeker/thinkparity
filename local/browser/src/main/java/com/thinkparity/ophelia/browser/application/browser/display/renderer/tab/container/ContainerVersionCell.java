/*
 * Created On: Aug 3, 2006 6:42:00 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

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

import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.swing.border.BottomBorder;
import com.thinkparity.codebase.swing.border.TopBorder;

import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentIcon;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentImage;
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
public class ContainerVersionCell implements TabCell {

    /** The border for the bottom of the container cell. */
    private static final Border BORDER_BOTTOM;

    /** The border for the top of the container cell. */
    private static final Border BORDER_TOP;
    
    /** The border insets for the top of the container cell. */
    private static final Insets BORDER_TOP_INSETS;
    
    /** The cell's text foreground color. */
    private static final Color TEXT_FG;

    static {
        BORDER_TOP_INSETS = new Insets(2,0,0,0);  // Top, left, bottom, right 
        BORDER_BOTTOM = new BottomBorder(Colours.MAIN_CELL_DEFAULT_BORDER1);
        BORDER_TOP = new TopBorder(Color.WHITE, BORDER_TOP_INSETS);
        
        TEXT_FG = Color.BLACK;
    }

    /** The container cell. */
    private final ContainerCell container;

    /** A flag indicating the expand\collapse status. */
    private Boolean expanded = Boolean.FALSE;

    /** An image cache. */
    private final MainCellImageCache imageCache;
    
    /** The draft cell localization. */
    private final MainCellL18n localization; 
    
    /** A popup item factory. */
    private final PopupItemFactory popupItemFactory;
    
    /** The container version associated with this cell. */
    private ContainerVersion containerVersion;

    /** Create MainCellContainerVersion. */
    public ContainerVersionCell(final ContainerCell container,
            final ContainerVersion containerVersion) {
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
        this.imageCache = new MainCellImageCache();
        this.localization = new MainCellL18n("MainCellContainerVersion");
        this.popupItemFactory = PopupItemFactory.getInstance();
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) { return super.equals(obj); }
    
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
        if (lastCell) {
            return BorderFactory.createCompoundBorder(BORDER_TOP, BORDER_BOTTOM);
        } else {
            return BORDER_TOP;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIcon()
     * 
     */
    public ImageIcon getNodeIcon() {
        if (isExpanded()) {
            return imageCache.read(DocumentIcon.NODE_EXPANDED);
        } else {
            return imageCache.read(DocumentIcon.NODE_DEFAULT);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIconSelected()
     * 
     */
    public ImageIcon getNodeIconSelected() {
        if (isExpanded()) {
            return imageCache.read(DocumentIcon.NODE_SEL_EXPANDED);
        } else {
            return imageCache.read(DocumentIcon.NODE_SEL_DEFAULT);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getParent()
     * 
     */
    public TabCell getParent() {
        return container;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getText()
     * 
     */
    public String getText() {
        return localization.getString("Text", new Object[] {containerVersion.getVersionId()});
    }
    
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getSecondaryText()
     * 
     */
    public String getSecondaryText() {
        return "Publish Person";
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
        return InsetFactors.LEVEL_1;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getToolTip()
     * 
     */
    public String getToolTip() {
        return null;
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
    public int hashCode() { return super.hashCode(); }

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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerDoubleClickAction(com.thinkparity.ophelia.browser.application.browser.Browser)
     */
    public void triggerDoubleClickAction(Browser browser) {       
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isExpanded()
     */
    public Boolean isExpanded() {
        return expanded;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#setExpanded(java.lang.Boolean)
     */
    public Boolean setExpanded(Boolean expand) {
        if (this.expanded != expand) {
            this.expanded = expand;
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }   
}
