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
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.packet.VCard;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.model.LoggerFactory;
import com.thinkparity.model.Constants.Xml;
import com.thinkparity.model.parity.IParityModelConstants;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.io.xmpp.XMPPMethod;
import com.thinkparity.model.parity.model.session.KeyResponse;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.smackx.packet.*;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.events.XMPPArtifactListener;
import com.thinkparity.model.xmpp.events.XMPPContactListener;
import com.thinkparity.model.xmpp.events.XMPPDocumentListener;
import com.thinkparity.model.xmpp.events.XMPPExtensionListener;
import com.thinkparity.model.xmpp.events.XMPPSessionListener;
import com.thinkparity.model.xmpp.user.User;
import com.thinkparity.model.xmpp.user.UserVCard;

/**
 * XMPPSessionImpl
 * Implementation of the XMPPSession interface into the Smack library.
 * @author raykroeker@gmail.com
 * @version 1.7
 */
public class XMPPSessionImpl implements XMPPCore, XMPPSession {

	/**
	 * Interal logger implemenation.
	 */
	private static final Logger logger =
		LoggerFactory.getLogger(XMPPSessionImpl.class);

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

	/**
	 * The artifact xmpp interface.
	 * 
	 */
	private final XMPPArtifact xmppArtifact;

    /**
	 * The xmpp contact interface.
	 * 
	 */
	private final XMPPContact xmppContact;

	/**
     * The document xmpp interface.
     * 
     */
    private final com.thinkparity.model.xmpp.XMPPDocument xmppDocument;

	private Vector<XMPPExtensionListener> xmppExtensionListeners;

	private final Object xmppExtensionListenersLock = new Object();

	private Vector<XMPPContactListener> xmppPresenceListeners;

	private Vector<XMPPSessionListener> xmppSessionListeners;

	/**
	 * The xmpp user interface.
	 * 
	 */
	private final XMPPUser xmppUser;

	/**
	 * Create a XMPPSessionImpl
	 * 
	 */
	XMPPSessionImpl() {
		logger.info("Jive Software:  Smack:  " + SmackConfiguration.getVersion());
		this.xmppExtensionListeners = new Vector<XMPPExtensionListener>(10);
		this.xmppPresenceListeners = new Vector<XMPPContactListener>(10);
		this.xmppSessionListeners = new Vector<XMPPSessionListener>(10);

		this.xmppArtifact = new XMPPArtifact(this);
        this.xmppDocument = new com.thinkparity.model.xmpp.XMPPDocument(this);
		this.xmppContact = new XMPPContact(this);
		this.xmppUser = new XMPPUser(this);

		XMPPConnection.addConnectionListener(new ConnectionEstablishedListener() {
			public void connectionEstablished(final XMPPConnection connection) {
				doNotifyConnectionEstablished(connection);
			}
		});
	}

