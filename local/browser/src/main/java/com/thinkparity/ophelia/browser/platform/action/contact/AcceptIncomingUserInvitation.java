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
 * <b>Title:</b>thinkParity OpheliaUI Accept Incoming User Invitation Action<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class AcceptIncomingUserInvitation extends AbstractBrowserAction {

    /**
     * Create AcceptIncomingUserInvitation.
     * 
     * @param browser
     *            The thinkParity <code>Browser</code> application.
     */
	public AcceptIncomingUserInvitation(final Browser browser) {
		super(ActionId.CONTACT_ACCEPT_INCOMING_EMAIL_INVITATION);
	}

    /**
	 * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
	    final Long invitationId = (Long) data.get(DataKey.INVITATION_ID);

        final ContactModel contactModel = getContactModel();
        contactModel.acceptIncomingUserInvitation(invitationId);
	}

	public enum DataKey { INVITATION_ID }
}
