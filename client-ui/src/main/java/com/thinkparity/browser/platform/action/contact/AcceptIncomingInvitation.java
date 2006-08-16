/*
 * Feb 28, 2006
 */
package com.thinkparity.browser.platform.action.contact;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.contact.ContactModel;
import com.thinkparity.model.parity.model.contact.IncomingInvitation;
import com.thinkparity.model.parity.model.user.UserModel;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AcceptIncomingInvitation extends AbstractAction {

	/** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** The thinkParity browser application. */
    private final Browser browser;

	/** Create AcceptInvitation. */
	public AcceptIncomingInvitation(final Browser browser) {
		super(ActionId.CONTACT_ACCEPT_INCOMING_INVITATION);
        this.browser = browser;
	}

    /**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
	    final Long invitationId = (Long) data.get(DataKey.INVITATION_ID);

        final ContactModel contactModel = getContactModel();
        final UserModel userModel = getUserModel();
        final IncomingInvitation invitation = contactModel.readIncomingInvitation(invitationId);
        final User invitedBy = userModel.read(invitation.getUserId());
        if (browser.confirm(
                "ContactIncomingInvitationAccept.ConfirmAcceptMessage",
                new Object[] { invitedBy.getName() })) {
            contactModel.acceptIncomingInvitation(invitationId);
        }
	}

	public enum DataKey { INVITATION_ID }
}
