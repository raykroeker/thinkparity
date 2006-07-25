/**
 * Created On: 13-Jul-06 11:02:44 AM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.thinkparity.browser.application.browser.display.avatar.container.CellContainer;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCell;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellRenderer;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.ContentProvider;
import com.thinkparity.browser.application.browser.dnd.CreateDocumentTxHandler;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.SwingUtil;

import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.index.IndexHit;

/**
 * The containers avatar displays the list of containers (packages)
 * and the documents inside the containers.
 * 
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class BrowserContainersAvatar extends Avatar {
    
    /** The relative location of the "hot" zone in each cell. */
    private static final Point CELL_NODE_LOCATION = new Point(10, 2);

    /** The size of the "hot" zone in each cell. */
    private static final Dimension CELL_NODE_SIZE = new Dimension(20, 20);

    /** A logger error statement. */
    //private static final String ERROR_INIT_TMLX =
    //    "[BROWSER2] [APP] [B2] [MAIN LIST] [INIT] [TOO MANY DROP TARGET LISTENERS]";

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The swing JList. */
    private JList jList;
    
    /** The model. */
    private final BrowserContainersModel containersModel;
    
    /**
     * The search filter.
     * 
     * @see #applySearchFilter(List)
     * @see #removeSearchFilter()
     */
    //private Search searchFilter;

    /** Create a BrowserContainersAvatar. */
    BrowserContainersAvatar() {
        super("BrowserContainersAvatar", ScrollPolicy.NONE, Color.WHITE);
        this.containersModel = new BrowserContainersModel(getController());
        setLayout(new GridBagLayout());
        initComponents();
    }

    /**
     * Apply a key holder filter to the main list.
     * 
     * @param keyHolder
     *            If true; results are filtered where the user has the key if
     *            false; results are filtered where the user does not have the
     *            key.
     * 
     * @see #removeKeyHolderFilter()
     */
    /*
    public void applyKeyHolderFilter(final Boolean keyHolder) {
        mainDocumentModel.applyKeyHolderFilter(keyHolder);
    }
    */

    /**
     * Apply the search results to filter the main list.
     * 
     * @param searchResult
     *            The search results.
     * 
     * @see #searchFilter
     * @see #applyFilter(Filter)
     * @see #removeSearchFilter()
     */
    public void applySearchFilter(final List<IndexHit> searchResult) {
    //    mainDocumentModel.applySearchFilter(searchResult);
    }

    /**
     * Apply an artifact state filter.
     * 
     * @param state
     *            The artifact state to filter by.
     * 
     * @see #removeStateFilter()
     */
    /*
    public void applyStateFilter(final ArtifactState state) {
        mainDocumentModel.applyStateFilter(state);
    }
    */

    /** Clear all filters. */
    //public void clearFilters() { mainDocumentModel.clearDocumentFilters(); }
    
    /** Debug the model. */
    //public void debug() { mainDocumentModel.debug(); }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
     *
     */
    public AvatarId getId() { return AvatarId.BROWSER_CONTAINERS; }
    
    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()
     *
     */
    public State getState() { return null; }
    
    /**
     * Determine whether or not the filter is enabled.
     *
     * @return True if it is; false otherwise.
     */
    /*
    public Boolean isFilterEnabled() {
        return mainDocumentModel.isDocumentListFiltered();
    }
    */

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
     *
     */
    public void reload() {}

    /**
     * Remove the key holder filter.
     *
     * @see #applyKeyHolderFilter(Boolean)
     */
    /*
    public void removeKeyHolderFilter() {
        mainDocumentModel.removeKeyHolderFilters();
    }
    */

    /**
     * Remove the search filter from the list.
     *
     * @see #applySearchFilter(List)
     */
    public void removeSearchFilter() {
    //    mainDocumentModel.removeSearchFilter();
    }

    /**
     * Remove the state filter from the list.
     * 
     * @see #applyStateFilter(ArtifactState)
     */
    //public void removeStateFilter() { mainDocumentModel.removeStateFilters(); }
    
    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setContentProvider(com.thinkparity.browser.application.browser.display.provider.ContentProvider)
     * 
     */
    public void setContentProvider(final ContentProvider contentProvider) {
        containersModel.setContentProvider((CompositeFlatSingleContentProvider) contentProvider);
        // set initial selection
        if(0 < jList.getModel().getSize()) { jList.setSelectedIndex(0); }
    }
    
    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
     *
     */
    public void setState(final State state) {}
    
    /**
     * Synchronize the container in the list.
     * Called, for example, if a new container is created.
     * 
     * @param containerId
     *            The container id.
     * @param remote
     *            Indicates whether the sync is the result of a remote event
     */
    public void syncContainer(final Long containerId, final Boolean remote) {
        final CellContainer selectedContainer = getSelectedContainer();
        containersModel.syncContainer(containerId, remote);
        if(containersModel.isContainerVisible(selectedContainer))
            selectContainer(selectedContainer);  
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
        final CellContainer selectedContainer = getSelectedContainer();
        containersModel.syncDocument(containerId, documentId, remote);
        if(containersModel.isContainerVisible(selectedContainer))
            selectContainer(selectedContainer);
    }

    /**
     * Synchronize the document in the list.
     * 
     * @param documentId
     *            The document id.
     * @param remote
     *            Indicates whether the sync is the result of a remote event
     */
    /*
    public void syncDocument(final Long documentId, final Boolean remote) {
        final MainCellDocument selectedDocument = getSelectedDocument();
        mainDocumentModel.syncDocument(documentId, remote);
        if(mainDocumentModel.isDocumentVisible(selectedDocument))
            selectDocument(selectedDocument);
    }
    */
    
    /**
     * Synchronize the container in the list.
     * 
     * @param containerId
     *            The container id.
     * @param remote
     *            Indicates whether the sync is the result of a remote event
     */
    /*
    public void syncContainer(final Long containerId, final Boolean remote) {
        this.containerId = containerId;
        mainDocumentModel.setContainer(containerId);
    }
    */

    /**
     * Synchronize the documents in the list.
     *
     * @param documentIds
     *            The document ids.
     * @param remote
     *            Indicates whether the sync is the result of a remove event.
     */
    /*
    public void syncDocuments(final List<Long> documentIds, final Boolean remote) {
        final MainCellDocument selectedDocument = getSelectedDocument();
        mainDocumentModel.syncDocuments(documentIds, remote);
        if(mainDocumentModel.isDocumentVisible(selectedDocument))
            selectDocument(selectedDocument);
    }
    */

    /**
     * Obtain the selected container from the list.
     * 
     * @return The selected container.
     */
    private CellContainer getSelectedContainer() {
        final MainCell mc = (MainCell) jList.getSelectedValue();
        if(mc instanceof CellContainer) {
            return (CellContainer) mc;
        }
        return null;
    }

    /**
     * Initialize the swing components.
     *
     */
    private void initComponents() {
        // the list that resides on the browser's containers avatar
        //  * is a single selection list
        //  * spans the width of the entire avatar
        //  * uses a custom cell renderer
        jList = new JList(containersModel.getListModel());
        jList.setCellRenderer(new MainCellRenderer());
        jList.setDragEnabled(true);
        jList.setLayoutOrientation(JList.VERTICAL);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.setTransferHandler(new CreateDocumentTxHandler(getController(), jList, containersModel));
        // Available in Java version 6... Improve the behavior of selection during drag and drop.
        // so that it does not change the selection.
        //  -- jList.setDropMode(DropMode.ON);
        //CopyActionEnforcer.applyEnforcer(this);
        jList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(final MouseEvent e) {
                if(2 == e.getClickCount()) {
                    final Point p = e.getPoint();
                    final Integer listIndex = jList.locationToIndex(p);
                    // Don't process double click if it is on white space below the last document
                    final Rectangle cellBounds = jList.getCellBounds(listIndex, listIndex);
                    if(SwingUtil.regionContains(cellBounds, p)){                 
                        containersModel.triggerDoubleClick((MainCell) jList.getSelectedValue());  
                        jList.setSelectedIndex(listIndex);   // Otherwise all lines get unselected.
                    }
                }
                else if(1 == e.getClickCount()) {
                    // first; we grab the index of the list item of the event
                    // second; we grab the bounds of the list item's icon
                    // third; we check to see that the icon was clicked and if it was
                    //      we trigger expand.
                    final Point p = e.getPoint();
                    final Integer listIndex = jList.locationToIndex(p);
                    final Integer selectedIndex = jList.getSelectedIndex();
                    if ((selectedIndex != -1) && (listIndex == selectedIndex)) {
                        final MainCell mc = (MainCell) jList.getSelectedValue();
                        final Rectangle cellBounds = jList.getCellBounds(listIndex, listIndex);
                        cellBounds.x += CELL_NODE_LOCATION.x * mc.getTextInsetFactor();
                        cellBounds.y += CELL_NODE_LOCATION.y;
                        cellBounds.width = CELL_NODE_SIZE.width;
                        cellBounds.height = CELL_NODE_SIZE.height;
                        if(SwingUtil.regionContains(cellBounds, p)) {                            
                            containersModel.triggerExpand(mc);
                            jList.setSelectedIndex(listIndex);   // Otherwise all lines get unselected.
                        }
                    }
                }
            }
            public void mouseReleased(final MouseEvent e) {
                if(e.isPopupTrigger()) {
                    // Desired behavior: if click on an entry in the list then trigger a popup for that entry.
                    // If click in the blank area below the last entry in the list then trigger a popup for entry "null".
                    // If there are no containers then expect getSelectedIndex() to return -1.
                    // If there are 1 or more containers and the user clicks below the final entry then expect
                    // locationToIndex() to return the last entry.
                    final Point p = e.getPoint();
                    final Integer listIndex = jList.locationToIndex(p);
                    final Integer selectedIndex = jList.getSelectedIndex();
                    if (selectedIndex==-1) {  // No entries in the list
                        containersModel.triggerPopup(null, jList, e, e.getX(), e.getY());
                    }
                    else { // Check if the click is below the bottom entry in the list
                        final Rectangle cellBounds = jList.getCellBounds(listIndex, listIndex);
                        if (SwingUtil.regionContains(cellBounds,p)) {
                            jList.setSelectedIndex(listIndex);
                            containersModel.triggerPopup((MainCell) jList.getSelectedValue(), jList, e, e.getX(), e.getY());
                        }
                        else {   // Below the bottom entry
                            containersModel.triggerPopup(null, jList, e, e.getX(), e.getY());
                        }
                    }
                }
            }
        });
        /*
        jList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    mainDocumentModel.triggerSelection((MainCell) jList.getSelectedValue());
                }
            }
        });
        try {
            jList.getDropTarget().addDropTargetListener(new DropTargetAdapter() {
                public void dragOver(final DropTargetDragEvent dtde) {
                    mainDocumentModel.triggerDragOver((MainCell) jList.getSelectedValue(), dtde);
                }
                public void drop(final DropTargetDropEvent dtde) {}
            });
        }
        catch(final TooManyListenersException tmlx) {
            logger.error(ERROR_INIT_TMLX, tmlx);
            throw new RuntimeException(tmlx);
        }
        */

        final JScrollPane jListScrollPane = new JScrollPane(jList);
        jListScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets.left = c.insets.right = 2;
        c.insets.top = c.insets.bottom = 1;
        c.weightx = 1;
        c.weighty = 1;
        add(jListScrollPane, c.clone());
    }

    /**
     * Select a container cell.
     * 
     * @param cc
     *            The container cell.
     */
    private void selectContainer(final CellContainer cc) {
        jList.setSelectedValue(cc, true);
    }
}
