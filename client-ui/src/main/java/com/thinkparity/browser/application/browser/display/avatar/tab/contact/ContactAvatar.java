/**
 * Created On: 20-Jun-2006 12:05:08 PM
 */
package com.thinkparity.browser.application.browser.display.avatar.tab.contact;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.thinkparity.browser.application.browser.component.MenuFactory;
import com.thinkparity.browser.application.browser.component.PopupItemFactory;
import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.provider.ContentProvider;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCellRenderer;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;
import com.thinkparity.browser.platform.application.display.avatar.Avatar;
import com.thinkparity.browser.platform.util.State;
import com.thinkparity.browser.platform.util.SwingUtil;

import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.index.IndexHit;
import com.thinkparity.model.xmpp.JabberId;

/**
 * The contacts list avatar displays the list of contacts.
 *
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContactAvatar extends Avatar {

    /** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

    /** The swing JList. */
	private JList jList;
    
    /** The model. */
    private final ContactAvatarModel model;
    
    /** A popup menu item factory. */
    private final PopupItemFactory popupItemFactory;
    
    /** Create a BrowserContactsAvatar. */
    public ContactAvatar() {
        super("BrowserContactsAvatar", ScrollPolicy.NONE, Color.WHITE);
        this.model = new ContactAvatarModel(getController());
        this.popupItemFactory = PopupItemFactory.getInstance();
        setLayout(new GridBagLayout());   // Layout manager for this container
        initComponents();
    }

    /**
     * Apply the search results to filter the contacts list.
     * 
     * @param searchResult
     *            The search results.
     * 
     * @see #searchFilter
     * @see #applyFilter(Filter)
     * @see #removeSearchFilter()
     */
    public void applySearchFilter(final List<IndexHit> searchResult) {
        model.applySearchFilter(searchResult);
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
     *
     */
    public AvatarId getId() { return AvatarId.TAB_CONTACT; }
    
    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getState()
     *
     */
    public State getState() { return null; }
    
	/**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#reload()
     *
     */
    public void reload() {}

    /**
     * Remove the search filter from the list.
     *
     * @see #applySearchFilter(List)
     */
    public void removeSearchFilter() {
        model.removeSearchFilter();
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setContentProvider(com.thinkparity.browser.application.browser.display.provider.ContentProvider)
     * 
     */
    public void setContentProvider(final ContentProvider contentProvider) {
        model.setContentProvider((CompositeFlatSingleContentProvider) contentProvider);
        if(0 < jList.getModel().getSize()) {
            jList.setSelectedIndex(0);
        }
    }
    
    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setState(com.thinkparity.browser.platform.util.State)
     *
     */
    public void setState(final State state) {}

    /**
     * Synchronize the contact in the list, for example if it is deleted.
     * 
     * @param contactId
     *            The contact id.
     * @param remote
     *            Indicates whether the sync is the result of a remote event
     */
    public void syncContact(final JabberId contactId, final Boolean remote) {
        final TabCell selectedCell = getSelectedCell();
        model.syncContact(contactId, remote);
        if(model.isCellVisible(selectedCell))
            selectCell(selectedCell);
    }

    /**
     * Synchronize the incoming invitation in the list.
     * 
     * @param invitationId
     *            An invitation id.
     * @param remote
     *            Whether or not the the source is a remote event.
     */
    public void syncIncomingInvitation(final Long invitationId,
            final Boolean remote) {
        final TabCell selectedCell = getSelectedCell();
        model.syncIncomingInvitation(invitationId, remote);
        if (model.isCellVisible(selectedCell))
            selectCell(selectedCell);
    }

    /**
     * Synchronize an invitation in the list.
     * 
     * @param invitationId
     *            An invitation id.
     * @param remote
     *            Whether or not the the source is a remote event.
     */
    public void syncOutgoingInvitation(final Long invitationId,
            final Boolean remote) {
        final TabCell selectedCell = getSelectedCell();
        model.syncOutgoingInvitation(invitationId, remote);
        if (model.isCellVisible(selectedCell))
            selectCell(selectedCell);
    }

    /**
     * Obtain the selected contact from the list.
     * 
     * @return The selected contact.
     */
    private TabCell getSelectedCell() {
        return (TabCell) jList.getSelectedValue();
    }
    
    /**
     * Initialize the swing components.
     *
     */
    private void initComponents() {
        // The contacts list that resides on the browser's main avatar
        // 	* is a single selection list
        //	* spans the width of the entire avatar
        // 	* uses a custom cell renderer
        jList = new JList(model.getListModel());
        jList.setCellRenderer(new TabCellRenderer());
        jList.setDragEnabled(true);
        jList.setLayoutOrientation(JList.VERTICAL);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(final MouseEvent e) {
                if(2 == e.getClickCount()) {
                    final Point p = e.getPoint();
                    final Integer listIndex = jList.locationToIndex(p);
                    // Don't process double click if it is on white space below the last contact
                    final Rectangle cellBounds = jList.getCellBounds(listIndex, listIndex);
                    if(SwingUtil.regionContains(cellBounds, p)){
                        jList.setSelectedIndex(listIndex);                    
                        model.triggerDoubleClick((TabCell) jList.getSelectedValue());    
                    }
                }
            }
            public void mouseReleased(final MouseEvent e) {
                if(e.isPopupTrigger()) {
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
        jList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    model.triggerSelection((TabCell) jList.getSelectedValue());
                }
            }
        });

        final JScrollPane jListScrollPane = new JScrollPane(jList);
        jListScrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // If the user clicks on the tab above the jList, show a popup menu.
        addMouseListener(new MouseAdapter() {
            public void mouseReleased(final MouseEvent e) {
                if(e.isPopupTrigger()) {
                    triggerPopup(ContactAvatar.this, e.getX(), e.getY());
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
        add(jListScrollPane, c.clone());  // Add the jListScrollPane to the container
    }    

    /**
     * Select a cell.
     * 
     * @param tabCell
     *            A display cell.
     */
    private void selectCell(final TabCell tabCell) {
        jList.setSelectedValue(tabCell, true);
    }
    
    /**
     * Trigger a popup when clicking in a blank area.
     * 
     */
    private void triggerPopup(final Component invoker, final int x, final int y) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        jPopupMenu.add(popupItemFactory.createPopupItem(ActionId.CONTACT_CREATE_INCOMING_INVITATION, new Data(0)));
        jPopupMenu.show(invoker, x, y);
    }   
}
