/*
 * Feb 28, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import com.thinkparity.codebase.model.contact.IncomingUserInvitation;

import com.thinkparity.ophelia.model.contact.ContactModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity OpheliaUI Decline Incoming User Invitation Action<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DeclineIncomingUserInvitation extends AbstractBrowserAction {

    /** The thinkParity <code>Browser</code> application. */
    private final Browser browser;

	/**
     * Create DeclineIncomingUserInvitation.
     * 
     * @param browser
     *            The thinkParity <code>Browser</code> application.
     */
	public DeclineIncomingUserInvitation(final Browser browser) {
		super(ActionId.CONTACT_DECLINE_INCOMING_USER_INVITATION);
        this.browser = browser;
	}

    /**
	 * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
	    final Long invitationId = (Long) data.get(DataKey.INVITATION_ID);

        final ContactModel contactModel = getContactModel();
        final IncomingUserInvitation invitation = contactModel.readIncomingUserInvitation(invitationId);
        if (null == invitation) {
            logger.logInfo("Invitation no longer exists.");
        } else {
            if (browser.confirm("DeclineIncomingUserInvitation.ConfirmDecline",
                    new Object[] { invitation.getExtendedBy().getName() })) {
                contactModel.declineInvitation(invitation);
            }
        }
        // clear any displayed notifications
        final Data clearData = new Data(1);
        clearData.set(ClearIncomingUserInvitationNotifications.DataKey.INVITATION_ID, invitationId);
        invoke(ActionId.CONTACT_CLEAR_INCOMING_USER_INVITATION_NOTIFICATIONS, clearData);
	}

	public enum DataKey { INVITATION_ID }
}
