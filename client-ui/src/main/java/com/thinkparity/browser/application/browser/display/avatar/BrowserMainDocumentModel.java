/*
 * Apr 4, 2006
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Component;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.DefaultListModel;
import javax.swing.JPopupMenu;
import javax.swing.ListModel;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCell;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellDocument;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellHistoryItem;
import com.thinkparity.browser.application.browser.display.avatar.main.popup.PopupDocument;
import com.thinkparity.browser.application.browser.display.avatar.main.popup.PopupHistoryItem;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.parity.model.artifact.Artifact;
import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.filter.FilterChain;
import com.thinkparity.model.parity.model.filter.ModelFilterManager;
import com.thinkparity.model.parity.model.filter.artifact.Active;
import com.thinkparity.model.parity.model.filter.artifact.Closed;
import com.thinkparity.model.parity.model.filter.artifact.IsKeyHolder;
import com.thinkparity.model.parity.model.filter.artifact.IsNotKeyHolder;
import com.thinkparity.model.parity.model.filter.artifact.Search;
import com.thinkparity.model.parity.model.index.IndexHit;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class BrowserMainDocumentModel {

    /**
     * Collection of all filters used by the document model.
     * 
     */
    private static final Map<Enum<?>, Filter<Artifact>> DOCUMENT_FILTERS;

    static {
        DOCUMENT_FILTERS = new HashMap<Enum<?>, Filter<Artifact>>(5, 0.75F);
        DOCUMENT_FILTERS.put(DocumentFilterKey.STATE_ACTIVE, new Active());
        DOCUMENT_FILTERS.put(DocumentFilterKey.STATE_CLOSED, new Closed());
        DOCUMENT_FILTERS.put(DocumentFilterKey.KEY_HOLDER_FALSE, new IsNotKeyHolder());
        DOCUMENT_FILTERS.put(DocumentFilterKey.KEY_HOLDER_TRUE, new IsKeyHolder());
        DOCUMENT_FILTERS.put(DocumentFilterKey.SEARCH, new Search(new LinkedList<IndexHit>()));
    }

    /** An apache logger. */
    protected final Logger logger;

    /** The application. */
    private final Browser browser;

    /** The content provider. */
    private CompositeFlatSingleContentProvider contentProvider;

    /** The filter that is used to filter documents to produce visibleDocuments. */
    private final FilterChain<Artifact> documentFilter;

    /** A list of all documents. */
    private final List<MainCellDocument> documents;

    /** The swing list model. */
    private final DefaultListModel jListModel;

    /**
     * The list of all updated documents.
     * 
     * @see #syncDocument(Long, Boolean)
     * @see #syncDocuments()
     */
    private final List<MainCellDocument> touchedDocuments;

    /** The set of all visible documents. */
    private final List<MainCellDocument> visibleDocuments;

    /** Visible document histories. */
    private final Map<MainCellDocument, List<MainCellHistoryItem>> visibleHistory;

    /**
     * Create a BrowserMainDocumentModel.
     * 
     */
    BrowserMainDocumentModel(final Browser browser) {
        super();
        this.browser = browser;
        this.documentFilter = new FilterChain<Artifact>();
        this.documents = new LinkedList<MainCellDocument>();
        this.visibleHistory = new Hashtable<MainCellDocument, List<MainCellHistoryItem>>(10, 0.65F);
        this.jListModel = new DefaultListModel();
        this.logger = browser.getPlatform().getLogger(getClass());
        this.touchedDocuments = new LinkedList<MainCellDocument>();
        this.visibleDocuments = new LinkedList<MainCellDocument>();
    }

    /**
     * Determine whether or not the main cell is expanded.
     * 
     * @param mainCell
     *            The cell.
     * @return True if the cell is expanded; false otherwise.
     */
    public Boolean isExpanded(final MainCell mainCell) {
        if(mainCell instanceof MainCellDocument) {
            return ((MainCellDocument) mainCell).isExpanded();
        }
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
    void applyKeyHolderFilter(final Boolean keyHolder) {
        applyDocumentFilter(keyHolder
                ? DOCUMENT_FILTERS.get(DocumentFilterKey.KEY_HOLDER_TRUE)
                        : DOCUMENT_FILTERS.get(DocumentFilterKey.KEY_HOLDER_FALSE));
        syncDocuments();
    }

    /**
     * Apply a search filter to the list of visible documents.
     * 
     * @param searchResult
     *            The search result to filter by.
     * 
     * @see #removeSearchFilter()
     */
    void applySearchFilter(final List<IndexHit> searchResult) {
        final Search search = (Search) DOCUMENT_FILTERS.get(DocumentFilterKey.SEARCH);
        search.setResults(searchResult);

        applyDocumentFilter(search);
        syncDocuments();
    }

    /**
     * Apply an artifact state filter to the list of visible documents.
     * 
     * @param state
     *            The artifact state to filter by.
     * 
     * @see #removeStateFilters()
     */
    void applyStateFilter(final ArtifactState state) {
        if(ArtifactState.ACTIVE == state) {
            applyDocumentFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.STATE_ACTIVE));
            syncDocuments();
        }
        else if(ArtifactState.CLOSED == state) {
            applyDocumentFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.STATE_CLOSED));
            syncDocuments();
        }
        else {
            Assert.assertUnreachable(
                    "[BROWSER2] [APP] [B2] [MAIN AVATAR] [MODEL] [CANNOT FILTER BY STATE " + state + "]");
        }
    }

    /**
     * Clear the filter on the visible documents. Note that the search filter
     * will still be applied.
     * 
     * @see #removeSearchFilter()
     */
    void clearDocumentFilters() {
        // remove all document filters save the search filter and apply
        // changes
        for(final DocumentFilterKey filterKey : DocumentFilterKey.values()) {
            if(filterKey == DocumentFilterKey.SEARCH) { continue; }
            documentFilter.removeFilter(DOCUMENT_FILTERS.get(filterKey));
        }
        syncDocuments();
    }

    /**
     * Debug the document filter.
     *
     */
    void debug() {
        if(browser.getPlatform().isDebugMode()) {
            // documents
            logger.debug("[BROWSER2] [APP] [B2] [MAIN MODEL] [" + documents.size() + " DOCUMENTS]");
            for(final MainCellDocument mcd : documents)
                logger.debug("[BROWSER2] [APP] [B2] [MAIN MODEL] [" + mcd.getText() + "]");
            // visible documents
            logger.debug("[BROWSER2] [APP] [B2] [MAIN MODEL] [" + visibleDocuments.size() + " VISIBLE DOCUMENTS]");
            for(final MainCellDocument mcd : visibleDocuments)
                logger.debug("[BROWSER2] [APP] [B2] [MAIN MODEL] [" + mcd.getText() + "]");
            // history items
            logger.debug("[BROWSER2] [APP] [B2] [MAIN MODEL] [" + visibleHistorySize() + " HISTORY ITEMS]");
            for(final List<MainCellHistoryItem> l : visibleHistory.values()) {
                for(final MainCellHistoryItem mchi : l) {
                    logger.debug("[BROWSER2] [APP] [B2] [MAIN MODEL] [" + mchi.getText() + "]");
                }
            }
            logger.debug("[BROWSER2] [APP] [B2] [MAIN MODEL] [" + jListModel.size() + " LIST CELLS]");
            final Enumeration e = jListModel.elements();
            MainCell mc;
            while(e.hasMoreElements()) {
                mc = (MainCell) e.nextElement();
                logger.debug("[BROWSER2] [APP] [B2] [MAIN MODEL] [" + mc.getText() + "] ["
                    + mc.isGroupSelected() + "]");
            }
            documentFilter.debug(logger);
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
    Boolean isDocumentListFiltered() {
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
    }

    /**
     * Determine if the document is visible.
     * 
     * @param displayDocument
     *            The display document.
     * @return True if the document is visible; false otherwise.
     */
    Boolean isDocumentVisible(final MainCellDocument displayDocument) {
        return visibleDocuments.contains(displayDocument);
    }

    /**
     * Remove all key holder filters.
     *
     * @see #applyKeyHolderFilter(Boolean)
     */
    void removeKeyHolderFilters() {
        removeDocumentFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.KEY_HOLDER_FALSE));
        removeDocumentFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.KEY_HOLDER_TRUE));
        syncDocuments();
    }

    /**
     * Remove the search filter.
     * 
     * @see #applySearchFilter(List)
     */
    void removeSearchFilter() {
        removeDocumentFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.SEARCH));
        syncDocuments();
    }

    /**
     * Remove the state filters.
     * 
     * @see #applyStateFilter(ArtifactState)
     */
    void removeStateFilters() {
        removeDocumentFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.STATE_ACTIVE));
        removeDocumentFilter(DOCUMENT_FILTERS.get(DocumentFilterKey.STATE_CLOSED));
        syncDocuments();
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
     * Synchronize the document with the list. The content provider is queried
     * for the document and if it can be obtained; it will either be added to or
     * updated in the list. If it cannot be found; it will be removed from the
     * list.
     * 
     * @param documentId
     *            The document id.
     * @param remote
     *            Whether or not the reload is the result of a remote event or
     *            not.
     */
    void syncDocument(final Long documentId, final Boolean remote) {
        syncDocumentInternal(documentId, remote);
        syncDocuments();
    }

    /**
     * Synchronize the documents with the list. The content provider is queried
     * for the document and if it can be obtained; it will either be added to or
     * updated in the list. If it cannot be found; it will be removed from the
     * list.
     * 
     * @param documentId
     *            The document id.
     * @param remote
     *            Whether or not the reload is the result of a remote event or
     *            not.
     * @see #syncDocumentInternal(Long, Boolean)
     * @see #syncDocuments()
     */
    void syncDocuments(final Set<Long> documentIds, final Boolean remote) {
        for(final Long documentId : documentIds) {
            syncDocumentInternal(documentId, remote);
        }
        syncDocuments();
    }

    /**
     * Trigger a double click event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    void triggerDoubleClick(final MainCell mainCell) {
        debug();
        triggerExpand(mainCell);
    }

    /**
     * Trigger a drag event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     * @param dtde
     *            The drop target drag event.
     */
    void triggerDragOver(final MainCell mainCell, final DropTargetDragEvent dtde) {}

    /**
     * Trigger the expansion of the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    void triggerExpand(final MainCell mainCell) {
        if(mainCell instanceof MainCellDocument) {
            final MainCellDocument mcd = (MainCellDocument) mainCell;
            if(isExpanded(mcd)) { collapse(mcd); }
            else { expand(mcd); }

            syncDocuments();
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
        if(mainCell instanceof MainCellDocument) {
            new PopupDocument((MainCellDocument) mainCell).trigger(browser, jPopupMenu, e);
        }
        else if(mainCell instanceof MainCellHistoryItem) {
            new PopupHistoryItem((MainCellHistoryItem) mainCell).trigger(browser, jPopupMenu, e);
        }
        jPopupMenu.show(invoker, x, y);
    }

    /**
     * Trigger a selection event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    void triggerSelection(final MainCell mainCell) {
        if(mainCell instanceof MainCellDocument) {
            browser.selectDocument(((MainCellDocument) mainCell).getId());
        }
        // set group selection
        if(mainCell instanceof MainCellDocument ||
            mainCell instanceof MainCellHistoryItem) {
            for(final MainCell mc : getAllCells()) {
                mc.setGroupSelected(Boolean.FALSE);
            }
            for(final MainCell mc : getDocumentGroup(mainCell)) {
                mc.setGroupSelected(Boolean.TRUE);
            }
        }
    }

    /**
     * Apply the specified filter.
     * 
     * @param filter
     *            The document filter.
     */
    private void applyDocumentFilter(final Filter<Artifact> filter) {
        if(!documentFilter.containsFilter(filter)) {
            documentFilter.addFilter(filter);
        }
    }

    /**
     * Collapse the history.
     * 
     * @param mcd
     *            The main cell document.
     */
    private void collapse(final MainCellDocument mcd) {
        mcd.setExpanded(Boolean.FALSE);
        MainCellHistoryItem mchi;
        for(final Iterator<MainCellHistoryItem> i = visibleHistory.get(mcd).iterator(); i.hasNext();) {
            mchi = i.next();
            if(mchi.getDocument().equals(mcd)) { i.remove(); }
        }
    }

    /**
     * Expand the history for the document.
     * 
     * @param mcd
     *            The main cell document.
     */
    private void expand(final MainCellDocument mcd) {
        mcd.setExpanded(Boolean.TRUE);
        final MainCellHistoryItem[] mchiArray = readHistory(mcd);
        final List<MainCellHistoryItem> lmchi = visibleHistory.containsKey(mcd)
            ? visibleHistory.get(mcd)
            : new LinkedList<MainCellHistoryItem>();
        for(final MainCellHistoryItem mchi : mchiArray) { lmchi.add(mchi); }
        visibleHistory.put(mcd, lmchi);
    }

    /**
     * Obtain the list of cells adhering to the document group.  This
     * consists of the document; as well as all history cells.
     *
     */
    private MainCell[] getAllCells() {
        final MainCell[] allCells = new MainCell[jListModel.size()];
        jListModel.copyInto(allCells);
        return allCells;
    }

    /**
     * Obtain the list of cells adhering to the document group.  This
     * consists of the document; as well as all history cells.
     *
     */
    private MainCell[] getDocumentGroup(final MainCell mainCell) {
        final Set<MainCell> group = new HashSet<MainCell>();
        group.add(mainCell);
        if(visibleHistory.containsKey(mainCell))
            group.addAll(visibleHistory.get(mainCell));
        return group.toArray(new MainCell[] {});
    }

    /**
     * Initialize the document model
     * <ol>
     * <li>Load the documents from the provider.
     * <li>Load the history from the provider.
     * <li>Synchronize the data with the display.
     * <ol>
     */
    private void initModel() {
        // read the documents from the provider into the list
        documents.clear();
        final MainCellDocument[] mcdArray = readDocuments();
        for(final MainCellDocument mcd : mcdArray) { documents.add(mcd); }
        syncDocuments();
    }

    /**
     * Read the documents from the provider.
     * 
     * @return The documents.
     */
    private MainCellDocument[] readDocuments() {
        return (MainCellDocument[]) contentProvider.getElements(0, null);
    }

    /**
     * Read the history for the document from the provider.
     * 
     * @param mainCellDocument
     *            The document.
     * @return The history.
     */
    private MainCellHistoryItem[] readHistory(
            final MainCellDocument mainCellDocument) {
        return (MainCellHistoryItem[]) contentProvider.getElements(
                1, mainCellDocument);
    }

    /**
     * Remove a document filter.
     * 
     * @param filter
     *            The document filter.
     */
    private void removeDocumentFilter(final Filter<Artifact> filter) {
        if(documentFilter.containsFilter(filter)) {
            documentFilter.removeFilter(filter);
        }
    }

    /**
     * Synchronize the document with the list. The content provider is queried
     * for the document and if it can be obtained; it will either be added to or
     * updated in the list as well as its history updated. If it cannot be found;
     * it will be removed from the list.
     * 
     * @param documentId
     *            The document id.
     * @param remote
     *            Whether or not the reload is the result of a remote event or
     *            not.
     * 
     * @see #syncDocument(Long, Boolean)
     * @see #syncDocuments()
     */
    private void syncDocumentInternal(final Long documentId,
            final Boolean remote) {
        final MainCellDocument displayDocument =
            (MainCellDocument) contentProvider.getElement(0, documentId);
        // if the display document is null; we can assume the document has been
        // deleted (it's not longer being created by the provider); so we find
        // the document and remove it
        if(null == displayDocument) {
            for(int i = 0; i < documents.size(); i++) {
                if(documents.get(i).getId().equals(documentId)) {
                    documents.remove(i);
                    break;
                }
            }
        }
        else {
            // if the document is in the list; we need to remove it;
            // and re-add it.
            if(documents.contains(displayDocument)) {
                final int index = documents.indexOf(displayDocument);
                documents.remove(index);

                // if the reload is not the result of a remote event; put it back
                // where it was; otherwise move it to the top
                if(remote) {
                    documents.add(0, displayDocument);
                    touchedDocuments.add(displayDocument);
                }
                else {
                    documents.add(index, displayDocument);
                    touchedDocuments.add(displayDocument);
                }
            }
            // if it's not in the list; just add it to the top
            else { documents.add(0, displayDocument); }
        }
    }

    /**
     * Synchronize the document list with the list of visible documents and
     * the list model.
     * 
     */
    private void syncDocuments() {
        // sync the documents with the visible documents
        visibleDocuments.clear();
        visibleDocuments.addAll(documents);
        ModelFilterManager.filter(visibleDocuments, documentFilter);
        // sync visible documents with the swing list's model
        MainCell mc;
        for(int i = 0; i < visibleDocuments.size(); i++) {
            mc = (MainCell) visibleDocuments.get(i);
            if(!jListModel.contains(mc)) { jListModel.add(i, mc); }
        }
        final MainCell[] cells = new MainCell[jListModel.size()];
        jListModel.copyInto(cells);
        int visibleIndex;
        for(int i = 0; i < cells.length; i++) {
            mc = cells[i];
            // remove documents that are no longer visible
            if(!visibleDocuments.contains(mc))
                jListModel.removeElement(mc);
            // re-create the list item of those that have been touched
            if(touchedDocuments.contains(mc)) {
                visibleIndex = visibleDocuments.indexOf(mc);
                jListModel.remove(i);
                jListModel.add(visibleIndex, visibleDocuments.get(visibleIndex));
            }
        }
        // insert the history
        if(0 < visibleHistorySize()) {
            int index, prevIndex = 0;
            int count = 0;
            for(final MainCellDocument mcd : visibleHistory.keySet()) {
                // if the document isn't visible; neither is the history
                if(!visibleDocuments.contains(mcd)) { continue; }

                for(final MainCellHistoryItem mchi : visibleHistory.get(mcd)) {
                    index = jListModel.indexOf(mcd);
                    if(index != prevIndex) { count = 0; }
                    jListModel.add(index + (++count), mchi);
                    prevIndex = index;
                }
            }
        }
        touchedDocuments.clear();

        if(isDocumentListFiltered()) { browser.fireFilterApplied(); }
        else { browser.fireFilterRevoked(); }

        debug();
    }

    /**
     * Obtain the number of visible history items.
     *
     * @return The number of visible history items.
     */
    private Integer visibleHistorySize() {
        Integer size = 0;
        for(final List<MainCellHistoryItem> l : visibleHistory.values()) {
            size += l.size();
        }
        return size;
    }

    /**
     * Unique keys used in the {@link DOCUMENT_FILTERS} collection.
     * 
     */
    private enum DocumentFilterKey {
        KEY_HOLDER_FALSE, KEY_HOLDER_TRUE, SEARCH, STATE_ACTIVE, STATE_CLOSED
    }
}
