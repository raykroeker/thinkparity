/**
 * Created On: 13-Jul-06 11:02:44 AM
 */
package com.thinkparity.browser.application.browser.display.avatar.tab.container;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.avatar.Resizer;
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
    
    /** Variables used to modify behavior of selection. */
    private Integer selectedIndex = -1;
    private Boolean selectingLastIndex = Boolean.FALSE;

    /** Create ContainerAvatar. */
    public ContainerAvatar() {
        super("BrowserContainersAvatar", ScrollPolicy.NONE, Color.WHITE);
        this.model = new ContainerAvatarModel(getController());
        this.popupItemFactory = PopupItemFactory.getInstance();
        setLayout(new GridBagLayout());
        setResizeEdges(Resizer.FormLocation.MIDDLE);        
        initComponents();
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
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
     *
     */
    public void reload() {
        final String searchExpression = getInputSearchExpression();
        if (null == searchExpression) {
            model.removeSearch();
        } else {
            model.applySearch(searchExpression);
        }
    }

    /**
     * Obtain the input search expression.
     * 
     * @return A search expression <code>String</code>.
     */
    private String getInputSearchExpression() {
        if (null == input) {
            return null;
        } else {
            return (String) ((Data) input).get(DataKey.SEARCH_EXPRESSION);
        }
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setContentProvider(com.thinkparity.browser.application.browser.display.provider.ContentProvider)
     * 
     */
    public void setContentProvider(final ContentProvider contentProvider) {
        model.setContentProvider((CompositeFlatSingleContentProvider) contentProvider);

        // set initial selection
        if(0 < jList.getModel().getSize()) { setSelectedIndex(0); }

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
                setSelectedCell(container);
            }
        }
        else {
            setSelectedCell(selectedContainer);
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
        setSelectedCell(selectedContainer);
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
        setSelectedCell(selectedContainer);
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
        
        // The purpose of this ListSelectionListener is to change JList selection behavior
        // so clicking below the last entry of the list does not cause selection of 
        // the last entry in the list. See also the mousePressed method below.
        jList.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                final Integer newSelectedIndex = jList.getSelectedIndex();
                final Integer lastIndex = jList.getModel().getSize() - 1;
                
                // The first time here, or if the current selection is the last item
                // in the list, or if the new selection is not the last item in
                // the list, then proceed as usual.
                if ((selectedIndex == -1) || (selectedIndex == lastIndex)
                        || (newSelectedIndex != lastIndex)) {
                    selectedIndex = newSelectedIndex;
                    selectingLastIndex = Boolean.FALSE;                    
                }
                // If the last item is being selected then hold off until we can determine
                // that the user has clicked on the cell and not below the cell.
                else {
                    jList.setSelectedIndex(selectedIndex);
                    selectingLastIndex = Boolean.TRUE;   
                }
            }
        });
        
        jList.addMouseListener(new MouseAdapter() {
            public void mousePressed(final MouseEvent e) {
                // If selectingLastIndex is true then the selection was interrupted. Don't
                // perform the selection if the user clicked below the last entry, otherwise
                // proceed with the selection.
                if (selectingLastIndex) {
                    Boolean allowSelection = Boolean.TRUE;
                    final Point p = e.getPoint();
                    final Integer listIndex = jList.locationToIndex(p);
                    if (listIndex != -1) {
                        final Rectangle cellBounds = jList.getCellBounds(listIndex, listIndex);
                        if (!SwingUtil.regionContains(cellBounds,p)) {
                            allowSelection = Boolean.FALSE;
                        }
                    }
                    if (allowSelection) {
                        selectedIndex = listIndex;                        
                        jList.setSelectedIndex(listIndex);
                    }
                    selectingLastIndex = Boolean.FALSE;  
                }
            }
            public void mouseClicked(final MouseEvent e) {
                if(2 == e.getClickCount()) {
                    final Point p = e.getPoint();
                    final Integer listIndex = jList.locationToIndex(p);
                    // Don't process double click if it is on white space below the last document
                    final Rectangle cellBounds = jList.getCellBounds(listIndex, listIndex);
                    if(SwingUtil.regionContains(cellBounds, p)){                 
                        model.triggerDoubleClick((TabCell) jList.getSelectedValue());  
                        setSelectedIndex(listIndex);   // Otherwise all lines get unselected.
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
                        final Rectangle iconBounds = jList.getCellBounds(listIndex, listIndex);
                        iconBounds.x += CELL_NODE_LOCATION.x * mc.getTextInsetFactor();
                        iconBounds.y += CELL_NODE_LOCATION.y;
                        iconBounds.width = CELL_NODE_SIZE.width;
                        iconBounds.height = CELL_NODE_SIZE.height;
                        if(SwingUtil.regionContains(iconBounds, p)) {                            
                            model.triggerExpand(mc);
                            setSelectedIndex(listIndex);   // Otherwise all lines get unselected.
                        }
                    }
                }
            }
            public void mouseReleased(final MouseEvent e) {
                if(e.isPopupTrigger()) {
                    // Desired behavior: if click on an entry in the list then trigger a popup for that entry.
                    // If click in the blank area below the last entry in the list then trigger a popup that
                    // allows the user to create a container.
                    // If there are no containers then expect getSelectedIndex() to return -1.
                    // If there are 1 or more containers and the user clicks below the final entry then expect
                    // locationToIndex() to return the last entry.
                    final Point p = e.getPoint();
                    final Integer listIndex = jList.locationToIndex(p);
                    final Integer listSelectedIndex = jList.getSelectedIndex();
                    if (listSelectedIndex==-1) {  // No entries in the list
                        triggerPopup(jList, e.getX(), e.getY());
                    }
                    else { // Check if the click is below the bottom entry in the list
                        final Rectangle cellBounds = jList.getCellBounds(listIndex, listIndex);
                        if (SwingUtil.regionContains(cellBounds,p)) {
                            setSelectedIndex(listIndex);
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
        jListScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
       
        // If the user clicks on the tab above the jList, show a popup menu.
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(final MouseEvent e) {
                if(e.isPopupTrigger()) {
                    triggerPopup(ContainerAvatar.this, e.getX(), e.getY());
                }
            }
        });
        
        final GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.insets.left = c.insets.right = 2;
        c.insets.top = 15;
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
    private void setSelectedCell(final ContainerCell cc) {
        if (model.isContainerVisible(cc)) {
            // Set selectedIndex to -1 so the ListSelectionListener behaves correctly
            selectedIndex = -1;
            jList.setSelectedValue(cc, true);
        }
    }
    
    /**
     * Select an entry in the JList.
     * 
     * @param index
     *              The JList index to select.
     */
    private void setSelectedIndex(final Integer index) {
        // Set selectedIndex to -1 so the ListSelectionListener behaves correctly        
        selectedIndex = -1;
        jList.setSelectedIndex(index);
    }
    
    /**
     * Trigger a popup when clicking in a blank area.
     * 
     */
    private void triggerPopup(final Component invoker, final int x, final int y) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTAINER_CREATE, Data.emptyData()));
        jPopupMenu.show(invoker, x, y);
    }    

    public enum DataKey { SEARCH_EXPRESSION }
}
