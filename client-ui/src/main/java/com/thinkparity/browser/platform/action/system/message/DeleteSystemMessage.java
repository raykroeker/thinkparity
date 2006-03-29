/*
 * Feb 28, 2006
 */
package com.thinkparity.browser.platform.action.system.message;

import javax.swing.ImageIcon;

import com.thinkparity.browser.application.browser.Browser;
import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

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

	private final Browser browser;

	/**
	 * Create a DeleteSystemMessage.
	 * 
	 */
	public DeleteSystemMessage(final Browser browser) {
		super("DeleteSystemMessage", ID, NAME, ICON); 
		this.browser = browser;
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
		final Long messageId = (Long) data.get(DataKey.SYSTEM_MESSAGE_ID);
		getSystemMessageModel().delete(messageId);

		browser.fireSystemMessageDeleted(messageId);
	}

	public enum DataKey { SYSTEM_MESSAGE_ID }
}
