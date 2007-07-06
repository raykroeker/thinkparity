/*
 * Feb 28, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import com.thinkparity.ophelia.model.contact.ContactModel;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * <b>Title:</b>thinkParity OpheliaUI Accept Incoming EMail Invitation Action<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class AcceptIncomingEMailInvitation extends AbstractBrowserAction {

    /**
     * Create AcceptIncomingEMailInvitation.
     * 
     * @param browser
     *            The thinkParity <code>Browser</code> application.
     */
	public AcceptIncomingEMailInvitation(final Browser browser) {
		super(ActionId.CONTACT_ACCEPT_INCOMING_EMAIL_INVITATION);
	}

    /**
	 * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
	    final Long invitationId = (Long) data.get(DataKey.INVITATION_ID);

        final ContactModel contactModel = getContactModel();
        contactModel.acceptIncomingEMailInvitation(invitationId);

        // clear any displayed notifications
        final Data clearData = new Data(1);
        clearData.set(ClearIncomingEMailInvitationNotifications.DataKey.INVITATION_ID, invitationId);
        invoke(ActionId.CONTACT_CLEAR_INCOMING_EMAIL_INVITATION_NOTIFICATIONS, data);
	}

	public enum DataKey { INVITATION_ID }
}
