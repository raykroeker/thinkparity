/*
 * Feb 28, 2006
 */
package com.thinkparity.browser.platform.action.session;

import javax.swing.ImageIcon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.model.message.system.ContactInvitationMessage;
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
	public DeclineInvitation(final Browser browser) {
		super("DeclineInvitation", ID, NAME, ICON);
		this.browser = browser;
	}

	private final Browser browser;

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final Long messageId = (Long) data.get(DataKey.SYSTEM_MESSAGE_ID);
		final SystemMessageModel sMModel = getSystemMessageModel();
		final ContactInvitationMessage message =
			(ContactInvitationMessage) sMModel.read(messageId);
		getSessionModel().declineInvitation(message.getInvitedBy());
		sMModel.delete(messageId);

		browser.fireSystemMessageDeleted(messageId);
	}

	public enum DataKey { SYSTEM_MESSAGE_ID }
}
