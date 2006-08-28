/*
 * Feb 6, 2005
 */
package com.thinkparity.model.xmpp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import com.thinkparity.codebase.email.EMail;

import com.thinkparity.model.Constants.Jabber;
import com.thinkparity.model.Constants.Xml.Service;
import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.container.ContainerVersion;
import com.thinkparity.model.parity.model.document.DocumentVersion;
import com.thinkparity.model.parity.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.model.parity.model.profile.Profile;
import com.thinkparity.model.parity.model.session.KeyResponse;
import com.thinkparity.model.profile.ProfileEMail;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.smackx.packet.*;
import com.thinkparity.model.xmpp.contact.Contact;
import com.thinkparity.model.xmpp.events.XMPPArtifactListener;
import com.thinkparity.model.xmpp.events.XMPPContactListener;
import com.thinkparity.model.xmpp.events.XMPPContainerListener;
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
		Logger.getLogger(XMPPSessionImpl.class);

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

		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:acceptkeyrequest", new IQAcceptKeyRequestProvider());
		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:closeartifact", new IQCloseArtifactProvider());
		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:denykeyrequest", new IQDenyKeyRequestProvider());
		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:getkeyholder", new IQGetKeyHolderProvider());
		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:getkeys", new IQGetKeysProvider());
		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:getsubscriptions", new IQGetSubscriptionProvider());
		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:keyrequest", new IQKeyRequestProvider());
	}

	private XMPPConnection smackXMPPConnection;

	/** The artifact xmpp interface. */
	private final XMPPArtifact xmppArtifact;

	/** The contact xmpp interface. */
	private final XMPPContact xmppContact;

	/** The container xmpp interface. */
    private final XMPPContainer xmppContainer;

    /** The document xmpp interface. */
    private final XMPPDocument xmppDocument;

    private Vector<XMPPExtensionListener> xmppExtensionListeners;

    private final Object xmppExtensionListenersLock = new Object();

	private Vector<XMPPContactListener> xmppPresenceListeners;

	/** The thinkParity xmpp profile interface. */
    private final XMPPProfile xmppProfile;

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
        this.xmppContainer = new XMPPContainer(this);
        this.xmppDocument = new XMPPDocument(this);
		this.xmppContact = new XMPPContact(this);
        this.xmppProfile = new XMPPProfile(this);
		this.xmppUser = new XMPPUser(this);

		XMPPConnection.addConnectionListener(new ConnectionEstablishedListener() {
			public void connectionEstablished(final XMPPConnection connection) {
				doNotifyConnectionEstablished(connection);
			}
		});
	}

	/**
     * @see com.thinkparity.model.xmpp.XMPPSession#acceptInvitation(com.thinkparity.model.xmpp.JabberId)
     * 
     */
	public void acceptInvitation(final JabberId invitedBy)
            throws SmackException {
		xmppContact.accept(invitedBy);
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
     * @see com.thinkparity.model.xmpp.XMPPSession#addListener(com.thinkparity.model.xmpp.events.XMPPContainerListener)
     * 
     */
    public void addListener(final XMPPContainerListener l) {
        xmppContainer.addListener(l);
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
     * @see com.thinkparity.model.xmpp.XMPPSession#addProfileEmail(com.thinkparity.model.xmpp.JabberId, com.thinkparity.model.profile.ProfileEMail)
     */
    public void addProfileEmail(final JabberId userId, final EMail email) {
        xmppProfile.addEmail(userId, email);
    }

	/**
     * @see com.thinkparity.model.xmpp.XMPPSession#removeProfileEmail(com.thinkparity.model.xmpp.JabberId, com.thinkparity.codebase.email.EMail)
     */
    public void removeProfileEmail(final JabberId userId, final EMail email) {
        xmppProfile.removeEmail(userId, email);
    }

    /**
     * @see com.thinkparity.model.xmpp.XMPPSession#addTeamMember(java.util.UUID,
     *      com.thinkparity.model.xmpp.JabberId)
     * 
     */
	public void addTeamMember(final UUID uniqueId, final JabberId jabberId)
            throws SmackException {
	    xmppArtifact.addTeamMember(uniqueId, jabberId);
	}

	/**
     * @see com.thinkparity.model.xmpp.XMPPCore#assertContainsResult(java.lang.Object,
     *      com.thinkparity.model.parity.model.io.xmpp.XMPPMethodResponse)
     * 
     */
    public void assertContainsResult(final Object assertion,
            final XMPPMethodResponse response) {
        Assert.assertTrue(assertion, response.containsResult());
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
	public void createArtifact(final UUID artifactUniqueId)
            throws SmackException {
		logger.info("sendCreate(UUID)");
		logger.debug(artifactUniqueId);
		final IQArtifact iq = new IQArtifactCreate(artifactUniqueId);
		iq.setType(IQ.Type.SET);
		logger.debug(iq);
		sendAndConfirmPacket(iq);
	}

	/**
     * @see com.thinkparity.model.xmpp.XMPPSession#createDraft(java.util.UUID)
     */
    public void createDraft(final UUID uniqueId) {
        logger.info("[XMPP SESSION] [CREATE DRAFT]");
        logger.debug(uniqueId);
        xmppArtifact.createDraft(uniqueId);
    }

    /**
     * @see com.thinkparity.model.xmpp.XMPPSession#declineInvitation(java.lang.String,
     *      com.thinkparity.model.xmpp.JabberId)
     * 
     */
    public void declineInvitation(final EMail invitedAs,
            final JabberId invitedBy) throws SmackException {
		xmppContact.decline(invitedAs, invitedBy);
	}

	/**
     * @see com.thinkparity.model.xmpp.XMPPSession#deleteArtifact(java.util.UUID)
     */
    public void deleteArtifact(final UUID uniqueId) {
        xmppArtifact.delete(uniqueId);
    }

	/**
     * @see com.thinkparity.model.xmpp.XMPPSession#deleteDraft(java.util.UUID)
     */
    public void deleteDraft(final UUID uniqueId) {
        xmppArtifact.deleteDraft(uniqueId);
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
     * Obtain the connection's jabber id.
     * 
     * @return A jabber id.
     */
	public JabberId getJabberId() {
		return JabberIdBuilder.parseQualifiedJabberId(smackXMPPConnection.getUser());
	}

	/**
	 * @see com.thinkparity.model.xmpp.XMPPSession#sendInvitation(com.thinkparity.model.xmpp.JabberId)
	 * 
	 */
	public void inviteContact(final EMail email) throws SmackException {
		xmppContact.invite(email);
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
            // add the container listeners
            xmppContainer.addPacketListeners(smackXMPPConnection);
            // add the document listeners
            xmppDocument.addPacketListeners(smackXMPPConnection);
            // add the profile listeners
            xmppProfile.addPacketListeners(smackXMPPConnection);
			// add the user listeners
			xmppUser.addPacketListeners(smackXMPPConnection);

			smackXMPPConnection.login(username, password, Jabber.RESOURCE);
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
     * @see com.thinkparity.model.xmpp.XMPPSession#publish(com.thinkparity.model.parity.model.container.ContainerVersion,
     *      java.util.Map, java.util.List, com.thinkparity.model.xmpp.JabberId,
     *      java.util.Calendar)
     * 
     */
    public void publish(ContainerVersion container, Map<DocumentVersion, InputStream> documents, List<JabberId> publishTo, JabberId publishedBy, Calendar publishedOn) throws SmackException {
        try {
            xmppContainer.publish(container, documents, publishTo, publishedBy,
                    publishedOn);
        } catch (final IOException iox) {
            throw XMPPErrorTranslator.translate(iox);
        }
    }

	/**
     * @see com.thinkparity.model.xmpp.XMPPSession#readArtifactTeam(java.util.UUID)
     * 
     */
	public List<User> readArtifactTeam(final UUID uniqueId)
            throws SmackException {
		return xmppArtifact.readTeam(uniqueId);
	}

	/**
     * Read a contact.
     * 
     * @param contactId
     *            A contact id.
     * @return A contact.
     */
    public Contact readContact(final JabberId contactId) throws SmackException {
        try {
            return xmppContact.read(contactId);
        } catch (final XMPPException xmppx) {
            throw XMPPErrorTranslator.translate(xmppx);
        }
    }

    /**
	 * @see com.thinkparity.model.xmpp.XMPPSession#getContacts()
	 * 
	 */
	public List<Contact> readContacts() throws SmackException {
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
     * @see com.thinkparity.model.xmpp.XMPPSession#readKeyHolder(java.util.UUID)
     * 
     */
	public JabberId readKeyHolder(final UUID uniqueId) {
		return xmppArtifact.readKeyHolder(uniqueId);
	}

    /**
     * @see com.thinkparity.model.xmpp.XMPPSession#readProfile()
     * 
     */
    public Profile readProfile() throws SmackException {
        assertLoggedIn("[LMODEL] [XMPP] [READ PROFILE] [USER NOT ONLINE]");
        return xmppProfile.read(getJabberId());
    }

	/**
     * @see com.thinkparity.model.xmpp.XMPPSession#readProfileEMails()
     */
    public List<ProfileEMail> readProfileEMails() {
        return xmppProfile.readEMails(getJabberId());
    }

    /**
     * @see com.thinkparity.model.xmpp.XMPPSession#readUsers(java.util.Set)
     * 
     */
	public Set<User> readUsers(final Set<JabberId> jabberIds) throws SmackException {
		return xmppUser.read(jabberIds);
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
     * Remove a team member from the artifact team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
    public void removeTeamMember(final UUID uniqueId, final JabberId jabberId) {
        xmppArtifact.removeTeamMember(uniqueId, jabberId);
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
     * @see com.thinkparity.model.xmpp.XMPPSession#send(ContainerVersion, Map,
     *      User, JabberId, Calendar)
     *
     */
    public void send(final ContainerVersion container,
            final Map<DocumentVersion, InputStream> documents,
            final List<JabberId> sendTo, final JabberId sentBy, final Calendar sentOn)
            throws SmackException {
        try {
            xmppContainer.send(container, documents, sendTo, sentBy, sentOn);
        } catch (final IOException iox) {
            throw XMPPErrorTranslator.translate(iox);
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
	 * @see com.thinkparity.model.xmpp.XMPPSession#sendKeyResponse(java.util.UUID,
	 *      com.thinkparity.model.parity.model.session.KeyResponse,
	 *      com.thinkparity.model.xmpp.user.User)
	 * 
	 */
	public void sendKeyResponse(final UUID artifactUniqueId,
            final KeyResponse keyResponse, final JabberId jabberId)
            throws SmackException {
		logger.info("sendKeyResponse(UUID,KeyResponse,User)");
		logger.debug(artifactUniqueId);
		logger.debug(jabberId);
		final IQArtifact iq;
		switch(keyResponse) {
		case ACCEPT:
			iq = new IQAcceptKeyRequest(artifactUniqueId, jabberId);
			break;
		case DENY:
			iq = new IQDenyKeyRequest(artifactUniqueId, jabberId);
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
     * @see com.thinkparity.model.xmpp.XMPPSession#updateProfile(com.thinkparity.model.parity.model.profile.Profile)
     */
    public void updateProfile(final Profile profile) {
        xmppUser.updateProfile(profile);
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
