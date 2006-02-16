/*
 * Feb 6, 2005
 */
package com.thinkparity.model.xmpp;

import java.io.File;
import java.util.*;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Presence.Mode;
import org.jivesoftware.smack.packet.Presence.Type;
import org.jivesoftware.smack.provider.ProviderManager;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.IParityModelConstants;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.session.KeyResponse;
import com.thinkparity.model.smack.SmackConnectionListener;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.smack.SmackRosterListener;
import com.thinkparity.model.smack.packet.SmackPresenceFilter;
import com.thinkparity.model.smack.packet.SmackPresenceListener;
import com.thinkparity.model.smackx.XFactory;
import com.thinkparity.model.smackx.document.XMPPDocumentXFilter;
import com.thinkparity.model.smackx.document.XMPPDocumentXListener;
import com.thinkparity.model.smackx.packet.*;
import com.thinkparity.model.xmpp.document.XMPPDocument;
import com.thinkparity.model.xmpp.events.XMPPExtensionListener;
import com.thinkparity.model.xmpp.events.XMPPPresenceListener;
import com.thinkparity.model.xmpp.events.XMPPSessionListener;
import com.thinkparity.model.xmpp.user.User;

/**
 * XMPPSessionImpl
 * Implementation of the XMPPSession interface into the Smack library.
 * @author raykroeker@gmail.com
 * @version 1.7
 */
public class XMPPSessionImpl implements XMPPSession {

	/**
	 * Interal logger implemenation.
	 */
	private static final Logger logger =
		ModelLoggerFactory.getLogger(XMPPSessionImpl.class);

	/**
	 * The number of milliseconds to sleep subsequent to each maual packet sent
	 * via the smack connection. If this number is reduced or removed, the smack
	 * library will have a tendancy to swallow messages.
	 * @see XMPPSessionImpl#send(Collection, PacketExtension)
	 */
	private static final int SEND_PACKET_SLEEP_DURATION = 75;

	static {
		// set the subscription mode such that all requests are manual
		Roster.setDefaultSubscriptionMode(Roster.SUBSCRIPTION_MANUAL);

		ProviderManager.addIQProvider("query", "jabber:iq:parity:keyrequest", new IQKeyRequestProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:acceptkeyrequest", new IQAcceptKeyRequestProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:denykeyrequest", new IQDenyKeyRequestProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:getkeyholder", new IQGetKeyHolderProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:getsubscriptions", new IQGetSubscriptionProvider());
	}

	private Map<String, User> pendingXMPPUsers;

	private SmackConnectionListenerImpl smackConnectionListenerImpl;

	private SmackPresenceFilter smackPresenceFilter;

