/**
 * Created On: 13-Jul-06 11:02:44 AM
 */
package com.thinkparity.browser.application.browser.display.avatar.tab.container;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.ContentProvider;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCellRenderer;
import com.thinkparity.browser.application.browser.display.renderer.tab.container.ContainerCell;
import com.thinkparity.browser.application.browser.dnd.ImportTxHandler;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.SwingUtil;

import com.thinkparity.model.parity.model.artifact.ArtifactState;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.index.IndexHit;

/**
 * The containers avatar displays the list of containers (packages)
 * and the documents inside the containers.
 * 
 * @author rob_masako@shaw.ca; raymond@thinkparity.com
 * @version 1.1.2.5
 */
public class ContainerAvatar extends Avatar {
    
    /** The relative location of the "hot" zone in each cell. */
    private static final Point CELL_NODE_LOCATION = new Point(10, 2);

    /** The size of the "hot" zone in each cell. */
    private static final Dimension CELL_NODE_SIZE = new Dimension(20, 20);

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The swing JList. */
    private JList jList;
    
    /** The model. */
    private final ContainerAvatarModel model;
    
    /** A popup menu item factory. */
    private final PopupItemFactory popupItemFactory;

    /** Create ContainerAvatar. */
    public ContainerAvatar() {
        super("BrowserContainersAvatar", ScrollPolicy.NONE, Color.WHITE);
        this.model = new ContainerAvatarModel(getController());
        this.popupItemFactory = PopupItemFactory.getInstance();
        setLayout(new GridBagLayout());
        initComponents();
    }

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
        throw Assert
                .createNotYetImplemented("ContainerAvatar#applySearchFilter");
    }

    /** Clear all filters. */
    public void clearFilters() {
        throw Assert.createNotYetImplemented("ContainerAvatar#clearFilters");
    }
    
    /** Debug the model. */
    public void debug() {
        model.debug();
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
     *
     */
    public AvatarId getId() { return AvatarId.TAB_CONTAINER; }
    
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
    public Boolean isFilterEnabled() {
        throw Assert.createNotYetImplemented("ContainerAvatar#isFilterEnabled");
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
     *
     */
    public void reload() {
        /* NOCOMMIT */
        model.reload();
    }

    /**
     * Remove the key holder filter.
     *
     * @see #applyKeyHolderFilter(Boolean)
     */
    public void removeKeyHolderFilter() {
        throw Assert
                .createNotYetImplemented("ContainerAvatar#removeKeyHolderFilter");
    }

    /**
     * Remove the search filter from the list.
     *
     * @see #applySearchFilter(List)
     */
    public void removeSearchFilter() {
        throw Assert
                .createNotYetImplemented("ContainerAvatar#removeSearchFilter");
    }

    /**
     * Remove the state filter from the list.
     * 
     * @see #applyStateFilter(ArtifactState)
     */
    public void removeStateFilter() {
        throw Assert
                .createNotYetImplemented("ContainerAvatar#removeStateFilter");
    }
    
    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setContentProvider(com.thinkparity.browser.application.browser.display.provider.ContentProvider)
     * 
     */
    public void setContentProvider(final ContentProvider contentProvider) {
        model.setContentProvider((CompositeFlatSingleContentProvider) contentProvider);
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
     * @param select
     *            Indicates whether the container should be selected.
     */
    public void syncContainer(final Long containerId, final Boolean remote, final Boolean select) {
        final ContainerCell selectedContainer = getSelectedContainer();
        model.syncContainer(containerId, remote);
        // If "select" then attempt to select and expand this container,
        // otherwise select the previously selected container
        if (select) {
            final ContainerCell container = model.getContainerCell(containerId);
            if (null!=container) {
                model.triggerExpand(container);
                selectContainer(container);
            }
        }
        else {
            selectContainer(selectedContainer);
        }
    }
    
    /**
     * Synchronized a document in the model.
     * 
     * @param containerId
     *            The container id.
     * @param documentId
     *            The document id.           
     * @param remote
     *            Indicates whether the sync is the result of a remote event.
     */
    public void syncDocument(final Long containerId, final Long documentId,
            final Boolean remote) {
        final ContainerCell selectedContainer = getSelectedContainer();
        model.syncDocument(containerId, documentId, remote);
        selectContainer(selectedContainer);
    }

    /**
     * Synchronize a document in the model.
     * 
     * @param documentId
     *            A document id.
     * @param remote
     *            Indicates whether the sync is the result of a remote event.
     */
    public void syncDocument(final Long documentId, final Boolean remote) {
        final ContainerCell selectedContainer = getSelectedContainer();
        model.syncDocument(documentId, remote);
        selectContainer(selectedContainer);
    }
    /**
     * Obtain the selected container from the list.
     * 
     * @return The selected container.
     */
    private ContainerCell getSelectedContainer() {
        final TabCell mc = (TabCell) jList.getSelectedValue();
        if(mc instanceof ContainerCell) {
            return (ContainerCell) mc;
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
        jList = new JList(model.getListModel());
        jList.setCellRenderer(new TabCellRenderer());
        jList.setDragEnabled(true);
        jList.setLayoutOrientation(JList.VERTICAL);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.setTransferHandler(new ImportTxHandler(getController(), jList, model));
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
                        model.triggerDoubleClick((TabCell) jList.getSelectedValue());  
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
                        final TabCell mc = (TabCell) jList.getSelectedValue();
                        final Rectangle cellBounds = jList.getCellBounds(listIndex, listIndex);
                        cellBounds.x += CELL_NODE_LOCATION.x * mc.getTextInsetFactor();
                        cellBounds.y += CELL_NODE_LOCATION.y;
                        cellBounds.width = CELL_NODE_SIZE.width;
                        cellBounds.height = CELL_NODE_SIZE.height;
                        if(SwingUtil.regionContains(cellBounds, p)) {                            
                            model.triggerExpand(mc);
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
                        triggerPopup(jList, e.getX(), e.getY());
                    }
                    else { // Check if the click is below the bottom entry in the list
                        final Rectangle cellBounds = jList.getCellBounds(listIndex, listIndex);
                        if (SwingUtil.regionContains(cellBounds,p)) {
                            jList.setSelectedIndex(listIndex);
                            model.triggerPopup((TabCell) jList.getSelectedValue(), jList, e, e.getX(), e.getY());
                        }
                        else {   // Below the bottom entry
                            triggerPopup(jList, e.getX(), e.getY());
                        }
                    }
                }
            }
        });

        final JScrollPane jListScrollPane = new JScrollPane(jList);
        jListScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets.left = c.insets.right = 2;
        c.insets.top = 10;
        c.insets.bottom = 1;
        c.weightx = 1;
        c.weighty = 1;
        add(jListScrollPane, c.clone());
    }

    /**
     * Select a container cell, if it is visible.
     * 
     * @param cc
     *            The container cell.
     */
    private void selectContainer(final ContainerCell cc) {
        if (model.isContainerVisible(cc)) {
            jList.setSelectedValue(cc, true);
        }
    }
    
    /**
     * Trigger a popup when clicking in a blank area.
     * 
     */
    private void triggerPopup(final Component invoker, final int x, final int y) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_CREATE, new Data(0)));
        jPopupMenu.show(invoker, x, y);
    }    
}
