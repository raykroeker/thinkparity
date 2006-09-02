/**
 * Created On: 3-Aug-06 5:38:18 PM
 */
package com.thinkparity.browser.application.browser.display.renderer.tab.container;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JPopupMenu;
import javax.swing.border.Border;

import com.thinkparity.browser.Constants.InsetFactors;
import com.thinkparity.browser.application.browser.BrowserConstants.Fonts;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.ContainerIcon;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellImageCache.DocumentImage;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.platform.Platform.Connection;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.action.container.AddDocument;
import com.thinkparity.browser.platform.action.container.DeleteDraft;
import com.thinkparity.browser.platform.action.container.Publish;
import com.thinkparity.browser.platform.util.l10n.MainCellL18n;

import com.thinkparity.model.parity.model.container.ContainerDraft;
import com.thinkparity.model.parity.model.document.Document;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class DraftCell extends ContainerDraft implements TabCell  {
    
    /** The cell's text foreground color. */
    private static final Color TEXT_FG;

    static { TEXT_FG = Color.BLACK; }

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
        return getParent().getBorder(index);
    }

    /**
     * Obtain the container display cell.
     * 
     * @return The container display cell.
     */
    public ContainerCell getContainerDisplay() { return container; }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getNodeIcon()
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
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getNodeIconSelected()
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
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getParent()
     * 
     */
    public TabCell getParent() {
        return container;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getText()
     * 
     */
    public String getText() { return localization.getString("Draft"); }
    
    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getSecondaryText()
     * 
     */
    public String getSecondaryText() {
        return null;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getTextFont()
     * 
     */
    public Font getTextFont() { return Fonts.DefaultFont; }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getTextForeground()
     * 
     */
    public Color getTextForeground() { return TEXT_FG; }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getTextInsetFactor()
     * 
     */
    public Float getTextInsetFactor() {
        return InsetFactors.LEVEL_1;
    }

    /**
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#getToolTip()
     * 
     */
    public String getToolTip() {
        return null;
    }

    /**
     * @see com.thinkparity.model.parity.model.artifact.Artifact#hashCode()
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
     * @see com.thinkparity.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();

        final Data addDocumentData = new Data(1);
        addDocumentData.set(AddDocument.DataKey.CONTAINER_ID, getContainerId());
        addDocumentData.set(AddDocument.DataKey.FILES, new File[0]);
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_ADD_DOCUMENT, addDocumentData));
        
        if(connection == Connection.ONLINE) {
            jPopupMenu.addSeparator();
            
            final Data publishData = new Data(1);
            publishData.set(Publish.DataKey.CONTAINER_ID, getContainerId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PUBLISH, publishData));

            final Data deleteData = new Data(1);
            deleteData.set(DeleteDraft.DataKey.CONTAINER_ID, getContainerId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_DELETE_DRAFT, deleteData));
        }
        jPopupMenu.addSeparator();
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_EXPORT, Data.emptyData()));
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PRINT, Data.emptyData()));
        jPopupMenu.show(invoker, e.getX(), e.getY());
    }
}
