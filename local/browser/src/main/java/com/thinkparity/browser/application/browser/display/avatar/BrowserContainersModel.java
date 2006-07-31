/**
 * Created On: 13-Jul-06 1:03:06 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.LinkedHashMap;
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
import com.thinkparity.browser.application.browser.display.avatar.container.CellDocument;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCell;
import com.thinkparity.browser.application.browser.display.avatar.main.popup.PopupContainer;
import com.thinkparity.browser.application.browser.display.avatar.main.popup.PopupDocument;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserContainersModel {

    /**
     * Collection of all filters used by the document model.
     * 
     */
    //private static final Map<Enum<?>, Filter<Artifact>> DOCUMENT_FILTERS;

    /*
    static {
        DOCUMENT_FILTERS = new HashMap<Enum<?>, Filter<Artifact>>(5, 0.75F);
        DOCUMENT_FILTERS.put(DocumentFilterKey.STATE_ACTIVE, new Active());
        DOCUMENT_FILTERS.put(DocumentFilterKey.STATE_CLOSED, new Closed());
        DOCUMENT_FILTERS.put(DocumentFilterKey.KEY_HOLDER_FALSE, new IsNotKeyHolder());
        DOCUMENT_FILTERS.put(DocumentFilterKey.KEY_HOLDER_TRUE, new IsKeyHolder());
        DOCUMENT_FILTERS.put(DocumentFilterKey.SEARCH, new Search(new LinkedList<IndexHit>()));
    }
    */

    /** An apache logger. */
    protected final Logger logger;

    /** The application. */
    private final Browser browser;

    /** The content provider. */
    private CompositeFlatSingleContentProvider contentProvider;

    /** A list of "dirty" cells. */
    //private final List<MainCell> dirtyCells;

    /** The filter that is used to filter documents to produce visibleDocuments. */
    //private final FilterChain<Artifact> documentFilter;

    /** A map of documents to their history. */
    //private final Map<MainCellDocument, List<MainCellHistoryItem>> documentHistory;
    
    /** A list of all containers (packages). */
    private final List<CellContainer> containers;

    /** A map of containers to their documents. */
    private final Map<CellContainer, List<CellDocument>> containerDocuments;

    /** The team cell for the document. */
    //private final Map<CellDocument, CellTeam> documentTeam;
    
    /** The swing list model. */
    private final DefaultListModel jListModel;

    /** The list of cells that are pseudo selected. */
    //private final List<MainCell> pseudoSelection;

    /** A list of all visible cells. */
    private final List<MainCell> visibleCells;

    /**
     * Create a BrowserMainDocumentModel.
     * 
     */
    BrowserContainersModel(final Browser browser) {
        super();
        this.browser = browser;
        this.containers = new LinkedList<CellContainer>();   
        this.containerDocuments = new LinkedHashMap<CellContainer, List<CellDocument>>(10, 0.75F);
        /*
        this.documentFilter = new FilterChain<Artifact>();
        this.documentHistory = new Hashtable<MainCellDocument, List<MainCellHistoryItem>>(10, 0.65F);
        this.documentTeam = new HashMap<MainCellDocument, MainCellTeam>(10, 10.0F);
        */
        this.jListModel = new DefaultListModel();
        this.logger = browser.getPlatform().getLogger(getClass());
        /*
        this.dirtyCells = new LinkedList<MainCell>();
        this.pseudoSelection = new LinkedList<MainCell>();
        */
        this.visibleCells = new LinkedList<MainCell>();
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
        final List<String> l = new LinkedList<String>();        
        for(final CellDocument cd : containerDocuments.get(cellContainer)) {
            l.add(cd.getName());
        }
        return l;
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
    public Long getDocumentId(final CellContainer cellContainer, final String documentName) {
        Long documentId = null;
        for(final CellDocument cd : containerDocuments.get(cellContainer)) {
            if (cd.getName().equals(documentName)) {
                documentId = cd.getId();
                break;
            }
        }
        return documentId;        
    }

    /**
     * Determine whether or not the cell is expanded.
     * 
     * @param mainCell
     *            The cell.
     * @return True if the cell is expanded; false otherwise.
     */
    public Boolean isExpanded(final MainCell mainCell) {
        if(mainCell instanceof CellContainer) {
            return ((CellContainer) mainCell).isExpanded();
        }
        /*
        else if(mainCell instanceof MainCellHistoryRoot) {
            return ((MainCellHistoryRoot) mainCell).isExpanded();
        }
        else if(mainCell instanceof MainCellTeamRoot) {
            return ((MainCellTeamRoot) mainCell).isExpanded();
        }
        */
        else { return Boolean.FALSE; }
    }

    /**
     * Apply a key holder filter to the list of visible documents.
     * 
     * @param keyHolder
     *            If true; will filter documents with a key; if false; it will
     *            filter documents without the key.
     * 
     * @see #removeKeyHolderFilters()
     */
/*    void applyKeyHolderFilter(final Boolean keyHolder) {
        applyDocumentFilter(keyHolder
                ? DOCUMENT_FILTERS.get(DocumentFilterKey.KEY_HOLDER_TRUE)
                        : DOCUMENT_FILTERS.get(DocumentFilterKey.KEY_HOLDER_FALSE));
        syncModel();
    }*/

    /**
     * Apply a search filter to the list of visible documents.
     * 
     * @param searchResult
     *            The search result to filter by.
     * 
     * @see #removeSearchFilter()
     */
/*    void applySearchFilter(final List<IndexHit> searchResult) {
        final Search search = (Search) DOCUMENT_FILTERS.get(DocumentFilterKey.SEARCH);
        search.setResults(searchResult);

        applyDocumentFilter(search);
        syncModel();
    }*/

    /**
     * Apply an artifact state filter to the list of visible documents.
     * 
     * @param state
     *            The artifact state to filter by.
     * 
     * @see #removeStateFilters()
     */
/*    void applyStateFilter(final ArtifactState state) {
        if(ArtifactState.ACTIVE == state) {
            applyDocumentFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.STATE_ACTIVE));
            syncModel();
        }
        else if(ArtifactState.CLOSED == state) {
            applyDocumentFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.STATE_CLOSED));
            syncModel();
        }
        else {
            Assert.assertUnreachable(
                    "[BROWSER2] [APP] [B2] [MAIN AVATAR] [MODEL] [CANNOT FILTER BY STATE " + state + "]");
        }
    }*/

    /**
     * Clear the filter on the visible documents. Note that the search filter
     * will still be applied.
     * 
     * @see #removeSearchFilter()
     */
/*    void clearDocumentFilters() {
        // remove all document filters save the search filter and apply
        // changes
        for(final DocumentFilterKey filterKey : DocumentFilterKey.values()) {
            if(filterKey == DocumentFilterKey.SEARCH) { continue; }
            documentFilter.removeFilter(DOCUMENT_FILTERS.get(filterKey));
        }
        syncModel();
    }*/

    /**
     * Debug the container filter.
     *
     */
    void debug() {
        if(browser.getPlatform().isDevelopmentMode()) {
            logger.debug("[BROWSER2] [APP] [B2] [CONTAINERS MODEL] [" + containers.size() + " CONTAINERS]");
            Integer documentItems = 0;
            for(final CellContainer cc : containers) {
                documentItems += containerDocuments.get(cc).size();
            }
            logger.debug("[BROWSER2] [APP] [B2] [CONTAINERS MODEL] [" + documentItems + " DOCUMENTS]");
            /*
            Integer historyItems = 0;
            for(final MainCellDocument mcd : documents) {
                historyItems += documentHistory.get(mcd).size();
            }
            logger.debug("[BROWSER2] [APP] [B2] [MAIN MODEL] [" + historyItems + " HISTORY EVENTS]");
            */
            logger.debug("[BROWSER2] [APP] [B2] [CONTAINERS MODEL] [" + visibleCells.size() + " VISIBLE CELLS]");
            //logger.debug("[BROWSER2] [APP] [B2] [MAIN MODEL] [" + dirtyCells.size() + " DIRTY CELLS]");
            logger.debug("[BROWSER2] [APP] [B2] [CONTAINERS MODEL] [" + jListModel.size() + " MODEL ELEMENTS]");
            
            // containers
            logger.debug("[BROWSER2] [APP] [B2] [CONTAINERS MODEL] [CONTAINERS (" + containers.size() + ")]");
            for(final CellContainer cc : containers) {
                logger.debug("[BROWSER2] [APP] [B2] [CONTAINERS MODEL]\t[" + cc.getName() + "]");
            }
 
            // documents
            logger.debug("[BROWSER2] [APP] [B2] [CONTAINERS MODEL] [DOCUMENTS (" + documentItems + ")]");
            for(final CellContainer cc : containers) {
                for(final CellDocument cd : containerDocuments.get(cc)) {
                    logger.debug("[BROWSER2] [APP] [B2] [CONTAINERS MODEL]\t[" + cd.getText() + "]");
                }
            }
            
            /*
            // history
            logger.debug("[BROWSER2] [APP] [B2] [MAIN MODEL] [HISTORY EVENTS (" + historyItems + ")]");
            for(final MainCellDocument mcd : documents) {
                for(final MainCellHistoryItem mchi : documentHistory.get(mcd)) {
                    logger.debug("[BROWSER2] [APP] [B2] [MAIN MODEL]\t[" + mchi.getText() + "]");
                }
            }
            */
            // visible cells
            logger.debug("[BROWSER2] [APP] [B2] [CONTAINERS MODEL] [VISIBLE CELLS (" + visibleCells.size() + ")]");
            for(final MainCell mc : visibleCells) {
                logger.debug("[BROWSER2] [APP] [B2] [CONTAINERS MODEL]\t[" + mc.getText() + "]");
            }
            /*
            // pseudo selection
            logger.debug("[BROWSER2] [APP] [B2] [MAIN MODEL] [PSEUDO SELECTION (" + pseudoSelection.size() + ")]");
            for(final MainCell mc : pseudoSelection) {
                logger.debug("[BROWSER2] [APP] [B2] [MAIN MODEL]\t[" + mc.getText() + "]");
            }
            */
            // list elements
            final Enumeration e = jListModel.elements();
            MainCell mc;
            logger.debug("[BROWSER2] [APP] [B2] [CONTAINERS MODEL] [MODEL ELEMENTS (" + jListModel.size() + ")]");
            while(e.hasMoreElements()) {
                mc = (MainCell) e.nextElement();
                logger.debug("[BROWSER2] [APP] [B2] [CONTAINERS MODEL]\t[" + mc.getText() + "]");
            }
            /*
            documentFilter.debug(logger);
            */
        }
    }

    /**
     * Obtain the swing list model.
     * 
     * @return The swing list model.
     */
    ListModel getListModel() { return jListModel; }

    /**
     * Determine whether the document list is currently filtered.
     * 
     * @return True if the document list is filtered; false otherwise.
     */
/*    Boolean isDocumentListFiltered() {
        if(documentFilter.isEmpty()) { return Boolean.FALSE; }
        else {
            if(documentFilter.containsFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.SEARCH))) {
                if(documentFilter.containsFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.STATE_ACTIVE)) ||
                        documentFilter.containsFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.STATE_CLOSED)) ||
                        documentFilter.containsFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.KEY_HOLDER_FALSE)) ||
                        documentFilter.containsFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.KEY_HOLDER_TRUE))) {
                    return Boolean.TRUE;
                }
                else { return Boolean.FALSE; }
                
            }
            else { return Boolean.TRUE; }
        }
    }*/

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
     * Remove all key holder filters.
     *
     * @see #applyKeyHolderFilter(Boolean)
     */
/*    void removeKeyHolderFilters() {
        removeDocumentFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.KEY_HOLDER_FALSE));
        removeDocumentFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.KEY_HOLDER_TRUE));
        syncModel();
    }*/

    /**
     * Remove the search filter.
     * 
     * @see #applySearchFilter(List)
     */
/*    void removeSearchFilter() {
        removeDocumentFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.SEARCH));
        syncModel();
    }*/

    /**
     * Remove the state filters.
     * 
     * @see #applyStateFilter(ArtifactState)
     */
/*    void removeStateFilters() {
        removeDocumentFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.STATE_ACTIVE));
        removeDocumentFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.STATE_CLOSED));
        syncModel();
    }*/

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
        syncContainerInternal(containerId, null, remote);
        syncModel();
    }
    
    /**
     * Synchronize the document in the container list.
     * Called, for example, if a new document is created in the container.
     * This will move the container to the top, and also expand the container.
     * 
     * @param containerId
     *            The container id.
     * @param documentId
     *            The document id.           
     * @param remote
     *            Indicates whether the sync is the result of a remote event
     */
    public void syncDocument(final Long containerId, final Long documentId, final Boolean remote) {
        syncContainerInternal(containerId, documentId, remote);
        syncModel();
    }

    /**
     * Synchronize the documents with the list. The content provider is queried
     * for the document and if it can be obtained; it will either be added to or
     * updated in the list. If it cannot be found; it will be removed from the
     * list.
     * 
     * @param documentIds
     *            The document ids.
     * @param remote
     *            Whether or not the reload is the result of a remote event or
     *            not.
     * @see #syncDocumentInternal(Long, Boolean)
     * @see #syncModel()
     */