	/**
	 * Accept a contact's invitation.
	 * 
	 */
	public void acceptInvitation(final JabberId jabberId) throws SmackException {
		xmppContact.accept(jabberId);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#addArtifactTeamMember(java.util.UUID)
	 * 
	 */
	public void addArtifactTeamMember(final UUID uniqueId)
            throws SmackException {
		logger.info("[LMODEL] [XMPP] [ARTIFACT] [ADD TEAM MEMBER]");
		logger.debug(uniqueId);
		final IQArtifact iq = new IQArtifactSubscribe(uniqueId);
		iq.setType(IQ.Type.SET);
		logger.debug(iq);
		sendAndConfirmPacket(iq);
	}

	/**
     * @see com.thinkparity.model.xmpp.XMPPSession#addListener(com.thinkparity.model.xmpp.events.XMPPArtifactListener)
     * 
     */
	public void addListener(final XMPPArtifactListener l) {
		xmppArtifact.addListener(l);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#addListener(com.thinkparity.model.xmpp.events.XMPPContactListener)
	 * 
	 */
	public void addListener(final XMPPContactListener l) {
		xmppContact.addListener(l);
	}

	/**
     * @see com.thinkparity.model.xmpp.XMPPSession#addListener(com.thinkparity.model.xmpp.events.XMPPDocumentListener)
     * 
     */
    public void addListener(final XMPPDocumentListener l) {
        xmppDocument.addListener(l);
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
	 * @see com.thinkparity.model.xmpp.XMPPSession#closeArtifact(java.util.UUID)
	 * 
	 */
	public void closeArtifact(final UUID artifactUniqueId) throws SmackException {
		logger.info("sendClose(UUID)");
		logger.debug(artifactUniqueId);
		final IQArtifact iq = new IQCloseArtifact(artifactUniqueId);
		iq.setType(IQ.Type.SET);
		logger.debug(iq);
		sendAndConfirmPacket(iq);
	}

	/**
     * @see com.thinkparity.model.xmpp.XMPPSession#confirmArtifactReceipt(com.thinkparity.model.xmpp.JabberId,
     *      java.util.UUID, java.lang.Long)
     * 
     */
    public void confirmArtifactReceipt(final JabberId receivedFrom,
            final UUID uniqueId, final Long versionId) throws SmackException {
        xmppArtifact.confirmReceipt(receivedFrom, uniqueId, versionId);
    }

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#createArtifact(java.util.UUID)
	 * 
	 */
	public void createArtifact(final UUID artifactUniqueId) throws SmackException {
		logger.info("sendCreate(UUID)");
		logger.debug(artifactUniqueId);
		final IQArtifact iq = new IQArtifactCreate(artifactUniqueId);
		iq.setType(IQ.Type.SET);
		logger.debug(iq);
		sendAndConfirmPacket(iq);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#declineInvitation(com.thinkparity.model.xmpp.JabberId)
	 * 
	 */
	public void declineInvitation(final JabberId jabberId) throws SmackException {
		xmppContact.decline(jabberId);
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
	 * @see com.thinkparity.model.xmpp.XMPPCore#getConnection()
	 * 
	 */
	public XMPPConnection getConnection() { return smackXMPPConnection; }

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

            // connection termination
			smackXMPPConnection.addConnectionListener(new ConnectionListener() {
			    public void connectionClosed() { doNotifyConnectionClosed(); }
			    public void connectionClosedOnError(final Exception e) {
			        doNotifyConnectionClosedOnError(e);
			    }
			});
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
			// add the artifact listeners
			xmppArtifact.addPacketListeners(smackXMPPConnection);
			// add the contact listeners
			xmppContact.addPacketListeners(smackXMPPConnection);
            // add the document listeners
            xmppDocument.addPacketListeners(smackXMPPConnection);
			// add the user listeners
			xmppUser.addPacketListeners(smackXMPPConnection);

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
     * @see com.thinkparity.model.xmpp.XMPPSession#readArtifactKeyHolder(java.util.UUID)
     * 
     */
	public User readArtifactKeyHolder(final UUID artifactUniqueId)
			throws SmackException {
		logger.info("getArtifactKeyHolder(UUID)");
		logger.debug(artifactUniqueId);
		final IQArtifact iq = new IQGetKeyHolder(artifactUniqueId);
		iq.setType(IQ.Type.GET);
		logger.debug(iq);
		final IQGetKeyHolderResponse response =
			(IQGetKeyHolderResponse) sendAndConfirmPacket(iq);
		return xmppUser.read(response.getKeyHolder());
	}

	/**
     * @see com.thinkparity.model.xmpp.XMPPSession#readArtifactTeam(java.util.UUID)
     * 
     */
	public Set<User> readArtifactTeam(final UUID uniqueId)
            throws SmackException {
		return xmppArtifact.readTeam(uniqueId);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#getContacts()
	 * 
	 */
	public Set<Contact> readContacts() throws SmackException {
		return xmppContact.read();
	}

    /**
	 * @see com.thinkparity.model.xmpp.XMPPSession#readCurrentUser()
	 * 
	 */
	public User readCurrentUser() throws SmackException {
		assertLoggedIn("[LMODEL] [XMPP] [READ CURRENT USER] [NO SESSION]");
		final String qualifiedJabberId = smackXMPPConnection.getUser();
        final JabberId jabberId =
            JabberIdBuilder.parseQualifiedJabberId(qualifiedJabberId);
		return xmppUser.read(jabberId);
	}

	/**
     * @see com.thinkparity.model.xmpp.XMPPSession#readUsers(java.util.Set)
     * 
     */
	public Set<User> readUsers(final Set<JabberId> jabberIds) throws SmackException {
		return xmppUser.read(jabberIds);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#readVCard(com.thinkparity.model.xmpp.JabberId)
	 * 
	 */
	public UserVCard readVCard(final JabberId jabberId) throws SmackException {
		return xmppUser.readVCard(jabberId);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#removeArtifactTeamMember(java.util.UUID)
	 * 
	 */
	public void removeArtifactTeamMember(final UUID artifactUniqueId) throws SmackException {
		logger.info("sendDelete(UUID)");
		logger.debug(artifactUniqueId);
		final XMPPMethod method = new XMPPMethod("unsubscribeuser");
        method.setParameter(Xml.Artifact.UNIQUE_ID, artifactUniqueId);
        method.execute(getConnection());
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#removeListener(com.thinkparity.model.xmpp.events.XMPPArtifactListener)
	 * 
	 */
	public void removeListener(final XMPPArtifactListener l) {
		xmppArtifact.removeListener(l);
	}

    /**
	 * @see com.thinkparity.model.xmpp.XMPPSession#removeListener(com.thinkparity.model.xmpp.events.XMPPContactListener)
	 * 
	 */
	public void removeListener(final XMPPContactListener xmppPresenceListener) {
		logger.info("removeListener(XMPPContactListener)");
		logger.debug(xmppPresenceListener);
		Assert.assertNotNull("Cannot un-register a null presence listener.",
				xmppPresenceListener);
		Assert.assertTrue(
				"Cannot un-register a non-registered presence listener.",
				xmppPresenceListeners.contains(xmppPresenceListener));
		xmppPresenceListeners.remove(xmppPresenceListener);
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
	 * @see com.thinkparity.model.xmpp.XMPPSession#requestArtifactKey(java.util.UUID)
	 * 
	 */
	public void requestArtifactKey(final UUID artifactUniqueId)
			throws SmackException {
		logger.info("sendKeyRequest(UUID)");
		logger.debug(artifactUniqueId);
		final IQArtifact iq = new IQKeyRequest(artifactUniqueId);
		iq.setType(IQ.Type.SET);
		sendAndConfirmPacket(iq);
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
		vCard.setFirstName(userVCard.getName());
		vCard.setLastName(userVCard.getName());
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
	 * Send the packet and wait for a response. If the response conains an
	 * error; a SmackException will be thrown.
	 * 
	 * @param packet
	 *            The packet.
	 * @return The confirmation packet.
	 * @throws SmackException
	 *             If the response contains an error.
	 */
	public Packet sendAndConfirmPacket(final Packet packet)
			throws SmackException {
		final String packetId = packet.getPacketID();
        final PacketCollector collector =
            smackXMPPConnection.createPacketCollector(new PacketIDFilter(packetId));
        sendPacket(packet);
        final Packet confirmationPacket = collector.nextResult();
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
     * Execute a remote method call to reactivate a document.
     * 
     * @throws SmackException
     */
    public void sendDocumentReactivate(final List<JabberId> team,
            final UUID uniqueId, final Long versionId, final String name,
            final byte[] bytes) throws SmackException {
        xmppDocument.sendReactivate(team, uniqueId, versionId, name, bytes);
    }

	/**
     * @see com.thinkparity.model.xmpp.XMPPSession#sendDocument(java.util.Set,
     *      java.util.UUID, java.lang.String, byte[])
     * 
     */
	public void sendDocumentVersion(final Set<JabberId> sendTo,
            final UUID uniqueId, final Long versionId, final String name,
            final byte[] content) throws SmackException {
	    xmppDocument.sendVersion(sendTo, uniqueId, versionId, name, content);
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#sendInvitation(com.thinkparity.model.xmpp.JabberId)
	 * 
	 */
	public void sendInvitation(final JabberId jabberId) throws SmackException {
		xmppContact.invite(jabberId);
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
     * @see com.thinkparity.model.xmpp.XMPPSession#updateCurrentUser(java.lang.String,
     *      java.lang.String, java.lang.String)
     * 
     */
    public void updateCurrentUser(final String name, final String email,
            final String organization) throws SmackException {
        assertLoggedIn("[LMODEL] [XMPP] [UPDATE CURRENT USER] [NO SESSION]");
        final String qualifiedJabberId = smackXMPPConnection.getUser();
        final JabberId jabberId =
            JabberIdBuilder.parseQualifiedJabberId(qualifiedJabberId);
        final UserVCard vCard = xmppUser.readVCard(jabberId);
        vCard.setName(name);
        vCard.setEmail(email);
        vCard.setOrganization(organization);
        xmppUser.updateVCard(jabberId, vCard);
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
	 * Obtain the jabber id for the logged in user.
	 * 
	 * @return The jabber id of the logged in user.
	 */
	private JabberId getJabberId() {
		return JabberIdBuilder.parseQualifiedJabberId(smackXMPPConnection.getUser());
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
				l.artifactClosed(
						artifactClose.getArtifactUUID(),
						artifactClose.getClosedBy());
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
			final JabberId acceptedBy = parseJabberId(iq);
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
			final JabberId deniedBy = parseJabberId(iq);
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
			final JabberId requestedBy = parseJabberId(iq);
			final UUID artifactUniqueId = iq.getArtifactUUID();
			for(final XMPPExtensionListener l : xmppExtensionListeners) {
				l.keyRequested(artifactUniqueId, requestedBy);
			}
		}
	}

    /**
	 * Obtain the from user's jabber id from the packet.
	 * 
	 * @param packet
	 *            The xmpp packet.
	 * @return The from user's jabber id.
	 */
	private JabberId parseJabberId(final Packet packet) {
		return JabberIdBuilder.parseQualifiedJabberId(packet.getFrom());
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
}
