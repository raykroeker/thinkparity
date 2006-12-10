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
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel;
import com.thinkparity.ophelia.browser.application.browser.display.provider.CompositeFlatSingleContentProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell.TextGroup;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ContactCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ContactTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.DefaultInvitationCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.IncomingInvitationCell;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.OutgoingInvitationCell;

import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.contact.ContactProvider;
import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import java.util.Map;
import java.util.HashMap;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabListModel;
import java.util.Collections;
import com.thinkparity.codebase.sort.DefaultComparator;
import java.util.Comparator;


/**
 * @author rob_masako@shaw.ca; raymond@thinkparity.com
 * @version $Revision$
 */
public class ContactTabModel extends TabListModel {

    /** The <code>ContactTabActionDelegate</code>. */
    private final ContactTabActionDelegate actionDelegate;

    /** A list of filtered panels. */
    private final List<TabPanel> filteredPanels;

    /** The application. */
    private final Browser browser;

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

    /** A list of all visible cells. */
    private final List<TabPanel> visiblePanels;

    private TabPanel lookupPanel(final JabberId contactId) {
        final int panelIndex = lookupIndex(contactId);
        if (-1 == panelIndex)
            return null;
        else
            return panels.get(panelIndex);
    }

    private void applySort() {
        final DefaultComparator<TabPanel> comparator = new DefaultComparator<TabPanel>();
        for (final Ordering ordering : sortedBy) {
            comparator.add(ordering);
        }
        Collections.sort(filteredPanels, comparator);
    }

    private final List<Ordering> sortedBy;

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

    public void toggleExpansion(final TabPanel tabPanel) {
        doToggleExpansion(tabPanel);
        synchronize();
    }

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

    private void applySearch() {
        this.searchResults.clear();
        this.searchResults.addAll(readSearchResults());
    }

    /**
     * Debug the contact avatar.
     * 
     */
    @Override
    protected void debug() {
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
        final List<Contact> contacts = readContacts();
        for (final Contact contact : contacts) {
            addPanel(contact);
        }
        debug();
    }

    private Contact readContact(final JabberId contactId) {
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

    /** The contact model's expanded state map. */
    private final Map<TabPanel, Boolean> expandedState;

    private boolean isExpanded(final TabPanel tabPanel) {
        if (expandedState.containsKey(tabPanel)) {
            return expandedState.get(tabPanel).booleanValue();
        } else {
            // NOTE the default panel expanded state can be changed here
            expandedState.put(tabPanel, Boolean.FALSE);
            return isExpanded(tabPanel);
        }
    }

    private void addPanel(final Contact contact) {
        addPanel(panels.size() == 0 ? 0 : panels.size() - 1, contact);
    }

    private void addPanel(final int index, final Contact contact) {
        panels.add(index, toDisplay(contact));
    }

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

    private boolean isSearchApplied() {
        return null != searchExpression;
    }

    /**
     * Obtain the contact display cell for a contact.
     * 
     * @param contact
     *            A contact.
     * @return A contact display cell.
     */
    private TabPanel toDisplay(final Contact contact) {
        final ContactTabPanel tabPanel = new ContactTabPanel();
        tabPanel.setPanelData(contact);
        return tabPanel;
    }

    /**
     * Clear all panels.
     *
     */
    private void clearPanels() {
        panels.clear();
    }

    private enum Ordering implements Comparator<TabPanel> {
        NAME_ASC, NAME_DESC;
        public int compare(final TabPanel o1, final TabPanel o2) {
            return 0;
        }
    }

    private int lookupIndex(final JabberId contactId) {
        for (int i = 0; i < panels.size(); i++)
            if (((ContactTabPanel) panels.get(i)).getContact().getId().equals(contactId))
                return i;
        return -1;
    }
}
