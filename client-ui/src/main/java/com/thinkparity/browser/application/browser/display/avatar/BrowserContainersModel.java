/**
 * Created On: 13-Jul-06 1:03:06 PM
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JPopupMenu;
import javax.swing.ListModel;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.display.avatar.container.CellContainer;
import com.thinkparity.browser.application.browser.display.avatar.container.CellDraft;
import com.thinkparity.browser.application.browser.display.avatar.container.MainCellContainerVersion;
import com.thinkparity.browser.application.browser.display.avatar.container.MainCellDraftDocument;
import com.thinkparity.browser.application.browser.display.avatar.container.MainCellVersionDocument;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCell;
import com.thinkparity.browser.application.browser.display.avatar.main.popup.PopupContainer;
import com.thinkparity.browser.application.browser.display.avatar.main.popup.PopupDraft;
import com.thinkparity.browser.application.browser.display.avatar.main.popup.PopupDraftDocument;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;

import com.thinkparity.model.parity.model.container.ContainerDraft;
import com.thinkparity.model.parity.model.document.Document;

/**
 * @author rob_masako@shaw.ca; raykroeker@gmail.com
 * @version 1.1.2.4
 */
public class BrowserContainersModel {

    /** An apache logger. */
    protected final Logger logger;

    /** The application. */
    private final Browser browser;

    /** A map of the container cells to their draft. */
    private final Map<CellContainer, CellDraft> containerDrafts;

    /** A list of all containers. */
    private final List<CellContainer> containers;

    /** A map of the container cells to a list of their respective versions. */
    private final Map<CellContainer, List<MainCellContainerVersion>> containerVersions;

    /** The content provider. */
    private CompositeFlatSingleContentProvider contentProvider;

    /** A map of the container draft to the draft documents. */
    private final Map<CellDraft, List<MainCellDraftDocument>> draftDocuments;

    /** The swing list model. */
    private final DefaultListModel jListModel;

    /** A map of the container version to the draft documents. */
    private final Map<MainCellContainerVersion, List<MainCellVersionDocument>> versionDocuments;

    /** A list of all visible cells. */
    private final List<MainCell> visibleCells;

    /**
     * Create BrowserContainersModel.
     * 
     */
    BrowserContainersModel(final Browser browser) {
        super();
        this.browser = browser;
        this.containerDrafts = new HashMap<CellContainer, CellDraft>(50, 0.75F);
        this.containers = new LinkedList<CellContainer>();
        this.containerVersions = new HashMap<CellContainer, List<MainCellContainerVersion>>(50, 0.75F);
        this.draftDocuments = new HashMap<CellDraft, List<MainCellDraftDocument>>(50, 0.75F);
        this.jListModel = new DefaultListModel();
        this.logger = browser.getPlatform().getLogger(getClass());
        this.versionDocuments = new HashMap<MainCellContainerVersion, List<MainCellVersionDocument>>(50, 0.75F);
        this.visibleCells = new LinkedList<MainCell>();
    }
    

    /**
     * Get the document id, given the document name.
     * (this method accesses existing lists rather than using the provider)
     * 
     * @param cellContainer
     *              The cellContainer
     * @param documentName
     *              The document name
     * @return The document id
     */
    public Long getDocumentId(final CellContainer cellContainer,
            final String name) {
        final List<MainCellDraftDocument> draftDocuments = this.draftDocuments.get(cellContainer);
        for(final MainCellDraftDocument draftDocument : draftDocuments) {
            if(draftDocument.getName().equals(name)) { return draftDocument.getId(); }
        }
        return null;
    }

    /**
     * Get the list of document names associated with this cellContainer
     * (this method accesses existing lists rather than using the provider)
     * 
     * @param cellContainer
     *              The cellContainer
     * @return A list of document names.
     */
    public List<String> getDocumentNames(final CellContainer cellContainer) {
        final List<MainCellDraftDocument> draftDocuments = this.draftDocuments.get(cellContainer);
        final List<String> l = new ArrayList<String>(draftDocuments.size());
        for(final MainCellDraftDocument draftDocument : draftDocuments)
            l.add(draftDocument.getName());
        return l;
    }