/*    void syncDocuments(final List<Long> documentIds, final Boolean remote) {
        for(final Long documentId : documentIds) {
            syncDocumentInternal(documentId, remote);
        }
        syncModel();
    }*/

    /**
     * Trigger a double click event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    void triggerDoubleClick(final MainCell mainCell) {
        debug();
        if(mainCell instanceof CellContainer) {
            triggerExpand(mainCell);
        }
        else if(mainCell instanceof CellDocument) {
            final CellDocument cd = (CellDocument) mainCell;
            browser.runOpenDocument(cd.getContainerId(), cd.getId());
        }
        
        // RBM 13/06/05 #36 Double click will open the document instead of expanding
        // The old line of code commented out:
        // triggerExpand(mainCell);
/*
        else if(mainCell instanceof MainCellHistoryItem) {
            final MainCellHistoryItem mch = (MainCellHistoryItem) mainCell;
            browser.runOpenDocumentVersion(mch.getDocumentId(),mch.getVersionId());
        }
*/
    }

    /**
     * Trigger a drag event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     * @param dtde
     *            The drop target drag event.
     */
    //void triggerDragOver(final MainCell mainCell, final DropTargetDragEvent dtde) {}

    /**
     * Trigger the expansion of the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    void triggerExpand(final MainCell mainCell) {
        if(mainCell instanceof CellContainer) {
            final CellContainer cc = (CellContainer) mainCell;
            if(isExpanded(cc)) {
                collapse(cc);
                //pseudoUnselectAll(documentHistory.get(mcd));
            }
            else {
                expand(cc);
                //pseudoSelectAll(documentHistory.get(mcd));
            }

            //syncModel();  needed perhaps because of pseudoSelectAll?
        }
        /*
        else if(mainCell instanceof MainCellHistoryRoot) {
            final MainCellHistoryRoot mchr = (MainCellHistoryRoot) mainCell;
            if(isExpanded(mchr)) { collapse(mchr); }
            else { expand(mchr); }
            syncModel();
        }
        else if(mainCell instanceof MainCellTeamRoot) {
            final MainCellTeamRoot mctr = (MainCellTeamRoot) mainCell;
            if(isExpanded(mctr)) { collapse(mctr); }
            else { expand(mctr); }
            syncModel();
        }
        */
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
        else if (mainCell instanceof CellDocument) {
            new PopupDocument(contentProvider, (CellDocument) mainCell).trigger(browser, jPopupMenu, e);
        }
