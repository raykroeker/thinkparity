/**
 * Created On: Jun 21, 2006 11:32:04 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;

import javax.swing.DefaultListModel;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel;
import com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ContactCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.IncomingInvitationCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.InvitationCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.OutgoingInvitationCell;
import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;

/**
 * @author rob_masako@shaw.ca; raymond@thinkparity.com
 * @version $Revision$
 */
public class ContactModel extends TabModel {

    /** An apache logger. */
    protected final Logger logger;

    /** The application. */
    private final Browser browser;

    /** A list of all contacts. */
    private final List<ContactCell> contacts;

    /** A list of incoming invitations. */
    private final List<IncomingInvitationCell> incomingInvitations;

    /** The swing list model. */
    private final DefaultListModel jListModel;

    /** A list of outgoing invitations. */
    private final List<OutgoingInvitationCell> outgoingInvitations;

    /**
     * The user input search expression.
     * 
     * @see #applySearch(String)
     */
    private String searchExpression;

    /**
     * A list of contact ids matching the search criteria.
     * 
     * @see #applySearch(List)
     * @see #removeSearch()
     */
    private List<JabberId> searchResults;

    /** A list of all visible cells. */
    private final List<TabCell> visibleCells;

    /**
     * Create a BrowserContactsModel.
     * 
     */
    ContactModel() {
        super();
        this.browser = getBrowser();
        this.contacts = new ArrayList<ContactCell>(50);
        this.incomingInvitations = new ArrayList<IncomingInvitationCell>(50);
        this.jListModel = new DefaultListModel();
        this.logger = browser.getPlatform().getLogger(getClass());
        this.outgoingInvitations = new ArrayList<OutgoingInvitationCell>(50);
        this.visibleCells = new ArrayList<TabCell>();
    }

    /**
     * Apply the user's search to the contact list.
     * 
     * @param searchExpression
     *            A search expression <code>String</code>.
     * 
     * @see #searchExpression
     * @see #searchResults
     * @see #removeSearch()
     */
    @Override
    protected void applySearch(final String searchExpression) {
        // if the parameter search expression matches the member search
        // expression there is no need to search
        if (searchExpression.equals(this.searchExpression)) {
            return;
        } else {
            this.searchExpression = searchExpression;
            this.searchResults = readSearchResults();
            synchronize();
        }
    }

    /**
     * Debug the contact avatar.
     * 
     */
    @Override
    protected void debug() {
        if (logger.isDebugEnabled()) {
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
                        + cc.getText(TextGroup.MAIN_TEXT) + "]");
            }

