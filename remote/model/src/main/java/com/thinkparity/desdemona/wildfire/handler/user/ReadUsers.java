/*
 * Mar 1, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.user;

import java.util.List;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;


import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.user.User;
import com.thinkparity.desdemona.util.xmpp.packet.user.IQReadUsers;
import com.thinkparity.desdemona.wildfire.handler.IQAction;
import com.thinkparity.desdemona.wildfire.handler.IQHandler;

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
	 * @see com.thinkparity.desdemona.wildfire.handler.IQHandler#handleIQ(org.xmpp.packet.IQ, com.thinkparity.desdemona.model.session.Session)
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
