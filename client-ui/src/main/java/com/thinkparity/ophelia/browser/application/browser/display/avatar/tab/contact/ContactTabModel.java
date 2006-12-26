/*
 * Created On: Jun 21, 2006 11:32:04 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.sort.DefaultComparator;
import com.thinkparity.codebase.sort.StringComparator;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.application.browser.BrowserSession;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarSortBy;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarSortByDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarSortBy.SortDirection;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.contact.ContactProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ContactTabPanel;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;
import com.thinkparity.ophelia.browser.util.localization.JPanelLocalization;
import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;

/**
 * <b>Title:</b>thinkParity Contact Tab Model<br>
 * <b>Description:</b>The tab model for a contact<br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContactTabModel extends TabPanelModel implements
        TabAvatarSortByDelegate {

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

    /** A <code>JPanelLocalization</code>. */
    private JPanelLocalization localization;

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
    private final List<SortBy> sortedBy;

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
        this.sortedBy = new ArrayList<SortBy>();
        this.visiblePanels = new ArrayList<TabPanel>();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarSortByDelegate#getSortBy()
     *
     */
    public List<TabAvatarSortBy> getSortBy() {
        final List<TabAvatarSortBy> sortBy = new ArrayList<TabAvatarSortBy>();
        for (final SortBy sortByValue : SortBy.values()) {
            sortBy.add(new TabAvatarSortBy() {
                public Action getAction() {
                    return new AbstractAction() {
                        public void actionPerformed(final ActionEvent e) {
                            applySort(sortByValue);
                        }
                    };
                }
                public String getText() {
                    return getString(sortByValue);
                }
                public SortDirection getDirection() {
                    return getSortDirection(sortByValue);
                }
            });
        }
        return sortBy;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#toggleExpansion(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     *
     */
    public void toggleExpansion(final TabPanel tabPanel) {
        doToggleExpansion(tabPanel);
        synchronize();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#applySearch(java.lang.String)
     *
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
    protected void debug() {
        logger.logDebug("{0} container panels.", panels.size());
        logger.logDebug("{0} filtered panels.", filteredPanels.size());
        logger.logDebug("{0} visible panels.", visiblePanels.size());
        logger.logDebug("{0} model elements.", listModel.size());
        final TabPanel[] listModelPanels = new TabPanel[listModel.size()];
        listModel.copyInto(listModelPanels);
        for (final TabPanel listModelPanel : listModelPanels) {
            logger.logVariable("listModelPanel.getId()", listModelPanel.getId());
        }
        logger.logDebug("Search expression:  {0}", searchExpression);
        logger.logDebug("{0} search result hits.", searchResults.size());
    }

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
        applySort(SortBy.NAME);
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
        for (final TabPanel filteredPanel : filteredPanels) {
            visiblePanels.add(filteredPanel);
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
     * Set localization.
     *
     * @param localization
     *		A JPanelLocalization.
     */
    void setLocalization(final JPanelLocalization localization) {
        this.localization = localization;
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

    /**
     * Add a contact to the end of the panels list.
     * 
     * @param contact
     *            A <code>Contact</code>.
     */
    private void addPanel(final Contact contact) {
        addPanel(panels.size() == 0 ? 0 : panels.size(), contact);
    }

    /**
     * Add an incoming invitation to the end of the panels list.
     * 
     * @param invitation
     *            An <code>IncomingInvitation</code>.
     */
    private void addPanel(final IncomingInvitation invitation) {
        addPanel(panels.size() == 0 ? 0 : panels.size(), invitation);
    }

    /**
     * Add a contact to the panels list.
     * 
     * @param index
     *            The index at which to add the contact.
     * @param contact
     *            A <code>Contact</code>.
     */
    private void addPanel(final int index, final Contact contact) {
        panels.add(index, toDisplay(contact));
    }

    /**
     * Add an incoming invitation to the panels list.
     * 
     * @param index
     *            The index at which to add the invitation.
     * @param invitation
     *            An <code>IncomingInvitation</code>.
     */
    private void addPanel(final int index, final IncomingInvitation invitation) {
        panels.add(index, toDisplay(invitation));
    }

    /**
     * Add an outgoing invitation to the panels list.
     * 
     * @param index
     *            The index at which to add the invitation.
     * @param invitation
     *            An <code>OutgoingInvitation</code>.
     */
    private void addPanel(final int index, final OutgoingInvitation invitation) {
        panels.add(index, toDisplay(invitation));
    }
    
    /**
     * Add an outgoing invitation to the end of the panels list.
     * 
     * @param index
     *            The index at which to add the invitation.
     * @param invitation
     *            An <code>IncomingInvitation</code>.
     */
    private void addPanel(final OutgoingInvitation invitation) {
        addPanel(panels.size() == 0 ? 0 : panels.size(), invitation);
    }

    /**
     * Apply a series of filters on the panels.
     * 
     */
    private void applyFilters() {
        filteredPanels.clear();
        if (isSearchApplied()) {
            TabPanel searchResultPanel;
            for (final JabberId searchResult : searchResults) {
                searchResultPanel = lookupPanel(searchResult);
                if (!filteredPanels.contains(searchResultPanel))
                    filteredPanels.add(searchResultPanel);
            }
        } else {
            // no filter is applied
            filteredPanels.addAll(panels);
        }
    }

    /**
     * Apply the search results.
     *
     */
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
        for (final SortBy sortBy : sortedBy) {
            comparator.add(sortBy);
        }
        Collections.sort(filteredPanels, comparator);
    }

    /**
     * Apply an ordering to the panels.
     * 
     * @param ordering
     *            An <code>Ordering</code>.
     */
    private void applySort(final SortBy sortBy) {
        debug();
        // if the sorted by stack already contains the ordering do nothing
        if (isSortApplied(sortBy)) {
            if (sortBy.ascending) {
                sortBy.ascending = false;

                sortedBy.clear();
                sortedBy.add(sortBy);
            } else {
                sortedBy.clear();
            }
            synchronize();
        } else {
            sortBy.ascending = true;

            sortedBy.clear();
            sortedBy.add(sortBy);
            synchronize();
        }
    }

    /**
     * Clear all panels.
     *
     */
    private void clearPanels() {
        panels.clear();
    }

    /**
     * Toggle the expansion of a single panel.
     * 
     * @param tabPanel
     *            A <code>TabPanel</code>.
     */
    private void doToggleExpansion(final TabPanel tabPanel) {
        final ContactTabPanel contactTabPanel = (ContactTabPanel) tabPanel;
        if (isExpanded(contactTabPanel)) {
            // if the panel is already expanded; just collapse it
            contactTabPanel.collapse();
            expandedState.put(contactTabPanel, Boolean.FALSE);
        } else {
            // find the first expanded panel and collapse it
            boolean didHit = false;
            for (final TabPanel visiblePanel : visiblePanels) {
                final ContactTabPanel otherTabPanel = (ContactTabPanel) visiblePanel;
                if (isExpanded(otherTabPanel)) {
                    otherTabPanel.addPropertyChangeListener("expanded",
                            new PropertyChangeListener() {
                                public void propertyChange(
                                        final PropertyChangeEvent evt) {
                                    otherTabPanel.removePropertyChangeListener("expanded", this);

                                    contactTabPanel.expand();
                                    expandedState.put(contactTabPanel, Boolean.TRUE);
                                }
                            });
                    otherTabPanel.collapse();
                    expandedState.put(otherTabPanel, Boolean.FALSE);
                    didHit = true;
                    break;
                }
            }
            if (!didHit) {
                contactTabPanel.expand();
                expandedState.put(contactTabPanel, Boolean.TRUE);
            }
        }
    }
    
    /**
     * Get the sort direction.
     * 
     * @param sortBy
     *            A <code>SortBy</code>.
     * @return A <code>SortDirection</code>.        
     */
    public SortDirection getSortDirection(final SortBy sortBy) {
        if (isSortApplied(sortBy)) {
            if (sortBy.ascending) {
                return SortDirection.ASCENDING;
            } else {
                return SortDirection.DESCENDING;
            }
        } else {
            return SortDirection.NONE;
        }
    }

    /**
     * Obtain a localized string for an ordering.
     * 
     * @param sortBy
     *            A <code>SortBy</code>.
     * @return A localized <code>String</code>.
     */
    private String getString(final SortBy sortBy) {
        return localization.getString(sortBy);
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

    /**
     * Determine if search is applied.
     * 
     * @return True if a search expression is set.
     */
    private boolean isSearchApplied() {
        return null != searchExpression;
    }

    /**
     * Determine if an ordering is applied.
     * 
     * @param ordering
     *            An <code>Ordering</code>.
     * @return True if it is applied false otherwise.
     */
    private boolean isSortApplied(final SortBy sortBy) {
        debug();
        return sortedBy.contains(sortBy);
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

    private enum SortBy implements Comparator<TabPanel> {
    
        NAME(true), ORGANIZATION(true), TITLE(true);

        /** An ascending <code>StringComparator</code>. */
        private static final StringComparator STRING_COMPARATOR_ASC;

        /** A descending <code>StringComparator</code>. */
        private static final StringComparator STRING_COMPARATOR_DESC;

        static {
            STRING_COMPARATOR_ASC = new StringComparator(Boolean.TRUE);
            STRING_COMPARATOR_DESC = new StringComparator(Boolean.FALSE);
        }
        
        /** Whether or not to sort in ascending order. */
        private boolean ascending;

        /**
         * Create Ordering.
         *
         * @param ascending
         */
        private SortBy(final boolean ascending) {
            this.ascending = ascending;
        }

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         * 
         */
        public int compare(final TabPanel o1, final TabPanel o2) {
            final ContactTabPanel p1 = (ContactTabPanel) o1;
            final ContactTabPanel p2 = (ContactTabPanel) o2;
            if (p1.isSetContact()) {
                if (p2.isSetContact()) {
                    switch (this) {
                    case NAME:
                        // note the lack of multiplier
                        return ascending
                            ? STRING_COMPARATOR_ASC.compare(
                                    p1.getContact().getName(),
                                    p2.getContact().getName())
                            : STRING_COMPARATOR_DESC.compare(
                                    p1.getContact().getName(),
                                    p2.getContact().getName());
                    case TITLE:
                        // note the lack of multiplier
                        return ascending
                            ? STRING_COMPARATOR_ASC.compare(
                                    p1.getContact().getTitle(),
                                    p2.getContact().getTitle())
                                : STRING_COMPARATOR_DESC.compare(
                                        p1.getContact().getTitle(),
                                        p2.getContact().getTitle());
                    case ORGANIZATION:
                        // note the lack of multiplier
                        return ascending
                            ? STRING_COMPARATOR_ASC.compare(
                                    p1.getContact().getOrganization(),
                                    p2.getContact().getOrganization())
                                : STRING_COMPARATOR_DESC.compare(
                                        p1.getContact().getOrganization(),
                                        p2.getContact().getOrganization());
                    default:
                        throw Assert.createUnreachable("Unknown ordering.");
                    }
                } else {
                    return 1;
                }
            } else {
                if (p2.isSetContact()) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
    }
}
