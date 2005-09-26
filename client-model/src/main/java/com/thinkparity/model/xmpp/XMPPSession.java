/*
 * Feb 6, 2005
 */
package com.thinkparity.model.xmpp;

import java.util.Collection;


import com.thinkparity.model.parity.api.ParityObjectVersion;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.events.XMPPExtensionListener;
import com.thinkparity.model.xmpp.events.XMPPPresenceListener;
import com.thinkparity.model.xmpp.events.XMPPSessionListener;
import com.thinkparity.model.xmpp.user.User;

/**
 * XMPPSession
 * The XMPPSession is the main interface with which the client application
 * interacts.  It provides functionality for connectivity, registration of
 * listeners for session events, roster events as well as obtaining roster and
 * user information.  Additionally the session provides the capability to send
 * notes\files to other smack users.
 * @author raykroeker@gmail.com
 * @version 1.7
 */
public interface XMPPSession {

	public void addListener(final XMPPExtensionListener xmppExtensionListener);
	public void addListener(final XMPPPresenceListener xmppPresenceListener);
	public void addListener(final XMPPSessionListener xmppSessionListener);
	public void removeListener(final XMPPExtensionListener xmppExtensionListener);
	public void removeListener(final XMPPPresenceListener xmppPresenceListener);
	public void removeListener(final XMPPSessionListener xmppSessionListener);


	public void acceptPresence(final User xmppUser) throws SmackException;
	public void addRosterEntry(final User xmppUser)
			throws SmackException;
	public void denyPresence(final User xmppUser) throws SmackException;
	public void debugRoster();
	public Collection<User> getRosterEntries() throws SmackException;

	public Boolean isLoggedIn();
	public void login(final String host, final Integer port,
			final String username, final String password) throws SmackException;
	public void logout() throws SmackException;

	public void send(final User user,
			final ParityObjectVersion parityObjectVersion)
			throws SmackException;
}
