/*
 * Feb 6, 2005
 */
package com.thinkparity.model.xmpp;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.session.KeyResponse;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.document.XMPPDocument;
import com.thinkparity.model.xmpp.events.XMPPArtifactListener;
import com.thinkparity.model.xmpp.events.XMPPContactListener;
import com.thinkparity.model.xmpp.events.XMPPExtensionListener;
import com.thinkparity.model.xmpp.events.XMPPSessionListener;
import com.thinkparity.model.xmpp.user.User;
import com.thinkparity.model.xmpp.user.UserVCard;

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

	public void acceptInvitation(final JabberId jabberId) throws SmackException;
	public void addListener(final XMPPArtifactListener xmppArtifactListener);
	public void addListener(final XMPPContactListener xmppPresenceListener);
	public void addListener(final XMPPExtensionListener xmppExtensionListener);
	public void addListener(final XMPPSessionListener xmppSessionListener);
	public void create(final UUID artifactUniqueId) throws SmackException;
	public void declineInvitation(final JabberId jabberId) throws SmackException;
	public void flag(final UUID artifactUniqueId, final ArtifactFlag flag)
			throws SmackException;
	public User getArtifactKeyHolder(final UUID artifactUniqueId)
			throws SmackException;
	public User getUser() throws SmackException;
	public void inviteContact(final JabberId jabberId) throws SmackException;
	public Boolean isLoggedIn();
	public void login(final String host, final Integer port,
			final String username, final String password) throws SmackException;
	public void logout() throws SmackException;
	public void processOfflineQueue() throws SmackException;
	public List<Contact> readArtifactContacts(final UUID artifactUniqueId)
			throws SmackException;
	public List<Contact> readContacts() throws SmackException;
	public List<User> readUsers(final List<JabberId> jabberIds)
			throws SmackException;
	public UserVCard readVCard(final JabberId jabberId) throws SmackException;
	public void removeListener(final XMPPArtifactListener xmppArtifactListener);
	public void removeListener(final XMPPContactListener xmppPresenceListener);
	public void removeListener(final XMPPExtensionListener xmppExtensionListener);
	public void removeListener(final XMPPSessionListener xmppSessionListener);
	public void saveVCard(final UserVCard userVCard) throws SmackException;
	public void send(final Collection<User> users, final String message)
			throws SmackException;
	public void send(final Collection<User> users,
			final XMPPDocument xmppDocument) throws SmackException;
	public void sendClose(final UUID artifactUniqueId) throws SmackException;
	public void sendCreate(final UUID artifactUniqueId) throws SmackException;
	public void sendDelete(final UUID artifactUniqueId) throws SmackException;
	public void sendKeyRequest(final UUID artifactUniqueId) throws SmackException;
	public void sendKeyResponse(final UUID artifactUniqueId,
			final KeyResponse keyResponse, final User user)
			throws SmackException;
	public void sendLogFileArchive(final File logFileArchive, final User user)
			throws SmackException;
	public void sendSubscribe(final UUID artifactUniqueId) throws SmackException;
	public void subscribe(final UUID artifactUniqueId) throws SmackException;
	public void unsubscribe(final UUID artifactUniqueId) throws SmackException;
}
