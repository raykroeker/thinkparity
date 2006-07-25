/**
 * Created On: 20-Jun-2006 12:05:08 PM
 * $Id$
 */
package com.thinkparity.browser.application.browser.display.avatar;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.browser.application.browser.display.avatar.contact.CellContact;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCell;
import com.thinkparity.browser.application.browser.display.avatar.main.MainCellRenderer;
import com.thinkparity.browser.application.browser.display.provider.ContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatSingleContentProvider;
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
public class BrowserContactsAvatar extends Avatar {

    /** The relative location of the "hot" zone in each cell. */
    //private static final Point CELL_NODE_LOCATION = new Point(10, 2);

    /** The size of the "hot" zone in each cell. */
    //private static final Dimension CELL_NODE_SIZE = new Dimension(20, 20);

    /** A logger error statement. */
	//private static final String ERROR_INIT_TMLX =
    //    "[BROWSER2] [APP] [B2] [MAIN LIST] [INIT] [TOO MANY DROP TARGET LISTENERS]";

    /** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

    /** The model. */
    private final BrowserContactsModel contactsModel;
    
    /** The swing JList. */
	private JList jList;
    
    /**
     * The search filter.
     * 
     * @see #applySearchFilter(List)
     * @see #removeSearchFilter()
     */
    //private Search searchFilter;

    /** Create a BrowserContactsAvatar. */
    BrowserContactsAvatar() {
        super("BrowserContactsAvatar", ScrollPolicy.NONE, Color.WHITE);
        this.contactsModel = new BrowserContactsModel(getController());
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
        contactsModel.applySearchFilter(searchResult);
    }

    /** Clear all filters. */
    //public void clearFilters() { contactsModel.clearContactFilters(); }
    
    /** Debug the model. */
    //public void debug() { contactsModel.debug(); }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#getId()
     *
     */
    public AvatarId getId() { return AvatarId.BROWSER_CONTACTS; }
    
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
    //public Boolean isFilterEnabled() {
    //    return contactsModel.isContactListFiltered();
    //}

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
        contactsModel.removeSearchFilter();
    }

    /**
     * @see com.thinkparity.browser.platform.application.display.avatar.Avatar#setContentProvider(com.thinkparity.browser.application.browser.display.provider.ContentProvider)
     * 
     */
    public void setContentProvider(final ContentProvider contentProvider) {
        contactsModel.setContentProvider((FlatSingleContentProvider) contentProvider);
        // set initial selection
        if(0 < jList.getModel().getSize()) { jList.setSelectedIndex(0); }
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
        final CellContact selectedContact = getSelectedContact();
        contactsModel.syncContact(contactId, remote);
        if(contactsModel.isContactVisible(selectedContact))
            selectContact(selectedContact);
    }

    /**
     * Synchronize an invitation in the list.
     * 
     * @param invitationId
     *            A contact invitation id.
     * @param remote
     *            Whether or not the source of the syncrhonization is remote.
     */
    public void syncInvitation(final Long invitationId, final Boolean remote) {
        //Assert.assertNotYetImplemented("BrowserContactsAvatar#syncInvitation(Long,Boolean)");
    }

    /**
     * Synchronize the contacts in the list.
     *
     * @param contactIds
     *            The contact ids.
     * @param remote
     *            Indicates whether the sync is the result of a remove event.
     */
    //public void syncContacts(final List<JabberId> contactIds, final Boolean remote) {
    //    final cellContact selectedContact = getSelectedContact();
    //    contactsModel.syncContacts(contactIds, remote);
    //    if(contactsModel.isContactVisible(selectedContact))
    //        selectContact(selectedContact);
    //}

    /**
     * Obtain the selected contact from the list.
     * 
     * @return The selected contact.
     */
    private CellContact getSelectedContact() {
        final MainCell mc = (MainCell) jList.getSelectedValue();
        if(mc instanceof CellContact) {
            return (CellContact) mc;
        }
        return null;
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
        jList = new JList(contactsModel.getListModel());
        jList.setCellRenderer(new MainCellRenderer());
        jList.setDragEnabled(true);
        jList.setLayoutOrientation(JList.VERTICAL);
        jList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        /*
        jList.setTransferHandler(new UpdateContactTxHandler(getController(), jList));
        */
        jList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(final MouseEvent e) {
                if(2 == e.getClickCount()) {
                    final Point p = e.getPoint();
                    final Integer listIndex = jList.locationToIndex(p);
                    // Don't process double click if it is on white space below the last contact
                    final Rectangle cellBounds = jList.getCellBounds(listIndex, listIndex);
                    if(SwingUtil.regionContains(cellBounds, p)){
                        jList.setSelectedIndex(listIndex);                    
                        contactsModel.triggerDoubleClick((MainCell) jList.getSelectedValue());    
                    }
                }
                else if(1 == e.getClickCount()) {
                    // Do nothing in this event. There are no actions related to a
                    // single mouse click for contacts.
                }

            }
            public void mouseReleased(final MouseEvent e) {
                if(e.isPopupTrigger()) {
                    final Point p = e.getPoint();
                    final Integer listIndex = jList.locationToIndex(p);
                    jList.setSelectedIndex(listIndex);
                    contactsModel.triggerPopup((MainCell) jList.getSelectedValue(), jList, e, e.getX(), e.getY());
                }
            }
        });
        jList.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(final ListSelectionEvent e) {
                if(!e.getValueIsAdjusting()) {
                    contactsModel.triggerSelection((MainCell) jList.getSelectedValue());
                }
            }
        });
        /*
        try {
            jList.getDropTarget().addDropTargetListener(new DropTargetAdapter() {
                public void dragOver(final DropTargetDragEvent dtde) {
                    contactsModel.triggerDragOver((MainCell) jList.getSelectedValue(), dtde);
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
        add(jListScrollPane, c.clone());  // Add the jListScrollPane to the container
    }    

    /**
     * Select a contact cell.
     * 
     * @param mcd
     *            The contact cell.
     */
    private void selectContact(final CellContact cc) {
        jList.setSelectedValue(cc, true);
    }
}
