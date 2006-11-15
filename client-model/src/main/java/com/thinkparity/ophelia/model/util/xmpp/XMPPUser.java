/*
 * Created On: Mar 1, 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.ophelia.model.util.smack.SmackException;
import com.thinkparity.ophelia.model.util.xmpp.event.UserListener;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
final class XMPPUser extends AbstractXMPP<UserListener> {

    /**
	 * Create a XMPPUser.
	 * 
	 * @param xmppCore
	 *            The core xmpp interface.
	 */
	XMPPUser(final XMPPCore xmppCore) {
		super(xmppCore);
	}

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
        logger.logApiId();
        logger.logVariable("userId", userId);
        final XMPPMethod read = new XMPPMethod("user:read");
        read.setParameter("userId", userId);
        final XMPPMethodResponse response = execute(read, Boolean.TRUE);
        return logger.logVariable("user", extract(response));
    }
}
