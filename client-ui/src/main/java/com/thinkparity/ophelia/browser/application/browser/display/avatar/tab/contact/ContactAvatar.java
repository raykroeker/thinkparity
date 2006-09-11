/*
 * Created On: 20-Jun-2006 12:05:08 PM
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import java.awt.Component;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import com.thinkparity.codebase.jabber.JabberId;


import com.thinkparity.ophelia.browser.application.browser.component.MenuFactory;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.AvatarId;
import com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.TabAvatar;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabCell;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;


/**
 * The contacts list avatar displays the list of contacts.
 *
 * @author rob_masako@shaw.ca
 * @version 1.1.2.11
 */
public class ContactAvatar extends TabAvatar<ContactModel> {

    /** Create a BrowserContactsAvatar. */
    public ContactAvatar() {
        super(AvatarId.TAB_CONTACT, new ContactModel());
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
        final TabCell selectedCell = getSelectedCell();
        model.syncContact(contactId, remote);
        setSelectedCell(selectedCell);
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
        final TabCell selectedCell = getSelectedCell();
        model.syncIncomingInvitation(invitationId, remote);
        setSelectedCell(selectedCell);
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
        final TabCell selectedCell = getSelectedCell();
        model.syncOutgoingInvitation(invitationId, remote);
        setSelectedCell(selectedCell);
    }

    /**
     * Trigger a popup when clicking in a blank area.
     * 
     */
    @Override
    protected void triggerPopup(final Component invoker, final MouseEvent e) {
        final JPopupMenu jPopupMenu = MenuFactory.createPopup();
        jPopupMenu.add(menuItemFactory.createPopupItem(ActionId.CONTACT_CREATE_INCOMING_INVITATION, Data.emptyData()));
        jPopupMenu.show(invoker, e.getX(), e.getY());
    }   
}
