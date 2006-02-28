/*
 * Feb 6, 2005
 */
package com.thinkparity.model.xmpp;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

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
import org.jivesoftware.smackx.packet.VCard;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.IParityModelConstants;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.session.KeyResponse;
import com.thinkparity.model.smack.SmackConnectionListener;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.smackx.XFactory;
import com.thinkparity.model.smackx.document.XMPPDocumentXFilter;
import com.thinkparity.model.smackx.document.XMPPDocumentXListener;
import com.thinkparity.model.smackx.packet.*;
import com.thinkparity.model.xmpp.document.XMPPDocument;
import com.thinkparity.model.xmpp.events.XMPPExtensionListener;
import com.thinkparity.model.xmpp.events.XMPPPresenceListener;
import com.thinkparity.model.xmpp.events.XMPPSessionListener;
import com.thinkparity.model.xmpp.user.User;
import com.thinkparity.model.xmpp.user.UserVCard;

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

		ProviderManager.addIQProvider("query", "jabber:iq:parity:acceptkeyrequest", new IQAcceptKeyRequestProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:closeartifact", new IQCloseArtifactProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:denykeyrequest", new IQDenyKeyRequestProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:getkeyholder", new IQGetKeyHolderProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:getkeys", new IQGetKeysProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:getsubscriptions", new IQGetSubscriptionProvider());
		ProviderManager.addIQProvider("query", "jabber:iq:parity:keyrequest", new IQKeyRequestProvider());
	}

	private XMPPConnection smackXMPPConnection;
	private Vector<XMPPExtensionListener> xmppExtensionListeners;
	private final Object xmppExtensionListenersLock = new Object();
	private Vector<XMPPPresenceListener> xmppPresenceListeners;
	private Vector<XMPPSessionListener> xmppSessionListeners;

	/**
	 * Create a XMPPSessionImpl
	 * 
	 */
	XMPPSessionImpl() {
		logger.info("Jive Software:  Smack:  " + SmackConfiguration.getVersion());
		this.xmppExtensionListeners = new Vector<XMPPExtensionListener>(10);
		this.xmppPresenceListeners = new Vector<XMPPPresenceListener>(10);
		this.xmppSessionListeners = new Vector<XMPPSessionListener>(10);

		XMPPConnection.addConnectionListener((ConnectionEstablishedListener) new SmackConnectionListener() {
			public void connectionClosed() {
				doNotifyConnectionClosed();
			}
			public void connectionClosedOnError(final Exception e) {
				doNotifyConnectionClosedOnError(e);
			}
			public void connectionEstablished(final XMPPConnection connection) {
				doNotifyConnectionEstablished(connection);
			}
		});
	}

	/**
	 * Accept the presence request of user.  This will send a presence packet
	 * indicating that the user is subscribed and available.
	 * 
	 */
	public void acceptInvitation(final JabberId jabberId) throws SmackException {
		logger.info("acceptInvitation(JabberId)");
		logger.debug(jabberId);
		sendPresencePacket(jabberId.getQualifiedUsername(), Type.SUBSCRIBED, Mode.AVAILABLE);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#addListener(com.thinkparity.model.xmpp.events.XMPPExtensionListener)
	 * 
	 */
	public void addListener(final XMPPExtensionListener xmppExtensionListener) {
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
	 * @see com.thinkparity.model.xmpp.XMPPSession#create(java.util.UUID)
	 */
	public void create(final UUID artifactUniqueId) throws SmackException {
		logger.info("create(UUID)");
		logger.debug(artifactUniqueId);
		final IQArtifact iq = new IQArtifactCreate(artifactUniqueId);
		iq.setType(IQ.Type.SET);
		logger.debug(iq);
		sendPacket(iq);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#declineInvitation(com.thinkparity.model.xmpp.JabberId)
	 * 
	 */
	public void declineInvitation(final JabberId jabberId) throws SmackException {
		logger.info("declineInvitation(JabberId)");
		logger.debug(jabberId);

		sendPresencePacket(jabberId.getQualifiedUsername(), Type.UNSUBSCRIBED, Mode.AWAY);

		final RosterEntry rosterEntry = getRoster().getEntry(jabberId.getQualifiedJabberId());
		try { getRoster().removeEntry(rosterEntry); }
		catch(final XMPPException xmppx) {
			throw XMPPErrorTranslator.translate(xmppx);
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
		final IQArtifact iq = new IQArtifactFlag(artifactUniqueId, flag);
		iq.setType(IQ.Type.SET);
		logger.debug(iq);
		sendPacket(iq);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#getArtifactKeyHolder(java.util.UUID)
	 * 
	 */
	public User getArtifactKeyHolder(final UUID artifactUniqueId)
			throws SmackException {
		logger.info("getArtifactKeyHolder(UUID)");
		logger.debug(artifactUniqueId);
		final IQArtifact iq = new IQGetKeyHolder(artifactUniqueId);
		iq.setType(IQ.Type.GET);
		logger.debug(iq);
		final IQGetKeyHolderResponse response =
			(IQGetKeyHolderResponse) sendAndConfirmPacket(iq);
		return new User(response.getKeyHolder());
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#getArtifactKeys()
	 * 
	 */
	public List<UUID> getArtifactKeys() throws SmackException {
		assertLoggedIn("Cannot obtain artifact keys while offline.");
		logger.info("getArtifactKeys()");
		final IQParity iq = new IQGetKeys();
		iq.setType(IQ.Type.GET);
		logger.debug(iq);
		final IQGetKeysResponse response =
			(IQGetKeysResponse) sendAndConfirmPacket(iq);
		final List<UUID> keys = new LinkedList<UUID>();
		for(final UUID key : response.getKeys()) { keys.add(key); }
		return keys;
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#getArtifactSubscription(java.util.UUID)
	 * 
	 */
	public List<User> getArtifactSubscription(final UUID artifactUniqueId)
			throws SmackException {
		assertLoggedIn("Cannot obtain artifact subscription without being online.");
		logger.info("getArtifactSubscription(UUID)");
		logger.debug(artifactUniqueId);
		final IQArtifact iq = new IQGetSubscription(artifactUniqueId);
		iq.setType(IQ.Type.GET);
		logger.debug(iq);
		final IQGetSubscriptionResponse response =
			(IQGetSubscriptionResponse) sendAndConfirmPacket(iq);
		final List<User> subscription = new LinkedList<User>();
		for (final JabberId jabberId : response.getJids()) {
			subscription.add(createUser(jabberId));
		}
		return subscription;
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
	 * 
	 */
	public User getUser() throws SmackException {
		assertLoggedIn("Cannot obtain user while offline.");
		final String user = smackXMPPConnection.getUser();
		return new User(user.substring(0, user.indexOf('/')));
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#getUserVCard()
	 */
	public UserVCard getUserVCard() throws SmackException {
		logger.info("getUserVCard()");
		return getUserVCard(getJabberId());
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#getUserVCard(com.thinkparity.model.xmpp.JabberId)
	 * 
	 */
	public UserVCard getUserVCard(final JabberId jabberId)
			throws SmackException {
		logger.info("getUserVCard(JabberId)");
		logger.debug(jabberId);
		try {
			final VCard vCard = new VCard();
			vCard.load(smackXMPPConnection, jabberId.getQualifiedUsername());

			final UserVCard userVCard = new UserVCard();
			userVCard.setJabberId(jabberId);
			userVCard.setFirstName(vCard.getFirstName());
			userVCard.setLastName(vCard.getLastName());
			userVCard.setOrganization(vCard.getOrganization());
			return userVCard;
		}
		catch(final XMPPException xmppx) {
			logger.error("Could not load user vCard:  " + jabberId, xmppx);
			throw XMPPErrorTranslator.translate(xmppx);
		}
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#inviteContact(com.thinkparity.model.xmpp.JabberId)
	 * 
	 */
	public void inviteContact(final JabberId jabberId) throws SmackException {
		logger.info("inviteContact(JabberId)");
		logger.debug(jabberId);
		assertLoggedIn("Cannot invite a contact while offline.");
		Assert.assertNotNull("Cannot invite a null contact", jabberId);
		try {
			final Roster roster = getRoster();
			if(!roster.contains(jabberId.getQualifiedJabberId()))
				roster.createEntry(jabberId.getQualifiedJabberId(), null, null);
		}
		catch(final XMPPException xmppx) {
			logger.error("Could not invite contact:  " + jabberId, xmppx);
			throw XMPPErrorTranslator.translate(xmppx);
		}
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

			// packet debugger
			smackXMPPConnection.addPacketListener(
					new PacketListener() {
						public void processPacket(final Packet packet) {
							logger.debug(packet);
						}
					},
					new PacketFilter() {
						public boolean accept(final Packet packet) { return true; }
					});
			// close artifact
			smackXMPPConnection.addPacketListener(
					new PacketListener() {
						public void processPacket(final Packet packet) {
							notifyXMPPExtension_closeArtifact((IQCloseArtifact) packet);
						}
					},
					new PacketTypeFilter(IQCloseArtifact.class));
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
			// contact invitation
			smackXMPPConnection.addPacketListener(
					new PacketListener() {
						public void processPacket(final Packet packet) {
							notifyContactInvitation((Presence) packet);
						}
					},
					new PacketFilter() {
						public boolean accept(final Packet packet) {
							if(packet.getClass().isAssignableFrom(Presence.class)) {
								if(((Presence) packet).getType() == Presence.Type.SUBSCRIBE) {
									return true;
								}
							}
							return false;
						}
					});
			// contact deletion
			smackXMPPConnection.addPacketListener(
					new PacketListener() {
						public void processPacket(final Packet packet) {
							notifyContactDeletion((Presence) packet);
						}
					},
					new PacketFilter() {
						public boolean accept(final Packet packet) {
							if(packet.getClass().isAssignableFrom(Presence.class)) {
								if(((Presence) packet).getType() == Presence.Type.UNSUBSCRIBE) {
									return true;
								}
							}
							return false;
						}
					});
			// contact invitation response
			smackXMPPConnection.addPacketListener(
					new PacketListener() {
						public void processPacket(final Packet packet) {
							notifyContactInvitationResponse((Presence) packet);
						}
					},
					new PacketFilter() {
						public boolean accept(final Packet packet) {
							if(packet.getClass().isAssignableFrom(Presence.class)) {
								final Presence.Type type = ((Presence) packet).getType();
								if(Presence.Type.SUBSCRIBED == type
										|| Presence.Type.UNSUBSCRIBED == type) {
									return true;
								}
							}
							return false;
						}
					});

			smackXMPPConnection.login(username, password,
					IParityModelConstants.PARITY_CONNECTION_RESOURCE);
		}
		catch(final XMPPException xmppx) {
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
	 * @see com.thinkparity.model.xmpp.XMPPSession#processOfflineQueue()
	 * 
	 */
	public void processOfflineQueue() throws SmackException {
		logger.info("processOfflineQueue()");
		final IQParity processOfflineQueue = new IQProcessOfflineQueue();
		sendAndConfirmPacket(processOfflineQueue);
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
	 * @see com.thinkparity.model.xmpp.XMPPSession#saveVCard(com.thinkparity.model.xmpp.user.UserVCard)
	 * 
	 */
	public void saveVCard(final UserVCard userVCard) throws SmackException {
		logger.info("saveVCard(UserVCard)");
		logger.debug(userVCard);
		assertLoggedIn("Cannot save vCard while offline.");
		Assert.assertTrue("Cannot save other user's vCard.",
				userVCard.getJabberId().equals(getJabberId()));

		final VCard vCard = new VCard();
		vCard.setFirstName(userVCard.getFirstName());
		vCard.setLastName(userVCard.getLastName());
		vCard.setOrganization(userVCard.getOrganization());
		vCard.setType(IQ.Type.SET);
		sendAndConfirmPacket(vCard);
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
		for(final User user : users) {
			final Chat chat =
				smackXMPPConnection.createChat(user.getUsername());
			final Message messagePacket = chat.createMessage();
			messagePacket.setBody(message);
			sendPacket(messagePacket);
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
	 * @see com.thinkparity.model.xmpp.XMPPSession#sendClose(java.util.UUID)
	 * 
	 */
	public void sendClose(final UUID artifactUniqueId) throws SmackException {
		logger.info("sendClose(UUID)");
		logger.debug(artifactUniqueId);
		final IQArtifact iq = new IQCloseArtifact(artifactUniqueId);
		iq.setType(IQ.Type.SET);
		logger.debug(iq);
		sendAndConfirmPacket(iq);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#sendCreate(java.util.UUID)
	 * 
	 */
	public void sendCreate(final UUID artifactUniqueId) throws SmackException {
		logger.info("sendCreate(UUID)");
		logger.debug(artifactUniqueId);
		final IQArtifact iq = new IQArtifactCreate(artifactUniqueId);
		iq.setType(IQ.Type.SET);
		logger.debug(iq);
		sendAndConfirmPacket(iq);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#sendDelete(java.util.UUID)
	 * 
	 */
	public void sendDelete(final UUID artifactUniqueId) throws SmackException {
		logger.info("sendDelete(UUID)");
		logger.debug(artifactUniqueId);
		final IQArtifact delete = new IQDeleteArtifact(artifactUniqueId);
		delete.setType(IQ.Type.SET);
		sendAndConfirmPacket(delete);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#sendKeyRequest(java.util.UUID)
	 * 
	 */
	public void sendKeyRequest(final UUID artifactUniqueId)
			throws SmackException {
		logger.info("sendKeyRequest(UUID)");
		logger.debug(artifactUniqueId);
		final IQArtifact iq = new IQKeyRequest(artifactUniqueId);
		iq.setType(IQ.Type.SET);
		sendAndConfirmPacket(iq);
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
	 * 
	 */
	public void sendSubscribe(final UUID artifactUniqueId)
			throws SmackException {
		logger.info("sendSubscribe(UUID)");
		logger.debug(artifactUniqueId);
		final IQArtifact iq = new IQArtifactSubscribe(artifactUniqueId);
		iq.setType(IQ.Type.SET);
		logger.debug(iq);
		sendAndConfirmPacket(iq);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#subscribe(java.util.UUID)
	 * 
	 */
	public void subscribe(final UUID artifactUniqueId) throws SmackException {
		logger.info("subscribe(UUID)");
		logger.debug(artifactUniqueId);
		final IQArtifact iq = new IQArtifactSubscribe(artifactUniqueId);
		iq.setType(IQ.Type.SET);
		sendPacket(iq);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#unsubscribe(java.util.UUID)
	 */
	public void unsubscribe(final UUID artifactUniqueId) throws SmackException {
		logger.info("unsubscribe(UUID)");
		logger.debug(artifactUniqueId);
		final IQArtifact iq = new IQArtifactUnsubscribe(artifactUniqueId);
		iq.setType(IQ.Type.SET);
		sendPacket(iq);
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

	/**
	 * Create a parity user from a jabber id. This will pull the user's VCard
	 * information and set it in the user.
	 * 
	 * @param jabberId
	 *            The jabber id.
	 * @return The parity user.
	 * @throws SmackException
	 */
	private User createUser(final JabberId jabberId) throws SmackException {
		final User user = new User(jabberId);
		final UserVCard userVCard = getUserVCard(jabberId);
		user.setFirstName(userVCard.getFirstName());
		user.setLastName(userVCard.getLastName());
		user.setOrganization(userVCard.getOrganization());
		return user;
	}

	private User createUser(final Roster roster, final RosterEntry rosterEntry) {
		final JabberId jabberId = JabberIdBuilder.parseQualifiedUsername(rosterEntry.getUser());
		return new User(jabberId);
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
		for(final XMPPSessionListener l : xmppSessionListeners) {
			l.sessionEstablished();
		}
	}

	/**
	 * Obtain the from user's jabber id from the packet.
	 * 
	 * @param packet
	 *            The xmpp packet.
	 * @return The from user's jabber id.
	 */
	private JabberId getFromJabberId(final Packet packet) {
		return JabberIdBuilder.parseQualifiedJabberId(packet.getFrom());
	}

	/**
	 * Obtain the jabber id for the logged in user.
	 * 
	 * @return The jabber id of the logged in user.
	 */
	private JabberId getJabberId() {
		return JabberIdBuilder.parseQualifiedJabberId(smackXMPPConnection.getUser());
	}

	/**
	 * Obtain the roster from the underlying connection.  The roster is not
	 * automatically reloaded.
	 * 
	 * @return <code>org.jivesoftware.smack.Roster</code>
	 */
	private Roster getRoster() { return smackXMPPConnection.getRoster(); }

	/**
	 * Obtain the number of entries in the current roster.
	 * 
	 * @return <code>java.lang.Integer</code>
	 */
	private Integer getRosterEntryCount() { return getRoster().getEntryCount(); }

	/**
	 * Iterate through the list of xmppPresenceListeners and fire the
	 * presenceRequested event.
	 * 
	 * @param packet
	 *            <code>org.jivesoftware.packet.Packet</code>
	 */
	private void notifyContactInvitation(final Packet packet) {
		// NOTE The presence packets do not contain resource info
		final JabberId fromJabberId =
			JabberIdBuilder.parseQualifiedUsername(packet.getFrom());
		for(final XMPPPresenceListener l : xmppPresenceListeners) {
			l.presenceRequested(fromJabberId);
		}
	}

	private void notifyContactDeletion(final Packet packet) {
		final JabberId fromJabberId =
			JabberIdBuilder.parseQualifiedUsername(packet.getFrom());
		final RosterEntry rosterEntry = getRoster().getEntry(fromJabberId.getQualifiedJabberId());
		try { getRoster().removeEntry(rosterEntry); }
		catch(final XMPPException xmppx) {
			logger.error("Cannot delete contact.", xmppx);
		}
	}

	/**
	 * Process the contact invitation response.
	 * 
	 * @param presence
	 *            The presence packet.
	 */
	private void notifyContactInvitationResponse(final Presence presence) {
		final Presence.Type type = presence.getType();
		if(Presence.Type.SUBSCRIBED == type) {
			final Roster roster = getRoster();
			final Presence rosterPresence = roster.getPresence(presence.getFrom());
			if(null == rosterPresence) {
				try {  sendPresenceAcceptance(presence.getFrom()); }
				catch(final SmackException sx) {
					logger.error("Could not process presence packet.", sx);
				}
			}
		}
		else if(Presence.Type.UNSUBSCRIBED == type) {
			final JabberId jabberId = JabberIdBuilder.parseQualifiedJabberId(presence.getFrom());

			final RosterEntry rosterEntry = getRoster().getEntry(jabberId.getQualifiedJabberId());
			try { getRoster().removeEntry(rosterEntry); }
			catch(final XMPPException xmppx) {
				logger.error("Could not remove roster entry:  " + jabberId, xmppx);
			}
		}
		else {
			Assert.assertUnreachable(
					"Unknown presence type for contact invitation response:  "
					+ type);
		}
	}

	/**
	 * Fire the artifact closed event for all of the xmpp extension listeners.
	 * 
	 * @param artifactClose
	 *            The close artifact iq.
	 */
	private void notifyXMPPExtension_closeArtifact(
			final IQCloseArtifact artifactClose) {
		synchronized(xmppExtensionListenersLock) {
			for(final XMPPExtensionListener l : xmppExtensionListeners) {
				l.artifactClosed(artifactClose.getArtifactUUID());
			}
		}
	}

	/**
	 * Fire the documentReceived event for all of the XMPPExtension listeners.
	 * 
	 * @param xmppDocument
	 *            The xmpp document to use as the event source.
	 */
	private void notifyXMPPExtension_documentReceived(final XMPPDocument xmppDocument) {
		synchronized(xmppExtensionListenersLock) {
			for(final XMPPExtensionListener l : xmppExtensionListeners) {
				l.documentReceived(xmppDocument);
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
			final JabberId acceptedBy = getFromJabberId(iq);
			final UUID artifactUniqueId = iq.getArtifactUUID();
			for(final XMPPExtensionListener l : xmppExtensionListeners) {
				l.keyRequestAccepted(artifactUniqueId, acceptedBy);
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
			final JabberId deniedBy = getFromJabberId(iq);
			final UUID artifactUniqueId = iq.getArtifactUUID();
			for(final XMPPExtensionListener l : xmppExtensionListeners) {
				l.keyRequestDenied(artifactUniqueId, deniedBy);
			}
		}
	}

	/**
	 * Fire the keyRequested event for all of the XMPPExtension listeners.
	 * 
	 * @param iqKeyRequest
	 *            The IQKeyRequest to use to build the event source.
	 */
	private void notifyXMPPExtension_keyRequested(final IQKeyRequest iq) {
		synchronized(xmppExtensionListenersLock) {
			final JabberId requestedBy = getFromJabberId(iq);
			final UUID artifactUniqueId = iq.getArtifactUUID();
			for(final XMPPExtensionListener l : xmppExtensionListeners) {
				l.keyRequested(artifactUniqueId, requestedBy);
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
	 * @throws SmackException
	 *             If the response contains an error.
	 */
	private Packet sendAndConfirmPacket(final Packet packet)
			throws SmackException {
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
	private void sendPacket(final Packet packet) throws SmackException {
		logger.debug("packet");
		logger.debug(packet);
		smackXMPPConnection.sendPacket(packet);
		// this sleep has been inserted because when packets are sent within
		// x milliseconds of each other, they tend to get swallowed by the
		// smack library
		try { Thread.sleep(SEND_PACKET_SLEEP_DURATION); }
		catch(final InterruptedException ix) {
			throw XMPPErrorTranslator.translate(ix);
		}
	}

	/**
	 * Send a subscription acceptance presence packet to username.
	 * @param username <code>java.lang.String</code.
	 */
	private void sendPresenceAcceptance(final String username)
			throws SmackException {
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
			final Presence.Type type) throws SmackException {
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
			throws SmackException {
		final Presence presence = new Presence(type);
		if(null != mode) { presence.setMode(mode); }
		presence.setTo(username);
		presence.setFrom(smackXMPPConnection.getUser());
		sendPacket(presence);
	}
}
