/*
 * Feb 6, 2005
 */
package com.thinkparity.model.xmpp;

import java.util.Collection;
import java.util.UUID;

import com.thinkparity.model.parity.api.ParityObjectFlag;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.document.XMPPDocument;
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

	public void acceptPresence(final User user) throws SmackException;
	public void addListener(final XMPPExtensionListener xmppExtensionListener);
	public void addListener(final XMPPPresenceListener xmppPresenceListener);
	public void addListener(final XMPPSessionListener xmppSessionListener);
	public void addRosterEntry(final User user) throws SmackException;
	public void create(final UUID artifactUUID) throws SmackException;
	public void denyPresence(final User user) throws SmackException;
	public void flag(final UUID artifactUUID, final ParityObjectFlag flag)
			throws SmackException;
	public Collection<User> getRosterEntries() throws SmackException;
	public User getUser() throws SmackException;
	public Boolean isLoggedIn();
	public void login(final String host, final Integer port,
			final String username, final String password) throws SmackException;
	public void logout() throws SmackException;
	public void removeListener(final XMPPExtensionListener xmppExtensionListener);
	public void removeListener(final XMPPPresenceListener xmppPresenceListener);
	public void removeListener(final XMPPSessionListener xmppSessionListener);
	public void send(final Collection<User> users, final String message)
			throws SmackException;
	public void send(final Collection<User> users,
			final XMPPDocument xmppDocument) throws SmackException;
	public void subscribe(final UUID artifactUUID) throws SmackException;
	public void unsubscribe(final UUID artifactUUID) throws SmackException;
	public void updateRosterEntry(final User user);
}