//new PopupHistoryItem((MainCellHistoryItem) mainCell).trigger(browser, jPopupMenu, e);
        logger.info("[LBROWSER] [APPLICATION] [BROWSER] [CONTAINERS AVATAR] [TRIGGER POPUP]");
        logger.debug(browser.getConnection());
        jPopupMenu.show(invoker, x, y);
    }

    /**
     * Trigger a selection event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    // Make sure to call browser.selectContainer().
/*    void triggerSelection(final MainCell mainCell) {
        pseudoSelection.clear();

        if(mainCell instanceof MainCellDocument) {
            final MainCellDocument mcd = (MainCellDocument) mainCell;
            browser.selectDocument(mcd.getId());

            pseudoSelect(mcd);
            // this means that expand and collapse need to
            // update the selection as well
            if(mcd.isExpanded())
                pseudoSelectAll(documentHistory.get(mcd));
        }
        else if(mainCell instanceof MainCellHistoryItem) {
            final MainCellHistoryItem mchi = (MainCellHistoryItem) mainCell;

            pseudoSelect(mchi.getDocument());
            pseudoSelectAll(documentHistory.get(mchi.getDocument()));
        }
    }*/

    /**
     * Apply the specified filter.
     * 
     * @param filter
     *            The document filter.
     */
/*    private void applyDocumentFilter(final Filter<Artifact> filter) {
        if(!documentFilter.containsFilter(filter)) {
            documentFilter.addFilter(filter);
        }
    }*/

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

    /**
     * Collapse the details for the container.
     * 
     * @param cc
     *            The cell container.
     */
    private void collapse(final CellContainer cc) {
        cc.setExpanded(Boolean.FALSE);
        syncModel();
    }
