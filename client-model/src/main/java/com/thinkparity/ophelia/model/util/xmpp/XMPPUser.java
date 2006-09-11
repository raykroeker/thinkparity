/*
 * Created On: Mar 1, 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.Set;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.IQ;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.user.User;


import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.ophelia.model.util.smack.SmackException;
import com.thinkparity.ophelia.model.util.smackx.packet.user.IQReadUsers;
import com.thinkparity.ophelia.model.util.smackx.packet.user.IQReadUsersResult;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class XMPPUser extends AbstractXMPP {

    /**
	 * Create a XMPPUser.
	 * 
	 * @param xmppCore
	 *            The core xmpp interface.
	 */
	public XMPPUser(final XMPPCore xmppCore) {
		super(xmppCore);
	}

	/**
	 * Add the packet listeners to the connection.
	 * 
	 * @param xmppConnection
	 *            The xmpp connection.
	 */
	void addPacketListeners(final XMPPConnection xmppConnection) {}

    /**
     * Extract the user from the xmpp response.
     * 
     * @param response
     *            An <code>XMPPMethodResponse</code>.
     * @return A <code>User</code>.
     */
    User extract(final XMPPMethodResponse response) {
        final User user = new User();
        user.setId(response.readResultJabberId("id"));
        user.setName(response.readResultString("name"));
        final String organization = response.readResultString("organization");
        if (null != organization)
            user.setOrganization(organization);
        final String title = response.readResultString("title");
        if (null != title)
            user.setTitle(title);
        return user;
    }

    /**
     * Build a user from a jabber id.
     * 
     * @param jabberId
     *            The jabber id.
     * @return A user.
     * @throws SmackException
     */
    User read(final JabberId userId) {
        logApiId();
        logVariable("userId", userId);
        final XMPPMethod read = new XMPPMethod("user:read");
        read.setParameter("userId", userId);
        final XMPPMethodResponse response = execute(read, Boolean.TRUE);
        return logVariable("user", extract(response));
    }

	/**
     * Read a set of users.
     * 
     * @param jabberIds
     *            A set of user ids.
     * @return A set of users.
     * @throws SmackException
     */
	Set<User> read(final Set<JabberId> jabberIds) throws SmackException {
		logApiId();
		logVariable("jabberIds", jabberIds);
		final IQ iq = new IQReadUsers(jabberIds);
		final IQReadUsersResult result =
			(IQReadUsersResult) xmppCore.sendAndConfirmPacket(iq);
		return result.getUsers();
	}
}