    /**
     * Determine whether or not the cell is expanded.
     * 
     * @param mainCell
     *            The cell.
     * @return True if the cell is expanded; false otherwise.
     */
    public Boolean isExpanded(final MainCell mainCell) {
        if (mainCell instanceof CellContainer) {
            return ((CellContainer) mainCell).isExpanded();
        }
        else if (mainCell instanceof CellDraft) {
            return ((CellDraft) mainCell).isExpanded();
        }
        else if (mainCell instanceof MainCellContainerVersion) {
            return ((MainCellContainerVersion) mainCell).isExpanded();
        }
        else {
            return Boolean.FALSE;
        }
    }

    /**
     * Synchronize the document in the container list.
     * Called, for example, if a new document is created in the container (ie. draft).
     * This will move the container to the top, and also expand the container & draft.
     * 
     * @param containerId
     *            The container id.
     * @param documentId
     *            The document id.           
     * @param remote
     *            Indicates whether the sync is the result of a remote event
     */
    public void syncDocument(final Long containerId, final Long documentId, final Boolean remote) {
        syncDocumentInternal(containerId, documentId, remote);
        final CellContainer container = readContainer(containerId);
        container.setExpanded(Boolean.TRUE);
        final CellDraft containerDraft = containerDrafts.get(container);
        if (null != containerDraft) {
            containerDraft.setExpanded(Boolean.TRUE);
        }
        syncModel();
    }

    /**
     * Debug the container filter.
     *
     */
    void debug() {
        if(browser.getPlatform().isDevelopmentMode()) {
            logger.debug(getDebugId("[{0} VISIBLE CELLS]", visibleCells.size()));
            logger.debug(getDebugId("[{0} MODEL ELEMENTS]", jListModel.size()));
            // containers
            logger.debug(getDebugId("[{0} CONTAINERS]", containers.size()));
            for(final CellContainer mcContainer : containers) {
                logger.debug(getDebugId("  [{0}]", mcContainer.getName()));
                // drafts
                final CellDraft mcDraft = containerDrafts.get(mcContainer);
                logger.debug(getDebugId("    [{0}]", null == mcDraft ? "NO DRAFT INCLUDED" : "DRAFT INCLUDED"));
                if(null != mcDraft) {
                    final List<MainCellDraftDocument> draftDocuments = this.draftDocuments.get(mcDraft);
                    logger.debug(getDebugId("      [{0} DRAFT DOCUMENTS]", draftDocuments.size()));
                }
                // versions
                final List<MainCellContainerVersion> mcContainerVersions = containerVersions.get(mcContainer);
                logger.debug(getDebugId("    [{0} VERSIONS]", null == mcContainerVersions ? 0 : mcContainerVersions.size()));
            }
        }
    }

    /**
     * Obtain the swing list model.
     * 
     * @return The swing list model.
     */
    ListModel getListModel() { return jListModel; }

    /**
     * Determine if the container is visible.
     * 
     * @param cellContainer
     *            The display container.
     * @return True if the container is visible; false otherwise.
     */
    Boolean isContainerVisible(final CellContainer cellContainer) {
        return visibleCells.contains(cellContainer);
    }

    /**
     * Set the content provider. This will initialize the model with documents
     * via the provider.
     * 
     * @param contentProvider
     *            The content provider.
     */
    void setContentProvider(
            final CompositeFlatSingleContentProvider contentProvider) {
        this.contentProvider = contentProvider;
        initModel();
    }

    /**
     * Synchronize the container with the list.
     * Called, for example, if a new container is added.
     * The content provider is queried for the container and if it can be obtained,
     * it will either be added to or updated in the list. If it cannot be found,
     * it will be removed from the list.
     * 
     * @param containerId
     *            The container id.
     * @param remote
     *            Whether or not the reload is the result of a remote event or
     *            not.
     */
    void syncContainer(final Long containerId, final Boolean remote) {
        syncContainerInternal(containerId, remote);
        syncModel();
    }
    