/*
    private void collapse(final MainCellHistoryRoot mchr) {
        mchr.setExpanded(Boolean.FALSE);
        syncModel();
    }

    private void collapse(final MainCellTeamRoot mctr) {
        mctr.setExpanded(Boolean.FALSE);
        syncModel();
    }*/

    /**
     * Expand the details for the container.
     * 
     * @param cc
     *            The cell container.
     */
    private void expand(final CellContainer cc) {
        cc.setExpanded(Boolean.TRUE);
        syncModel();
    }
/*
    private void expand(final MainCellHistoryRoot mchr) {
        mchr.setExpanded(Boolean.TRUE);
        syncModel();
    }

    private void expand(final MainCellTeamRoot mctr) {
        mctr.setExpanded(Boolean.TRUE);
        syncModel();
    }*/

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
        
        // For each container, read the documents
        for(final CellContainer cc : containers) {
            containerDocuments.put(cc, readDocuments(cc));
        }
        
        /*
        documents.clear();
        if(!containers.isEmpty()) {
            final Container c = containers.get(0);
            // read the documents from the provider into the list
            documents.addAll(readDocuments(c));
            for(final MainCellDocument mcd : documents) {
                documentHistory.put(mcd, readHistory(mcd));
                documentTeam.put(mcd, new MainCellTeam(mcd, readTeam(mcd)));
            }           
        }
        */
        syncModel();
    }

