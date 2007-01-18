/*
 * Created On: 20-Jun-2006 12:05:08 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import java.util.ArrayList;
import java.util.List;

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
     * Show the contact invitation (scroll so it visible).
     * 
     * @param invitationIds
     *            The list of invitationIds.
     * @param index
     *            The index of the invitation to show (0 indicates the invitation displayed at top).   
     */
    public void showContactInvitation(final List<Long> invitationIds, final int index) {
        final List<ContactPanelId> panelIds = new ArrayList<ContactPanelId>(invitationIds.size());
        for (final Long invitationId : invitationIds)
            panelIds.add(new ContactPanelId(invitationId));
        final List<ContactPanelId> sortedInvitationIds = model.getCurrentVisibleOrder(panelIds);
        if (index < sortedInvitationIds.size()) {
            showPanel(sortedInvitationIds.get(index));
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
        model.syncContact(contactId, remote);
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
        model.syncIncomingInvitation(invitationId, remote);
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
        model.syncOutgoingInvitation(invitationId, remote);
    }

    /**
     * Show the contact panel.
     * 
     * @param panelId
     *            A panel id <code>ContactPanelId</code>.
     */
    private void showPanel(final ContactPanelId panelId) {
        model.scrollPanelToVisible(panelId); 
    }  
}