    /**
     * Trigger a double click event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    void triggerDoubleClick(final MainCell mainCell) {
        debug();
        if (mainCell instanceof CellContainer) {
            triggerExpand(mainCell);
        }
        else if (mainCell instanceof CellDraft) {
            triggerExpand(mainCell);
        }
        else if (mainCell instanceof MainCellContainerVersion) {
            triggerExpand(mainCell);
        }
        else if (mainCell instanceof MainCellDraftDocument) {
            final MainCellDraftDocument cd = (MainCellDraftDocument) mainCell;
            browser.runOpenDocument(cd.getContainerId(), cd.getId());
        }
        else if (mainCell instanceof MainCellVersionDocument) {
            final MainCellVersionDocument cv = (MainCellVersionDocument) mainCell;
            //browser.runOpenDocumentVersion(cv.getContainerId(), cv.getId());           
        }
    }

    /**
     * Trigger the expansion of the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    void triggerExpand(final MainCell mainCell) {
        if (mainCell instanceof CellContainer) {
            final CellContainer cc = (CellContainer) mainCell;
            if(isExpanded(cc)) {
                cc.setExpanded(Boolean.FALSE);
            }
            else {
                cc.setExpanded(Boolean.TRUE);
            }
            syncModel();
        }
        else if (mainCell instanceof CellDraft) {
            final CellDraft cd = (CellDraft) mainCell;
            if(isExpanded(cd)) {
                cd.setExpanded(Boolean.FALSE);
            }
            else {
                cd.setExpanded(Boolean.TRUE);
            }
            syncModel();            
        }
        else if (mainCell instanceof MainCellContainerVersion) {
            final MainCellContainerVersion cv = (MainCellContainerVersion) mainCell;
            if(isExpanded(cv)) {
                cv.setExpanded(Boolean.FALSE);
            }
            else {
                cv.setExpanded(Boolean.TRUE);
            }
            syncModel();             
        }
    }

    /**
     * Trigger a popup event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    void triggerPopup(final MainCell mainCell, final Component invoker, final MouseEvent e,
            final int x, final int y) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        if ((null==mainCell) ||    // null if right-click on an empty area of the JList
            (mainCell instanceof CellContainer)) {
            new PopupContainer(contentProvider, (CellContainer) mainCell).trigger(browser, jPopupMenu, e);                
        }
        else if (mainCell instanceof CellDraft) {
            new PopupDraft(contentProvider, (CellDraft) mainCell).trigger(browser, jPopupMenu, e);
        }
        else if (mainCell instanceof MainCellDraftDocument) {
            new PopupDraftDocument(contentProvider, (MainCellDraftDocument) mainCell).trigger(browser, jPopupMenu, e);
        }
        logger.info("[LBROWSER] [APPLICATION] [BROWSER] [CONTAINERS AVATAR] [TRIGGER POPUP]");
        logger.debug(browser.getConnection());
        jPopupMenu.show(invoker, x, y);
    }

    /**
     * Perform a shallow clone of the containers list.
     * 
     * @return A copy of the containers list.
     */
    private List<CellContainer> cloneContainers() {
        final List<CellContainer> clone = new LinkedList<CellContainer>();
        clone.addAll(containers);
        return clone;
    }

    private Object getDebugId(final String message, final Object ... arguments) {
        return new StringBuffer("[CONTAINER LIST] ").append(MessageFormat.format(message, arguments));
    }

    /**
     * Initialize the document model
     * <ol>
     * <li>Load the containers from the provider.
     * <li>Load the documents from the provider.
     * <li>Load the history from the provider.
     * <li>Synchronize the data with the model.
     * <ol>
     */
    private void initModel() {
        // read the containers from the provider into the list.
        containers.clear();
        containers.addAll(readContainers());
        // drafts
        containerDrafts.clear();
        draftDocuments.clear();
        containerVersions.clear();
        versionDocuments.clear();
        for(final CellContainer container : containers) {
            if(container.isSetDraft()) {
                final CellDraft draft = toDisplay(container, container.getDraft());
                containerDrafts.put(container, draft);
                final List<MainCellDraftDocument> mcDocuments = toDisplay(draft, draft.getDocuments());
                draftDocuments.put(draft, mcDocuments);
            }
            // versions
            containerVersions.clear();
            final List<MainCellContainerVersion> versions = readVersions(container);
            containerVersions.put(container, versions);
            versionDocuments.clear();
            for(final MainCellContainerVersion version : versions) {
                versionDocuments.put(version, readVersionDocuments(version));
            }
        }
        syncModel();
    }

