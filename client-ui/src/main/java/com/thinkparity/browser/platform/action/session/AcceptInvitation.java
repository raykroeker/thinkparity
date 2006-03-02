/*
 * Feb 28, 2006
 */
package com.thinkparity.browser.platform.action.session;

import javax.swing.ImageIcon;

import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.message.system.ContactInvitationMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AcceptInvitation extends AbstractAction {

	private static final ImageIcon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.SESSION_ACCEPT_INVITATION;
		NAME = "Accept Invitation";
	}

	/**
	 * Create a AcceptInvitation.
	 * 
	 */
	public AcceptInvitation() { super("AcceptInvitation", ID, NAME, ICON); }

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final Long messageId = (Long) data.get(DataKey.SYSTEM_MESSAGE_ID);
		try {
			final SystemMessageModel sMModel = getSystemMessageModel();
			final ContactInvitationMessage message =
				(ContactInvitationMessage) sMModel.read(messageId);
			getSessionModel().acceptInvitation(message.getInvitedBy());
			sMModel.delete(messageId);
		}
		catch(final ParityException px) { throw new RuntimeException(px); }
	}

	public enum DataKey { SYSTEM_MESSAGE_ID }
}
