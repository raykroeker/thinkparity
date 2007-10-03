/*
 * Created On:  Saturday Dec 9 2006, 21:44
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingInvitation;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.application.browser.DefaultBrowserActionDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate;
import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ActionDelegate;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionInvocation;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.contact.AcceptIncomingEMailInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.AcceptIncomingUserInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.DeclineIncomingEMailInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.DeclineIncomingUserInvitation;

/**
 * <b>Title:</b>thinkParity OpheliaUI Contact Tab Action Delegate<br>
 * <b>Description:</b>Responsible for invoking various actions for the data
 * types presented by the contact tab.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
final class ContactTabActionDelegate extends DefaultBrowserActionDelegate implements
        ActionDelegate, TabButtonActionDelegate {
    
    /** The <code>ContactTabModel</code>. */
    private final ContactTabModel model;
    
    /** The accept e-mail invitation <code>AbstractAction</code>. */
    private final ActionInvocation acceptIncomingEMailInvitation;

    /** The accept user invitation <code>AbstractAction</code>. */
    private final ActionInvocation acceptIncomingUserInvitation;
    
    /** The decline email invitation <code>AbstractAction</code>. */
    private final ActionInvocation declineIncomingEMailInvitation;

    /** The decline user invitation <code>AbstractAction</code>. */
    private final ActionInvocation declineIncomingUserInvitation;

    /**
     * Create ContactTabActionDelegate.
     * 
     * @param model
     *            The <code>ContactTabModel</code>.
     */
    ContactTabActionDelegate(final ContactTabModel model) {
        super();
        this.model = model;
        this.acceptIncomingEMailInvitation = getInstance(ActionId.CONTACT_ACCEPT_INCOMING_EMAIL_INVITATION);
        this.acceptIncomingUserInvitation = getInstance(ActionId.CONTACT_ACCEPT_INCOMING_USER_INVITATION);
        this.declineIncomingEMailInvitation = getInstance(ActionId.CONTACT_DECLINE_INCOMING_EMAIL_INVITATION);
        this.declineIncomingUserInvitation = getInstance(ActionId.CONTACT_DECLINE_INCOMING_USER_INVITATION);
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ActionDelegate#invokeForContact(com.thinkparity.codebase.model.contact.Contact)
     */
    public void invokeForContact(final Contact contact) {}

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ActionDelegate#invokeForProfile(com.thinkparity.codebase.model.profile.Profile)
     */
    public void invokeForProfile(final Profile profile) {}

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ActionDelegate#invokeForInvitation(com.thinkparity.codebase.model.contact.IncomingEMailInvitation,
     *      com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ActionDelegate.Action)
     * 
     */
    public void invokeForInvitation(final IncomingEMailInvitation invitation, final Action action) {
        if (isOnline()) {
            switch (action) {
            case ACCEPT:
                final Data acceptData = new Data(1);
                acceptData.set(AcceptIncomingEMailInvitation.DataKey.INVITATION_ID, invitation.getId());
                acceptIncomingEMailInvitation.invokeAction(getApplication(), acceptData);
                break;
            case DECLINE:
                final Data declineData = new Data(2);
                declineData.set(DeclineIncomingEMailInvitation.DataKey.CONFIRM, Boolean.TRUE);
                declineData.set(DeclineIncomingEMailInvitation.DataKey.INVITATION_ID, invitation.getId());
                declineIncomingEMailInvitation.invokeAction(getApplication(), declineData);
                break;
            default:
                Assert.assertUnreachable("Unknown incoming invitation action.");
            }
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ActionDelegate#invokeForInvitation(com.thinkparity.codebase.model.contact.IncomingUserInvitation,
     *      com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ActionDelegate.Action)
     * 
     */
    public void invokeForInvitation(final IncomingUserInvitation invitation, final Action action) {
        if (isOnline()) {
            switch (action) {
            case ACCEPT:
                final Data acceptData = new Data(1);
                acceptData.set(AcceptIncomingUserInvitation.DataKey.INVITATION_ID, invitation.getId());
                acceptIncomingUserInvitation.invokeAction(getApplication(), acceptData);
                break;
            case DECLINE:
                final Data declineData = new Data(2);
                declineData.set(DeclineIncomingUserInvitation.DataKey.CONFIRM, Boolean.TRUE);
                declineData.set(DeclineIncomingUserInvitation.DataKey.INVITATION_ID, invitation.getId());
                declineIncomingUserInvitation.invokeAction(getApplication(), declineData);
                break;
            default:
                Assert.assertUnreachable("Unknown incoming invitation action.");
            }
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ActionDelegate#invokeForInvitation(com.thinkparity.codebase.model.contact.OutgoingInvitation)
     * 
     */
    public void invokeForInvitation(final OutgoingInvitation invitation) {}

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate#invokeForTabButton()
     */
    public void invokeForTabButton() {
    }

    /**
     * Determine whether or not we are online.
     * 
     * @return True if we are online.
     */
    private boolean isOnline() {
        return model.isOnline().booleanValue();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.TabButtonActionDelegate#isTabButtonActionAvailable()
     */
    public Boolean isTabButtonActionAvailable() {
        return Boolean.FALSE;
    }
}
