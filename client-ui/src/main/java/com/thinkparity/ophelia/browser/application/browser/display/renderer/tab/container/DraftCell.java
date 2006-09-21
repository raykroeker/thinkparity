/**
 * Created On: 3-Aug-06 5:38:18 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

import javax.swing.JPopupMenu;

import com.thinkparity.codebase.model.document.Document;

import com.thinkparity.ophelia.browser.Constants.InsetFactors;
import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.DefaultTabCell;
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

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class DraftCell extends DefaultTabCell implements TabCell  {

    /** The parent cell. */
    private final ContainerCell container;
    
    /** Flag to indicate if there are documents. */
    private Boolean haveDocuments = Boolean.FALSE;    
    
    /** The draft cell localization. */
    private final MainCellL18n localization; 
    
    /** The container draft associated with this cell. */
    private ContainerDraft containerDraft;

    /**
     * Create a CellContainer.
     */
    public DraftCell(final ContainerCell containerDisplay, final ContainerDraft draft) {
        this.containerDraft = new ContainerDraft();
        this.containerDraft.setContainerId(draft.getContainerId());
        this.haveDocuments = Boolean.FALSE;
        for(final Document document : draft.getDocuments()) {
            this.containerDraft.addDocument(document);
            this.containerDraft.putState(document, draft.getState(document));
            this.haveDocuments = Boolean.TRUE;
        }
        this.container = containerDisplay;
        this.localization = new MainCellL18n("MainCellContainerDraft");
    }

    /**
     * @see com.thinkparity.codebase.model.artifact.Artifact#equals(java.lang.Object)
     * 
     */
    public boolean equals(final Object obj) {
        if (null != obj && obj instanceof DraftCell) {
            return ((DraftCell) obj).containerDraft.equals(containerDraft);
        }
        return false;
    }
    
    /**
     * Get container ID.
     * 
     * @return Long container id.
     */
    public Long getContainerId() {
        return containerDraft.getContainerId();
    }
    
    /**
     * Get the draft state.
     * 
     * @return 
     */
    public ContainerDraft.ArtifactState getState(final Long artifactId) {
        return containerDraft.getState(artifactId);
    }
    
    /**
     * Get documents.
     * 
     * 
     */
    public List<Document> getDocuments() {
        return containerDraft.getDocuments();
    }

    /**
     * Obtain the container display cell.
     * 
     * @return The container display cell.
     */
    public ContainerCell getContainerDisplay() { return container; }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getParent()
     * 
     */
    public TabCell getParent() {
        return container;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#getTextNoClipping(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup)
     *
     */
    public String getTextNoClipping(TextGroup textGroup) {
        if (textGroup == TextGroup.MAIN_TEXT) {
            return localization.getString("Draft");
        } else {
            return null;
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
    public int hashCode() { return containerDraft.hashCode(); }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#triggerPopup(com.thinkparity.ophelia.browser.platform.Platform.Connection, java.awt.Component, java.awt.event.MouseEvent, int, int)
     */
    public void triggerPopup(final Connection connection,
            final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        
        if(connection == Connection.ONLINE) {
            final Data publishData = new Data(1);
            publishData.set(Publish.DataKey.CONTAINER_ID, containerDraft.getContainerId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PUBLISH, publishData));

            final Data deleteData = new Data(1);
            deleteData.set(DeleteDraft.DataKey.CONTAINER_ID, containerDraft.getContainerId());
            jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_DELETE_DRAFT, deleteData));
            
            jPopupMenu.addSeparator();
        }
        
        final Data addDocumentData = new Data(2);
        addDocumentData.set(AddDocument.DataKey.CONTAINER_ID, containerDraft.getContainerId());
        addDocumentData.set(AddDocument.DataKey.FILES, new File[0]);
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_ADD_DOCUMENT, addDocumentData));       
        jPopupMenu.addSeparator();       
        
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_EXPORT_DRAFT, Data.emptyData()));
        final Data printData = new Data(1);
        printData.set(PrintDraft.DataKey.CONTAINER_ID, containerDraft.getContainerId());
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_PRINT_DRAFT, printData));
        jPopupMenu.show(invoker, e.getX(), e.getY());
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell#isChildren()
     */
    public Boolean isChildren() {
        return haveDocuments;
    }  
}
