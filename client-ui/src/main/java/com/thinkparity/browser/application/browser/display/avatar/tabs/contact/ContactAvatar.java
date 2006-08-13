/**
 * Created On: 20-Jun-2006 12:05:08 PM
 */
package com.thinkparity.browser.application.browser.display.avatar.tab.contact;

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

import com.thinkparity.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.browser.application.browser.display.provider.ContentProvider;
import com.thinkparity.browser.application.browser.display.provider.FlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCellRenderer;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.contact.ContactCell;
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

    /** The model. */
    private final ContactAvatarModel model;
    
    /** The swing JList. */
	private JList jList;
    
    /** Create a BrowserContactsAvatar. */
    public ContactAvatar() {
        super("BrowserContactsAvatar", ScrollPolicy.NONE, Color.WHITE);
        this.model = new ContactAvatarModel(getController());
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
        model.setContentProvider((FlatSingleContentProvider) contentProvider);
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
        final ContactCell selectedContact = getSelectedContact();
        model.syncContact(contactId, remote);
        if(model.isContactVisible(selectedContact))
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
        throw Assert.createNotYetImplemented("ContactAvatar#syncInvitation");
    }

    /**
     * Obtain the selected contact from the list.
     * 
     * @return The selected contact.
     */
    private ContactCell getSelectedContact() {
        final TabCell mc = (TabCell) jList.getSelectedValue();
        if(mc instanceof ContactCell) {
            return (ContactCell) mc;
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
                    jList.setSelectedIndex(listIndex);
                    model.triggerPopup((TabCell) jList.getSelectedValue(), jList, e, e.getX(), e.getY());
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
    private void selectContact(final ContactCell cc) {
        jList.setSelectedValue(cc, true);
    }
}
