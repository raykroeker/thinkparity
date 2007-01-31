/*
 * Created On:  Saturday Dec 9 2006, 21:44
 */
package com.thinkparity.ophelia.browser.application.browser.display.avatar.tab.contact;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.profile.Profile;

import com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ActionDelegate;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.ActionInvocation;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.browser.platform.action.DefaultActionDelegate;
import com.thinkparity.ophelia.browser.platform.action.contact.AcceptIncomingInvitation;
import com.thinkparity.ophelia.browser.platform.action.contact.DeclineIncomingInvitation;
import com.thinkparity.ophelia.model.contact.IncomingInvitation;
import com.thinkparity.ophelia.model.contact.OutgoingInvitation;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class ContactTabActionDelegate extends DefaultActionDelegate implements
        ActionDelegate {
    
    /** The <code>ContactTabModel</code>. */
    private final ContactTabModel model;
    
    /** The accept invitation <code>AbstractAction</code>. */
    private final ActionInvocation acceptIncomingInvitation;
    
    /** The decline invitation <code>AbstractAction</code>. */
    private final ActionInvocation declineIncomingInvitation;

    /**
     * Create ContactTabActionDelegate.
     * 
     * @param model
     *            The <code>ContactTabModel</code>.
     */
    ContactTabActionDelegate(final ContactTabModel model) {
        super();
        this.model = model;
        this.acceptIncomingInvitation = getInstance(ActionId.CONTACT_ACCEPT_INCOMING_INVITATION);
        this.declineIncomingInvitation = getInstance(ActionId.CONTACT_DECLINE_INCOMING_INVITATION);
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
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ActionDelegate#invokeForInvitation(com.thinkparity.ophelia.model.contact.IncomingInvitation)
     */
    public void invokeForInvitation(final IncomingInvitation invitation, final Action action) {
        if (isOnline()) {
            if (Action.ACCEPT == action) {
                final Data acceptData = new Data(1);
                acceptData.set(AcceptIncomingInvitation.DataKey.INVITATION_ID, invitation.getId());
                acceptIncomingInvitation.invokeAction(acceptData);
            } else if (Action.DECLINE == action) {
                final Data declineData = new Data(1);
                declineData.set(DeclineIncomingInvitation.DataKey.INVITATION_ID, invitation.getId());
                declineIncomingInvitation.invokeAction(declineData);
            } else {
                Assert.assertUnreachable("Unknown incoming invitation action.");
            }
        }
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.browser.display.renderer.tab.contact.ActionDelegate#invokeForInvitation(com.thinkparity.ophelia.model.contact.OutgoingInvitation)
     */
    public void invokeForInvitation(final OutgoingInvitation invitation) {}
    
    /**
     * Determine whether or not we are online.
     * 
     * @return True if we are online.
     */
    private boolean isOnline() {
        return model.isOnline().booleanValue();
    }
}
