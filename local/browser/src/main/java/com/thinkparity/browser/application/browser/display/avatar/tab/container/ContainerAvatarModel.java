/**
 * Created On: 13-Jul-06 1:03:06 PM
 */
package com.thinkparity.browser.application.browser.display.avatar.tab.container;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.container.ContainerCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.container.ContainerVersionCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.container.ContainerVersionDocumentCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.container.DraftCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.container.DraftDocumentCell;

import com.thinkparity.model.parity.model.container.ContainerDraft;
import com.thinkparity.model.parity.model.document.Document;

/**
 * @author rob_masako@shaw.ca; raykroeker@gmail.com
 * @version 1.1.2.4
 */
public class ContainerAvatarModel {

    /** An apache logger. */
    protected final Logger logger;

    /** The application. */
    private final Browser browser;

    /** A list of all containers. */
    private final List<ContainerCell> containerCells;

    /** A map of document ids to container ids. */
    private final Map<Long, Long> containerIdLookup;

    /** The content provider. */
    private CompositeFlatSingleContentProvider contentProvider;

    /** A map of the container cells to their draft. */
    private final Map<ContainerCell, DraftCell> draftCells;

    /** A map of the container draft to the draft documents. */
    private final Map<DraftCell, List<DraftDocumentCell>> draftDocumentCells;

    /** The swing list model. */
    private final DefaultListModel jListModel;

    /** A map of the container cells to a list of their respective versions. */
    private final Map<ContainerCell, List<ContainerVersionCell>> versionCells;

    /** A map of the container version to the draft documents. */
    private final Map<ContainerVersionCell, List<ContainerVersionDocumentCell>> versionDocumentCells;
    

    /** A list of all visible cells. */
    private final List<TabCell> visibleCells;

    /**
     * Create BrowserContainersModel.
     * 
     */
    ContainerAvatarModel(final Browser browser) {
        super();
        this.browser = browser;
        this.containerCells = new LinkedList<ContainerCell>();
        this.containerIdLookup = new HashMap<Long, Long>(50, 0.75F);
        this.draftCells = new HashMap<ContainerCell, DraftCell>(50, 0.75F);
        this.draftDocumentCells = new HashMap<DraftCell, List<DraftDocumentCell>>(50, 0.75F);
        this.versionCells = new HashMap<ContainerCell, List<ContainerVersionCell>>(50, 0.75F);
        this.jListModel = new DefaultListModel();
        this.logger = browser.getPlatform().getLogger(getClass());
        this.versionDocumentCells = new HashMap<ContainerVersionCell, List<ContainerVersionDocumentCell>>(50, 0.75F);
        this.visibleCells = new LinkedList<TabCell>();
    }
    
