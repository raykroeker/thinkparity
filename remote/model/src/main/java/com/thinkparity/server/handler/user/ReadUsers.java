/*
 * Mar 1, 2006
 */
package com.thinkparity.server.handler.user;

import java.util.List;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.server.handler.IQAction;
import com.thinkparity.server.handler.IQHandler;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.session.Session;
import com.thinkparity.server.model.user.User;
import com.thinkparity.server.org.xmpp.packet.user.IQReadUsers;

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
	 * @see com.thinkparity.server.handler.IQHandler#handleIQ(org.xmpp.packet.IQ, com.thinkparity.server.model.session.Session)
	 * 
	 */
	public IQ handleIQ(final IQ iq, final Session session)
			throws ParityServerModelException, UnauthorizedException {
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
