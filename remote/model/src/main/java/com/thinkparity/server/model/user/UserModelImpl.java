/*
 * Mar 1, 2006
 */
package com.thinkparity.server.model.user;

import java.util.LinkedList;
import java.util.List;

import org.jivesoftware.messenger.vcard.VCardManager;

import com.thinkparity.server.JabberId;
import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityErrorTranslator;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.session.Session;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class UserModelImpl extends AbstractModelImpl {

	/**
	 * The default vcard manager.
	 */
	private final VCardManager vCardManager;

	/**
	 * Create a UserModelImpl.
	 * 
	 * @param session
	 *            The user session.
	 */
	UserModelImpl(final Session session) {
		super(session);
		this.vCardManager = VCardManager.getInstance();
	}

	List<User> readUsers(final List<JabberId> jabberIds)
			throws ParityServerModelException {
		logger.info("readUsers()");
		try {
			final List<User> users = new LinkedList<User>();
			User user;
			for(final JabberId jabberId : jabberIds) {
				user = new User();
				user.setId(jabberId);
				user.setVCard(vCardManager.getVCard(jabberId.getUsername()));
				users.add(user);
			}
			return users;
		}
		catch(final RuntimeException rx) {
			logger.error("Could not read users.", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}
}
