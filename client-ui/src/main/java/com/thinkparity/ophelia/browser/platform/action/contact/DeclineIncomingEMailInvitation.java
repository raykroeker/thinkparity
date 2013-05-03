/*
 * Feb 28, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;

import com.thinkparity.ophelia.model.contact.ContactModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity OpheliaUI Decline Incoming EMail Invitation Action<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DeclineIncomingEMailInvitation extends AbstractBrowserAction {

    /** The thinkParity <code>Browser</code> application. */
    private final Browser browser;

    /**
     * Create DeclineIncomingEMailInvitation.
     * 
     * @param browser
     *            The thinkParity <code>Browser</code> application.
     */
	public DeclineIncomingEMailInvitation(final Browser browser) {
		super(ActionId.CONTACT_DECLINE_INCOMING_EMAIL_INVITATION);
        this.browser = browser;
	}

    /**
	 * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
        final Boolean confirm = (Boolean) data.get(DataKey.CONFIRM);
	    final Long invitationId = (Long) data.get(DataKey.INVITATION_ID);

        final ContactModel contactModel = getContactModel();
        final IncomingEMailInvitation invitation = contactModel.readIncomingEMailInvitation(invitationId);
        if (null == invitation) {
            logger.logInfo("Invitation no longer exists.");
            clearDisplayedNotifications(invitationId);
        } else {
            if (!confirm || browser.confirm("DeclineIncomingEMailInvitation.ConfirmDecline",
                    new Object[] { invitation.getExtendedBy().getName() })) {
                contactModel.declineInvitation(invitation);
                clearDisplayedNotifications(invitationId);
            }
        }
	}

    /**
     * Clear displayed notifications.
     * 
     * @param invitationId
     *            The <code>Long</code> invitation id.
     */
    private void clearDisplayedNotifications(final Long invitationId) {
        final Data clearData = new Data(1);
        clearData.set(ClearIncomingEMailInvitationNotifications.DataKey.INVITATION_ID, invitationId);
        invoke(ActionId.CONTACT_CLEAR_INCOMING_EMAIL_INVITATION_NOTIFICATIONS, clearData);
    }

	public enum DataKey { CONFIRM, INVITATION_ID }
}