            // visible cells
            logger
                    .debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL] [VISIBLE CELLS ("
                            + visibleCells.size() + ")]");
            for (final TabCell mc : visibleCells) {
                logger.debug("[BROWSER2] [APP] [B2] [CONTACTS MODEL]\t["
                        + mc.getText(TextGroup.MAIN_TEXT) + "]");
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
                        + mc.getText(TextGroup.MAIN_TEXT) + "]");
            }
        }
    }

    /**
     * Obtain the swing list model.
     * 
     * @return The swing list model.
     */
    @Override
    protected DefaultListModel getListModel() {
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
     * Remove the search.
     * 
     * @see #searchExpression
     * @see #searchResults
     * @see #applySearch(String)
     */
    @Override
    protected void removeSearch() {
        // if the member search expression is already null; then there is no
        // search applied -> do nothing
        if (null == searchExpression) {
            return;
        } else {
            searchExpression = null;
            searchResults = null;
            synchronize();
        }
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
        synchronize();
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
        synchronize();
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
        synchronize();
    }

    /**
     * Trigger a double click event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    @Override
    protected void triggerDoubleClick(final TabCell tabCell) {
        debug();
        tabCell.triggerDoubleClickAction(browser);
    }

    /**
     * Trigger a popup event for the cell.
     * 
     * @param mainCell
     *            The main cell.
     */
    protected void triggerPopup(final TabCell tabCell, final Component invoker,
            final java.awt.event.MouseEvent e) {
        tabCell.triggerPopup(browser.getConnection(), invoker, e);
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
    @Override
    protected void initialize() {
        contacts.clear();
        contacts.addAll(readContacts());
        incomingInvitations.clear();
        incomingInvitations.addAll(readIncomingInvitations());
        outgoingInvitations.clear();
        outgoingInvitations.addAll(readOutgoingInvitations());
        synchronize();
    }

    /**
     * Read a contact from the provider.
     * 
     * @param contactId
     *            The contact id.
     * @return The contact.
     */
    private ContactCell readContact(final JabberId contactId) {
        final Contact contact = (Contact) ((CompositeFlatSingleContentProvider) contentProvider).getElement(0, contactId);
        return null == contact ? null : toDisplay(contact);
    }

    /**
     * Read the contacts from the provider.
     * 
     * @return The contacts.
     */
    private List<ContactCell> readContacts() {
        final List<ContactCell> list = new LinkedList<ContactCell>();
        final Contact[] array = (Contact[]) ((CompositeFlatSingleContentProvider) contentProvider).getElements(0, null);
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
        return toDisplay((IncomingInvitation) ((CompositeFlatSingleContentProvider) contentProvider).getElement(1,
                invitationId));
    }

    /**
     * Read the incoming invitations from the provider.
     * 
     * @return The contacts.
     */
    private List<IncomingInvitationCell> readIncomingInvitations() {
        final List<IncomingInvitationCell> list = new LinkedList<IncomingInvitationCell>();
        final IncomingInvitation[] array =
            (IncomingInvitation[]) ((CompositeFlatSingleContentProvider) contentProvider).getElements(1, null);
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
        return toDisplay((OutgoingInvitation) ((CompositeFlatSingleContentProvider) contentProvider).getElement(2,
                invitationId));
    }

    /**
     * Read the outgoing invitations from the provider.
     * 
     * @return A list of outgoing invitations.
     */
    private List<OutgoingInvitationCell> readOutgoingInvitations() {
        final List<OutgoingInvitationCell> list = new LinkedList<OutgoingInvitationCell>();
        final OutgoingInvitation[] array =
            (OutgoingInvitation[]) ((CompositeFlatSingleContentProvider) contentProvider).getElements(2, null);
        for (final OutgoingInvitation outgoingInvitation : array) {
            list.add(toDisplay(outgoingInvitation));
        }
        return list;
    }

    /**
     * Search for a list of contact jabber ids through the content provider.
     * 
     * @return A list of contact ids.
     */
    private List<JabberId> readSearchResults() {
        final List<JabberId> list = new LinkedList<JabberId>();
        final JabberId[] array = (JabberId[]) ((CompositeFlatSingleContentProvider) contentProvider).getElements(3, searchExpression);
        for (final JabberId contactId : array) {
            list.add(contactId);
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
        return (User) ((CompositeFlatSingleContentProvider) contentProvider).getElement(3, jabberId);
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
     * @see #synchronize()
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
     *            A contact invitation cell.
     * @param invitationId
     *            An invitation id.
     * @param invitation
     *            An invitation.
     * @param invitations
     *            A list of invitations.
     * @param remote
     *            Whether or not this was the result of a remote event.
     */
    private <T extends InvitationCell> void syncInvitationInternal(
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
     * Create a final list of contact cells and invitation cells. Apply the
     * search results to the list.
     * 
     */
    @Override
    protected void synchronize() {
        debug();

        // search filtered contacts
        final List<ContactCell> filteredContacts = cloneContacts();
        final List<OutgoingInvitationCell> filteredOutgoingInvitations = cloneInvitations(outgoingInvitations);
        final List<IncomingInvitationCell> filteredIncomingInvitations = cloneInvitations(incomingInvitations);
        if (null != searchExpression && null != searchResults) {
            FilterManager.filter(filteredContacts, new SearchFilter(searchResults));
        }

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
        final ContactCell contactCell = new ContactCell(contact);
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
        if (null == incoming) {
            return null;
        } else {
            final User invitedByUser = readUser(incoming.getInvitedBy());
            final IncomingInvitationCell incomingCell = new IncomingInvitationCell(incoming, invitedByUser);
            return incomingCell;
        }
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
            final OutgoingInvitationCell outgoingCell = new OutgoingInvitationCell(outgoing);
            return outgoingCell;
        }
    }

    /**
     * <b>Title:</b>thinkParity Contact Search Filter<br>
     * <b>Description:</b>Provides the capability to filter the contact cells
     * that do not match the search results.
     */
    private class SearchFilter implements Filter<ContactCell> {

        /** The search results. */
        private final List<JabberId> searchResults;

        /**
         * Create SearchFilter.
         * 
         * @param searchResults
         *            A <code>List&lt;JabberId&gt;</code>.
         */
        private SearchFilter(final List<JabberId> searchResults) {
            this.searchResults = searchResults;
        }

        /**
         * @see com.thinkparity.ophelia.model.util.filter.Filter#doFilter(java.lang.Object)
         */
        public Boolean doFilter(final ContactCell o) {
            for (final JabberId searchResult : searchResults) {
                if (searchResult.equals(o.getId()))
                    return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
    }
}
