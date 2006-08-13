/**
 * Created On: 21-Jun-2006 11:32:04 AM
 */
package com.thinkparity.browser.application.browser.display.avatar.tab.contact;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.display.provider.FlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.contact.ContactCell;

import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.filter.FilterChain;
import com.thinkparity.model.parity.model.filter.UserFilterManager;
import com.thinkparity.model.parity.model.filter.user.SearchUser;
import com.thinkparity.model.parity.model.index.IndexHit;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author rob_masako@shaw.ca
 * @version $Revision$
 */
public class ContactAvatarModel {

    /**
     * Collection of all filters used by the contacts model.
     * In the case of contacts, there are no filters that apply, only search.
     * 
     */
    private static final Map<Enum<?>, Filter<User>> CONTACT_FILTERS;

    static {
        CONTACT_FILTERS = new HashMap<Enum<?>, Filter<User>>(2, 0.75F);
        CONTACT_FILTERS.put(ContactFilterKey.SEARCH, new SearchUser(new LinkedList<IndexHit>()));
    }

    /** An apache logger. */
    protected final Logger logger;

    /** The application. */
    private final Browser browser;

    /** The filter that is used to filter contacts to produce visibleContacts. */
    private final FilterChain<User> contactFilter;

    /** A list of all contacts. */
    private final List<ContactCell> contacts;

    /** The content provider. */
    private FlatSingleContentProvider contentProvider;

    /** The swing list model. */
    private final DefaultListModel jListModel;

    /** A list of all visible cells. */
    private final List<TabCell> visibleCells;
    
    /**
     * Create a BrowserContactsModel.
     * 
     */
    ContactAvatarModel(final Browser browser) {
        super();
        this.browser = browser;
        this.contacts = new LinkedList<ContactCell>();       
        this.contactFilter = new FilterChain<User>();
        this.jListModel = new DefaultListModel();
        this.logger = browser.getPlatform().getLogger(getClass());
        this.visibleCells = new LinkedList<TabCell>();
    }

    /**
     * Apply a search filter to the list of visible contacts.
     * 
     * @param searchResult
     *            The search result to filter by.
     * 
     * @see #removeSearchFilter()
     */
    void applySearchFilter(final List<IndexHit> searchResult) {
        final SearchUser search = (SearchUser) CONTACT_FILTERS.get(ContactFilterKey.SEARCH);
        search.setResults(searchResult);

        applyContactFilter(search);
        syncModel();
    }