    /**
     * Get the container cell, given the container id.
     * 
     * @param containerId
     *            The container id.
     */
    public ContainerCell getContainerCell(final Long containerId) {
        ContainerCell cc = null;
        for(final ContainerCell mcContainer : containerCells) {
            if (mcContainer.getId().equals(containerId)) {
                cc = mcContainer;
                break;
            }           
        }
        
        return cc;
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
    public Long getDocumentId(final ContainerCell cellContainer, final String name) {
        final List<DraftDocumentCell> draftDocuments = this.draftDocumentCells.get(cellContainer);
        for(final DraftDocumentCell draftDocument : draftDocuments) {
            if(draftDocument.getName().equals(name)) {
                return draftDocument.getId();
            }
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
    public List<String> getDocumentNames(final ContainerCell cellContainer) {
        final List<DraftDocumentCell> draftDocuments = this.draftDocumentCells.get(cellContainer);
        final List<String> l = new ArrayList<String>(draftDocuments.size());
        for (final DraftDocumentCell draftDocument : draftDocuments) {
            l.add(draftDocument.getName());
        }
        return l;
    }

    /**
     * Determine whether or not the draft for the document has been
     * modified.
     * 
     * @param documentId
     *            A document id.
     * @return True if the draft has been modified; false otherwise.
     */
    public Boolean isDraftModified(final Long documentId) {
        return readIsDraftModified(documentId);
    }

    /**
     * Determine whether or not the cell is expanded.
     * 
     * @param mainCell
     *            The cell.
     * @return True if the cell is expanded; false otherwise.
     */
    public Boolean isExpanded(final TabCell mainCell) {
        if (mainCell instanceof ContainerCell) {
            return ((ContainerCell) mainCell).isExpanded();
        }
        else if (mainCell instanceof DraftCell) {
            return ((DraftCell) mainCell).isExpanded();
        }
        else if (mainCell instanceof ContainerVersionCell) {
            return ((ContainerVersionCell) mainCell).isExpanded();
        }
        else {
            return Boolean.FALSE;
        }
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
            logger.debug(getDebugId("[{0} CONTAINERS]", containerCells.size()));
            for(final ContainerCell mcContainer : containerCells) {
                logger.debug(getDebugId("  [{0}]", mcContainer.getName()));
                // drafts
                final DraftCell draft = draftCells.get(mcContainer);
                logger.debug(getDebugId("    [{0}]", null == draft ? "NO DRAFT INCLUDED" : "DRAFT INCLUDED"));
                if(null != draft) {
                    final List<DraftDocumentCell> draftDocuments = this.draftDocumentCells.get(draft);
                    logger.debug(getDebugId("      [{0} DRAFT DOCUMENTS]", draftDocuments.size()));
                }
                // versions
                final List<ContainerVersionCell> containerVersions = this.versionCells.get(mcContainer);
                logger.debug(getDebugId("    [{0} VERSIONS]", null == containerVersions ? 0 : containerVersions.size()));
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
    Boolean isContainerVisible(final ContainerCell cellContainer) {
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
     * Synchronized a document in the model.
     * 
     * @param documentId
     *            A document id.
     * @param remote
     *            A remote event indicator.
     */
    void syncDocument(final Long documentId, final Boolean remote) {
        final Long containerId = containerIdLookup.get(documentId);
        syncDocument(containerId, documentId, remote);
    }

    /**
     * Synchronize the document in the container list.
     * Called, for example, if a new document is created in the container (ie. draft).
     * This will move the container to the top, and also expand the container & draft.
     * 
     * @param documentId
     *            The document id.           
     * @param remote
     *            Indicates whether the sync is the result of a remote event
     */
    void syncDocument(final Long containerId, final Long documentId,
            final Boolean remote) {
        syncDocumentInternal(containerId, documentId, remote);
        syncModel();
    }

    /**
     * Trigger a double click event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    void triggerDoubleClick(final TabCell mainCell) {
        debug();
        if (mainCell instanceof ContainerCell) {
            triggerExpand(mainCell);
        }
        else if (mainCell instanceof DraftCell) {
            triggerExpand(mainCell);
        }
        else if (mainCell instanceof ContainerVersionCell) {
            triggerExpand(mainCell);
        }
        else if (mainCell instanceof DraftDocumentCell) {
            final DraftDocumentCell draftDocument = (DraftDocumentCell) mainCell;
            browser.runOpenDocument(draftDocument.getId());
        }
        else if (mainCell instanceof ContainerVersionDocumentCell) {
        }
    }

    /**
     * Trigger the expansion of the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    void triggerExpand(final TabCell mainCell) {
        if (mainCell instanceof ContainerCell) {
            final ContainerCell cc = (ContainerCell) mainCell;
            if(isExpanded(cc)) {
                cc.setExpanded(Boolean.FALSE);
            }
            else {
                cc.setExpanded(Boolean.TRUE);
            }
            syncModel();
        }
        else if (mainCell instanceof DraftCell) {
            final DraftCell draft = (DraftCell) mainCell;
            if(isExpanded(draft)) {
                draft.setExpanded(Boolean.FALSE);
            }
            else {
                draft.setExpanded(Boolean.TRUE);
            }
            syncModel();            
        }
        else if (mainCell instanceof ContainerVersionCell) {
            final ContainerVersionCell cv = (ContainerVersionCell) mainCell;
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
    void triggerPopup(final TabCell mainCell, final Component invoker,
            final MouseEvent e, final int x, final int y) {
        mainCell.triggerPopup(browser.getConnection(), invoker, e, x, y);
    }

    /**
     * Perform a shallow clone of the containers list.
     * 
     * @return A copy of the containers list.
     */
    private List<ContainerCell> cloneContainers() {
        final List<ContainerCell> clone = new LinkedList<ContainerCell>();
        clone.addAll(containerCells);
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
        containerCells.clear();
        containerCells.addAll(readContainers());
        // drafts
        draftCells.clear();
        versionCells.clear();
        draftDocumentCells.clear();
        versionDocumentCells.clear();
        for(final ContainerCell container : containerCells) {
            if (container.isDraft() && container.isLocalDraft()) {
                final DraftCell draft = readDraft(container);
                draftCells.put(container, draft);
                final List<DraftDocumentCell> draftDocuments = toDisplay(draft, draft.getDocuments());
                for(final DraftDocumentCell draftDocument : draftDocuments) {
                    containerIdLookup.put(draftDocument.getId(), container.getId());
                }
                draftDocumentCells.put(draft, draftDocuments);
            }
            final List<ContainerVersionCell> versions = readVersions(container);
            versionCells.put(container, versions);
            for(final ContainerVersionCell version : versions) {
                final List<ContainerVersionDocumentCell> versionDocuments = readVersionDocuments(version);
                for(final ContainerVersionDocumentCell versionDocument : versionDocuments) {
                    containerIdLookup.put(versionDocument.getId(), container.getId());
                }
                versionDocumentCells.put(version, versionDocuments);
            }
        }
        syncModel();
    }

    /* NOCOMMIT */
    void reload() {
        initModel();
    }

    /**
     * Read a container from the provider.
     * 
     * @param containerId
     *            The container id.
     * @return The container.
     */
    private ContainerCell readContainer(final Long containerId) {
        return (ContainerCell) contentProvider.getElement(0, containerId);
    }

    /**
     * Read the containers (packages) from the provider.
     * 
     * @return The containers.
     */
    private List<ContainerCell> readContainers() {
        final List<ContainerCell> l = new LinkedList<ContainerCell>();
        final ContainerCell[] a = (ContainerCell[]) contentProvider.getElements(0, null);
        for(final ContainerCell c : a) { l.add(c); }
        return l;
    }

    /**
     * Read a draft for a container.
     * 
     * @param container
     *            A container.
     * @return A display cell for a draft.
     */
    private DraftCell readDraft(final ContainerCell container) {
        final ContainerDraft draft = (ContainerDraft) contentProvider.getElement(1, container.getId());
        return toDisplay(container, draft);
    }

    /**
     * Read from the provider whether or not the draft for the document has been
     * modified.
     * 
     * @param documentId
     *            A document id.
     * @return True if the draft has been modified; false otherwise.
     */
    private Boolean readIsDraftModified(final Long documentId) {
        return (Boolean) contentProvider.getElement(2, documentId);
    }

    /**
     * Read the version documents from the provider.
     * 
     * @param version
     *            The version.
     * @return A list of display documents.
     */
    private List<ContainerVersionDocumentCell> readVersionDocuments(
            final ContainerVersionCell version) {
        final List<ContainerVersionDocumentCell> l = new LinkedList<ContainerVersionDocumentCell>();
        final ContainerVersionDocumentCell[] a = (ContainerVersionDocumentCell[]) contentProvider.getElements(2, version);
        for(final ContainerVersionDocumentCell c : a) { l.add(c); }
        return l;
    }

    /**
     * Read the container versions from the provider.
     * 
     * @param container
     *            The container.
     * @return The container versions.
     */
    private List<ContainerVersionCell> readVersions(
            final ContainerCell container) {
        final List<ContainerVersionCell> l = new LinkedList<ContainerVersionCell>();
        final ContainerVersionCell[] a = (ContainerVersionCell[]) contentProvider.getElements(1, container);
        for(final ContainerVersionCell c : a) { l.add(c); }
        return l;
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
        final ContainerCell container = readContainer(containerId);

        // if the container is null; we can assume the container has been
        // deleted (it's not longer being created by the provider); so we find
        // the container and remove it
        if(null == container) {
            for(int i = 0; i < containerCells.size(); i++) {
                if(containerCells.get(i).getId().equals(containerId)) {
                    containerCells.remove(i);
                    break;
                }
            }
        }
        // the container is not null; therefore it is either new; or updated
        else {
            // the container is new
            if(!containerCells.contains(container)) {
                containerCells.add(0, container);
                
                // Get the draft
                if(container.isDraft() && container.isLocalDraft()) {
                    final DraftCell draft = readDraft(container);
                    draftCells.put(container, draft);
                    final List<DraftDocumentCell> draftDocuments = toDisplay(draft, draft.getDocuments());
                    for(final DraftDocumentCell draftDocument : draftDocuments) {
                        containerIdLookup.put(draftDocument.getId(), container.getId());
                    }
                    draftDocumentCells.put(draft, draftDocuments);
                }
                // Get the versions
                final List<ContainerVersionCell> versions = readVersions(container);
                versionCells.put(container, versions);
                for(final ContainerVersionCell version : versions) {
                    final List<ContainerVersionDocumentCell> versionDocuments = readVersionDocuments(version);
                    for(final ContainerVersionDocumentCell versionDocument : versionDocuments) {
                        containerIdLookup.put(versionDocument.getId(), container.getId());
                    }
                    versionDocumentCells.put(version, readVersionDocuments(version));
                }
            }
            // The container has been updated
            else {
                final int index = containerCells.indexOf(container);

                // if the reload is the result of a remote event add the container
                // at the top of the list; otherwise add it in the same location
                // it previously existed
                containerCells.remove(index);
                if(remote) { containerCells.add(0, container); }
                else { containerCells.add(index, container); }
            }
        }
    }

    /**
     * Synchronize the container and document with the list. This method
     * is called when there is a change to documents in a container.
     * 
     * @parem documentId
     *            The document id.
     * @param remote
     *            Whether or not the reload is the result of a remote event or
     *            not.
     * 
     * @see #syncDocument(Long, Boolean)
     * @see #syncModel()
     */
    private void syncDocumentInternal(final Long containerId,
            final Long documentId, final Boolean remote) {
        final ContainerCell container = readContainer(containerId);
        // get the draft
        if (container.isDraft() && container.isLocalDraft()) {
            final DraftCell draft = readDraft(container);
            draftCells.put(container, draft);
            final List<DraftDocumentCell> draftDocuments = toDisplay(draft, draft.getDocuments());
            for(final DraftDocumentCell draftDocument : draftDocuments) {
                containerIdLookup.put(draftDocument.getId(), container.getId());
            }
            draftDocumentCells.put(draft, draftDocuments);
        }
        // Get the versions
        versionCells.clear();
        final List<ContainerVersionCell> versions = readVersions(container);
        versionCells.put(container, versions);
        versionDocumentCells.clear();
        for(final ContainerVersionCell version : versions) {
            final List<ContainerVersionDocumentCell> versionDocuments = readVersionDocuments(version);
            for(final ContainerVersionDocumentCell versionDocument : versionDocuments) {
                containerIdLookup.put(versionDocument.getId(), container.getId());
            }
            versionDocumentCells.put(version, readVersionDocuments(version));
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
        final List<ContainerCell> filteredContainers = cloneContainers();
        // update all visible cells
        visibleCells.clear();
        for (final ContainerCell cc : filteredContainers) {
            visibleCells.add(cc);
            if (cc.isExpanded()) {
                // if a draft exists display it
                final DraftCell containerDraft = draftCells.get(cc);
                if (null != containerDraft) {
                    visibleCells.add(containerDraft);
                    if (containerDraft.isExpanded()) {
                        // add draft documents
                        final List<DraftDocumentCell> draftDocuments = this.draftDocumentCells.get(containerDraft);
                        if (null != draftDocuments) {
                            visibleCells.addAll(draftDocuments);
                        }                        
                    }
                }
                // if versions exist display them
                final List<ContainerVersionCell> containerVersions = this.versionCells.get(cc);
                if (null != containerVersions) {
                    for (final ContainerVersionCell cv : containerVersions) {
                        visibleCells.add(cv);
                        if (cv.isExpanded()) {
                            // add version documents
                            final List<ContainerVersionDocumentCell> versionDocuments = this.versionDocumentCells.get(cv);
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
        for(final TabCell mc : visibleCells) {
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
        final TabCell[] mcModel = new TabCell[jListModel.size()];
        jListModel.copyInto(mcModel);
        for(final TabCell mc : mcModel) {
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
    private DraftCell toDisplay(final ContainerCell mcContainer,
            final ContainerDraft containerDraft) {
        return new DraftCell(mcContainer, containerDraft);
    }

    /**
     * Create a list of draft document display cells.
     * 
     * @param draft
     *            A draft display cell.
     * @param documents
     *            A list of documents.
     * @return A list of draft document display cells.
     */
    private List<DraftDocumentCell> toDisplay(final DraftCell draft,
            final List<Document> documents) {
        final List<DraftDocumentCell> documentCells =
            new ArrayList<DraftDocumentCell>(documents.size());
        for (final Document document : documents) {
            documentCells.add(new DraftDocumentCell(draft, document));
        }
        return documentCells;
    }
}
