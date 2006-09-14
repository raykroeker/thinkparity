/**
 * Created On: 3-Aug-06 5:38:18 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;


import com.thinkparity.codebase.swing.border.BottomBorder;
import com.thinkparity.codebase.swing.border.TopBorder;


import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.BrowserConstants.Colours;
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
import com.thinkparity.ophelia.browser.platform.action.container.AddDocument;
import com.thinkparity.ophelia.browser.platform.action.container.DeleteDraft;
import com.thinkparity.ophelia.browser.platform.action.container.PrintDraft;
import com.thinkparity.ophelia.browser.platform.action.container.Publish;
import com.thinkparity.ophelia.browser.util.localization.MainCellL18n;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.document.Document;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class DraftCell extends ContainerDraft implements TabCell  {
    
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

    /** The parent cell. */
    private final ContainerCell container;

    /** A flag indicating the expand\collapse status. */
    private Boolean expanded = Boolean.FALSE;
    
    /** Flag to indicate if there are documents. */
    private Boolean haveDocuments = Boolean.FALSE;    

    /** An image cache. */
    private final MainCellImageCache imageCache;
    
    /** The draft cell localization. */
    private final MainCellL18n localization; 
    
    /** A popup menu item factory. */
    private final PopupItemFactory popupItemFactory;

    /**
     * Create a CellContainer.
     */
    public DraftCell(final ContainerCell containerDisplay, final ContainerDraft draft) {
        super();
        setContainerId(draft.getContainerId());
        this.haveDocuments = Boolean.FALSE;
        for(final Document document : draft.getDocuments()) {
            addDocument(document);
            putState(document, draft.getState(document));
            this.haveDocuments = Boolean.TRUE;
        }
        this.container = containerDisplay;
        this.localization = new MainCellL18n("MainCellContainerDraft");
        this.popupItemFactory = PopupItemFactory.getInstance();
        this.imageCache = new MainCellImageCache();
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
     * Obtain the container display cell.
     * 
     * @return The container display cell.
     */
    public ContainerCell getContainerDisplay() { return container; }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIcon()
     * 
     */
    public ImageIcon getNodeIcon() {
        if (!haveDocuments) {
            return imageCache.read(ContainerIcon.NODE_NOCHILDREN);            
        } else if (isExpanded()) {
            return imageCache.read(ContainerIcon.NODE_EXPANDED);
        } else {
            return imageCache.read(ContainerIcon.NODE_COLLAPSED);
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getNodeIconSelected()
     * 
     */
    public ImageIcon getNodeIconSelected() {
        if (!haveDocuments) {
            return imageCache.read(ContainerIcon.NODE_NOCHILDREN); 
        } else if (isExpanded()) {
            return imageCache.read(ContainerIcon.NODE_SEL_EXPANDED);
        } else {
            return imageCache.read(ContainerIcon.NODE_SEL_COLLAPSED);
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
    public String getText() { return localization.getString("Draft"); }
    
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
    public void setExpanded(final Boolean expanded) {
        this.expanded = expanded;
        if (!haveDocuments) {
            this.expanded = Boolean.FALSE;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        
        if(connection == Connection.ONLINE) {
            final Data publishData = new Data(1);
            publishData.set(Publish.DataKey.CONTAINER_ID, getContainerId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PUBLISH, publishData));

            final Data deleteData = new Data(1);
            deleteData.set(DeleteDraft.DataKey.CONTAINER_ID, getContainerId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_DELETE_DRAFT, deleteData));
            
            jPopupMenu.addSeparator();
        }
        
        final Data addDocumentData = new Data(2);
        addDocumentData.set(AddDocument.DataKey.CONTAINER_ID, getContainerId());
        addDocumentData.set(AddDocument.DataKey.FILES, new File[0]);
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_ADD_DOCUMENT, addDocumentData));       
        jPopupMenu.addSeparator();       
        
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_EXPORT_DRAFT, Data.emptyData()));
        final Data printData = new Data(1);
        printData.set(PrintDraft.DataKey.CONTAINER_ID, getContainerId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PRINT_DRAFT, printData));
        jPopupMenu.show(invoker, e.getX(), e.getY());
    }
}
