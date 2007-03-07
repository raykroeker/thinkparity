/*
 * Feb 28, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import com.thinkparity.ophelia.model.contact.ContactModel;
import com.thinkparity.ophelia.model.contact.IncomingInvitation;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractBrowserAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DeclineIncomingInvitation extends AbstractBrowserAction {

	/** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The thinkParity browser application. */
    private final Browser browser;

	/** Create DeclineIncomingInvitation. */
	public DeclineIncomingInvitation(final Browser browser) {
		super(ActionId.CONTACT_DECLINE_INCOMING_INVITATION);
        this.browser = browser;
	}

    /**
	 * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
	    final Long invitationId = (Long) data.get(DataKey.INVITATION_ID);

        final ContactModel contactModel = getContactModel();
        final IncomingInvitation invitation = contactModel.readIncomingInvitation(invitationId);
        if (browser.confirm(
                "ContactIncomingInvitationDecline.ConfirmDeclineMessage",
                new Object[] { invitation.getInvitedBy().getName() })) {
            contactModel.declineIncomingInvitation(invitationId);
        }
	}

	public enum DataKey { INVITATION_ID }
}