	private SmackPresenceListenerImpl smackPresenceListenerImpl;
	private SmackRosterListenerImpl smackRosterListenerImpl;
	private XMPPConnection smackXMPPConnection;
	private Vector<XMPPExtensionListener> xmppExtensionListeners;
	private final Object xmppExtensionListenersLock = new Object();
	private Vector<XMPPPresenceListener> xmppPresenceListeners;
	private Vector<XMPPSessionListener> xmppSessionListeners;
	/**
	 * Create a XMPPSessionImpl
	 */
	XMPPSessionImpl() {
		logger.info("Jive Software:  Smack:  " + SmackConfiguration.getVersion());
		xmppExtensionListeners = new Vector<XMPPExtensionListener>(10);
		xmppPresenceListeners = new Vector<XMPPPresenceListener>(10);
		xmppSessionListeners = new Vector<XMPPSessionListener>(10);

		smackConnectionListenerImpl = new SmackConnectionListenerImpl();
		SSLXMPPConnection.addConnectionListener((ConnectionEstablishedListener) smackConnectionListenerImpl);

		pendingXMPPUsers = new Hashtable<String, User>(10);

		smackRosterListenerImpl = new SmackRosterListenerImpl();

		smackPresenceFilter = new SmackPresenceFilter();
		smackPresenceListenerImpl = new SmackPresenceListenerImpl();
	}
	/**
	 * Accept the presence request of user.  This will send a presence packet
	 * indicating that the user is subscribed and available.
	 * 
	 */
	public void acceptPresence(final User user) throws SmackException {
		logger.info("acceptPresence(UserRenderer)");
		logger.debug(user);
		try {
			// save the user for use by the roster event later on
			pendingXMPPUsers.put(user.getUsername(), user);
			sendPresencePacket(user.getUsername(), Type.SUBSCRIBED, Mode.AVAILABLE);
		}
		catch(InterruptedException ix) {
			logger.error("acceptPresence(User)", ix);
			throw XMPPErrorTranslator.translate(ix);
		}
	}
	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#addListener(com.thinkparity.model.xmpp.events.XMPPExtensionListener)
	 */
	public void addListener(XMPPExtensionListener xmppExtensionListener) {
		logger.info("addListener(XMPPExtensionListener)");
		logger.debug(xmppExtensionListener);
		Assert.assertNotNull("Cannot register a null xmpp extension listener.",
				xmppExtensionListener);
		Assert.assertTrue(
				"Cannot register an extension listener more than once.",
				!xmppExtensionListeners.contains(xmppExtensionListener));
		xmppExtensionListeners.add(xmppExtensionListener);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#addListener(com.thinkparity.model.xmpp.events.XMPPPresenceListener)
	 */
	public void addListener(final XMPPPresenceListener xmppPresenceListener) {
		logger.info("addListener(XMPPPresenceListener)");
		logger.debug(xmppPresenceListener);
		Assert.assertNotNull("Cannot register a null presence listener.",
				xmppPresenceListener);
		Assert.assertTrue("Cannot re-register the same presence listener.",
				!xmppPresenceListeners.contains(xmppPresenceListener));
		xmppPresenceListeners.add(xmppPresenceListener);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#addListener(com.thinkparity.model.xmpp.events.XMPPSessionListener)
	 */
	public void addListener(final XMPPSessionListener xmppSessionListener) {
		logger.info("addListener(XMPPSessionListener)");
		logger.debug(xmppSessionListener);
		Assert.assertNotNull("Cannot register a null session listener.",
				xmppSessionListener);
		Assert.assertTrue("Cannot re-register the same session listener.",
				!xmppSessionListeners.contains(xmppSessionListener));
		xmppSessionListeners.add(xmppSessionListener);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#addRosterEntry(com.thinkparity.model.xmpp.user.User)
	 */
	public void addRosterEntry(final User xmppUser)
			throws SmackException {
		logger.info("addRosterEntry(UserRenderer)");
		logger.debug(xmppUser);
		assertLoggedIn("addRosterEntry(UserRenderer)");
		Assert.assertNotNull("Cannot add null user to your roster.", xmppUser);
		// in phase 2, we'll implement the groups
		try {
			final Roster roster = getRoster();
			if(!roster.contains(xmppUser.getUsername()))
				roster.createEntry(xmppUser.getUsername(), xmppUser.getName(), null);
		}
		catch(XMPPException xmppx) {
			logger.error("addRosterEntry(User)", xmppx);
			throw XMPPErrorTranslator.translate(xmppx);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#create(java.util.UUID)
	 */
	public void create(final UUID artifactUniqueId) throws SmackException {
		logger.info("create(UUID)");
		logger.debug(artifactUniqueId);
		try {
			final IQArtifact iq = new IQArtifactCreate(artifactUniqueId);
			iq.setType(IQ.Type.SET);
			logger.debug(iq);
			sendPacket(iq);
		}
		catch(InterruptedException ix) {
			logger.error("create(UUID)", ix);
			throw XMPPErrorTranslator.translate(ix);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#denyPresence(com.thinkparity.model.xmpp.user.User)
	 */
	public void denyPresence(User user) throws SmackException {
		logger.info("denyPresence(UserRenderer)");
		logger.debug(user);
		try {
			// save the user for use by the roster event later on
			pendingXMPPUsers.put(user.getUsername(), user);
			sendPresencePacket(user.getUsername(), Type.UNSUBSCRIBED);
		}
		catch(InterruptedException ix) {
			logger.error("denyPresence(User)", ix);
			throw XMPPErrorTranslator.translate(ix);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#flag(java.util.UUID,
	 *      com.thinkparity.model.parity.model.artifact.ArtifactFlag)
	 */
	public void flag(final UUID artifactUniqueId, final ArtifactFlag flag)
			throws SmackException {
		logger.info("send(UUID,ArtifactFlag)");
		logger.debug(artifactUniqueId);
		logger.debug(flag);
		try {
			final IQArtifact iq = new IQArtifactFlag(artifactUniqueId, flag);
			iq.setType(IQ.Type.SET);
			logger.debug(iq);
			sendPacket(iq);
		}
		catch(InterruptedException ix) {
			logger.error("flag(UUID,ArtifactFlag)", ix);
			throw XMPPErrorTranslator.translate(ix);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#getArtifactKeyHolder(java.util.UUID)
	 * 
	 */
	public User getArtifactKeyHolder(final UUID artifactUniqueId)
			throws SmackException {
		logger.info("getArtifactKeyHolder(UUID)");
		logger.debug(artifactUniqueId);
		try {
			final IQArtifact iq = new IQGetKeyHolder(artifactUniqueId);
			iq.setType(IQ.Type.GET);
			logger.debug(iq);
			final IQGetKeyHolderResponse response =
				(IQGetKeyHolderResponse) sendAndConfirmPacket(iq);
			return new User(null, response.getUsername(), User.Presence.OFFLINE);
		}
		catch(final InterruptedException ix) {
			throw XMPPErrorTranslator.translate(ix);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#getArtifactSubscription(java.util.UUID)
	 * 
	 */
	public List<User> getArtifactSubscription(final UUID artifactUniqueId)
			throws SmackException {
		logger.info("getArtifactSubscription(UUID)");
		logger.debug(artifactUniqueId);
		try {
			final IQArtifact iq = new IQGetSubscription(artifactUniqueId);
			iq.setType(IQ.Type.GET);
			logger.debug(iq);
			final IQGetSubscriptionResponse response =
				(IQGetSubscriptionResponse) sendAndConfirmPacket(iq);
			final List<User> subscription = new LinkedList<User>();
			for(final String s : response.getJids()) {
				subscription.add(new User(null, s, User.Presence.OFFLINE));
			}
			return subscription;
		}
		catch(final InterruptedException ix) {
			throw XMPPErrorTranslator.translate(ix);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#getRosterEntries()
	 */
	public Collection<User> getRosterEntries() throws SmackException {
		logger.info("getRosterEntries()");
		assertLoggedIn("getRosterEntries()");
		final Collection<User> rosterEntries =
			new Vector<User>(getRosterEntryCount());
		final Roster roster = getRoster();
		RosterEntry rosterEntry;
		for(Iterator<?> i = roster.getEntries(); i.hasNext();) {
			rosterEntry = (RosterEntry) i.next();
			rosterEntries.add(createUser(roster, rosterEntry));
		}
		return rosterEntries;
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#getUser()
	 */
	public User getUser() {
		assertLoggedIn("getUser()");
		final String user = smackXMPPConnection.getUser();
		return new User(
				null, user.substring(0, user.indexOf("/")), User.Presence.AVAILABLE);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#isLoggedIn()
	 */
	public Boolean isLoggedIn() {
		logger.info("isLoggedIn()");
		return (null != smackXMPPConnection
				&& smackXMPPConnection.isConnected()
				&& smackXMPPConnection.isAuthenticated());
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#login(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	public void login(final String host, final Integer port,
			final String username, final String password) throws SmackException {
		logger.info("login(String, Integer, String, String)");
		logger.debug(host);
		logger.debug(port);
		logger.debug(username);
		try {
			if(Boolean.TRUE == isLoggedIn())
				logout();
			if(Boolean.getBoolean("parity.insecure")) {
				logger.warn("Non ssl connection to host");
				smackXMPPConnection = new XMPPConnection(host, port);
			}
			else { smackXMPPConnection = new SSLXMPPConnection(host, port); }
			smackXMPPConnection.addConnectionListener((ConnectionListener) smackConnectionListenerImpl);

			// packet debugger
			smackXMPPConnection.addPacketListener(
					new PacketListener() {
						public void processPacket(Packet packet) {
							logger.debug(packet);
						}
					},
					new PacketFilter() {
						public boolean accept(Packet packet) { return true; }
					});

			// key request
			smackXMPPConnection.addPacketListener(
					new PacketListener() {
						public void processPacket(Packet packet) {
							notifyXMPPExtension_keyRequested((IQKeyRequest) packet);
						}
					},
					new PacketTypeFilter(IQKeyRequest.class));
			// accept key request
			smackXMPPConnection.addPacketListener(
					new PacketListener() {
						public void processPacket(Packet packet) {
							notifyXMPPExtension_keyRequestAccepted((IQAcceptKeyRequest) packet);
						}
					},
					new PacketTypeFilter(IQAcceptKeyRequest.class));
			// deny key request
			smackXMPPConnection.addPacketListener(
					new PacketListener() {
						public void processPacket(Packet packet) {
							notifyXMPPExtension_keyRequestDenied((IQDenyKeyRequest) packet);
						}
					},
					new PacketTypeFilter(IQDenyKeyRequest.class));

			// document extension handler
			smackXMPPConnection.addPacketListener(
					new XMPPDocumentXListener() {
						public void documentRecieved(
								final XMPPDocument xmppDocument) {
							notifyXMPPExtension_documentReceived(xmppDocument);
						}
					},
					new XMPPDocumentXFilter());

			smackXMPPConnection.addPacketListener(smackPresenceListenerImpl, smackPresenceFilter);

			smackXMPPConnection.login(username, password,
					IParityModelConstants.PARITY_CONNECTION_RESOURCE);
			smackXMPPConnection.getRoster().addRosterListener(smackRosterListenerImpl);
		}
		catch(XMPPException xmppx) {
			logger.error("login(String,Integer,String,String)", xmppx);
			throw XMPPErrorTranslator.translate(xmppx);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#logout()
	 */
	public void logout() throws SmackException {
		logger.info("logout()");
		smackXMPPConnection.close();
		smackXMPPConnection = null;
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#removeListener(com.thinkparity.model.xmpp.events.XMPPExtensionListener)
	 */
	public void removeListener(XMPPExtensionListener xmppExtensionListener) {
		logger.info("removeListener(XMPPExtensionListener)");
		logger.debug(xmppExtensionListener);
		Assert.assertNotNull("Cannot remove a null xmpp extension listener.",
				xmppExtensionListener);
		Assert.assertTrue(
				"Cannot remove an extension listener that has note been registered.",
				xmppExtensionListeners.contains(xmppExtensionListener));
		xmppExtensionListeners.remove(xmppExtensionListener);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#removeListener(com.thinkparity.model.xmpp.events.XMPPPresenceListener)
	 */
	public void removeListener(final XMPPPresenceListener xmppPresenceListener) {
		logger.info("removeListener(XMPPPresenceListener)");
		logger.debug(xmppPresenceListener);
		Assert.assertNotNull("Cannot un-register a null presence listener.",
				xmppPresenceListener);
		Assert.assertTrue(
				"Cannot un-register a non-registered presence listener.",
				xmppPresenceListeners.contains(xmppPresenceListener));
		xmppPresenceListeners.remove(xmppPresenceListener);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#removeListener(com.thinkparity.model.xmpp.events.XMPPSessionListener)
	 */
	public void removeListener(final XMPPSessionListener xmppSessionListener) {
		logger.info("removeListener(XMPPSessionListener)");
		logger.debug(xmppSessionListener);
		Assert.assertNotNull("Cannot un-register a null session listener.",
				xmppSessionListener);
		Assert.assertTrue(
				"Cannot unregister a non-registered session listener.",
				xmppSessionListeners.contains(xmppSessionListener));
		xmppSessionListeners.remove(xmppSessionListener);
	}

	/**
	 * Send a message to a list of users.
	 * 
	 * @param users
	 *            The list of users to send the message to.
	 * @param message
	 *            The message to send.
	 * @see com.thinkparity.model.xmpp.XMPPSession#send(java.util.Collection,
	 *      java.lang.String)
	 */
	public void send(Collection<User> users, String message)
			throws SmackException {
		logger.info("send(Collection<User>,String)");
		logger.debug(users);
		logger.debug(message);
		try {
			for(User user : users) {
				final Chat chat =
					smackXMPPConnection.createChat(user.getUsername());
				final Message messagePacket = chat.createMessage();
				messagePacket.setBody(message);
				sendPacket(messagePacket);
			}
		}
		catch(InterruptedException ix) {
			logger.error("send(Collection<User>,String)", ix);
			throw XMPPErrorTranslator.translate(ix);
		}
	}

	/**
	 * Send the document to the user. This is done by creating a smack packet
	 * extension for the xmpp document, using the xstream library to serialize
	 * the xmpp document; then sending the packet extension as an attachment to
	 * a message to the user.
	 * 
	 * @param users
	 *            The users to send the document to.
	 * @param xmppDocument
	 *            The document to send.
	 * @see XMPPSessionImpl#send(Collection, PacketExtension)
	 */
	public void send(final Collection<User> users, final XMPPDocument xmppDocument)
			throws SmackException {
		logger.info("send(Collection<User>,XMPPDocument)");
		logger.debug(users);
		logger.debug(xmppDocument);
		try { send(users, XFactory.createPacketX(xmppDocument)); }
		catch(InterruptedException ix) {
			logger.error("send(Collection<User>,XMPPDocument)", ix);
			throw XMPPErrorTranslator.translate(ix);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#sendCreate(java.util.UUID)
	 * 
	 */
	public void sendCreate(final UUID artifactUniqueId) throws SmackException {
		logger.info("sendCreate(UUID)");
		logger.debug(artifactUniqueId);
		try {
			final IQArtifact iq = new IQArtifactCreate(artifactUniqueId);
			iq.setType(IQ.Type.SET);
			logger.debug(iq);
			sendAndConfirmPacket(iq);
		}
		catch(InterruptedException ix) {
			logger.error("sendCreate(UUID)", ix);
			throw XMPPErrorTranslator.translate(ix);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#sendKeyRequest(java.util.UUID)
	 */
	public void sendKeyRequest(final UUID artifactUniqueId) throws SmackException {
		logger.info("sendKeyRequest(UUID)");
		logger.debug(artifactUniqueId);
		try {
			final IQArtifact iq = new IQKeyRequest(artifactUniqueId);
			iq.setType(IQ.Type.GET);
			logger.debug(iq);
			sendPacket(iq);
		}
		catch(InterruptedException ix) {
			logger.error("sendKeyRequest(UUID)", ix);
			throw XMPPErrorTranslator.translate(ix);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#sendKeyResponse(java.util.UUID,
	 *      com.thinkparity.model.parity.model.session.KeyResponse,
	 *      com.thinkparity.model.xmpp.user.User)
	 * 
	 */
	public void sendKeyResponse(final UUID artifactUniqueId,
			final KeyResponse keyResponse, final User user)
			throws SmackException {
		logger.info("sendKeyResponse(UUID,KeyResponse,User)");
		logger.debug(artifactUniqueId);
		logger.debug(user);
		try {
			final IQArtifact iq;
			switch(keyResponse) {
			case ACCEPT:
				iq = new IQAcceptKeyRequest(artifactUniqueId, user);
				break;
			case DENY:
				iq = new IQDenyKeyRequest(artifactUniqueId, user);
				break;
			default:
				throw Assert.createUnreachable(
						"sendKeyResponse(UUID,KeyResponse,User)");
			}
			iq.setType(IQ.Type.SET);
			sendAndConfirmPacket(iq);
		}
		catch(final InterruptedException ix) {
			logger.error("sendKeyResponse(UUID,KeyResponse,User)", ix);
			throw XMPPErrorTranslator.translate(ix);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#sendLogFileArchive(java.io.File)
	 */
	public void sendLogFileArchive(final File logFileArchive, final User user)
			throws SmackException {
		logger.info("sendLogFileArchive(File)");
		logger.debug(logFileArchive);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#sendSubscribe(java.util.UUID)
	 */
	public void sendSubscribe(final UUID artifactUniqueId) throws SmackException {
		logger.info("sendSubscribe(UUID)");
		logger.debug(artifactUniqueId);
		try {
			final IQArtifact iq = new IQArtifactSubscribe(artifactUniqueId);
			iq.setType(IQ.Type.SET);
			logger.debug(iq);
			sendAndConfirmPacket(iq);
		}
		catch(InterruptedException ix) {
			logger.error("sendSubscribe(UUID)", ix);
			throw XMPPErrorTranslator.translate(ix);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#subscribe(java.util.UUID)
	 */
	public void subscribe(final UUID artifactUniqueId) throws SmackException {
		logger.info("subscribe(UUID)");
		logger.debug(artifactUniqueId);
		try {
			final IQArtifact iq = new IQArtifactSubscribe(artifactUniqueId);
			iq.setType(IQ.Type.SET);
			sendPacket(iq);
		}
		catch(InterruptedException ix) {
			logger.error("subscribe(UUID)", ix);
			throw XMPPErrorTranslator.translate(ix);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#unsubscribe(java.util.UUID)
	 */
	public void unsubscribe(final UUID artifactUniqueId) throws SmackException {
		logger.info("unsubscribe(UUID)");
		logger.debug(artifactUniqueId);
		try {
			final IQArtifact iq = new IQArtifactUnsubscribe(artifactUniqueId);
			iq.setType(IQ.Type.SET);
			sendPacket(iq);
		}
		catch(InterruptedException ix) {
			logger.error("unsubscribe(UUID)", ix);
			throw XMPPErrorTranslator.translate(ix);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#updateRosterEntry(com.thinkparity.model.xmpp.user.User)
	 */
	public void updateRosterEntry(User user) {
		logger.info("updateRosterEntry(UserRenderer)");
		logger.debug(user);
		final Roster roster = getRoster();
		final RosterEntry rosterEntry = roster.getEntry(user.getUsername());
		rosterEntry.setName(user.getName());
	}

	/**
	 * Assert that the underlying connection is authenticated.
	 * 
	 * @param callerName
	 *            The caller of this method.
	 */
	private void assertLoggedIn(final String callerName) {
		Assert.assertTrue(
				callerName,
				smackXMPPConnection.isAuthenticated());
	}

	private User createUser(final Roster roster, final RosterEntry rosterEntry) {
		final Presence presence = roster.getPresence(rosterEntry.getUser());

		final User.Presence userPresence;
		if(presence == null) { userPresence = User.Presence.OFFLINE; }
		else {
			final Type type = presence.getType();
			final Mode mode = presence.getMode();
			if(Type.AVAILABLE == type && Mode.AVAILABLE == mode) {
				userPresence = User.Presence.AVAILABLE;
			}
			else { userPresence = User.Presence.UNAVAILABLE; }
		}
		return new User(rosterEntry.getName(), rosterEntry.getUser(), userPresence);
	}

	/**
	 * Iterate through the list of xmppPresenceListeners and fire the
	 * presenceRequested event.
	 * 
	 * @param packet
	 *            <code>org.jivesoftware.packet.Packet</code>
	 */
	private void doFirePresenceRequested(final Packet packet) {
		final User fromXMPPUser = getUserPacketFrom(packet);
		for(XMPPPresenceListener listener : xmppPresenceListeners) {
			listener.presenceRequested(fromXMPPUser);
		}
	}

	/**
	 * Event handler for the connectionClosed event generated by the smack
	 * connection listener impl. This will iterate through the
	 * xmppSessionListeners list and fire the sessionTerminated event.
	 */
	private void doNotifyConnectionClosed() {
		for (Iterator<XMPPSessionListener> i = xmppSessionListeners.iterator(); i
				.hasNext();) {
			i.next().sessionTerminated();
		}	
	}

	/**
	 * Event handler for the connectionClosedOnerror event generated by the
	 * smack connection listener impl. This will iterate through the
	 * xmppSessionListeners list and fire the sessionTerminated event.
	 * 
	 * @param x
	 *            <code>java.lang.Exception</code>
	 */
	private void doNotifyConnectionClosedOnError(final Exception x) {
		for (Iterator<XMPPSessionListener> i = xmppSessionListeners.iterator(); i
				.hasNext();) {
			i.next().sessionTerminated(x);
		}
	}

	/**
	 * Event handler for the connectionEstablished event generated by the smack
	 * connection listener impl. This will iterate through the
	 * xmppSessionListeners list and fire the sessionEstablished event.
	 * 
	 * @param xmppConnection
	 *            <code>org.jivesoftware.smack.XMPPConnection</code>
	 */
	private void doNotifyConnectionEstablished(final XMPPConnection xmppConnection) {
		for (Iterator<XMPPSessionListener> i = xmppSessionListeners.iterator(); i
				.hasNext();) {
			i.next().sessionEstablished();
		}
	}

	/**
	 * Event handler for the presenceChanged event generated by the
	 * smackRosterListenerImpl. This will check to see if there is a pending
	 * user in the map for the given address, and if there is, it will update
	 * the roster name accordingly.
	 * 
	 * @param xmppAddress
	 *            <code>java.lang.String</code>
	 */
	private void doNotifyPresenceChanged(final String xmppAddress) {
		final User pendingXMPPUser = pendingXMPPUsers.remove(xmppAddress);
		if(null != pendingXMPPUser) {
			final RosterEntry rosterEntry = getRosterEntry(pendingXMPPUser);
			rosterEntry.setName(pendingXMPPUser.getName());
		}
	}

	/**
	 * Event handler for the procesPacket event generated by the
	 * smackPresenceListenerImpl. If the presence packet type is a subscription
	 * request, a presence requested event is passed on to the
	 * xmppPresenceListeners list. If the presence packet type is a subscription
	 * accepted notification, and the current user's roster does not have an
	 * entry for the requesting user, a subscription accepted event is sent
	 * back.
	 * 
	 * @param presence
	 *            <code>org.jivesoftware.packet.Presence</code>
	 */
	private void doNotifyProcessPresence(final Presence presence)
			throws InterruptedException {
		final Type presencePacketType = presence.getType();
		if(presencePacketType == Type.SUBSCRIBE) {
			doFirePresenceRequested(presence);
		}
		else if(presencePacketType == Type.SUBSCRIBED) {
			final Roster roster = getRoster();
			final Presence rosterPresence = roster.getPresence(presence.getFrom());
			if(null == rosterPresence) {
				sendPresenceAcceptance(presence.getFrom());
			}
		}
	}

	/**
	 * Obtain the roster from the underlying connection.  The roster is not
	 * automatically reloaded.
	 * 
	 * @return <code>org.jivesoftware.smack.Roster</code>
	 */
	private Roster getRoster() { return smackXMPPConnection.getRoster(); }

	/**
	 * Obtain the roster entry for a given user in 3 stages.  All versions of
	 * the username are attempted.
	 * username
	 * username@host.com
	 * username@host.com/resource
	 * @param username <code>java.lang.String</code>
	 * @return <code>java.lang.String</code>
	 * @throws SmackException
	 */
	private RosterEntry getRosterEntry(final User xmppUser) {
		final Roster roster = getRoster();
		RosterEntry rosterEntry = roster.getEntry(xmppUser.getUsername());
		if(null != rosterEntry) { return rosterEntry; }
		else {
			// check if there's a trailing resource on the username, and if so
			// strip it and try again
			final String username = xmppUser.getUsername();
			if(0 < username.lastIndexOf("/")) {
				rosterEntry = roster.getEntry(StringUtil.removeAfter(username, "/"));
				if(null != rosterEntry) { return rosterEntry; }
				else {
					if(0 < username.lastIndexOf("@")) {
						rosterEntry = roster.getEntry(StringUtil.removeAfter(username, "@"));
						if(null != rosterEntry) { return rosterEntry; }
					}
				}
			}
		}
		return null;
	}

	/**
	 * Obtain the number of entries in the current roster.
	 * 
	 * @return <code>java.lang.Integer</code>
	 */
	private Integer getRosterEntryCount() { return getRoster().getEntryCount(); }

	/**
	 * Extract an UserRenderer using the packet information.
	 * @param packet <code>org.jivesoftware.smack.packet.Packet</code>
	 * @return <code>org.kcs.projectmanager.xmpp.user.XMPPUser</code>
	 */
	private User getUserPacketFrom(final Packet packet) {
		// TODO:  Figure out a way to extract the name.
		return new User(null, packet.getFrom(), User.Presence.OFFLINE);
	}

	/**
	 * Fire the documentReceived event for all of the XMPPExtension listeners.
	 * 
	 * @param xmppDocument
	 *            The xmpp document to use as the event source.
	 */
	private void notifyXMPPExtension_documentReceived(final XMPPDocument xmppDocument) {
		synchronized(xmppExtensionListenersLock) {
			for(XMPPExtensionListener listener : xmppExtensionListeners) {
				listener.documentReceived(xmppDocument);
			}
		}
	}

	/**
	 * Fire the keyRequestAccepted event for all of the XMPPExtension listeners.
	 * 
	 * @param iq
	 *            The IQAcceptKeyRequest send from the parity server.
	 */
	private void notifyXMPPExtension_keyRequestAccepted(
			final IQAcceptKeyRequest iq) {
		synchronized(xmppExtensionListenersLock) {
			final User user = new User(null, iq.getQualifiedJID(), null);
			final UUID artifactUniqueId = iq.getArtifactUUID();
			for(XMPPExtensionListener listener : xmppExtensionListeners) {
				listener.keyRequestAccepted(user, artifactUniqueId);
			}
		}
	}

	/**
	 * Fire the keyRequestDenied event for all of the XMPPExtension listeners.
	 * 
	 * @param iq
	 *            The IQDenyKeyRequest send from the parity server.
	 */
	private void notifyXMPPExtension_keyRequestDenied(final IQDenyKeyRequest iq) {
		synchronized(xmppExtensionListenersLock) {
			final User user = new User(null, iq.getQualifiedJID(), null);
			final UUID artifactUniqueId = iq.getArtifactUUID();
			for(XMPPExtensionListener listener : xmppExtensionListeners) {
				listener.keyRequestDenied(user, artifactUniqueId);
			}
		}
	}

	/**
	 * Fire the keyRequested event for all of the XMPPExtension listeners.
	 * 
	 * @param iqKeyRequest
	 *            The IQKeyRequest to use to build the event source.
	 */
	private void notifyXMPPExtension_keyRequested(final IQKeyRequest iqKeyRequest) {
		synchronized(xmppExtensionListenersLock) {
			final User user = getUserPacketFrom(iqKeyRequest);
			final UUID artifactUniqueId = iqKeyRequest.getArtifactUUID();
			for(XMPPExtensionListener listener : xmppExtensionListeners) {
				listener.keyRequested(user, artifactUniqueId);
			}
		}
	}

	/**
	 * Send a packet extension to a single user.
	 * 
	 * @param user
	 *            The user to send the packet extension to.
	 * @param packetExtension
	 *            The packet extension to send.
	 */
	private void send(final Collection<User> users,
			final PacketExtension packetExtension) throws InterruptedException,
			SmackException {
		for(final User user : users) {
			final Message message = smackXMPPConnection.createChat(
					user.getUsername()).createMessage();
			message.addExtension(packetExtension);
			logger.info("messageSize:" + message.toXML().length());
			sendPacket(message);
		}
	}

	/**
	 * Send the packet and wait for a response. If the response conains an
	 * error; a SmackException will be thrown.
	 * 
	 * @param packet
	 *            The packet.
	 * @return The confirmation packet.
	 * @throws InterruptedException
	 * @throws SmackException
	 *             If the response contains an error.
	 */
	private Packet sendAndConfirmPacket(final Packet packet)
			throws InterruptedException, SmackException {
		final String packetId = packet.getPacketID();
        final PacketCollector collector =
            smackXMPPConnection.createPacketCollector(new PacketIDFilter(packetId));
        sendPacket(packet);
        final Packet confirmationPacket = collector.nextResult();
        logger.debug("confirmationPacket");
        logger.debug(confirmationPacket);
        if(null == confirmationPacket) {
        	throw new SmackException("Send and confirm packet timeout.");
        }
        else if(null != confirmationPacket.getError()) {
            throw XMPPErrorTranslator.translate(confirmationPacket.getError());
        }
        return confirmationPacket;
	}

	/**
	 * Send a packet through the smack xmpp connection.
	 * 
	 * @param packet
	 *            The packet to send.
	 */
	private void sendPacket(final Packet packet) throws InterruptedException {
		logger.debug("packet");
		logger.debug(packet);
		smackXMPPConnection.sendPacket(packet);
		// this sleep has been inserted because when packets are sent within
		// x milliseconds of each other, they tend to get swallowed by the
		// smack library
		Thread.sleep(SEND_PACKET_SLEEP_DURATION);
	}

	/**
	 * Send a subscription acceptance presence packet to username.
	 * @param username <code>java.lang.String</code.
	 */
	private void sendPresenceAcceptance(final String username)
			throws InterruptedException {
		sendPresencePacket(username, Type.SUBSCRIBED, Mode.AVAILABLE);
	}

	/**
	 * Send a presence packet to a user.
	 * 
	 * @param username
	 *            The user to send the packet to.
	 * @param type
	 *            The type of packet to send.
	 */
	private void sendPresencePacket(final String username,
			final Presence.Type type) throws InterruptedException {
		sendPresencePacket(username, type, null);
	}

	/**
	 * Send a presence packet to a user.
	 * 
	 * @param username
	 *            The user to send the packet to.
	 * @param type
	 *            The type of packet to send.
	 * @param mode
	 *            The mode of the packet. If the mode is null, no mode is set.
	 * @see XMPPSessionImpl#sendPresencePacket(String, Presence.Type)
	 */
	private void sendPresencePacket(final String username,
			final Presence.Type type, final Presence.Mode mode)
			throws InterruptedException {
		final Presence presence = new Presence(type);
		if(null != mode) { presence.setMode(mode); }
		presence.setTo(username);
		presence.setFrom(smackXMPPConnection.getUser());
		sendPacket(presence);
	}

	/**
	 * SmackConnectionListenerImpl Is used to translate smack connection events
	 * to local xmpp session events. Note that the same class is used to listen
	 * for both connection established events and connection termination events.
	 * 
	 * @author raykroeker@gmail.com
	 */
	private class SmackConnectionListenerImpl extends SmackConnectionListener {
		/**
		 * @see org.jivesoftware.smack.ConnectionListener#connectionClosed()
		 */
		public void connectionClosed() { doNotifyConnectionClosed(); }
		/**
		 * @see org.jivesoftware.smack.ConnectionListener#connectionClosedOnError(java.lang.Exception)
		 */
		public void connectionClosedOnError(Exception x) {
			doNotifyConnectionClosedOnError(x);
		}
		/**
		 * @see org.jivesoftware.smack.ConnectionEstablishedListener#connectionEstablished(org.jivesoftware.smack.XMPPConnection)
		 */
		public void connectionEstablished(final XMPPConnection xmppConnection) {
			doNotifyConnectionEstablished(xmppConnection);
		}
	}

	/**
	 * SmackPresenceListenerImpl
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class SmackPresenceListenerImpl extends SmackPresenceListener {
		/**
		 * @see org.jivesoftware.smack.PacketListener#processPacket(org.jivesoftware.smack.packet.Packet)
		 */
		public void processPacket(final Packet packet) {
			try { doNotifyProcessPresence((Presence) packet); }
			catch(InterruptedException ix) {
				logger.error("processPacket(Packet)", ix);
			}
		}
	}

	/**
	 * SmackRosterListenerImpl
	 * @author raykroeker@gmail.com
	 * @version 1.1
	 */
	private class SmackRosterListenerImpl extends SmackRosterListener {
		/**
		 * @see com.thinkparity.model.smack.SmackRosterListener#presenceChanged(String)
		 */
		public void presenceChanged(final String xmppAddress) {
			doNotifyPresenceChanged(xmppAddress);
		}
		/**
		 * @see com.thinkparity.model.smack.SmackRosterListener#rosterModified()
		 */
		public void rosterModified() {}
	}


}
