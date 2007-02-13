/*
 * Created On: 20-Jun-2006 12:05:08 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelAvatar;


/**
 * The contacts list avatar displays the list of contacts.
 *
 * @author rob_masako@shaw.ca
 * @version 1.1.2.11
 */
public class ContactTabAvatar extends TabPanelAvatar<ContactTabModel> {

    /**
     * Create ContactTabAvatar.
     *
     */
    public ContactTabAvatar() {
        super(AvatarId.TAB_CONTACT, new ContactTabModel());
        model.setLocalization(getLocalization());
        model.setSession(getSession());
        setSortByDelegate(model);
    }

    /**
     * Collapse the contact.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void collapseContact(final JabberId contactId) {
        final ContactPanelId contactPanelId = new ContactPanelId(contactId);
        if (EventQueue.isDispatchThread()) {
            model.collapsePanel(contactPanelId, Boolean.FALSE);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    model.collapsePanel(contactPanelId, Boolean.FALSE);
                }
            });
        }
    }

    /**
     * Expand the contact.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void expandContact(final JabberId contactId) {
        final ContactPanelId contactPanelId = new ContactPanelId(contactId);
        showPanel(contactPanelId, true);
    }

    /**
     * Show the contact invitation (select the panel and scroll so it visible).
     * 
     * @param invitationIds
     *            The list of invitationIds.
     * @param index
     *            The index of the invitation to show (0 indicates the invitation displayed at top).   
     */
    public void showContactInvitation(final List<Long> invitationIds, final int index) {
        if (EventQueue.isDispatchThread()) {
            showContactInvitationImpl(invitationIds, index);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    showContactInvitationImpl(invitationIds, index);
                }
            });
        }
    }

    /**
     * Synchronize the contact in the list, for example if it is deleted.
     * 
     * @param contactId
     *            The contact id.
     * @param remote
     *            Indicates whether the sync is the result of a remote event
     */
    public void syncContact(final JabberId contactId, final Boolean remote) {
        if (EventQueue.isDispatchThread()) {
            model.syncContact(contactId, remote);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    model.syncContact(contactId, remote);
                }
            });
        }
    }

    /**
     * Synchronize the incoming invitation in the list.
     * 
     * @param invitationId
     *            An invitation id.
     * @param remote
     *            Whether or not the the source is a remote event.
     */
    public void syncIncomingInvitation(final Long invitationId,
            final Boolean remote) {
        if (EventQueue.isDispatchThread()) {
            model.syncIncomingInvitation(invitationId, remote);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    model.syncIncomingInvitation(invitationId, remote);
                }
            });
        }
        getController().runDisplayContactInvitationInfo();
    }

    /**
     * Synchronize an invitation in the list.
     * 
     * @param invitationId
     *            An invitation id.
     * @param remote
     *            Whether or not the the source is a remote event.
     */
    public void syncOutgoingInvitation(final Long invitationId,
            final Boolean remote) {
        if (EventQueue.isDispatchThread()) {
            model.syncOutgoingInvitation(invitationId, remote);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    model.syncOutgoingInvitation(invitationId, remote);
                }
            });
        }
    }

    /**
     * Synchronize the profile in the list, for example if it is changed.
     * 
     * @param remote
     *            Indicates whether the sync is the result of a remote event
     */
    public void syncProfile(final Boolean remote) {
        if (EventQueue.isDispatchThread()) {
            model.syncProfile(remote);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    model.syncProfile(remote);
                }
            });
        }
    }

    /**
     * Expand a panel.
     * 
     * @param panelId
     *            A panel id <code>ContactPanelId</code>.
     */
    private void expandPanel(final ContactPanelId panelId) {
        if (EventQueue.isDispatchThread()) {
            model.expandPanel(panelId, Boolean.FALSE);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    model.expandPanel(panelId, Boolean.FALSE);
                }
            });
        }
    }

    /**
     * Scroll a panel to make it visible.
     * 
     * @param panelId
     *            A panel id <code>ContactPanelId</code>.
     */
    private void scrollPanelToVisible(final ContactPanelId panelId) {
        if (EventQueue.isDispatchThread()) {
            model.scrollPanelToVisible(panelId);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    model.scrollPanelToVisible(panelId);
                }
            });
        }
    }

    /**
     * Select a panel.
     * 
     * @param panelId
     *            A panel id <code>ContactPanelId</code>.
     */
    private void selectPanel(final ContactPanelId panelId) {
        if (EventQueue.isDispatchThread()) {
            model.selectPanel(panelId);
        } else {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    model.selectPanel(panelId);
                }
            });
        }
    }

    /**
     * Show the contact invitation (select the panel and scroll so it visible).
     * 
     * @param invitationIds
     *            The list of invitationIds.
     * @param index
     *            The index of the invitation to show (0 indicates the invitation displayed at top).   
     */
    private void showContactInvitationImpl(final List<Long> invitationIds, final int index) {
        final List<ContactPanelId> panelIds = new ArrayList<ContactPanelId>(invitationIds.size());
        for (final Long invitationId : invitationIds)
            panelIds.add(new ContactPanelId(invitationId));
        final List<ContactPanelId> sortedInvitationIds = model.getCurrentVisibleOrder(panelIds);
        if (index < sortedInvitationIds.size()) {
            showPanel(sortedInvitationIds.get(index), false);
        }
    }

    /**
     * Show the contact panel.
     * 
     * @param panelId
     *            A panel id <code>ContactPanelId</code>.
     */
    private void showPanel(final ContactPanelId panelId, final boolean expand) {
        selectPanel(panelId);
        if (expand) {
            expandPanel(panelId);
        }
        scrollPanelToVisible(panelId); 
    }  
}
