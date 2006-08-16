/**
 * Created On: Jun 21, 2006 11:32:04 AM
 */
package com.thinkparity.browser.application.browser.display.avatar.tab.contact;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.ListModel;

import org.apache.log4j.Logger;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.contact.ContactCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.contact.IncomingInvitationCell;
import com.thinkparity.browser.application.browser.display.renderer.tab.contact.OutgoingInvitationCell;

import com.thinkparity.model.parity.model.contact.ContactInvitation;
import com.thinkparity.model.parity.model.contact.IncomingInvitation;
import com.thinkparity.model.parity.model.contact.OutgoingInvitation;
import com.thinkparity.model.parity.model.filter.Filter;
import com.thinkparity.model.parity.model.filter.FilterChain;
import com.thinkparity.model.parity.model.filter.UserFilterManager;
import com.thinkparity.model.parity.model.filter.user.SearchUser;
import com.thinkparity.model.parity.model.index.IndexHit;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author rob_masako@shaw.ca; raymond@thinkparity.com
 * @version $Revision$
 */
public class ContactAvatarModel {

    /**
     * Collection of all filters used by the contacts model. In the case of
     * contacts, there are no filters that apply, only search.
     * 
     */
    private static final Map<FilterKey, Filter<User>> CONTACT_FILTERS;

