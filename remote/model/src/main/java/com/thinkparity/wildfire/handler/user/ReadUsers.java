/*
 * Mar 1, 2006
 */
package com.thinkparity.wildfire.handler.user;

import java.util.List;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.model.ParityServerModelException;
import com.thinkparity.model.session.Session;
import com.thinkparity.model.user.User;

import com.thinkparity.server.org.xmpp.packet.user.IQReadUsers;
import com.thinkparity.wildfire.handler.IQAction;
import com.thinkparity.wildfire.handler.IQHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReadUsers extends IQHandler {

	/**
	 * Create a ReadUsers.
	 * @param action
	 */
	public ReadUsers() { super(IQAction.READUSERS); }

	/**
	 * @see com.thinkparity.wildfire.handler.IQHandler#handleIQ(org.xmpp.packet.IQ, com.thinkparity.model.session.Session)
	 * 
	 */
	public IQ handleIQ(final IQ iq, final Session session)
			throws ParityServerModelException, UnauthorizedException {
        logApiId();
		final List<User> users = getUserModel(session).readUsers(extractJabberIds(iq));
		return createResult(iq, session, users);
	}

	private IQ createResult(final IQ iq, final Session session,
			final List<User> users) {
		final IQ result = new IQReadUsers(users);
		result.setID(iq.getID());
		result.setTo(session.getJID());
		result.setFrom(session.getJID());
		return result;
	}
}
