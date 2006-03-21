/*
 * Jan 19, 2006
 */
package com.thinkparity.browser.platform.action.session;

import java.util.Collection;

import javax.swing.Icon;

import com.thinkparity.browser.platform.action.AbstractAction;
import com.thinkparity.browser.platform.action.ActionId;
import com.thinkparity.browser.platform.action.Data;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Send extends AbstractAction {

	public enum DataKey { DOCUMENT_ID, USERS }

	private static final Icon ICON;

	private static final ActionId ID;

	private static final String NAME;

	static {
		ICON = null;
		ID = ActionId.SESSION_SEND;
		NAME = "Send";
	}

	/**
	 * Create a Send.
	 * 
	 */
	public Send() { super("Session.Send", ID, NAME, ICON); }

	/**
	 * @see com.thinkparity.browser.platform.action.AbstractAction#invoke(com.thinkparity.browser.platform.action.Data)
	 */
	public void invoke(Data data) throws Exception {
		final Long documentId = (Long) data.get(DataKey.DOCUMENT_ID);
		final Collection<User> users = (Collection<User>) data.get(DataKey.USERS);
		getSessionModel().send(users, documentId);
		getArtifactModel().applyFlagSeen(documentId);
	}
}
