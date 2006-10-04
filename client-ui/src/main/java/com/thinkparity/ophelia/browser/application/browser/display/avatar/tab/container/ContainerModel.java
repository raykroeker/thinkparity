/**
 * Created On: 13-Jul-06 1:03:06 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.container;

import java.awt.Component;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel;
import com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.container.ContainerProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionDocumentCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.ContainerVersionSentToUserCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.DraftCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.container.DraftDocumentCell;
import com.thinkparity.ophelia.model.container.ContainerDraft;

/**
 * @author rob_masako@shaw.ca; raykroeker@gmail.com
 * @version 1.1.2.4
 */
public class ContainerModel extends TabModel {

    /** The application. */
    public final Browser browser;

    /** An apache logger. */
    protected final Logger logger;

    /** A list of all containers. */
    private final List<ContainerCell> containerCells;

    /** A map of document ids to container ids. */
    private final Map<Long, Long> containerIdLookup;

    /** A map of the container cells to their draft. */
    private final Map<ContainerCell, DraftCell> draftCells;

    /** A map of the container draft to the draft documents. */
    private final Map<DraftCell, List<DraftDocumentCell>> draftDocumentCells;
    
    /** A map of the container cells to a list of their respective versions. */
    private final Map<ContainerCell, List<ContainerVersionCell>> versionCells;  
    
    /** A map of the container versions to the draft documents. */
    private final Map<ContainerVersionCell, List<ContainerVersionDocumentCell>> versionDocumentCells;
    
    /** A map of the container versions to the team members. */
    private final Map<ContainerVersionCell, List<TabCell>> versionSentToUserCells;
    
    /** A list of all visible cells. */
    private final List<TabCell> visibleCells;    
    
    /** The swing list model. */
    private final DefaultListModel listModel;

    /**
     * The user input search expression.
     * 
     * @see #applySearch(String)
     */
    private String searchExpression;

    /**
     * A list of contact ids matching the search criteria.
     * 
     * @see #applySearch(List)
     * @see #removeSearch()
     */
    private List<Long> searchResults;

    /**
     * Create BrowserContainersModel.
     * 
     */
    ContainerModel() {
        super();
        this.browser = getBrowser();
        this.containerCells = new LinkedList<ContainerCell>();
        this.containerIdLookup = new HashMap<Long, Long>(50, 0.75F);
        this.draftCells = new HashMap<ContainerCell, DraftCell>(50, 0.75F);
        this.draftDocumentCells = new HashMap<DraftCell, List<DraftDocumentCell>>(50, 0.75F);
        this.versionCells = new HashMap<ContainerCell, List<ContainerVersionCell>>(50, 0.75F);
        this.versionDocumentCells = new HashMap<ContainerVersionCell, List<ContainerVersionDocumentCell>>(50, 0.75F);
        this.versionSentToUserCells = new HashMap<ContainerVersionCell, List<TabCell>>(50, 0.75F);        
        this.listModel = new DefaultListModel();
        this.logger = browser.getPlatform().getLogger(getClass());
        this.visibleCells = new LinkedList<TabCell>();
    }

    /**
     * Apply the user's search to the container list.
     * 
     * @param searchExpression
     *            A search expression <code>String</code>.
     * 
     * @see #searchExpression
     * @see #searchResults
     * @see #removeSearch()
     */
    @Override
    public void applySearch(final String searchExpression) {
        if (searchExpression.equals(this.searchExpression)) {
            return;
        } else {
            this.searchExpression = searchExpression;
            this.searchResults = readSearchResults();
            synchronize();
        }
    }