/*    private void pseudoSelect(final MainCell mainCell) {
        pseudoSelection.add(mainCell);
    }

    private void pseudoSelectAll(final List<? extends MainCell> mainCells) {
        if(null == mainCells) { logger.warn("[LBROWSER] [APPLICATION] [BROWSER] [MAIN] [CANNOT PSEUDO SELECT NULL]"); }
        else { for(final MainCell mc : mainCells) { pseudoSelect(mc); } }
    }

    private void pseudoUnselect(final MainCell mainCell) {
        pseudoSelection.remove(mainCell);
    }

    private void pseudoUnselectAll(final List<? extends MainCell> mainCells) {
        for(final MainCell mc : mainCells) { pseudoUnselect(mc); }
    }*/

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
     * Read the documents from the provider.
     * 
     * @param cellContainer
     *            The container.
     * 
     * @return The documents.
     */
    private List<CellDocument> readDocuments(final CellContainer cellContainer) {
        final List<CellDocument> l = new LinkedList<CellDocument>();
        final CellDocument[] a =
                (CellDocument[]) contentProvider.getElements(1, cellContainer);
        for(final CellDocument cd : a) { l.add(cd); }
        return l;
    }

    /**
     * Read the history for the document from the provider.
     * 
     * @param mainCellDocument
     *            The document.
     * @return The history.
     */
/*    private List<MainCellHistoryItem> readHistory(
            final MainCellDocument mainCellDocument) {
        final List<MainCellHistoryItem> l = new LinkedList<MainCellHistoryItem>();
        final MainCellHistoryItem[] a =
                (MainCellHistoryItem[]) contentProvider.getElements(1, mainCellDocument);
        for(final MainCellHistoryItem mchi : a) { l.add(mchi); }
        return l;
    }*/

    /**
     * Read the team for the document from the provider.
     * 
     * @param mcd
     *            The document.
     * @return The document team.
     */
/*    private List<MainCellUser> readTeam(final MainCellDocument mcd) {
        final List<MainCellUser> l = new ArrayList<MainCellUser>();
        final MainCellUser[] a =
            (MainCellUser[]) contentProvider.getElements(5, mcd);
        for(final MainCellUser mcu : a) { l.add(mcu); }
        return l;
    }*/

    /**
     * Remove a document filter.
     * 
     * @param filter
     *            The document filter.
     */