    static {
        CONTACT_FILTERS = new HashMap<FilterKey, Filter<User>>(FilterKey
                .values().length, 1.0F);
        CONTACT_FILTERS.put(FilterKey.SEARCH, new SearchUser(
                new LinkedList<IndexHit>()));
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
    private CompositeFlatSingleContentProvider contentProvider;

    /** A list of incoming invitations. */
    private final List<IncomingInvitationCell> incomingInvitations;

    /** The swing list model. */
    private final DefaultListModel jListModel;

    /** A list of outgoing invitations. */
    private final List<OutgoingInvitationCell> outgoingInvitations;

    /** A list of all visible cells. */
    private final List<TabCell> visibleCells;

    /**
     * Create a BrowserContactsModel.
     * 
     */
    ContactAvatarModel(final Browser browser) {
        super();
        this.browser = browser;
        this.contacts = new ArrayList<ContactCell>(50);
        this.contactFilter = new FilterChain<User>();
        this.incomingInvitations = new ArrayList<IncomingInvitationCell>(50);
        this.jListModel = new DefaultListModel();
        this.logger = browser.getPlatform().getLogger(getClass());
        this.outgoingInvitations = new ArrayList<OutgoingInvitationCell>(50);
        this.visibleCells = new ArrayList<TabCell>();
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
        final SearchUser search = (SearchUser) CONTACT_FILTERS
                .get(FilterKey.SEARCH);
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
            logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL] ["
                    + contacts.size() + " CONTACTS]");
            logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL] ["
                    + visibleCells.size() + " VISIBLE CELLS]");
            logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL] ["
                    + jListModel.size() + " MODEL ELEMENTS]");

            // contacts
            logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL] [CONTACTS ("
                    + contacts.size() + ")]");
            for (final ContactCell cc : contacts) {
                logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL]\t["
                        + cc.getText() + "]");
            }

            // visible cells
            logger
                    .debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL] [VISIBLE CELLS ("
                            + visibleCells.size() + ")]");
            for (final TabCell mc : visibleCells) {
                logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL]\t["
                        + mc.getText() + "]");
            }

            // list elements
            final Enumeration e = jListModel.elements();
            TabCell mc;
            logger
                    .debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL] [MODEL ELEMENTS ("
                            + jListModel.size() + ")]");
            while (e.hasMoreElements()) {
                mc = (TabCell) e.nextElement();
                logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL]\t["
                        + mc.getText() + "]");
            }

            contactFilter.debug(logger);
        }
    }

    /**
     * Obtain the swing list model.
     * 
     * @return The swing list model.
     */
    ListModel getListModel() {
        return jListModel;
    }

    /**
     * Determine if the cell is visible.
     * 
     * @param tablCell
     *            A display cell.
     * @return True if the contact is visible; false otherwise.
     */
    Boolean isCellVisible(final TabCell tabCell) {
        return visibleCells.contains(tabCell);
    }

    /**
     * Determine whether the contact list is currently filtered. (Search doesn't
     * count.)
     * 
     * @return True if the contact list is filtered; false otherwise.
     */
    Boolean isContactListFiltered() {
        return Boolean.FALSE;
    }

    /**
     * Remove the search filter.
     * 
     * @see #applySearchFilter(List)
     */
    void removeSearchFilter() {
        removeContactFilter(CONTACT_FILTERS.get(FilterKey.SEARCH));
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
            final CompositeFlatSingleContentProvider contentProvider) {
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
     * Synchronize an incoming invitation with the list.
     * 
     * @param invitationId
     *            An outgoing invitation id.
     * @param remote
     *            True if the sync is the result of a remote event.
     */
    void syncIncomingInvitation(final Long invitationId, final Boolean remote) {
        syncInvitationInternal(invitationId,
                readIncomingInvitation(invitationId), incomingInvitations,
                remote);
        syncModel();
    }

    /**
     * Synchronize an outgoing invitation with the list.
     * 
     * @param invitationId
     *            An outgoing invitation id.
     * @param remote
     *            True if the sync is the result of a remote event.
     */
    void syncOutgoingInvitation(final Long invitationId, final Boolean remote) {
        syncInvitationInternal(invitationId,
                readOutgoingInvitation(invitationId), outgoingInvitations,
                remote);
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
     * void syncContacts(final List<JabberId> contactIds, final Boolean remote) {
     * for(final JabberId contactId : contactIds) {
     * syncContactInternal(contactId, remote); } syncModel(); }
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
            browser.runReadContact(cc.getId()); // Jabber ID
        }
    }

    /**
     * Trigger a popup event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    void triggerPopup(final TabCell tabCell, final Component invoker,
            final MouseEvent e, final int x, final int y) {
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
        final List<ContactCell> clone = new ArrayList<ContactCell>();
        clone.addAll(contacts);
        return clone;
    }

    /**
     * Perform a shallow clone of a list of invitations.
     * 
     * @return A copy of the invitations list.
     */
    private <T extends TabCell> List<T> cloneInvitations(final List<T> invitations) {
        final List<T> clone = new ArrayList<T>();
        clone.addAll(invitations);
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
        contacts.clear();
        contacts.addAll(readContacts());
        incomingInvitations.clear();
        incomingInvitations.addAll(readIncomingInvitations());
        outgoingInvitations.clear();
        outgoingInvitations.addAll(readOutgoingInvitations());
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
        final Contact contact = (Contact) contentProvider.getElement(0,
                contactId);
        return null == contact ? null : toDisplay(contact);
    }

    /**
     * Read the contacts from the provider.
     * 
     * @return The contacts.
     */
    private List<ContactCell> readContacts() {
        final List<ContactCell> list = new LinkedList<ContactCell>();
        final Contact[] array = (Contact[]) contentProvider
                .getElements(0, null);
        for (final Contact contact : array) {
            list.add(toDisplay(contact));
        }
        return list;
    }

    /**
     * Read the incoming invitation from the provider.
     * 
     * @return An incoming invitation.
     */
    private IncomingInvitationCell readIncomingInvitation(
            final Long invitationId) {
        return toDisplay((IncomingInvitation) contentProvider.getElement(1,
                invitationId));
    }

    /**
     * Read the incoming invitations from the provider.
     * 
     * @return The contacts.
     */
    private List<IncomingInvitationCell> readIncomingInvitations() {
        final List<IncomingInvitationCell> list = new LinkedList<IncomingInvitationCell>();
        final IncomingInvitation[] array = (IncomingInvitation[]) contentProvider
                .getElements(1, null);
        for (final IncomingInvitation incomingInvitation : array) {
            list.add(toDisplay(incomingInvitation));
        }
        return list;
    }

    /**
     * Read the outgoing invitation from the provider.
     * 
     * @return An outgoing invitation.
     */
    private OutgoingInvitationCell readOutgoingInvitation(
            final Long invitationId) {
        return toDisplay((OutgoingInvitation) contentProvider.getElement(2,
                invitationId));
    }

    /**
     * Read the outgoing invitations from the provider.
     * 
     * @return A list of outgoing invitations.
     */
    private List<OutgoingInvitationCell> readOutgoingInvitations() {
        final List<OutgoingInvitationCell> list = new LinkedList<OutgoingInvitationCell>();
        final OutgoingInvitation[] array = (OutgoingInvitation[]) contentProvider
                .getElements(2, null);
        for (final OutgoingInvitation outgoingInvitation : array) {
            list.add(toDisplay(outgoingInvitation));
        }
        return list;
    }

    /**
     * Read a user from the content provider.
     * 
     * @param jabberId
     *            A jabber id.
     * @return A user.
     */
    private User readUser(final JabberId jabberId) {
        return (User) contentProvider.getElement(3, jabberId);
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
     * updated in the list. If it cannot be found, it will be removed from the
     * list.
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
            for (int i = 0; i < contacts.size(); i++) {
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
                if(remote) {
                    contacts.add(0, cellContact);
                }
                else {
                    contacts.add(index, cellContact);
                }
            }
        }
    }

    /**
     * Synchronize an invitation in an invitation list.
     * 
     * @param <T>
     *            A contact invitation.
     * @param invitationId
     *            An invitation id.
     * @param invitation
     *            An invitation.
     * @param invitations
     *            A list of invitations.
     * @param remote
     *            Whether or not this was the result of a remote event.
     */
    private <T extends ContactInvitation> void syncInvitationInternal(
            final Long invitationId, final T invitation,
            final List<T> invitations, final Boolean remote) {
        // if the invitation is null; we can assume it has been
        // deleted (it's not longer being created by the provider); so we find
        // it and remove it
        if (null == invitation) {
            for (int i = 0; i < invitations.size(); i++) {
                if (invitations.get(i).getId().equals(invitationId)) {
                    invitations.remove(i);
                    break;
                }
            }
        }
        // the invitation is not null; therefore it is either new; or updated
        else {
            // the invitation is new
            if (!invitations.contains(invitation)) {
                invitations.add(0, invitation);
            }
            // the invitation has been updated
            else {
                final int index = invitations.indexOf(invitation);
                invitations.remove(index);

                // if the reload is the result of a remote event add the
                // invitation at the top of the list; otherwise add it in the
                // same location it previously existed
                if(remote) { invitations.add(0, invitation); }
                else { invitations.add(index, invitation); }
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
        final List<OutgoingInvitationCell> filteredOutgoingInvitations = cloneInvitations(outgoingInvitations);
        final List<IncomingInvitationCell> filteredIncomingInvitations = cloneInvitations(incomingInvitations);
        UserFilterManager.filter(filteredContacts, contactFilter);
        // TODO Filter the invitations

        // update all visible cells
        visibleCells.clear();
        for (final IncomingInvitationCell incomingInvitationCell : filteredIncomingInvitations) {
            visibleCells.add(incomingInvitationCell);
        }
        for (final OutgoingInvitationCell outgoingInvitationCell : filteredOutgoingInvitations) {
            visibleCells.add(outgoingInvitationCell);
        }
        for (final ContactCell contactCell : filteredContacts) {
            visibleCells.add(contactCell);
        }

        // add visible cells not in the model; as well as update cell locations
        for (final TabCell tabCell : visibleCells) {
            if(!jListModel.contains(tabCell)) {
                jListModel.add(visibleCells.indexOf(tabCell), tabCell);
            }
            else {
                if(jListModel.indexOf(tabCell) != visibleCells.indexOf(tabCell)) {
                    jListModel.removeElement(tabCell);
                    jListModel.add(visibleCells.indexOf(tabCell), tabCell);
                }
            }
        }

        // prune cells
        final TabCell[] tabCells = new TabCell[jListModel.size()];
        jListModel.copyInto(tabCells);
        for (final TabCell tabCell : tabCells) {
            if(!visibleCells.contains(tabCell)) {
                jListModel.removeElement(tabCell);
            }
        }
        debug();
    }

    /**
     * Obtain the contact display cell for a contact.
     * 
     * @param contact
     *            A contact.
     * @return A contact display cell.
     */
    private ContactCell toDisplay(final Contact contact) {
        final ContactCell contactCell = new ContactCell();
        contactCell.setId(contact.getId());
        contactCell.setLocalId(contact.getLocalId());
        contactCell.setName(contact.getName());
        contactCell.setOrganization(contact.getOrganization());
        contactCell.setVCard(contact.getVCard());
        contactCell.addAllEmails(contact.getEmails());
        return contactCell;
    }

    /**
     * Obtain the incoming invitation display cell for an incoming invitation.
     * 
     * @param incoming
     *            An incoming invitation.
     * @return An incoming invitation display cell.
     */
    private IncomingInvitationCell toDisplay(final IncomingInvitation incoming) {
        final IncomingInvitationCell incomingCell = new IncomingInvitationCell();
        incomingCell.setCreatedBy(incoming.getCreatedBy());
        incomingCell.setCreatedOn(incoming.getCreatedOn());
        incomingCell.setId(incoming.getId());
        incomingCell.setInvitedBy(readUser(incoming.getUserId()));
        return incomingCell;
    }

    /**
     * Obtain the outgoing invitation display cell for an outgoing invitation.
     * 
     * @param incoming
     *            An incoming invitation.
     * @return An incoming invitation display cell.
     */
    private OutgoingInvitationCell toDisplay(final OutgoingInvitation outgoing) {
        if(null == outgoing) {
            return null;
        }
        else {
            final OutgoingInvitationCell outgoingCell = new OutgoingInvitationCell();
            outgoingCell.setCreatedBy(outgoing.getCreatedBy());
            outgoingCell.setCreatedOn(outgoing.getCreatedOn());
            outgoingCell.setEmail(outgoing.getEmail());
            outgoingCell.setId(outgoing.getId());
            return outgoingCell;
        }
    }

    /** A key to locate filters. */
    private enum FilterKey {
        SEARCH
    }
}