    /**
     * Debug the container avatar.
     *
     */
    @Override
    public void debug() {
        if(browser.getPlatform().isDevelopmentMode()) {
            logger.debug(getDebugId("[{0} VISIBLE CELLS]", visibleCells.size()));
            logger.debug(getDebugId("[{0} MODEL ELEMENTS]", listModel.size()));
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
        if (null!=draftDocuments) {
            for(final DraftDocumentCell draftDocument : draftDocuments) {
                if(draftDocument.getName().equals(name)) {
                    return draftDocument.getId();
                }
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
        if (null!=draftDocuments) {
            final List<String> l = new ArrayList<String>(draftDocuments.size());
            for (final DraftDocumentCell draftDocument : draftDocuments) {
                l.add(draftDocument.getName());
            }
            return l;
        }
        else {
            // return empty list
            final List<String> l = new ArrayList<String>(1);
            return l;
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#getListModel()
     */
    @Override
    public DefaultListModel getListModel() {
        return listModel;
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
     * Remove the search.
     * 
     * @see #searchExpression
     * @see #searchResults
     * @see #applySearch(String)
     */
    @Override
    public void removeSearch() {
        // if the member search expression is already null; then there is no
        // search applied -> do nothing
        if (null == searchExpression) {
            return;
        } else {
            searchExpression = null;
            searchResults = null;
            synchronize();
        }
    }
    
    /**
     * Create the final list of container cells; container draft cells; draft
     * document cells; container version cells and container version document
     * cells. The search filter is also applied here.
     * 
     */
    @Override
    public void synchronize() {
        debug();

        // search filtered containers
        final List<ContainerCell> filteredContainers = cloneContainers();
        if (null != searchExpression && null != searchResults) {
            FilterManager.filter(filteredContainers, new SearchFilter(searchResults));
        }
        
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
                            // Add version documents
                            final List<ContainerVersionDocumentCell> versionDocuments = this.versionDocumentCells.get(cv);
                            if (null != versionDocuments) {
                                visibleCells.addAll(versionDocuments);
                            }
                            // Add sent-to users
                            final List<TabCell> users = this.versionSentToUserCells.get(cv);
                            if (null != users) {
                                visibleCells.addAll(users);
                            }
                        }
                    }
                }
            }
        }

        // add visible cells not in the model; as well as update cell
        // locations
        for(final TabCell mc : visibleCells) {
            if(!listModel.contains(mc)) {
                listModel.add(visibleCells.indexOf(mc), mc);
            }
            else {
                // Always replace the element in the jList Model. The value of the
                // expanded flag may have changed in syncContainerInternal().
                listModel.removeElement(mc);
                listModel.add(visibleCells.indexOf(mc), mc);
            }
        }

        // prune cells
        final TabCell[] mcModel = new TabCell[listModel.size()];
        listModel.copyInto(mcModel);
        for(final TabCell mc : mcModel) {
            if(!visibleCells.contains(mc)) { listModel.removeElement(mc); }
        }
        debug();
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
    @Override
    protected void initialize() {
        // read the containers from the provider into the list.
        containerCells.clear();
        containerCells.addAll(readContainers());
        sortContainers();
        
        // Clear all the cells under the containers
        draftCells.clear();
        draftDocumentCells.clear();
        versionCells.clear();
        versionDocumentCells.clear();
        versionSentToUserCells.clear();

        // Add all the cells under each container
        for(final ContainerCell container : containerCells) {
            initializeDraft(container);
            initializeVersion(container);
        }
        
        synchronize();
    }
    
    /**
     * Trigger a double click event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    @Override
    protected void triggerDoubleClick(final TabCell tabCell) {
        debug();
       
        if ((tabCell instanceof ContainerCell) ||
            (tabCell instanceof DraftCell) ||
            (tabCell instanceof ContainerVersionCell)) {
            // Do nothing (every click does expand or collapse)
        } else {
            tabCell.triggerDoubleClickAction(browser);
        }
    }
 
    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#triggerExpand(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell)
     */
    @Override
    protected void triggerExpand(final TabCell tabCell) {
        super.triggerExpand(tabCell);
        if (tabCell.isExpanded() && tabCell instanceof ContainerCell) {
            // flag the container as having been seen
            browser.runApplyContainerFlagSeen(((ContainerCell) tabCell).getId());
        }
    }

    /**
     * Trigger a popup event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    protected void triggerPopup(final TabCell tabCell, final Component invoker,
            final java.awt.event.MouseEvent e) {
        tabCell.triggerPopup(browser.getConnection(), invoker, e);
    }

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
        synchronize();
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
        synchronize();
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
     * Initialize the draft for one container.
     * 
     * @param container
     *          The container.
     */
    private void initializeDraft(ContainerCell container) {
        if (container.isDraft() && container.isLocalDraft()) {
            final DraftCell draft = readDraft(container);
            draftCells.put(container, draft);
            final List<DraftDocumentCell> draftDocuments = toDisplay(draft, draft.getDocuments());
            draftDocumentCells.put(draft, draftDocuments);
            for(final DraftDocumentCell draftDocument : draftDocuments) {
                containerIdLookup.put(draftDocument.getId(), container.getId());
            }
        }
    }

    /**
     * Initialize the versions for one container.
     * 
     * @param container
     *          The container.
     */
    private void initializeVersion(ContainerCell container) {
        final List<ContainerVersionCell> versions = readVersions(container);
        versionCells.put(container, versions);
        for(final ContainerVersionCell version : versions) {                          
            final List<ContainerVersionDocumentCell> versionDocuments = readVersionDocuments(version);
            versionDocumentCells.put(version, versionDocuments);  
            for(final ContainerVersionDocumentCell versionDocument : versionDocuments) {
                containerIdLookup.put(versionDocument.getId(), container.getId());
            }
            
            final List<TabCell> users = readUsers(version, version.getArtifactId(), version.getVersionId());
            versionSentToUserCells.put(version, users);
        }
    }

    /**
     * Read a container from the provider.
     * 
     * @param containerId
     *            The container id.
     * @return The container.
     */
    private ContainerCell readContainer(final Long containerId) {
        return (ContainerCell) ((CompositeFlatSingleContentProvider) contentProvider).getElement(0, containerId);
    }

    /**
     * Read the containers (packages) from the provider.
     * 
     * @return The containers.
     */
    private List<ContainerCell> readContainers() {
        final List<ContainerCell> l = new LinkedList<ContainerCell>();
        final ContainerCell[] a = (ContainerCell[]) ((CompositeFlatSingleContentProvider) contentProvider).getElements(0, null);
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
        final ContainerDraft draft = (ContainerDraft) ((CompositeFlatSingleContentProvider) contentProvider).getElement(1, container.getId());
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
        return (Boolean) ((CompositeFlatSingleContentProvider) contentProvider).getElement(2, documentId);
    }

    /**
     * Search for a list of container ids through the content provider.
     * 
     * @return A list of container ids.
     */
    private List<Long> readSearchResults() {
        final List<Long> list = new LinkedList<Long>();
        final Long[] array = (Long[]) ((CompositeFlatSingleContentProvider) contentProvider).getElements(4, searchExpression);
        for (final Long containerId : array) {
            list.add(containerId);
        }
        return list;
    }

    /**
     * Read the published to users.
     * 
     * @param parent
     *            A parent <code>TabCell</code>.
     * @param containerId
     *            A container id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return A list of cells.
     */
    private List<TabCell> readUsers(final TabCell parent,
            final Long containerId, final Long versionId) {
        final List<TabCell> display = new ArrayList<TabCell>();
        final Map<User, ArtifactReceipt> publishedTo = ((ContainerProvider) contentProvider).readPublishedTo(containerId, versionId);
        final Map<User, ArtifactReceipt> sharedWith = ((ContainerProvider) contentProvider).readSharedWith(containerId, versionId);
        for (final Entry<User, ArtifactReceipt> entry : publishedTo.entrySet()) {
            display.add(toDisplay(parent, entry.getKey(), entry.getValue()));
        }
        TabCell displayCell;
        for (final Entry<User, ArtifactReceipt> entry : sharedWith.entrySet()) {
            displayCell = toDisplay(parent, entry.getKey(), entry.getValue());
            if (!display.contains(displayCell)) {
                display.add(displayCell);
            }
        }
        return display;
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
        final ContainerVersionDocumentCell[] a = (ContainerVersionDocumentCell[]) ((CompositeFlatSingleContentProvider) contentProvider)
                .getElements(2, version);
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
        final ContainerProvider provider = (ContainerProvider) contentProvider;
        final List<ContainerVersionCell> display = new ArrayList<ContainerVersionCell>();
        final List<ContainerVersion> versions = provider.readVersions(container.getId());
        for(final ContainerVersion version : versions) {
            display.add(toDisplay(container, version,
                    provider.readUser(version.getUpdatedBy())));
        }
        return display;
    }

    /**
     * Sort containers, and also set the "firstInGroup" flag for CellContainers.
     */
    private void sortContainers() {
        Collections.sort(containerCells, new ContainerCellComparator());
        
        ContainerCell prevContainer = null;
        for(final ContainerCell container : containerCells) {
            container.setFirstInGroup(Boolean.FALSE);
            if (prevContainer!=null) {
                if (prevContainer.isLocalDraft()!=container.isLocalDraft()) {
                    container.setFirstInGroup(Boolean.TRUE);
                    break;
                }
            }
            prevContainer = container;
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
     *            Whether or not the reload is the result of a remote event.
     * 
     * @see #syncContainer(Long, Boolean)
     * @see #synchronize()
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
        // The container is not null; therefore it is either new; or updated.
        else {
            // the container is new
            if(!containerCells.contains(container)) {
                containerCells.add(0, container);
            }
            // The container has been updated
            else {
                final int index = containerCells.indexOf(container);
                
                // Preserve expanded state.
                final Boolean isExpanded = containerCells.get(index).isExpanded();
                container.setExpanded(isExpanded);

                // if the reload is the result of a remote event add the container
                // at the top of the list; otherwise add it in the same location
                // it previously existed
                containerCells.remove(index);
                if(remote) { containerCells.add(0, container); }
                else { containerCells.add(index, container); }
            }
            
            // Synchronize the draft and versions
            syncDraftInternal(containerId, remote);
            syncVersionInternal(containerId, remote);
            
            // Sort the container list
            sortContainers();
        }
    }

    /**
     * Synchronize the container and document with the list. This method
     * is called when there is a change to documents in a container.
     * 
     * @parem documentId
     *            The document id.
     * @param remote
     *            Whether or not the reload is the result of a remote event.
     * 
     * @see #syncDocument(Long, Boolean)
     * @see #synchronize()
     */
    private void syncDocumentInternal(final Long containerId,
            final Long documentId, final Boolean remote) {
        // If there is a document change then only the draft cells needs updating.
        syncDraftInternal(containerId, remote);
    }

    /**
     * Synchronize the draft with the list for one container.
     * 
     * @param containerId
     *            The container id.
     * @param remote
     *            Whether or not the reload is the result of a remote event.
     */
    private void syncDraftInternal(final Long containerId, final Boolean remote) {
        final ContainerCell container = readContainer(containerId);
        
        // Remove the draft cell and associated document cells
        final DraftCell containerDraft = draftCells.get(container);
        if (null!=containerDraft) {
            draftDocumentCells.remove(containerDraft);
            draftCells.remove(container);
        }
        
        // Add the draft cell and associated document cells
        initializeDraft(container);
    }
    
    /**
     * Synchronize the versions with the list for one container.
     * 
     * @param containerId
     *            The container id.
     * @param remote
     *            Whether or not the reload is the result of a remote event.
     */
    private void syncVersionInternal(final Long containerId, final Boolean remote) {
        final ContainerCell container = readContainer(containerId);
        
        // Remove the version cells and associated child cells
        final List<ContainerVersionCell> containerVersions = versionCells.get(container);
        if (null!=containerVersions) {
            for(final ContainerVersionCell containerVersion : containerVersions) {
                versionDocumentCells.remove(containerVersion);
                versionSentToUserCells.remove(containerVersion);
            }
            versionCells.remove(container);
        }
        
        // Add the version cells and associated child cells
        initializeVersion(container);
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

    /**
     * Create a container version display cell.
     * 
     * @param parent
     *            The parent cell.
     * @param version
     *            The version.
     * @param publishedBy
     *            The published by user.
     * @return The display cell.
     */
    private ContainerVersionCell toDisplay(final TabCell parent,
            final ContainerVersion version, final User publishedBy) {
        final ContainerVersionCell display = new ContainerVersionCell();
        display.setParent(parent);
        display.setPublishedBy(publishedBy);
        display.setVersion(version);
        return display;
    }

    /**
     * Create a display cell from a parent cell; a user; and an artifact
     * receipt.
     * 
     * @param parent
     *            A parent <code>TabCell</code>.
     * @param user
     *            A <code>User</code>.
     * @param receipt
     *            A <code>ArtifactReceipt</code>.
     * @return A <code>TabCell</code>.
     */
    private TabCell toDisplay(final TabCell parent, final User user,
            final ArtifactReceipt receipt) {
        final ContainerVersionSentToUserCell display = new ContainerVersionSentToUserCell();
        display.setParent(parent);
        display.setReceipt(receipt);
        display.setUser(user);
        return display;
    }

    /**
     * <b>Title:</b>thinkParity Container Search Filter<br>
     * <b>Description:</b>Provides the capability to filter the container cells
     * that do not match the search results.
     */
    private class SearchFilter implements Filter<ContainerCell> {

        /** The search results. */
        private final List<Long> searchResults;

        /**
         * Create SearchFilter.
         * 
         * @param searchResults
         *            A <code>List&lt;Long&gt;</code>.
         */
        private SearchFilter(final List<Long> searchResults) {
            this.searchResults = searchResults;
        }

        /**
         * @see com.thinkparity.ophelia.model.util.filter.Filter#doFilter(java.lang.Object)
         */
        public Boolean doFilter(final ContainerCell o) {
            for (final Long searchResult : searchResults) {
                if (searchResult.equals(o.getId()))
                    return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
    }
}
