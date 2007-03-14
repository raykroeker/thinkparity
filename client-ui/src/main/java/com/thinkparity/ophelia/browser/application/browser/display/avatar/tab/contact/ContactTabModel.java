/*
 * Created On: Jun 21, 2006 11:32:04 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractAction;
import javax.swing.Action;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.sort.DefaultComparator;
import com.thinkparity.codebase.sort.StringComparator;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarSortBy;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarSortByDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarSortBy.SortDirection;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.contact.ContactProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ContactTabPanel;
import com.thinkparity.ophelia.browser.platform.Platform.Connection;

/**
 * <b>Title:</b>thinkParity Contact Tab Model<br>
 * <b>Description:</b>The tab model for a contact<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContactTabModel extends TabPanelModel<ContactPanelId> implements
        TabAvatarSortByDelegate {

    /** The <code>ContactTabActionDelegate</code>. */
    private final ContactTabActionDelegate actionDelegate;    

    /** An array of available <code>Locale</code>s. */
    private final Locale[] availableLocales;

    /** The <code>Locale</code>. */
    private final Locale locale;

    /** The <code>ContactTabPopupDelegate</code>. */
    private final ContactTabPopupDelegate popupDelegate;

    /** A list of the current sort orderings. */
    private final List<SortBy> sortedBy;

    /**
     * Create ContactTabModel.
     * 
     */
    ContactTabModel() {
        super();
        this.actionDelegate = new ContactTabActionDelegate(this);
        this.popupDelegate= new ContactTabPopupDelegate(this);
        this.sortedBy = new ArrayList<SortBy>();
        this.availableLocales = browser.getAvailableLocales();
        this.locale = browser.getLocale();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarSortByDelegate#getSortBy()
     *
     */
    public List<TabAvatarSortBy> getSortBy() {
        checkThread();
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
                public SortDirection getDirection() {
                    return getSortDirection(sortByValue);
                }
                public String getText() {
                    return getString(sortByValue);
                }
            });
        }
        return sortBy;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#applyConnection(com.thinkparity.ophelia.browser.platform.Platform.Connection)
     */
    @Override
    protected void applyConnection(final Connection connection) {
        for (final TabPanel panel : panels) {
            final ContactTabPanel contactPanel = (ContactTabPanel) panel;
            if (contactPanel.isSetIncomingEMail() || contactPanel.isSetIncomingUser()) {
                contactPanel.applyConnection(connection);
            }
        }
    }

    /**
     * Apply the sort to the filtered list of panels.
     *
     */
    protected void applySort() {
        checkThread();
        final DefaultComparator<TabPanel> comparator = new DefaultComparator<TabPanel>();
        for (final SortBy sortBy : sortedBy) {
            comparator.add(sortBy);
        }
        Collections.sort(filteredPanels, comparator);
    }

    /**
     * Debug the contact avatar.
     * 
     */
    @Override
    protected void debug() {
        checkThread();
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
        final List<IncomingEMailInvitation> incomingEMailInvitations = readIncomingEMailInvitations();
        for (final IncomingEMailInvitation iei : incomingEMailInvitations) {
            addPanel(iei);
        }
        final List<IncomingUserInvitation> incomingUserInvitations = readIncomingUserInvitations();
        for (final IncomingUserInvitation iui : incomingUserInvitations) {
            addPanel(iui);
        }
        final List<OutgoingEMailInvitation> emailInvitations = readOutgoingEMailInvitations();
        for (final OutgoingEMailInvitation emailInvitation : emailInvitations) {
            addPanel(emailInvitation);
        }
        final List<OutgoingUserInvitation> userInvitations = readOutgoingUserInvitations();
        for (final OutgoingUserInvitation userInvitation : userInvitations) {
            addPanel(userInvitation);
        }
        final Profile profile = readProfile();
        addPanel(profile);
        final List<Contact> contacts = readContacts();
        for (final Contact contact : contacts) {
            addPanel(contact);
        }
        applySort(getInitialSort());
        debug();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#lookupId(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     */
    @Override
    protected ContactPanelId lookupId(final TabPanel tabPanel) {
        final ContactTabPanel panel = (ContactTabPanel) tabPanel;
        if (panel.isSetContact()) {
            return new ContactPanelId(panel.getContact().getId());
        } else if (panel.isSetProfile()) {
            return new ContactPanelId(panel.getProfile().getId());
        } else if (panel.isSetIncomingEMail()) {
            return new ContactPanelId(panel.getIncomingEMail().getId());
        } else if (panel.isSetIncomingUser()) {
            return new ContactPanelId(panel.getIncomingUser().getId());
        } else if (panel.isSetOutgoingEMail()) {
            return new ContactPanelId(panel.getOutgoingEMail().getId());
        } else if (panel.isSetOutgoingUser()) {
            return new ContactPanelId(panel.getOutgoingUser().getId());
        } else {
            throw Assert.createUnreachable("Unknown contact panel type.");
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#lookupPanel(java.lang.Object)
     */
    @Override
    protected TabPanel lookupPanel(final ContactPanelId panelId) {
        final int panelIndex;
        if (panelId.isSetContactId()) {
            panelIndex = lookupIndex(panelId.getContactId());
        } else if (panelId.isSetInvitationId()) {
            panelIndex = lookupIndex(panelId.getInvitationId());
        } else {
            throw Assert.createUnreachable("Unknown contact panel id type.");
        }
        return -1 == panelIndex ? null : panels.get(panelIndex);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#readSearchResults()
     *
     */
    @Override
    protected List<ContactPanelId> readSearchResults() {
        checkThread();
        final List<JabberId> contactIds = ((ContactProvider) contentProvider).search(searchExpression);
        final List<Long> incomingEMailInvitationIds = ((ContactProvider) contentProvider).searchIncomingEMailInvitations(searchExpression);
        final List<Long> incomingUserInvitationIds = ((ContactProvider) contentProvider).searchIncomingUserInvitations(searchExpression);
        final List<Long> outgoingEMailInvitationIds = ((ContactProvider) contentProvider).searchOutgoingEMailInvitations(searchExpression);
        final List<Long> outgoingUserInvitationIds = ((ContactProvider) contentProvider).searchOutgoingUserInvitations(searchExpression);
        final List<JabberId> profileIds = ((ContactProvider) contentProvider).searchProfile(searchExpression);
        final int size = contactIds.size() + incomingEMailInvitationIds.size()
                + incomingUserInvitationIds.size()
                + outgoingEMailInvitationIds.size()
                + outgoingUserInvitationIds.size()
                + profileIds.size();
        final List<ContactPanelId> panelIds = new ArrayList<ContactPanelId>(size);
        for (final JabberId contactId : contactIds)
            panelIds.add(new ContactPanelId(contactId));
        for (final Long invitationId : incomingEMailInvitationIds)
            panelIds.add(new ContactPanelId(invitationId));
        for (final Long invitationId : incomingUserInvitationIds)
            panelIds.add(new ContactPanelId(invitationId));
        for (final Long invitationId : outgoingEMailInvitationIds)
            panelIds.add(new ContactPanelId(invitationId));
        for (final Long invitationId : outgoingUserInvitationIds)
            panelIds.add(new ContactPanelId(invitationId));
        for (final JabberId profileId : profileIds)
            panelIds.add(new ContactPanelId(profileId));
        return panelIds;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#requestFocusInWindow(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     */
    @Override
    protected void requestFocusInWindow(final TabPanel tabPanel) {
        ((ContactTabPanel)tabPanel).requestFocusInWindow();
    }

    /**
     * Obtain the popup delegate.
     * 
     * @return A <code>ContainerTabPopupDelegate</code>.
     */
    ContactTabPopupDelegate getPopupDelegate() {
        return popupDelegate;
    }
    
    void syncContact(final JabberId contactId, final Boolean remote) {
        checkThread();
        debug();
        final Contact contact = read(contactId);
        // remove the container from the panel list
        if (null == contact) {
            removePanel(contactId, true);
        } else {
            final int panelIndex = lookupIndex(contact.getId());
            if (-1 < panelIndex) {
                // if the reload is the result of a remote event add the panel
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

    void syncIncomingEMailInvitation(final Long invitationId, final Boolean remote) {
        checkThread();
        debug();
        final IncomingEMailInvitation invitation = readIncomingEMailInvitation(invitationId);
        // remove the container from the panel list
        if (null == invitation) {
            removePanel(invitationId, true);
        } else {
            final int panelIndex = lookupIndex(invitation.getId());
            if (-1 < panelIndex) {
                // if the reload is the result of a remote event add the panel
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
    void syncIncomingUserInvitation(final Long invitationId, final Boolean remote) {
        checkThread();
        debug();
        final IncomingUserInvitation invitation = readIncomingUserInvitation(invitationId);
        // remove the container from the panel list
        if (null == invitation) {
            removePanel(invitationId, true);
        } else {
            final int panelIndex = lookupIndex(invitation.getId());
            if (-1 < panelIndex) {
                // if the reload is the result of a remote event add the panel
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

    void syncOutgoingEMailInvitation(final Long invitationId, final Boolean remote) {
        checkThread();
        debug();
        final OutgoingEMailInvitation invitation = readOutgoingEMailInvitation(invitationId);
        // remove the invitation from the panel list
        if (null == invitation) {
            removePanel(invitationId, true);
        } else {
            final int panelIndex = lookupIndex(invitation.getId());
            if (-1 < panelIndex) {
                // if the reload is the result of a remote event add the panel
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

    void syncOutgoingUserInvitation(final Long invitationId, final Boolean remote) {
        checkThread();
        debug();
        final OutgoingUserInvitation invitation = readOutgoingUserInvitation(invitationId);
        // remove the invitation from the panel list
        if (null == invitation) {
            removePanel(invitationId, true);
        } else {
            final int panelIndex = lookupIndex(invitation.getId());
            if (-1 < panelIndex) {
                // if the reload is the result of a remote event add the panel
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

    void syncProfile(final Boolean remote) {
        checkThread();
        debug();
        final Profile profile = readProfile();
        final int panelIndex = lookupIndex(profile.getId());
        if (-1 < panelIndex) {
            // if the reload is the result of a remote event add the panel
            // at the top of the list; otherwise add it in the same location
            // it previously existed
            removePanel(profile.getId(), false);
            if (remote) {
                addPanel(0, profile);
            } else {
                addPanel(panelIndex, profile);
            }
        } else {
            addPanel(0, profile);
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
     * Add an incoming e-mail invitation to the end of the panels list.
     * 
     * @param invitation
     *            An <code>IncomingEMailInvitation</code>.
     */
    private void addPanel(final IncomingEMailInvitation invitation) {
        addPanel(panels.size() == 0 ? 0 : panels.size(), invitation);
    }

    /**
     * Add an incoming user invitation to the end of the panels list.
     * 
     * @param invitation
     *            An <code>IncomingUserInvitation</code>.
     */
    private void addPanel(final IncomingUserInvitation invitation) {
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
     * Add an incoming e-mail invitation to the panels list.
     * 
     * @param index
     *            The index at which to add the invitation.
     * @param invitation
     *            An <code>IncomingInvitation</code>.
     */
    private void addPanel(final int index, final IncomingEMailInvitation invitation) {
        panels.add(index, toDisplay(invitation));
    }

    /**
     * Add an incoming user invitation to the panels list.
     * 
     * @param index
     *            The index at which to add the invitation.
     * @param invitation
     *            An <code>IncomingInvitation</code>.
     */
    private void addPanel(final int index, final IncomingUserInvitation invitation) {
        panels.add(index, toDisplay(invitation));
    }

    /**
     * Add an outgoing e-mail invitation to the panels list.
     * 
     * @param index
     *            The index at which to add the invitation.
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     */
    private void addPanel(final int index,
            final OutgoingEMailInvitation invitation) {
        panels.add(index, toDisplay(invitation));
    }

    /**
     * Add an outgoing user invitation to the panels list.
     * 
     * @param index
     *            The index at which to add the invitation.
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     */
    private void addPanel(final int index,
            final OutgoingUserInvitation invitation) {
        panels.add(index, toDisplay(invitation));
    }

    /**
     * Add a profile to the panels list.
     * 
     * @param index
     *            The index at which to add the invitation.
     * @param profile
     *            A <code>Profile</code>.
     */
    private void addPanel(final int index, final Profile profile) {
        panels.add(index, toDisplay(profile));
    }

    /**
     * Add an outgoing e-mail invitation to the end of the panels list.
     * 
     * @param index
     *            The index at which to add the invitation.
     * @param invitation
     *            An <code>OutgoingEMailInvitation</code>.
     */
    private void addPanel(final OutgoingEMailInvitation invitation) {
        addPanel(panels.size() == 0 ? 0 : panels.size(), invitation);
    }

    /**
     * Add an outgoing user invitation to the end of the panels list.
     * 
     * @param index
     *            The index at which to add the invitation.
     * @param invitation
     *            An <code>OutgoingUserInvitation</code>.
     */
    private void addPanel(final OutgoingUserInvitation invitation) {
        addPanel(panels.size() == 0 ? 0 : panels.size(), invitation);
    }

    /**
     * Add a profile to the end of the panels list.
     * 
     * @param index
     *            The index at which to add the invitation.
     * @param profile
     *            A <code>Profile</code>.
     */
    private void addPanel(final Profile profile) {
        addPanel(panels.size() == 0 ? 0 : panels.size(), profile);
    }

    /**
     * Apply an ordering to the panels.
     * 
     * @param ordering
     *            An <code>Ordering</code>.
     */
    private void applySort(final SortBy sortBy) {
        debug();
        if (isSortApplied(sortBy)) {
            sortBy.ascending = !sortBy.ascending;
        }
        sortedBy.clear();
        sortedBy.add(sortBy);
        persistence.set(sortByKey, sortBy);
        persistence.set(sortAscendingKey, sortBy.ascending);
        synchronize();
    }

    /**
     * Check we are on the AWT event dispatching thread.
     */
    private void checkThread() {
        Assert.assertTrue(EventQueue.isDispatchThread(), "Contact tab model not on the AWT event dispatch thread.");
    }

    /**
     * Get the initial sort from persistence.
     * 
     * @return A <code>SortBy</code>.
     */
    private SortBy getInitialSort() {
        final SortBy sortBy = (SortBy)(Comparator<TabPanel>)persistence.get(sortByKey, SortBy.NAME);
        sortBy.ascending = persistence.get(sortAscendingKey, true);
        return sortBy;
    }

    /**
     * Get the sort direction.
     * 
     * @param sortBy
     *            A <code>SortBy</code>.
     * @return A <code>SortDirection</code>.        
     */
    private SortDirection getSortDirection(final SortBy sortBy) {
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

    private int lookupIndex(final JabberId userId) {
        ContactTabPanel panel;
        for (int i = 0; i < panels.size(); i++) {
            panel = ((ContactTabPanel) panels.get(i));
            if (panel.isSetContact()) {
                if (panel.getContact().getId().equals(userId)) {
                    return i;
                }
            }
            if (panel.isSetProfile()) {
                if (panel.getProfile().getId().equals(userId)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int lookupIndex(final Long invitationId) {
        ContactTabPanel panel;
        for (int i = 0; i < panels.size(); i++) {
            panel = ((ContactTabPanel) panels.get(i));
            if (panel.isSetIncomingEMail()) {
                if (panel.getIncomingEMail().getId().equals(invitationId)) {
                    return i;
                }
            } else if (panel.isSetIncomingUser()) {
                if (panel.getIncomingUser().getId().equals(invitationId)) {
                    return i;
                }
            } else if (panel.isSetOutgoingEMail()) {
                if (panel.getOutgoingEMail().getId().equals(invitationId)) {
                    return i;
                }
            } else if (panel.isSetOutgoingUser()) {
                if (panel.getOutgoingUser().getId().equals(invitationId)) {
                    return i;
                }
            }
        }
        return -1;
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

    /**
     * Read the emails.
     * 
     * @return A <code>List</code> of <code>ProfileEMail</code>.
     */
    private List<ProfileEMail> readEmails() {
        return ((ContactProvider) contentProvider).readEmails();
    }

    private IncomingEMailInvitation readIncomingEMailInvitation(final Long invitationId) {
        return ((ContactProvider) contentProvider).readIncomingEMailInvitation(invitationId);
    }

    /**
     * Read the incoming e-mail invitations.
     * 
     * @return A <code>List</code> of <code>IncomingEMailInvitation</code>.
     */
    private List<IncomingEMailInvitation> readIncomingEMailInvitations() {
        return ((ContactProvider) contentProvider).readIncomingEMailInvitations();
    }

    private IncomingUserInvitation readIncomingUserInvitation(final Long invitationId) {
        return ((ContactProvider) contentProvider).readIncomingUserInvitation(invitationId);
    }

    /**
     * Read the incoming e-mail invitations.
     * 
     * @return A <code>List</code> of <code>IncomingEMailInvitation</code>.
     */
    private List<IncomingUserInvitation> readIncomingUserInvitations() {
        return ((ContactProvider) contentProvider).readIncomingUserInvitations();
    }

    private OutgoingEMailInvitation readOutgoingEMailInvitation(
            final Long invitationId) {
        return ((ContactProvider) contentProvider).readOutgoingEMailInvitation(
                invitationId);
    }

    private List<OutgoingEMailInvitation> readOutgoingEMailInvitations() {
        return ((ContactProvider) contentProvider).readOutgoingEMailInvitations();
    }
    
    private OutgoingUserInvitation readOutgoingUserInvitation(
            final Long invitationId) {
        return ((ContactProvider) contentProvider).readOutgoingUserInvitation(
                invitationId);
    }

    private List<OutgoingUserInvitation> readOutgoingUserInvitations() {
        return ((ContactProvider) contentProvider).readOutgoingUserInvitations();
    }
    
    /**
     * Read the profile.
     * 
     * @return A <code>Profile</code>.
     */
    private Profile readProfile() {
        return ((ContactProvider) contentProvider).readProfile();
    }

    private void removePanel(final JabberId userId,
            final boolean removeExpandedState) {
        final int panelIndex = lookupIndex(userId);
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
        panel.setPanelData(contact, locale, availableLocales);
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
    private TabPanel toDisplay(final IncomingEMailInvitation invitation) {
        final ContactTabPanel panel = new ContactTabPanel(session);
        panel.setActionDelegate(actionDelegate);
        panel.setPanelData(invitation);
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
    private TabPanel toDisplay(final IncomingUserInvitation invitation) {
        final ContactTabPanel panel = new ContactTabPanel(session);
        panel.setActionDelegate(actionDelegate);
        panel.setPanelData(invitation);
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
    private TabPanel toDisplay(final OutgoingEMailInvitation invitation) {
        final ContactTabPanel panel = new ContactTabPanel(session);
        panel.setActionDelegate(actionDelegate);
        panel.setPanelData(invitation);
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
    private TabPanel toDisplay(final OutgoingUserInvitation invitation) {
        final ContactTabPanel panel = new ContactTabPanel(session);
        panel.setActionDelegate(actionDelegate);
        panel.setPanelData(invitation);
        panel.setPopupDelegate(popupDelegate);
        panel.setExpanded(isExpanded(panel));
        panel.setTabDelegate(this);
        return panel;
    }
    
    /**
     * Obtain the contact display cell for a profile.
     * 
     * @param profile
     *            A <code>Profile</code>.
     * @return A contact display cell.
     */
    private TabPanel toDisplay(final Profile profile) {
        final ContactTabPanel panel = new ContactTabPanel(session);
        panel.setActionDelegate(actionDelegate);
        panel.setPanelData(profile, readEmails(), locale, availableLocales);
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
         */
        public int compare(final TabPanel o1, final TabPanel o2) {
            final ContactTabPanel p1 = (ContactTabPanel) o1;
            final ContactTabPanel p2 = (ContactTabPanel) o2;
            
            // Sort first by panel type (incoming invite, outgoing invite, contacts)
            int result = getTypePriority(p1).compareTo(getTypePriority(p2));
            
            // Sort outgoing e-mail invitations alphabetically by email
            if (0 == result && p1.isSetOutgoingEMail()) {
                return STRING_COMPARATOR_ASC.compare(
                        p1.getOutgoingEMail().getInvitationEMail().toString(),
                        p2.getOutgoingEMail().getInvitationEMail().toString());
            }
            
            // Sort incoming invitations, contacts, and profile
            if (0 == result) {
                switch (this) {
                case NAME:
                    // note the lack of multiplier
                    return ascending
                        ? STRING_COMPARATOR_ASC.compare(
                                getUser(p1).getName(),
                                getUser(p2).getName())
                        : STRING_COMPARATOR_DESC.compare(
                                getUser(p1).getName(),
                                getUser(p2).getName());
                case TITLE:
                    // note the lack of multiplier
                    result =  ascending
                        ? STRING_COMPARATOR_ASC.compare(
                                getUser(p1).getTitle(),
                                getUser(p2).getTitle())
                        : STRING_COMPARATOR_DESC.compare(
                                getUser(p1).getTitle(),
                                getUser(p2).getTitle());
                    if (0 == result) {
                        result = compareDefault(p1, p2);
                    }
                    return result;
                case ORGANIZATION:
                    // note the lack of multiplier
                    result = ascending
                        ? STRING_COMPARATOR_ASC.compare(
                                getUser(p1).getOrganization(),
                                getUser(p2).getOrganization())
                        : STRING_COMPARATOR_DESC.compare(
                                getUser(p1).getOrganization(),
                                getUser(p2).getOrganization());
                    if (0 == result) {
                        result = compareDefault(p1, p2);
                    }
                    return result;            
                default:
                    throw Assert.createUnreachable("Unknown ordering.");
                }         
            } else {
                return result;
            }
        }

        /**
         * Apply a default ordering (assuming the same panel type, ie. both are contacts,
         * both are incoming invitations, etc).
         * 
         * @param p1
         *          A <code>ContactTabPanel</code>.
         * @param p2
         *          A <code>ContactTabPanel</code>.
         * @return An <code>int</code> compare result.         
         */
        private int compareDefault(final ContactTabPanel p1, final ContactTabPanel p2) {
            return ascending
                ? STRING_COMPARATOR_ASC.compare(
                        getUser(p1).getName(),
                        getUser(p2).getName())
                : STRING_COMPARATOR_DESC.compare(
                        getUser(p1).getName(),
                        getUser(p2).getName());
        }

        /**
         * Get the priority based on panel type. Incoming invitations, then
         * outgoing user invitations, then outgoing e-mail invitations, then
         * remaining panels.
         * 
         * @param p
         *            A <code>ContactTabPanel</code>.
         * @return An <code>Integer</code> priority.
         */
        private Integer getTypePriority(final ContactTabPanel panel) {
            if (panel.isSetIncomingEMail()) {
                return 0;
            } else if (panel.isSetIncomingUser()) {
                return 1;
            } else if (panel.isSetOutgoingUser()) {
                return 2;
            } else if (panel.isSetOutgoingEMail()) {
                return 3;
            } else {
                return 4;
            }
        }

        /**
         * Get the user associated with the panel.
         * 
         * @param p
         *          A <code>ContactTabPanel</code>.
         * @return A <code>User</code>.
         */
        private User getUser(final ContactTabPanel panel) {
            if (panel.isSetIncomingEMail()) {
                return panel.getIncomingEMail().getExtendedBy();
            } else if (panel.isSetIncomingUser()) {
                return panel.getIncomingEMail().getExtendedBy();
            } else if (panel.isSetOutgoingEMail()) {
                return null;
            } else if (panel.isSetOutgoingUser()) {
                return panel.getOutgoingUser().getInvitationUser();
            } else if (panel.isSetProfile()) {
                return panel.getProfile();
            } else if (panel.isSetContact()) {
                return panel.getContact();
            } else {
                throw Assert.createUnreachable("Inconsistent contact tab panel state.");
            }
        }
    }
}
