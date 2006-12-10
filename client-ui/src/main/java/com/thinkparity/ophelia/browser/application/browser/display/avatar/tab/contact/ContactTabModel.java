/*
 * Created On: Jun 21, 2006 11:32:04 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultListModel;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.sort.DefaultComparator;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.contact.ContactProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ContactTabPanel;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;

/**
 * <b>Title:</b>thinkParity Contact Tab Model<br>
 * <b>Description:</b>The tab model for a contact<br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContactTabModel extends TabPanelModel {

    /** The <code>ContactTabActionDelegate</code>. */
    private final ContactTabActionDelegate actionDelegate;

    /** The application. */
    private final Browser browser;

    /** The contact model's expanded state map. */
    private final Map<TabPanel, Boolean> expandedState;

    /** A list of filtered panels. */
    private final List<TabPanel> filteredPanels;

    /** The swing list model. */
    private final DefaultListModel listModel;

    /** A list of all contacts. */
    private final List<TabPanel> panels;

    /** The <code>ContactTabPopupDelegate</code>. */
    private final ContactTabPopupDelegate popupDelegate;

    /** A user search expression <code>String</code>. */
    private String searchExpression;

    /** A search result list of contact id <code>JabberId</code>. */
    private final List<JabberId> searchResults;

    /** A <code>BrowserSession</code>. */
    private BrowserSession session;

    /** A list of the current sort orderings. */
    private final List<Ordering> sortedBy;

    /** A list of all visible cells. */
    private final List<TabPanel> visiblePanels;

    /**
     * Create ContactTabModel.
     * 
     */
    ContactTabModel() {
        super();
        this.actionDelegate = new ContactTabActionDelegate(this);
        this.browser = getBrowser();
        this.expandedState = new HashMap<TabPanel, Boolean>();
        this.filteredPanels = new ArrayList<TabPanel>();
        this.listModel = new DefaultListModel();
        this.panels = new ArrayList<TabPanel>();
        this.popupDelegate= new ContactTabPopupDelegate(this);
        this.searchResults = new ArrayList<JabberId>();
        this.sortedBy = new ArrayList<Ordering>();
        this.visiblePanels = new ArrayList<TabPanel>();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#toggleExpansion(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     *
     */
    public void toggleExpansion(final TabPanel tabPanel) {
        logger.logTraceId();
        doToggleExpansion(tabPanel);
        logger.logTraceId();
        synchronize();
        logger.logTraceId();
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
        debug();
        if (searchExpression.equals(this.searchExpression)) {
            return;
        } else {
            this.searchExpression = searchExpression;
            applySearch();
            synchronize();
        }
    }

    /**
     * Debug the contact avatar.
     * 
     */
    @Override
    protected void debug() {}

    /**
     * Obtain the swing list model.
     * 
     * @return The swing list model.
     */
    @Override
    protected DefaultListModel getListModel() {
        return listModel;
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
        debug();
        clearPanels();
        final List<IncomingInvitation> incoming = readIncomingInvitations();
        for (final IncomingInvitation invitation : incoming) {
            addPanel(invitation);
        }
        final List<OutgoingInvitation> outgoing = readOutgoingInvitations();
        for (final OutgoingInvitation invitation : outgoing) {
            addPanel(invitation);
        }
        final List<Contact> contacts = readContacts();
        for (final Contact contact : contacts) {
            addPanel(contact);
        }
        debug();
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
        debug();
        if (null == searchExpression) {
            return;
        } else {
            searchExpression = null;
            searchResults.clear();
            synchronize();
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
        applyFilters();
        applySort();
        /* add the filtered panels the visibility list */
        visiblePanels.clear();
        for (final TabPanel panel : filteredPanels) {
            visiblePanels.add(panel);
        }
        // add newly visible panels to the model; and set other panels
        int listModelIndex;
        for (int i = 0; i < visiblePanels.size(); i++) {
            if (listModel.contains(visiblePanels.get(i))) {
                listModelIndex = listModel.indexOf(visiblePanels.get(i));
                /* the position of the panel in the model is identical to that
                 * of the panel the list */
                if (i == listModelIndex) {
                    listModel.set(i, visiblePanels.get(i));
                } else {
                    listModel.remove(listModelIndex);
                    listModel.add(i, visiblePanels.get(i));
                }
            } else {
                listModel.add(i, visiblePanels.get(i));
            }
        }
        // prune newly invisible panels from the model
        final TabPanel[] invisiblePanels = new TabPanel[listModel.size()];
        listModel.copyInto(invisiblePanels);
        for (int i = 0; i < invisiblePanels.length; i++) {
            if (!visiblePanels.contains(invisiblePanels[i])) {
                listModel.removeElement(invisiblePanels[i]);
            }
        }
        debug();
    }

    /**
     * Obtain the popup delegate.
     * 
     * @return A <code>ContainerTabPopupDelegate</code>.
     */
    ContactTabPopupDelegate getPopupDelegate() {
        return popupDelegate;
    }

    /**
     * Determine whether or not we are online.
     * 
     * @return True if we are online.
     */
    Boolean isOnline() {
        return browser.getConnection() == Connection.ONLINE;
    }

    /**
     * Set the session.
     * 
     * @param session
     *            A <code>BrowserSession</code>.
     */
    void setSession(final BrowserSession session) {
        this.session = session;
    }

    void syncContact(final JabberId contactId, final Boolean remote) {
        debug();
        final Contact contact = read(contactId);
        // remove the container from the panel list
        if (null == contact) {
            removePanel(contactId, true);
        } else {
            final int panelIndex = lookupIndex(contact.getId());
            if (-1 < panelIndex) {
                // if the reload is the result of a remote event add the container
                // at the top of the list; otherwise add it in the same location
                // it previously existed
                removePanel(contactId, false);
                if (remote) {
                    addPanel(0, contact);
                } else {
                    addPanel(panelIndex, contact);
                }
            } else {
                addPanel(0, contact);
            }
        }
        synchronize();
        debug();
    }

    void syncIncomingInvitation(final Long invitationId, final Boolean remote) {
        debug();
        final IncomingInvitation invitation = readIncomingInvitation(invitationId);
        // remove the container from the panel list
        if (null == invitation) {
            removePanel(invitationId, true);
        } else {
            final int panelIndex = lookupIndex(invitation.getId());
            if (-1 < panelIndex) {
                // if the reload is the result of a remote event add the container
                // at the top of the list; otherwise add it in the same location
                // it previously existed
                removePanel(invitationId, false);
                if (remote) {
                    addPanel(0, invitation);
                } else {
                    addPanel(panelIndex, invitation);
                }
            } else {
                addPanel(0, invitation);
            }
        }
        synchronize();
        debug();
    }

    void syncOutgoingInvitation(final Long invitationId, final Boolean remote) {
        debug();
        final OutgoingInvitation invitation = readOutgoingInvitation(invitationId);
        // remove the container from the panel list
        if (null == invitation) {
            removePanel(invitationId, true);
        } else {
            final int panelIndex = lookupIndex(invitation.getId());
            if (-1 < panelIndex) {
                // if the reload is the result of a remote event add the container
                // at the top of the list; otherwise add it in the same location
                // it previously existed
                removePanel(invitationId, false);
                if (remote) {
                    addPanel(0, invitation);
                } else {
                    addPanel(panelIndex, invitation);
                }
            } else {
                addPanel(0, invitation);
            }
        }
        synchronize();
        debug();

    }

    private void addPanel(final Contact contact) {
        addPanel(panels.size() == 0 ? 0 : panels.size() - 1, contact);
    }

    private void addPanel(final IncomingInvitation invitation) {
        addPanel(panels.size() == 0 ? 0 : panels.size() - 1, invitation);
    }

    private void addPanel(final int index, final Contact contact) {
        panels.add(index, toDisplay(contact));
    }

    private void addPanel(final int index, final IncomingInvitation invitation) {
        panels.add(index, toDisplay(invitation));
    }

    private void addPanel(final int index, final OutgoingInvitation invitation) {
        panels.add(index, toDisplay(invitation));
    }

    private void addPanel(final OutgoingInvitation invitation) {
        addPanel(panels.size() == 0 ? 0 : panels.size() - 1, invitation);
    }

    /**
     * Apply a series of filters on the panels.
     * 
     */
    private void applyFilters() {
        filteredPanels.clear();
        if (isSearchApplied()) {
            for (final JabberId searchResult : searchResults) {
                filteredPanels.add(lookupPanel(searchResult));
            }
        } else {
            // no filter is applied
            filteredPanels.addAll(panels);
        }
    }

    private void applySearch() {
        this.searchResults.clear();
        this.searchResults.addAll(readSearchResults());
    }

    
    /**
     * Apply the sort to the filtered list of panels.
     *
     */
    private void applySort() {
        final DefaultComparator<TabPanel> comparator = new DefaultComparator<TabPanel>();
        for (final Ordering ordering : sortedBy) {
            comparator.add(ordering);
        }
        Collections.sort(filteredPanels, comparator);
    }

    /**
     * Clear all panels.
     *
     */
    private void clearPanels() {
        panels.clear();
    }

    /**
     * Toggle a panel's expansion.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     */
    private void doToggleExpansion(final TabPanel tabPanel) {
        final ContactTabPanel contactTabPanel = (ContactTabPanel) tabPanel;
        final Boolean expanded;
        if (isExpanded(contactTabPanel)) {
            expanded = Boolean.FALSE;
        } else {
            // NOTE-BEGIN:multi-expand to allow multiple selection in the list; remove here
            for (final TabPanel visiblePanel : visiblePanels) {
                if (isExpanded(visiblePanel)) {
                    doToggleExpansion(visiblePanel);
                }
            }
            // NOTE-END:multi-expand

            expanded = Boolean.TRUE;
        }
        contactTabPanel.setExpanded(expanded);
        expandedState.put(tabPanel, expanded);
    }

    /**
     * Determine if a panel is expanded.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     * @return True if the panel is expanded; false otherwise.
     */
    private boolean isExpanded(final TabPanel tabPanel) {
        if (expandedState.containsKey(tabPanel)) {
            return expandedState.get(tabPanel).booleanValue();
        } else {
            // NOTE the default panel expanded state can be changed here
            expandedState.put(tabPanel, Boolean.FALSE);
            return isExpanded(tabPanel);
        }
    }

    private boolean isSearchApplied() {
        return null != searchExpression;
    }

    private int lookupIndex(final JabberId contactId) {
        Contact contact;
        for (int i = 0; i < panels.size(); i++) {
            contact = ((ContactTabPanel) panels.get(i)).getContact();
            if (null != contact && contact.getId().equals(contactId))
                return i;
        }
        return -1;
    }

    private int lookupIndex(final Long invitationId) {
        ContactTabPanel panel;
        for (int i = 0; i < panels.size(); i++) {
            panel = ((ContactTabPanel) panels.get(i));
            if (panel.isSetIncoming())
                if (panel.getIncoming().getId().equals(invitationId))
                    return i;
            if (panel.isSetOutgoing())
                if (panel.getOutgoing().getId().equals(invitationId))
                    return i;
        }
        return -1;
    }

    private TabPanel lookupPanel(final JabberId contactId) {
        final int panelIndex = lookupIndex(contactId);
        if (-1 == panelIndex)
            return null;
        else
            return panels.get(panelIndex);
    }

    private Contact read(final JabberId contactId) {
        return ((ContactProvider) contentProvider).readContact(contactId);
    }

    /**
     * Read the contacts from the provider.
     * 
     * @return The contacts.
     */
    private List<Contact> readContacts() {
        return ((ContactProvider) contentProvider).readContacts();
    }

    private IncomingInvitation readIncomingInvitation(final Long invitationId) {
        return ((ContactProvider) contentProvider).readIncomingInvitation(invitationId);
    }

    /**
     * Read the incoming invitations.
     * 
     * @return A <code>List</code> of <code>IncomingInvitation</code>.
     */
    private List<IncomingInvitation> readIncomingInvitations() {
        return ((ContactProvider) contentProvider).readIncomingInvitations();
    }

    private OutgoingInvitation readOutgoingInvitation(final Long invitationId) {
        return ((ContactProvider) contentProvider).readOutgoingInvitation(invitationId);
    }

    /**
     * Read the incoming invitations.
     * 
     * @return A <code>List</code> of <code>OutgoingInvitation</code>.
     */
    private List<OutgoingInvitation> readOutgoingInvitations() {
        return ((ContactProvider) contentProvider).readOutgoingInvitations();
    }

    /**
     * Search for a list of contact jabber ids through the content provider.
     * 
     * @return A list of contact ids.
     */
    private List<JabberId> readSearchResults() {
        return ((ContactProvider) contentProvider).search(searchExpression);
    }

    /**
     * Read a user from the content provider.
     * 
     * @param jabberId
     *            A jabber id.
     * @return A user.
     */
    private User readUser(final JabberId jabberId) {
        return (User) ((ContactProvider) contentProvider).readUser(jabberId);
    }

    private void removePanel(final JabberId contactId,
            final boolean removeExpandedState) {
        final int panelIndex = lookupIndex(contactId);
        if (removeExpandedState) {
            final TabPanel panel = panels.remove(panelIndex);
            expandedState.remove(panel);
        } else {
            panels.remove(panelIndex);
        }
    }

    private void removePanel(final Long invitationId,
            final boolean removeExpandedState) {
        final int panelIndex = lookupIndex(invitationId);
        if (removeExpandedState) {
            final TabPanel panel = panels.remove(panelIndex);
            expandedState.remove(panel);
        } else {
            panels.remove(panelIndex);
        }
    }

    /**
     * Obtain the contact display cell for a contact.
     * 
     * @param contact
     *            A contact.
     * @return A contact display cell.
     */
    private TabPanel toDisplay(final Contact contact) {
        final ContactTabPanel panel = new ContactTabPanel(session);
        panel.setActionDelegate(actionDelegate);
        panel.setPanelData(contact);
        panel.setPopupDelegate(popupDelegate);
        panel.setExpanded(isExpanded(panel));
        panel.setTabDelegate(this);
        return panel;
    }

    /**
     * Obtain the contact display cell for a contact.
     * 
     * @param invitation
     *            An <code>OutgoingInvitation</code>.
     * @return A contact display cell.
     */
    private TabPanel toDisplay(final IncomingInvitation invitation) {
        final ContactTabPanel panel = new ContactTabPanel(session);
        panel.setActionDelegate(actionDelegate);
        panel.setPanelData(invitation, readUser(invitation.getInvitedBy()));
        panel.setPopupDelegate(popupDelegate);
        panel.setExpanded(isExpanded(panel));
        panel.setTabDelegate(this);
        return panel;
    }

    /**
     * Obtain the contact display cell for a contact.
     * 
     * @param invitation
     *            An <code>OutgoingInvitation</code>.
     * @return A contact display cell.
     */
    private TabPanel toDisplay(final OutgoingInvitation invitation) {
        final ContactTabPanel panel = new ContactTabPanel(session);
        panel.setActionDelegate(actionDelegate);
        panel.setPanelData(invitation);
        panel.setPopupDelegate(popupDelegate);
        panel.setExpanded(isExpanded(panel));
        panel.setTabDelegate(this);
        return panel;
    }

    private enum Ordering implements Comparator<TabPanel> {
        NAME_ASC, NAME_DESC;
        public int compare(final TabPanel o1, final TabPanel o2) {
            return 0;
        }
    }
}
