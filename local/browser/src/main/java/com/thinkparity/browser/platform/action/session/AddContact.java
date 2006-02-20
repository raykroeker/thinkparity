/*
 * Feb 20, 2006
 */
package com.thinkparity.browser.platform.action.session;

import javax.swing.Icon;

import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AddContact extends AbstractAction {

	private static final Icon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.SESSION_ADD_CONTACT;
		NAME = "Add Contact";
	}

	/**
	 * Create a AddContact.
	 */
	public AddContact() {
		super("Session.AddContact", ID, NAME, ICON);
	}

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 * 
	 */
	public void invoke(final Data data) throws Exception {
//		getController().displaySessionAddContactFormAvatar();
	}
}
