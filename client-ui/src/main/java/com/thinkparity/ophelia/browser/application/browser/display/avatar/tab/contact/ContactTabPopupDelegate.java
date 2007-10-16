/*
 * Created On:  Saturday Dec 9 2006, 21:48
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;

import com.thinkparity.ophelia.browser.application.browser.DefaultBrowserPopupDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ContactTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.*;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ContactTabPopupDelegate extends DefaultBrowserPopupDelegate
        implements PopupDelegate {

    /** The <code>ContactTabModel</code>. */
    private final ContactTabModel model;

    /**
     * Create ContactTabPopupDelegate.
     * 
     * @param model
     *            The <code>ContactTabModel</code>.
     */
    ContactTabPopupDelegate(final ContactTabModel model) {
        super();
        this.model = model;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate#showForContact(com.thinkparity.codebase.model.contact.Contact, boolean)
     * 
     */
    public void showForContact(final Contact contact, final boolean expanded) {
        final boolean online = isOnline();

        if (online) {
            final Data deleteData = new Data(1);
            deleteData.set(Delete.DataKey.CONTACT_ID, contact.getLocalId());
            add(ActionId.CONTACT_DELETE, deleteData);

            show();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate#showForInvitation(com.thinkparity.codebase.model.contact.IncomingEMailInvitation)
     * 
     */
    public void showForInvitation(final IncomingEMailInvitation invitation) {
        if (isOnline()) {
            final Data acceptData = new Data(1);
            acceptData.set(AcceptIncomingEMailInvitation.DataKey.INVITATION_ID, invitation.getId());
            add(ActionId.CONTACT_ACCEPT_INCOMING_EMAIL_INVITATION, acceptData);

            final Data declineData = new Data(2);
            declineData.set(DeclineIncomingEMailInvitation.DataKey.CONFIRM, Boolean.TRUE);
            declineData.set(DeclineIncomingEMailInvitation.DataKey.INVITATION_ID, invitation.getId());
            add(ActionId.CONTACT_DECLINE_INCOMING_EMAIL_INVITATION, declineData);
            show();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate#showForInvitation(com.thinkparity.codebase.model.contact.IncomingUserInvitation)
     * 
     */
    public void showForInvitation(final IncomingUserInvitation invitation) {
        if (isOnline()) {
            final Data acceptData = new Data(1);
            acceptData.set(AcceptIncomingUserInvitation.DataKey.INVITATION_ID, invitation.getId());
            add(ActionId.CONTACT_ACCEPT_INCOMING_USER_INVITATION, acceptData);

            final Data declineData = new Data(2);
            declineData.set(DeclineIncomingUserInvitation.DataKey.CONFIRM, Boolean.TRUE);
            declineData.set(DeclineIncomingUserInvitation.DataKey.INVITATION_ID, invitation.getId());
            add(ActionId.CONTACT_DECLINE_INCOMING_USER_INVITATION, declineData);
            show();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate#showForInvitation(com.thinkparity.ophelia.model.contact.OutgoingEMailInvitation)
     * 
     */
    public void showForInvitation(final OutgoingEMailInvitation invitation) {
        if (isOnline()) {
            final Data deleteData = new Data(1);
            deleteData.set(DeleteOutgoingEMailInvitation.DataKey.INVITATION_ID, invitation.getId());
            add(ActionId.CONTACT_DELETE_OUTGOING_EMAIL_INVITATION, deleteData);
            show();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate#showForInvitation(com.thinkparity.ophelia.model.contact.OutgoingEMailInvitation)
     * 
     */
    public void showForInvitation(final OutgoingUserInvitation invitation) {
        if (isOnline()) {
            final Data deleteData = new Data(1);
            deleteData.set(DeleteOutgoingUserInvitation.DataKey.INVITATION_ID, invitation.getId());
            add(ActionId.CONTACT_DELETE_OUTGOING_USER_INVITATION, deleteData);
            show();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate#showForPanel(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     * 
     */
    public void showForPanel(final TabPanel tabPanel) {
        showForContact(((ContactTabPanel) tabPanel).getContact(), false);
    }

    /**
     * Determine whether or not the user experiences online behavior.
     * 
     * @return True if the user experiences online behavior.
     */
    private boolean isOnline() {
        return model.isOnlineUI().booleanValue();
    }
}
