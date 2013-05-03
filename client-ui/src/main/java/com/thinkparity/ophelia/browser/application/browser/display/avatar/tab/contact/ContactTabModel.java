/*
 * Created On: Jun 21, 2006 11:32:04 AM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import java.awt.Component;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.sort.StringComparator;

import com.thinkparity.codebase.model.contact.*;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterBy;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel;
import com.thinkparity.ophelia.browser.application.browser.display.provider.tab.contact.ContactProvider;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ContactTabPanel;

/**
 * <b>Title:</b>thinkParity Contact Tab Model<br>
 * <b>Description:</b>The tab model for a contact<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ContactTabModel extends TabPanelModel<ContactPanelId> implements
        TabAvatarFilterDelegate {

    /** The <code>ContactTabActionDelegate</code>. */
    private final ContactTabActionDelegate actionDelegate;

    /** An array of available <code>Locale</code>s. */
    private final Locale[] availableLocales;

    /** The <code>ContactTabComparator</code>. */
    private Comparator<TabPanel> comparator;    

    /** The <code>Locale</code>. */
    private final Locale locale;

    /** The <code>ContactTabPopupDelegate</code>. */
    private final ContactTabPopupDelegate popupDelegate;

    /** A tab action enabled flag. */
    private Boolean tabActionEnabled;

    /** A tab action initialized flag. */
    private Boolean tabActionInitialized;

    /**
     * Create ContactTabModel.
     * 
     */
    ContactTabModel() {
        super();
        this.actionDelegate = new ContactTabActionDelegate(this);
        this.popupDelegate= new ContactTabPopupDelegate(this);
        this.comparator = new ContactTabComparator();
        this.availableLocales = browser.getAvailableLocales();
        this.locale = browser.getLocale();
        this.tabActionInitialized = Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate#clearFilter()
     */
    public void clearFilter() {
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate#getFilterBy()
     */
    public List<TabAvatarFilterBy> getFilterBy() {
        final List<TabAvatarFilterBy> filterBy = Collections.emptyList();
        return filterBy;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#getTabButtonActionDelegate()
     */
    @Override
    public TabButtonActionDelegate getTabButtonActionDelegate() {
        return actionDelegate;
    }

    /**
     * Get the topmost visible incoming invitation.
     * 
     * @return The topmost <code>IncomingInvitation</code>.
     */
    public IncomingInvitation getTopVisibleIncomingInvitation() {
        for (final TabPanel visiblePanel : visiblePanels) {
            final ContactTabPanel contactTabPanel = (ContactTabPanel)visiblePanel;
            if (contactTabPanel.isSetIncomingEMail()) {
                return contactTabPanel.getIncomingEMail();
            } else if (contactTabPanel.isSetIncomingUser()) {
                return contactTabPanel.getIncomingUser();
            }
        }
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate#isFilterApplied()
     */
    public Boolean isFilterApplied() {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterDelegate#isFilterSelected(com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatarFilterBy)
     */
    public Boolean isFilterSelected(final TabAvatarFilterBy tabAvatarFilterBy) {
        return Boolean.FALSE;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#isOnlineUI()
     */
    @Override
    public Boolean isOnlineUI() {
        return isOnline() && readIsEMailVerified() && readIsProfileActive();
    }

    /**
     * Apply the sort to the filtered list of panels.
     * 
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabModel#applySort()
     */
    @Override
    protected void applySort() {
        checkThread();
        Collections.sort(filteredPanels, comparator);
    }

    /**
     * Debug the contact avatar.
     * 
     */
    @Override
    protected void debug() {
        checkThread();
        logger.logDebug("{0} contact panels.", panels.size());
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#deletePanel(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     */
    @Override
    protected void deletePanel(final TabPanel tabPanel) {
        final ContactTabPanel panel = (ContactTabPanel) tabPanel;
        if (panel.isSetContact()) {
            browser.runDeleteContact(lookupId(tabPanel).getContactId());
        } else if (panel.isSetOutgoingEMail()) {
            browser.runDeleteOutgoingEMailInvitation(lookupId(tabPanel).getInvitationId());
        } else if (panel.isSetOutgoingUser()) {
            browser.runDeleteOutgoingUserInvitation(lookupId(tabPanel).getInvitationId());
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
        super.initialize();
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
        final List<Contact> contacts = readContacts();
        for (final Contact contact : contacts) {
            addPanel(contact);
        }
        synchronize();
        debug();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#lookupId(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     */
    @Override
    protected ContactPanelId lookupId(final TabPanel tabPanel) {
        final ContactTabPanel panel = (ContactTabPanel) tabPanel;
        if (panel.isSetContact()) {
            return newPanelId(panel.getContact());
        } else if (panel.isSetIncomingEMail()) {
            return newPanelId(panel.getIncomingEMail());
        } else if (panel.isSetIncomingUser()) {
            return newPanelId(panel.getIncomingUser());
        } else if (panel.isSetOutgoingEMail()) {
            return newPanelId(panel.getOutgoingEMail());
        } else if (panel.isSetOutgoingUser()) {
            return newPanelId(panel.getOutgoingUser());
        } else {
            throw Assert.createUnreachable("Unknown contact panel type.");
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#lookupPanel(java.lang.Object)
     */
    @Override
    protected TabPanel lookupPanel(final ContactPanelId panelId) {
        final int panelIndex = lookupIndex(panelId);
        return -1 == panelIndex ? null : panels.get(panelIndex);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#readSearchResults()
     *
     */
    @Override
    protected List<ContactPanelId> readSearchResults() {
        checkThread();
        final List<Long> contactIds = ((ContactProvider) contentProvider).search(searchExpression);
        final List<Long> incomingEMailInvitationIds = ((ContactProvider) contentProvider).searchIncomingEMailInvitations(searchExpression);
        final List<Long> incomingUserInvitationIds = ((ContactProvider) contentProvider).searchIncomingUserInvitations(searchExpression);
        final List<Long> outgoingEMailInvitationIds = ((ContactProvider) contentProvider).searchOutgoingEMailInvitations(searchExpression);
        final List<Long> outgoingUserInvitationIds = ((ContactProvider) contentProvider).searchOutgoingUserInvitations(searchExpression);
        final int size = contactIds.size() + incomingEMailInvitationIds.size()
                + incomingUserInvitationIds.size()
                + outgoingEMailInvitationIds.size()
                + outgoingUserInvitationIds.size();
        final List<ContactPanelId> panelIds = new ArrayList<ContactPanelId>(size);
        for (final Long contactId : contactIds)
            panelIds.add(newContactPanelId(contactId));
        for (final Long invitationId : incomingEMailInvitationIds)
            panelIds.add(newInvitationPanelId(invitationId));
        for (final Long invitationId : incomingUserInvitationIds)
            panelIds.add(ContactPanelId.newInvitationPanelId(invitationId));
        for (final Long invitationId : outgoingEMailInvitationIds)
            panelIds.add(ContactPanelId.newInvitationPanelId(invitationId));
        for (final Long invitationId : outgoingUserInvitationIds)
            panelIds.add(ContactPanelId.newInvitationPanelId(invitationId));
        return panelIds;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelModel#setExpandedPanelData(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     * 
     */
    @Override
    protected void setExpandedPanelData(final TabPanel tabPanel) {
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
     * Determine if the tab action is enabled.
     * 
     * @return True if the tab action is enabled.
     */
    Boolean isTabActionEnabled() {
        if (!tabActionInitialized) {
            reloadTabActionEnabled();
        }
        return tabActionEnabled && isOnline();
    }

    /**
     * Determine whether or not the profile's e-mail address has been verified.
     * 
     * @return True if it is verified.
     */
    Boolean readIsEMailVerified() {
        return ((ContactProvider) contentProvider).readIsEMailVerified();
    }

    /**
     * Determine whether or not the profile is enabled.
     * 
     * @return True if it is enabled.
     */
    Boolean readIsProfileActive() {
        return ((ContactProvider) contentProvider).readIsActive();
    }

    /**
     * Reload the connection.
     */
    void reloadConnection() {
        final Boolean online = isOnlineUI();
        for (final TabPanel panel : panels) {
            final ContactTabPanel contactPanel = (ContactTabPanel) panel;
            if (contactPanel.isSetIncomingEMail() || contactPanel.isSetIncomingUser()) {
                contactPanel.reloadConnection(online);
            }
        }
    }

    /**
     * Synchronize the tab action's enabled flag.
     * 
     */
    void reloadTabActionEnabled() {
        tabActionEnabled = readIsInviteAvailable() && readIsProfileActive() && readIsEMailVerified();
        tabActionInitialized = Boolean.TRUE;
    }

    void syncContact(final Long contactId, final Boolean remote) {
        checkThread();
        debug();
        boolean requestFocus = false;
        final Contact contact = read(contactId);
        // remove the container from the panel list
        if (null == contact) {
            removeContactPanel(contactId, true);
        } else {
            final int panelIndex = lookupIndex(contact);
            if (-1 < panelIndex) {
                // if the reload is the result of a remote event add the panel
                // at the top of the list; otherwise add it in the same location
                // it previously existed
                requestFocus = ((Component)lookupPanel(newPanelId(contact))).hasFocus();
                removeContactPanel(contactId, false);
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
        if (requestFocus) {
            requestFocusInWindow(lookupPanel(newPanelId(contact)));
        }
        debug();
    }

    void syncIncomingEMailInvitation(final Long invitationId, final Boolean remote) {
        checkThread();
        debug();
        boolean requestFocus = false;
        final IncomingEMailInvitation invitation = readIncomingEMailInvitation(invitationId);
        // remove the container from the panel list
        if (null == invitation) {
            removeInvitationPanel(invitationId, true);
        } else {
            final int panelIndex = lookupIndex(invitation);
            if (-1 < panelIndex) {
                // if the reload is the result of a remote event add the panel
                // at the top of the list; otherwise add it in the same location
                // it previously existed
                requestFocus = ((Component)lookupPanel(newPanelId(invitation))).hasFocus();
                removeInvitationPanel(invitationId, false);
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
        if (requestFocus) {
            requestFocusInWindow(lookupPanel(newPanelId(invitation)));
        }
        debug();
    }

    void syncIncomingUserInvitation(final Long invitationId, final Boolean remote) {
        checkThread();
        debug();
        boolean requestFocus = false;
        final IncomingUserInvitation invitation = readIncomingUserInvitation(invitationId);
        // remove the container from the panel list
        if (null == invitation) {
            removeInvitationPanel(invitationId, true);
        } else {
            final int panelIndex = lookupIndex(invitation);
            if (-1 < panelIndex) {
                // if the reload is the result of a remote event add the panel
                // at the top of the list; otherwise add it in the same location
                // it previously existed
                requestFocus = ((Component)lookupPanel(newPanelId(invitation))).hasFocus();
                removeInvitationPanel(invitationId, false);
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
        if (requestFocus) {
            requestFocusInWindow(lookupPanel(newPanelId(invitation)));
        }
        debug();
    }

    void syncOutgoingEMailInvitation(final Long invitationId, final Boolean remote) {
        checkThread();
        debug();
        boolean requestFocus = false;
        final OutgoingEMailInvitation invitation = readOutgoingEMailInvitation(invitationId);
        // remove the invitation from the panel list
        if (null == invitation) {
            removeInvitationPanel(invitationId, true);
        } else {
            final int panelIndex = lookupIndex(invitation);
            if (-1 < panelIndex) {
                // if the reload is the result of a remote event add the panel
                // at the top of the list; otherwise add it in the same location
                // it previously existed
                requestFocus = ((Component)lookupPanel(newPanelId(invitation))).hasFocus();
                removeInvitationPanel(invitationId, false);
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
        if (requestFocus) {
            requestFocusInWindow(lookupPanel(newPanelId(invitation)));
        }
        debug();

    }
    void syncOutgoingUserInvitation(final Long invitationId, final Boolean remote) {
        checkThread();
        debug();
        boolean requestFocus = false;
        final OutgoingUserInvitation invitation = readOutgoingUserInvitation(invitationId);
        // remove the invitation from the panel list
        if (null == invitation) {
            removeInvitationPanel(invitationId, true);
        } else {
            final int panelIndex = lookupIndex(invitation);
            if (-1 < panelIndex) {
                // if the reload is the result of a remote event add the panel
                // at the top of the list; otherwise add it in the same location
                // it previously existed
                requestFocus = ((Component)lookupPanel(newPanelId(invitation))).hasFocus();
                removeInvitationPanel(invitationId, false);
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
        if (requestFocus) {
            requestFocusInWindow(lookupPanel(newPanelId(invitation)));
        }
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
     * Check we are on the AWT event dispatching thread.
     */
    private void checkThread() {
        Assert.assertTrue(EventQueue.isDispatchThread(), "Contact tab model not on the AWT event dispatch thread.");
    }

    /**
     * Lookup the index of a contact panel.
     * 
     * @param contactId
     *            A contact id <code>Long</code>.
     * @return An <code>int</code>.
     */
    private int lookupContactIndex(final Long contactId) {
        ContactTabPanel panel;
        for (int i = 0; i < panels.size(); i++) {
            panel = ((ContactTabPanel) panels.get(i));
            if (panel.isSetContact()) {
                if (panel.getContact().getLocalId().equals(contactId)) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int lookupIndex(final Contact contact) {
        return lookupContactIndex(contact.getLocalId());
    }

    private int lookupIndex(final ContactInvitation invitation) {
        return lookupInvitationIndex(invitation.getId());
    }

    private int lookupIndex(final ContactPanelId panelId) {
        if (panelId.isSetContactId()) {
            return lookupContactIndex(panelId.getContactId());
        } else if (panelId.isSetInvitationId()) {
            return lookupInvitationIndex(panelId.getInvitationId());
        } else {
            throw Assert.createUnreachable("Unkown panel id {0}.", panelId);
        }
    }

    /**
     * Lookup the index of an invitation panel.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     * @return An <code>int</code>.
     */
    private int lookupInvitationIndex(final Long invitationId) {
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

    /**
     * Create a new panel id for a contact.
     * 
     * @param contactId
     *            A <code>Long</code>.
     * @return A <code>ContactPanelId</code>.
     */
    private ContactPanelId newContactPanelId(final Long contactId) {
        return ContactPanelId.newContactPanelId(contactId);
    }

    /**
     * Create a new panel id for an invitation.
     * 
     * @param invitationId
     *            A <code>Long</code>.
     * @return A <code>ContactPanelId</code>.
     */
    private ContactPanelId newInvitationPanelId(final Long invitationId) {
        return ContactPanelId.newInvitationPanelId(invitationId);
    }

    /**
     * Create a new panel id for a contact.
     * 
     * @param contact
     *            A <code>Contact</code>.
     * @return A <code>ContactPanelId</code>.
     */
    private ContactPanelId newPanelId(final Contact contact) {
        return ContactPanelId.newPanelId(contact);
    }

    /**
     * Create a new panel id for an invitation.
     * 
     * @param invitation
     *            A <code>ContactInvitation</code>.
     * @return A <code>ContactPanelId</code>.
     */
    private ContactPanelId newPanelId(final ContactInvitation invitation) {
        return ContactPanelId.newPanelId(invitation);
    }

    private Contact read(final Long contactId) {
        return ((ContactProvider) contentProvider).read(contactId);
    }

    /**
     * Read the contacts from the provider.
     * 
     * @return The contacts.
     */
    private List<Contact> readContacts() {
        return ((ContactProvider) contentProvider).readContacts();
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

    /**
     * Read whether or not invite is available.
     * 
     * @return True if it is available.
     */
    private Boolean readIsInviteAvailable() {
        return ((ContactProvider) contentProvider).readIsInviteAvailable();
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
     * Remove a contact panel.
     * 
     * @param userId
     *            The user id <code>JabberId</code>.
     * @param removeExpandedState
     *            Remove expanded state <code>boolean</code>.
     */
    private void removeContactPanel(final Long contactId,
            final boolean removeExpandedState) {
        final int panelIndex = lookupContactIndex(contactId);
        if (-1 == panelIndex) {
            logger.logError("Cannot remove contact panel {0}.", contactId);
        } else {
            final TabPanel panel = panels.remove(panelIndex);
            if (removeExpandedState) {
                removeExpandedState(panel);
            }
        }
    }

    /**
     * Remove a contact panel.
     * 
     * @param invitationId
     *            The invitation id <code>Long</code>.
     * @param removeExpandedState
     *            Remove expanded state <code>boolean</code>.
     */
    private void removeInvitationPanel(final Long invitationId,
            final boolean removeExpandedState) {
        final int panelIndex = lookupInvitationIndex(invitationId);
        if (-1 == panelIndex) {
            logger.logError("Cannot remove contact panel, invitation id {0}.", invitationId);
        } else {
            final TabPanel panel = panels.remove(panelIndex);
            if (removeExpandedState) {
                removeExpandedState(panel);
            }
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
        panel.setSelected(isSelected(panel));
        panel.setTabDelegate(this);
        return panel;
    }

    /**
     * Obtain the contact display cell for a contact.
     * 
     * @param online
     *            A <code>Boolean</code>.
     * @param invitation
     *            An <code>OutgoingInvitation</code>.
     * @return A contact display cell.
     */
    private TabPanel toDisplay(final IncomingEMailInvitation invitation) {
        final ContactTabPanel panel = new ContactTabPanel(session);
        panel.setActionDelegate(actionDelegate);
        panel.setPanelData(isOnlineUI(), invitation);
        panel.setPopupDelegate(popupDelegate);
        panel.setExpanded(isExpanded(panel));
        panel.setSelected(isSelected(panel));
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
        panel.setPanelData(isOnlineUI(), invitation);
        panel.setPopupDelegate(popupDelegate);
        panel.setExpanded(isExpanded(panel));
        panel.setSelected(isSelected(panel));
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
        panel.setSelected(isSelected(panel));
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
        panel.setSelected(isSelected(panel));
        panel.setTabDelegate(this);
        return panel;
    }

    private class ContactTabComparator implements Comparator<TabPanel> {

        /**
         * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
         * 
         */
        public int compare(final TabPanel o1, final TabPanel o2) {
            final ContactTabPanel p1 = (ContactTabPanel) o1;
            final ContactTabPanel p2 = (ContactTabPanel) o2;
            final StringComparator stringComparatorAsc =
                new StringComparator(Boolean.TRUE);

            // Sort first by panel type (incoming invite, outgoing invite, contacts)
            int result = getTypePriority(p1).compareTo(getTypePriority(p2));
            if (result != 0) {
                return result;
            }

            // Sort outgoing e-mail invitations alphabetically by email.
            // Sort all other types by name.
            if (p1.isSetOutgoingEMail()) {
                return stringComparatorAsc.compare(
                        p1.getOutgoingEMail().getInvitationEMail().toString(),
                        p2.getOutgoingEMail().getInvitationEMail().toString());
            } else {
                return stringComparatorAsc.compare(
                                getUser(p1).getName(),
                                getUser(p2).getName());
            }
        }

        /**
         * Get the priority based on panel type. Incoming invitations, then
         * outgoing invitations, then remaining panels.
         * 
         * @param p
         *            A <code>ContactTabPanel</code>.
         * @return An <code>Integer</code> priority.
         */
        private Integer getTypePriority(final ContactTabPanel panel) {
            if (panel.isSetIncomingEMail() || panel.isSetIncomingUser()) {
                return 0;
            } else if (panel.isSetOutgoingUser()) {
                return 1;
            } else if (panel.isSetOutgoingEMail()) {
                return 2;
            } else {
                return 3;
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
                return panel.getIncomingUser().getExtendedBy();
            } else if (panel.isSetOutgoingEMail()) {
                return null;
            } else if (panel.isSetOutgoingUser()) {
                return panel.getOutgoingUser().getInvitationUser();
            } else if (panel.isSetContact()) {
                return panel.getContact();
            } else {
                throw Assert.createUnreachable("Inconsistent contact tab panel state.");
            }
        }
    }
}
