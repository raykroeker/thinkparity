/*
 * Feb 28, 2006
 */
package com.thinkparity.browser.platform.action.session;

import javax.swing.ImageIcon;

import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.message.system.PresenceRequestMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessageModel;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DeclineInvitation extends AbstractAction {

	private static final ImageIcon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.SESSION_DECLINE_INVITATION;
		NAME = "Decline Invitation";
	}

	/**
	 * Create a DeclineInvitation.
	 * 
	 */
	public DeclineInvitation() { super("DeclineInvitation", ID, NAME, ICON);  }

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final Long messageId = (Long) data.get(DataKey.SYSTEM_MESSAGE_ID);
		try {
			final SystemMessageModel sMModel = getSystemMessageModel();
			final PresenceRequestMessage message =
				(PresenceRequestMessage) sMModel.read(messageId);
			getSessionModel().declineInvitation(message.getRequestedBy());
			sMModel.delete(messageId);
		}
		catch(final ParityException px) { throw new RuntimeException(px); }
	}

	public enum DataKey { SYSTEM_MESSAGE_ID }
}