    /**
     * Read a container from the provider.
     * 
     * @param containerId
     *            The container id.
     * @return The container.
     */
    private CellContainer readContainer(final Long containerId) {
        return (CellContainer) contentProvider.getElement(0, containerId);
    }

    /**
     * Read the containers (packages) from the provider.
     * 
     * @return The containers.
     */
    private List<CellContainer> readContainers() {
        final List<CellContainer> l = new LinkedList<CellContainer>();
        final CellContainer[] a = (CellContainer[]) contentProvider.getElements(0, null);
        for(final CellContainer c : a) { l.add(c); }
        return l;
    }

    /**
     * Read the container versions from the provider.
     * 
     * @param container
     *            The container.
     * @return The container versions.
     */
    private List<MainCellContainerVersion> readVersions(
            final CellContainer container) {
        final List<MainCellContainerVersion> l = new LinkedList<MainCellContainerVersion>();
        final MainCellContainerVersion[] a = (MainCellContainerVersion[]) contentProvider.getElements(1, container);
        for(final MainCellContainerVersion c : a) { l.add(c); }
        return l;
    }

    /**
     * Read the version documents from the provider.
     * 
     * @param version
     *            The version.
     * @return A list of display documents.
     */
    private List<MainCellVersionDocument> readVersionDocuments(
            final MainCellContainerVersion version) {
        final List<MainCellVersionDocument> l = new LinkedList<MainCellVersionDocument>();
        final MainCellVersionDocument[] a = (MainCellVersionDocument[]) contentProvider.getElements(2, version);
        for(final MainCellVersionDocument c : a) { l.add(c); }
        return l;
    }
    
    /**
     * Synchronize the container and document with the list. This method
     * is called when there is a change to documents in a container.
     * 
     * @param containerId
     *            The container id.
     * @parem documentId
     *            The document id (or null if it is a container change only)
     * @param remote
     *            Whether or not the reload is the result of a remote event or
     *            not.
     * 
     * @see #syncDocument(Long, Long, Boolean)
     * @see #syncModel()
     */
    private void syncDocumentInternal(final Long containerId, final Long documentId,
            final Boolean remote) {
        final CellContainer container = readContainer(containerId);
        if (null!=container) {
            // Get the draft
            if(container.isSetDraft()) {
                final CellDraft draft = toDisplay(container, container.getDraft());
                containerDrafts.put(container, draft);
                final List<MainCellDraftDocument> mcDocuments = toDisplay(draft, draft.getDocuments());
                draftDocuments.put(draft, mcDocuments);
            }
            // Get the versions
            containerVersions.clear();
            final List<MainCellContainerVersion> versions = readVersions(container);
            containerVersions.put(container, versions);
            versionDocuments.clear();
            for(final MainCellContainerVersion version : versions) {
                versionDocuments.put(version, readVersionDocuments(version));
            }
        }
    }

    /**
     * Synchronize the container with the list. The content provider is queried
     * for the container and if it can be obtained; it will either be added to or
     * updated in the list as well as its history updated. If it cannot be found;
     * it will be removed from the list.
     * 
     * @param containerId
     *            The container id.
     * @parem documentId
     *            The document id (or null if it is a container change only)
     * @param remote
     *            Whether or not the reload is the result of a remote event or
     *            not.
     * 
     * @see #syncContainer(Long, Boolean)
     * @see #syncModel()
     */
    private void syncContainerInternal(final Long containerId,
            final Boolean remote) {
        final CellContainer container = readContainer(containerId);

        // if the container is null; we can assume the container has been
        // deleted (it's not longer being created by the provider); so we find
        // the container and remove it
        if(null == container) {
            for(int i = 0; i < containers.size(); i++) {
                if(containers.get(i).getId().equals(containerId)) {
                    containers.remove(i);
                    break;
                }
            }
        }
        // the container is not null; therefore it is either new; or updated
        else {
            // the container is new
            if(!containers.contains(container)) {
                containers.add(0, container);
                
                // Get the draft
                if(container.isSetDraft()) {
                    final CellDraft draft = toDisplay(container, container.getDraft());
                    containerDrafts.put(container, draft);
                    final List<MainCellDraftDocument> mcDocuments = toDisplay(draft, draft.getDocuments());
                    draftDocuments.put(draft, mcDocuments);
                }
                // Get the versions
                containerVersions.clear();
                final List<MainCellContainerVersion> versions = readVersions(container);
                containerVersions.put(container, versions);
                versionDocuments.clear();
                for(final MainCellContainerVersion version : versions) {
                    versionDocuments.put(version, readVersionDocuments(version));
                }
            }
            // The container has been updated
            else {
                final int index = containers.indexOf(container);

                // if the reload is the result of a remote event add the container
                // at the top of the list; otherwise add it in the same location
                // it previously existed
                containers.remove(index);
                if(remote) { containers.add(0, container); }
                else { containers.add(index, container); }
            }
        }
    }

