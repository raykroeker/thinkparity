/*
 * Created On:  Saturday Dec 9 2006, 21:48
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ContactTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.DefaultPopupDelegate;
import com.thinkparity.ophelia.browser.platform.action.contact.AcceptIncomingInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.DeclineIncomingInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.Delete;
import com.thinkparity.ophelia.browser.platform.action.contact.DeleteOutgoingInvitation;
import com.thinkparity.ophelia.browser.platform.action.profile.Update;
import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ContactTabPopupDelegate extends DefaultPopupDelegate implements
        PopupDelegate {

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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate#showForContact(com.thinkparity.codebase.model.contact.Contact)
     * 
     */
    public void showForContact(final Contact contact) {
        final Data deleteData = new Data(1);
        deleteData.set(Delete.DataKey.CONTACT_ID, contact.getId());
        add(ActionId.CONTACT_DELETE, deleteData);
        show();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate#showForInvitation(com.thinkparity.ophelia.model.contact.IncomingInvitation)
     *
     */
    public void showForInvitation(final IncomingInvitation invitation) {
        if (isOnline()) {
            final Data acceptData = new Data(1);
            acceptData.set(AcceptIncomingInvitation.DataKey.INVITATION_ID, invitation.getId());
            add(ActionId.CONTACT_ACCEPT_INCOMING_INVITATION, acceptData);

            final Data declineData = new Data(1);
            declineData.set(DeclineIncomingInvitation.DataKey.INVITATION_ID, invitation.getId());
            add(ActionId.CONTACT_DECLINE_INCOMING_INVITATION, declineData);
            show();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate#showForInvitation(com.thinkparity.ophelia.model.contact.OutgoingInvitation)
     *
     */
    public void showForInvitation(final OutgoingInvitation invitation) {
        if (isOnline()) {
            final Data deleteData = new Data(1);
            deleteData.set(DeleteOutgoingInvitation.DataKey.INVITATION_ID, invitation.getId());
            add(ActionId.CONTACT_DELETE_OUTGOING_INVITATION, deleteData);
            show();
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate#showForProfile(com.thinkparity.codebase.model.profile.Profile)
     */
    public void showForProfile(final Profile profile) {
        final Data profileData = new Data(1);
        profileData.set(Update.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
        add(ActionId.PROFILE_UPDATE, profileData);
        show();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanelPopupDelegate#showForPanel(com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel)
     * 
     */
    public void showForPanel(final TabPanel tabPanel) {
        showForContact(((ContactTabPanel) tabPanel).getContact());
    }

    /**
     * Determine whether or not we are online.
     * 
     * @return True if we are online.
     */
    private boolean isOnline() {
        return model.isOnline().booleanValue();
    }
}
