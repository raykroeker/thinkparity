/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.javax.swing.action.session;

import java.util.Collection;
import java.util.UUID;

import javax.swing.Icon;

import com.thinkparity.browser.javax.swing.action.AbstractAction;
import com.thinkparity.browser.javax.swing.action.Data;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Send extends AbstractAction {

	public enum DataKey { DOCUMENT_ID, USERS }

	private static final Icon ICON;

	private static final String NAME;

	static {
		NAME = "Send";
		ICON = null;
	}

	/**
	 * Create a Send.
	 * 
	 */
	public Send() { super(NAME, ICON); }

	/**
	 * @see com.thinkparity.browser.javax.swing.action.AbstractAction#invoke(com.thinkparity.browser.javax.swing.action.Data)
	 */
	public void invoke(Data data) throws Exception {
		final UUID documentId = (UUID) data.get(DataKey.DOCUMENT_ID);
		final Collection<User> users = (Collection<User>) data.get(DataKey.USERS);
		getSessionModel().send(users, documentId);
	}
}