/*    private void removeDocumentFilter(final Filter<Artifact> filter) {
        if(documentFilter.containsFilter(filter)) {
            documentFilter.removeFilter(filter);
        }
    }*/
    
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
    private void syncContainerInternal(final Long containerId, final Long documentId, final Boolean remote) {
        final CellContainer cellContainer = readContainer(containerId);

        // if the container is null; we can assume the container has been
        // deleted (it's not longer being created by the provider); so we find
        // the container and remove it
        if(null == cellContainer) {
            for(int i = 0; i < containers.size(); i++) {
                if(containers.get(i).getId().equals(containerId)) {
                    containers.remove(i);
                    break;
                }
            }
            /*
            final MainCellDocument[] historyKeys =
                (MainCellDocument[]) documentHistory.keySet().toArray(new MainCellDocument[] {});
            for(int i = 0; i < historyKeys.length; i++) {
                if(historyKeys[i].getId().equals(documentId)) {
                    documentHistory.remove(historyKeys[i]);
                    break;
                }
            }
            */
        }
        // the container is not null; therefore it is either new; or updated
        else {

            // the container is new
            if(!containers.contains(cellContainer)) {
                containers.add(0, cellContainer);
                containerDocuments.put(cellContainer, readDocuments(cellContainer));
                
                /*
                documentHistory.put(cellContainer, readHistory(cellContainer));
                documentTeam.put(cellContainer, new MainCellTeam(cellContainer, readTeam(cellContainer)));
                */
            }
            // The container has been updated
            // If document is not null then it could be, for example, a new document
            else {
                final int index = containers.indexOf(cellContainer);

                // preserve expand\collapse state, except if there is a new document
                // then force it to expand.
                if (documentId!=null) {
                    cellContainer.setExpanded(Boolean.TRUE);
                }
                else {
                    cellContainer.setExpanded(containers.get(index).isExpanded());
                }

                // if the reload is the result of a remote event add the container
                // at the top of the list; otherwise add it in the same location
                // it previously existed
                containers.remove(index);
                if(remote) { containers.add(0, cellContainer); }
                else { containers.add(index, cellContainer); }
                
                // Update the document list for this container ("put" replaces the old value)
                containerDocuments.put(cellContainer, readDocuments(cellContainer));
                
                /*
                documentTeam.put(mainCellDocument, new MainCellTeam(cellContainer, readTeam(cellContainer)));
                documentHistory.put(cellContainer, readHistory(cellContainer));

                dirtyCells.add(cellContainer);
                dirtyCells.addAll(documentHistory.get(cellContainer));
                dirtyCells.add(documentTeam.get(cellContainer));
                */
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
        /*
        ArtifactFilterManager.filter(filteredDocuments, documentFilter);
        */
        // update all visible cells
        visibleCells.clear();
        for(final CellContainer cc : filteredContainers) {
            visibleCells.add(cc);
            if(cc.isExpanded()) {
                visibleCells.addAll(containerDocuments.get(cc));
            }
            /*
            if(cc.isExpanded()) {
                visibleCells.add(documentTeam.get(cc));
                visibleCells.addAll(documentHistory.get(cc));
            }
            */
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
                /*
                final int jIndex = jListModel.indexOf(mc);
                final int vIndex = visibleCells.indexOf(mc);
                if(jIndex != vIndex) {
                    jListModel.removeElement(mc);
                    jListModel.add(vIndex, mc);
                }
                */
            }
        }

        // prune cells
        final MainCell[] mcModel = new MainCell[jListModel.size()];
        jListModel.copyInto(mcModel);
        for(final MainCell mc : mcModel) {
            if(!visibleCells.contains(mc)) { jListModel.removeElement(mc); }
        }
/*
        // update dirty cells
        final Iterator<MainCell> iDirty = dirtyCells.iterator();
        MainCell mcDirty;
        while(iDirty.hasNext()) {
            mcDirty = iDirty.next();
            if(jListModel.contains(mcDirty)) {  // might not contain history cells
                jListModel.removeElement(mcDirty);
                jListModel.add(visibleCells.indexOf(mcDirty), mcDirty);
                iDirty.remove();
            }
        }

        if(isDocumentListFiltered()) { browser.fireFilterApplied(); }
        else { browser.fireFilterRevoked(); }
*/
        debug();
    }

    /**
     * Unique keys used in the {@link DOCUMENT_FILTERS} collection.
     * 
     */
/*    private enum DocumentFilterKey {
        KEY_HOLDER_FALSE, KEY_HOLDER_TRUE, SEARCH, STATE_ACTIVE, STATE_CLOSED
    }*/
}
