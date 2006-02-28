/*
 * Feb 28, 2006
 */
package com.thinkparity.browser.platform.action.system.message;

import javax.swing.ImageIcon;

import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.parity.ParityException;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DeleteSystemMessage extends AbstractAction {

	private static final ImageIcon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.SYSTEM_MESSAGE_DELETE;
		NAME = "Delete system message";
	}

	/**
	 * Create a DeleteSystemMessage.
	 * 
	 */
	public DeleteSystemMessage() { super("DeleteSystemMessage", ID, NAME, ICON);  }

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final Long messageId = (Long) data.get(DataKey.SYSTEM_MESSAGE_ID);
		try { getSystemMessageModel().delete(messageId); }
		catch(final ParityException px) { throw new RuntimeException(px); }
	}

	public enum DataKey { SYSTEM_MESSAGE_ID }
}
