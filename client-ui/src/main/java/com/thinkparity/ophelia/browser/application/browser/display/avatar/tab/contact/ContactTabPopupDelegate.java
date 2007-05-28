/*
 * Created On:  Saturday Dec 9 2006, 21:48
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.application.browser.DefaultBrowserPopupDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ContactTabPanel;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.*;
import com.thinkparity.ophelia.browser.platform.action.profile.Update;
import com.thinkparity.ophelia.browser.platform.action.profile.UpdatePassword;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ContactTabPopupDelegate extends DefaultBrowserPopupDelegate
        implements PopupDelegate {

    /** A list of action ids, used for the contact popup. */
    private final List<ActionId> actionIds;

    /** A list of data, used for the container popup. */
    private final List<Data> dataList;

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
        this.actionIds = new ArrayList<ActionId>();
        this.dataList = new ArrayList<Data>();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate#showForContact(com.thinkparity.codebase.model.contact.Contact, boolean)
     * 
     */
    public void showForContact(final Contact contact, final boolean expanded) {
        final boolean online = isOnline();

        if (online) {
            final Data deleteData = new Data(1);
            deleteData.set(Delete.DataKey.CONTACT_ID, contact.getId());
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

            final Data declineData = new Data(1);
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

            final Data declineData = new Data(1);
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.PopupDelegate#showForProfile(com.thinkparity.codebase.model.profile.Profile, boolean)
     */
    public void showForProfile(final Profile profile, final boolean expanded) {
        final boolean online = isOnline();

        if (online) {
            final Data profileData = new Data(1);
            profileData.set(Update.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
            addWithExpand(ActionId.PROFILE_UPDATE, profileData, profile.getId());

            final Data updateProfileData = new Data(1);
            updateProfileData.set(UpdatePassword.DataKey.DISPLAY_AVATAR, Boolean.TRUE);
            addWithExpand(ActionId.PROFILE_UPDATE_PASSWORD, updateProfileData, profile.getId());
            
            show();
        }
    }

    /**
     * Add an action to a popup menu.
     * A second action to expand the contact is inserted.
     * 
     * @param actionId
     *            An <code>ActionId</code>.
     * @param data
     *            The <code>Data</code>.       
     * @param contactId
     *            A <code>JabberId</code>.          
     */
    private void addWithExpand(final ActionId actionId, final Data data,
            final JabberId contactId) {
        actionIds.clear();
        dataList.clear();
        
        final Data expandData = new Data(1);
        expandData.set(Expand.DataKey.CONTACT_ID, contactId);
        actionIds.add(ActionId.CONTACT_EXPAND);
        dataList.add(expandData);
        
        actionIds.add(actionId);
        dataList.add(data);
        add(actionIds, dataList, 1);
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
