/*
 * Feb 28, 2006
 */
package com.thinkparity.ophelia.browser.platform.action.contact;

import com.thinkparity.ophelia.browser.application.browser.Browser;
import com.thinkparity.ophelia.browser.platform.action.AbstractAction;
import com.thinkparity.ophelia.browser.platform.action.ActionId;
import com.thinkparity.ophelia.browser.platform.action.Data;
import com.thinkparity.ophelia.model.contact.ContactModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AcceptIncomingInvitation extends AbstractAction {

	/** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

	/** Create AcceptInvitation. */
	public AcceptIncomingInvitation(final Browser browser) {
		super(ActionId.CONTACT_ACCEPT_INCOMING_INVITATION);
	}

    /**
	 * @see com.thinkparity.ophelia.browser.platform.action.AbstractAction#invoke(com.thinkparity.ophelia.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) {
	    final Long invitationId = (Long) data.get(DataKey.INVITATION_ID);

        final ContactModel contactModel = getContactModel();
        contactModel.acceptIncomingInvitation(invitationId);
	}

	public enum DataKey { INVITATION_ID }
}