    /**
     * Debug the contact filter.
     *
     */
    void debug() {
        if(browser.getPlatform().isDevelopmentMode()) {
            logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL] [" + contacts.size() + " CONTACTS]");
            logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL] [" + visibleCells.size() + " VISIBLE CELLS]");
            logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL] [" + jListModel.size() + " MODEL ELEMENTS]");

            // contacts
            logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL] [CONTACTS (" + contacts.size() + ")]");
            for(final ContactCell cc : contacts) {
                logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL]\t[" + cc.getText() + "]");
            }

            // visible cells
            logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL] [VISIBLE CELLS (" + visibleCells.size() + ")]");
            for(final TabCell mc : visibleCells) {
                logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL]\t[" + mc.getText() + "]");
            }
                       
            // list elements
            final Enumeration e = jListModel.elements();
            TabCell mc;
            logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL] [MODEL ELEMENTS (" + jListModel.size() + ")]");
            while(e.hasMoreElements()) {
                mc = (TabCell) e.nextElement();
                logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL]\t[" + mc.getText() + "]");
            }

            contactFilter.debug(logger);
        }
    }

    /**
     * Obtain the swing list model.
     * 
     * @return The swing list model.
     */
    ListModel getListModel() { return jListModel; }

    /**
     * Determine whether the contact list is currently filtered. (Search doesn't count.)
     * 
     * @return True if the contact list is filtered; false otherwise.
     */
    Boolean isContactListFiltered() {
        return Boolean.FALSE;
    }
    
    /**
     * Determine if the contact is visible.
     * 
     * @param displayContact
     *            The display contact.
     * @return True if the contact is visible; false otherwise.
     */
    Boolean isContactVisible(final ContactCell cellContact) {
        return visibleCells.contains(cellContact);
    }

    /**
     * Remove the search filter.
     * 
     * @see #applySearchFilter(List)
     */
    void removeSearchFilter() {
        removeContactFilter(CONTACT_FILTERS.get(ContactFilterKey.SEARCH));
        syncModel();
    }

    /**
     * Set the content provider. This will initialize the model with contacts
     * via the provider.
     * 
     * @param contentProvider
     *            The content provider.
     */
    void setContentProvider(
            final FlatSingleContentProvider contentProvider) {
        this.contentProvider = contentProvider;
        initModel();
    }

    /**
     * Synchronize the contact with the list. The content provider is queried
     * for the contact and if it can be obtained; it will either be added to or
     * updated in the list. If it cannot be found; it will be removed from the
     * list.
     * 
     * @param contactId
     *            The contact id.
     * @param remote
     *            Whether or not the reload is the result of a remote event or
     *            not.
     */
    void syncContact(final JabberId contactId, final Boolean remote) {
        syncContactInternal(contactId, remote);
        syncModel();
    }

    /**
     * Synchronize the contacts with the list. The content provider is queried
     * for the contact and if it can be obtained; it will either be added to or
     * updated in the list. If it cannot be found; it will be removed from the
     * list.
     * 
     * @param contactIds
     *            The contact ids.
     * @param remote
     *            Whether or not the reload is the result of a remote event or
     *            not.
     * @see #syncContactInternal(JabberId, Boolean)
     * @see #syncModel()
     */
    /*
    void syncContacts(final List<JabberId> contactIds, final Boolean remote) {
        for(final JabberId contactId : contactIds) {
            syncContactInternal(contactId, remote);
        }
        syncModel();
    }
    */

    /**
     * Trigger a double click event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    void triggerDoubleClick(final TabCell mainCell) {
        debug();
        if(browser.getPlatform().isDevelopmentMode()) {
            logger.debug("Opening contact " + mainCell.getText());
        } 
        if(mainCell instanceof ContactCell) {
            final ContactCell cc = (ContactCell) mainCell;
            browser.runReadContact(cc.getId());  // Jabber ID
        }
    }

    /**
     * Trigger a popup event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    void triggerPopup(final TabCell tabCell, final Component invoker, final MouseEvent e,
            final int x, final int y) {
        tabCell.triggerPopup(browser.getConnection(), invoker, e, x, y);
    }

    /**
     * Trigger a selection event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    void triggerSelection(final TabCell mainCell) {
        if(mainCell instanceof ContactCell) {
            final ContactCell cc = (ContactCell) mainCell;
            browser.selectContact(cc.getId());
        }
    }

    /**
     * Apply the specified filter.
     * 
     * @param filter
     *            The contact filter.
     */
    private void applyContactFilter(final Filter<User> filter) {
        if(!contactFilter.containsFilter(filter)) {
            contactFilter.addFilter(filter);
        }
    }

    /**
     * Perform a shallow clone of the contacts list.
     * 
     * @return A copy of the contacts list.
     */
    private List<ContactCell> cloneContacts() {
        final List<ContactCell> clone = new LinkedList<ContactCell>();
        clone.addAll(contacts);
        return clone;
    }

    /**
     * Initialize the contact model
     * <ol>
     * <li>Load the contacts from the provider.
     * <li>Synchronize the data with the model.
     * <ol>
     */
    private void initModel() {
        // Read the contacts from the provider into the list
        contacts.clear();
        contacts.addAll(readContacts());
        syncModel();
    }

    /**
     * Read a contact from the provider.
     * 
     * @param contactId
     *            The contact id.
     * @return The contact.
     */
    private ContactCell readContact(final JabberId contactId) {
        final Contact contact = (Contact) contentProvider.getElement(contactId);        
        if (contact!=null) {
            return new ContactCell(contact);
        }
        else {
            return null;
        }
    }

    /**
     * Read the contacts from the provider.
     * 
     * @return The contacts.
     */
    private List<ContactCell> readContacts() {
        final List<ContactCell> l = new LinkedList<ContactCell>();
        final Contact[] a = (Contact[]) contentProvider.getElements(null);
        for(final Contact c : a) {
            final ContactCell cc = new ContactCell(c);
            l.add(cc);
        }
        return l;
    }

    /**
     * Remove a contact filter.
     * 
     * @param filter
     *            The contact filter.
     */
    private void removeContactFilter(final Filter<User> filter) {
        if(contactFilter.containsFilter(filter)) {
            contactFilter.removeFilter(filter);
        }
    }
    
    /**
     * Synchronize the contact with the list. The content provider is queried
     * for the contact and if it can be obtained; it will either be added to or
     * updated in the list. If it cannot be found, it will be removed from the list.
     * 
     * @param contactId
     *            The contact id.
     * @param remote
     *            Whether or not the reload is the result of a remote event or
     *            not.
     * 
     * @see #syncContact(JabberId, Boolean)
     * @see #syncModel()
     */
    private void syncContactInternal(final JabberId contactId,
            final Boolean remote) {
        final ContactCell cellContact = readContact(contactId);

        // if the contact is null; we can assume the contact has been
        // deleted (it's not longer being created by the provider); so we find
        // the contact and remove it
        if(null == cellContact) {
            for(int i = 0; i < contacts.size(); i++) {
                if(contacts.get(i).getId().equals(contactId)) {
                    contacts.remove(i);
                    break;
                }
            }
        }
        // the contact is not null; therefore it is either new; or updated
        else {
            // the contact is new
            if(!contacts.contains(cellContact)) {
                contacts.add(0, cellContact);
            }
            // the contact has been updated
            else {
                final int index = contacts.indexOf(cellContact);
                contacts.remove(index);

                // if the reload is the result of a remote event add the contact
                // at the top of the list; otherwise add it in the same location
                // it previously existed
                if(remote) { contacts.add(0, cellContact); }
                else { contacts.add(index, cellContact); }
            }
        }
    }

    /**
     * Filter the list of contacts. Update the visible cell list with contacts.
     * Update the model with the visible cell list.
     * 
     */
    private void syncModel() {
        debug();
       
        // filter contacts
        final List<ContactCell> filteredContacts = cloneContacts();
        UserFilterManager.filter(filteredContacts, contactFilter);

        // update all visible cells
        visibleCells.clear();
        for(final ContactCell cc : filteredContacts) {
            visibleCells.add(cc);
        }

        // add visible cells not in the model; as well as update cell locations
        for(final TabCell mc : visibleCells) {
            if(!jListModel.contains(mc)) {
                jListModel.add(visibleCells.indexOf(mc), mc);
            }
            else {
                if(jListModel.indexOf(mc) != visibleCells.indexOf(mc)) {
                    jListModel.removeElement(mc);
                    jListModel.add(visibleCells.indexOf(mc), mc);
                }
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
     * Unique keys used in the {@link CONTACT_FILTERS} collection.
     * 
     */
    private enum ContactFilterKey {
        SEARCH, UNDEFINED
    }
}