    /**
     * Filter the list of containers. Update the visible cell list with containers
     * as well as the documents. Update the model with the visible cell list.
     * 
     */
    private void syncModel() {
        debug();
        // filter containers
        final List<CellContainer> filteredContainers = cloneContainers();
        // update all visible cells
        visibleCells.clear();
        for (final CellContainer cc : filteredContainers) {
            visibleCells.add(cc);
            if (cc.isExpanded()) {
                // if a draft exists display it
                final CellDraft containerDraft = containerDrafts.get(cc);
                if (null != containerDraft) {
                    visibleCells.add(containerDraft);
                    if (containerDraft.isExpanded()) {
                        // add draft documents
                        final List<MainCellDraftDocument> draftDocuments = this.draftDocuments.get(containerDraft);
                        if (null != draftDocuments) {
                            visibleCells.addAll(draftDocuments);
                        }                        
                    }
                }
                // if versions exist display them
                final List<MainCellContainerVersion> containerVersions = this.containerVersions.get(cc);
                if (null != containerVersions) {
                    for (final MainCellContainerVersion cv : containerVersions) {
                        visibleCells.add(cv);
                        if (cv.isExpanded()) {
                            // add version documents
                            final List<MainCellVersionDocument> versionDocuments = this.versionDocuments.get(cv);
                            if (null != versionDocuments) {
                                visibleCells.addAll(versionDocuments);
                            }
                        }
                    }
                }
            }
        }

        // add visible cells not in the model; as well as update cell
        // locations
        for(final MainCell mc : visibleCells) {
            if(!jListModel.contains(mc)) {
                jListModel.add(visibleCells.indexOf(mc), mc);
            }
            else {
                // Always replace the element in the jList Model. The value of the
                // expanded flag may have changed in syncContainerInternal().
                jListModel.removeElement(mc);
                jListModel.add(visibleCells.indexOf(mc), mc);
            }
        }

        // prune cells
        final MainCell[] mcModel = new MainCell[jListModel.size()];
        jListModel.copyInto(mcModel);
        for(final MainCell mc : mcModel) {
            if(!visibleCells.contains(mc)) { jListModel.removeElement(mc); }
        }
        debug();
    }
    
    /**
     * Create a draft display cell.
     * 
     * @param mcContainer
     *            A container display cell.
     * @param containerDraft
     *            A draft.
     * @return A draft display cell.
     */
    private CellDraft toDisplay(final CellContainer mcContainer,
            final ContainerDraft containerDraft) {
        return new CellDraft(mcContainer, containerDraft);
    }

    /**
     * Create a list of draft document display cells.
     * 
     * @param mcDraft
     *            A draft display cell.
     * @param documents
     *            A list of documents.
     * @return A list of draft document display cells.
     */
    private List<MainCellDraftDocument> toDisplay(final CellDraft mcDraft,
            final List<Document> documents) {
        final List<MainCellDraftDocument> mcDocuments = new ArrayList<MainCellDraftDocument>(documents.size());
        for(final Document document : documents) {
            mcDocuments.add(new MainCellDraftDocument(mcDraft, document));
        }
        return mcDocuments;
    }
}
