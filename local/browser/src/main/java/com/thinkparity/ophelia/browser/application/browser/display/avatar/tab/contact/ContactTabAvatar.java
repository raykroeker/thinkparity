/*
 * Created On: 20-Jun-2006 12:05:08 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.swing.SwingUtil;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;

import com.thinkparity.ophelia.model.events.ContainerEvent;

import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabPanelAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.event.tab.contact.ContactTabDispatcher;


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
        setFilterDelegate(model);
        addPropertyChangeListener("eventDispatcher",
                new PropertyChangeListener() {
            public void propertyChange(final PropertyChangeEvent e) {
                if (null != e.getOldValue()) {
                    ((ContactTabDispatcher) e.getOldValue()).removeListeners(
                            ContactTabAvatar.this);
                }
                if (null != e.getNewValue()) {
                    ((ContactTabDispatcher) e.getNewValue()).addListeners(
                            ContactTabAvatar.this);
                }
            }
        });
    }

    /**
     * Collapse the contact.
     * 
     * @param contactId
     *            A contact id <code>Long</code>.
     */
    public void collapseContact(final Long contactId) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.collapsePanel(newContactPanelId(contactId), Boolean.FALSE);
            }
        });
    }

    /**
     * Expand the contact.
     * 
     * @param contactId
     *            A contact id <code>JabberId</code>.
     */
    public void expandContact(final Long contactId) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                showPanel(newContactPanelId(contactId), true);
            }
        });
    }

    /**
     * Fire an enable tab action.
     * 
     */
    public void fireEnableTabAction() {
        model.enableTabAction();
        getController().reloadTitle();
    }

    /**
     * Fire a container published event.
     * 
     * @param e
     *            A <code>ContainerEvent</code>.
     */
    public void firePublished(final ContainerEvent e) {
        // HACK - ContactTabAvatar#firePublished
        for (final OutgoingEMailInvitation oei : e.getOutgoingEMailInvitations()) {
            SwingUtil.ensureDispatchThread(new Runnable() {
                public void run() {
                    model.syncOutgoingEMailInvitation(oei.getId(), e.isRemote());
                }
            });
        }
    }

    /**
     * Fire a session event.
     *
     */
    public void fireSessionEvent() {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.reloadConnection();
            }
        });
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
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                final List<ContactPanelId> panelIds = new ArrayList<ContactPanelId>(invitationIds.size());
                for (final Long invitationId : invitationIds)
                    panelIds.add(newInvitationPanelId(invitationId));
                final List<ContactPanelId> sortedInvitationIds = model.getCurrentVisibleOrder(panelIds);
                if (index < sortedInvitationIds.size()) {
                    showPanel(sortedInvitationIds.get(index), false);
                }
            }
        });
    }

    /**
     * Show the contact invitation (select the panel and scroll so it visible).
     * 
     * @param invitationId
     *            The invitationId. 
     */
    public void showContactInvitation(final Long invitationId) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                showPanel(newInvitationPanelId(invitationId), false);
            }
        });
    }

    /**
     * Show the topmost visible incoming invitation.
     * 
     * The panel is expanded (without animation) and scrolled so it is visible.
     */
    public void showTopVisibleIncomingInvitation() {
        final IncomingInvitation incomingInvitation = model.getTopVisibleIncomingInvitation();
        if (null != incomingInvitation) {
            showContactInvitation(incomingInvitation.getId());
        }
    }

    /**
     * Synchronize the contact in the list, for example if it is deleted.
     * 
     * @param contact
     *            A <code>Contact</code>.
     * @param remote
     *            Indicates whether the sync is the result of a remote event
     */
    public void syncContact(final Contact contact, final Boolean remote) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncContact(contact.getLocalId(), remote);
            }
        });
    }

    /**
     * Synchronize the incoming invitation in the list.
     * 
     * @param invitationId
     *            An invitation id.
     * @param remote
     *            Whether or not the the source is a remote event.
     */
    public void syncIncomingEMailInvitation(final Long invitationId,
            final Boolean remote) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncIncomingEMailInvitation(invitationId, remote);
            }
        });
    }

    /**
     * Synchronize the incoming invitation in the list.
     * 
     * @param invitationId
     *            An invitation id.
     * @param remote
     *            Whether or not the the source is a remote event.
     */
    public void syncIncomingUserInvitation(final Long invitationId,
            final Boolean remote) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncIncomingUserInvitation(invitationId, remote);
            }
        });
    }

    /**
     * Synchronize an invitation in the list.
     * 
     * @param invitationId
     *            An invitation id.
     * @param remote
     *            Whether or not the the source is a remote event.
     */
    public void syncOutgoingEMailInvitation(final Long invitationId,
            final Boolean remote) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncOutgoingEMailInvitation(invitationId, remote);    
            }
        });
    }

    /**
     * Synchronize an outgoing user invitation in the list.
     * 
     * @param invitationId
     *            An invitation id <code>Long</code>.
     * @param remote
     *            Whether or not the source is a remove event
     *            <code>Boolean</code>.
     */
    public void syncOutgoingUserInvitation(final Long invitationId,
            final Boolean remote) {
        SwingUtil.ensureDispatchThread(new Runnable() {
            public void run() {
                model.syncOutgoingUserInvitation(invitationId, remote);    
            }
        });
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
     * Show the contact panel.
     * 
     * @param panelId
     *            A panel id <code>ContactPanelId</code>.
     */
    private void showPanel(final ContactPanelId panelId, final boolean expand) {
        model.selectPanel(panelId);
        if (expand) {
            model.expandPanel(panelId, Boolean.FALSE);
        }
        model.scrollPanelToVisible(panelId); 
    }  
}
