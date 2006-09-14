/*
 * Created On: Feb 6, 2005
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.ProviderManager;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.Constants.Jabber;
import com.thinkparity.ophelia.model.Constants.Xml.Service;
import com.thinkparity.ophelia.model.document.DocumentVersion;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.ophelia.model.util.EventNotifier;
import com.thinkparity.ophelia.model.util.smack.SmackException;
import com.thinkparity.ophelia.model.util.smackx.packet.*;
import com.thinkparity.ophelia.model.util.xmpp.events.ArtifactListener;
import com.thinkparity.ophelia.model.util.xmpp.events.ContactListener;
import com.thinkparity.ophelia.model.util.xmpp.events.ContainerListener;
import com.thinkparity.ophelia.model.util.xmpp.events.SessionListener;

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

		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:closeartifact", new IQCloseArtifactProvider());
		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:getkeyholder", new IQGetKeyHolderProvider());
		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:getkeys", new IQGetKeysProvider());
		ProviderManager.addIQProvider(Service.NAME, "jabber:iq:parity:getsubscriptions", new IQGetSubscriptionProvider());
	}

    /** A list of the session listeners. */
    private final List<SessionListener> listeners;

    /** The artifact xmpp interface. */
	private final XMPPArtifact xmppArtifact;

    /** The xmpp connection. */
    private XMPPConnection xmppConnection;

    /** The contact xmpp interface. */
	private final XMPPContact xmppContact;

    /** The container xmpp interface. */
    private final XMPPContainer xmppContainer;

	/** The thinkParity xmpp profile interface. */
    private final XMPPProfile xmppProfile;

    /**
	 * The xmpp user interface.
	 * 
	 */
	private final XMPPUser xmppUser;

    /**
	 * Create a XMPPSessionImpl
	 * 
	 */
	public XMPPSessionImpl() {
		logger.info("Jive Software:  Smack:  " + SmackConfiguration.getVersion());
		this.listeners = new ArrayList<SessionListener>();
		this.xmppArtifact = new XMPPArtifact(this);
        this.xmppContainer = new XMPPContainer(this);
		this.xmppContact = new XMPPContact(this);
        this.xmppProfile = new XMPPProfile(this);
		this.xmppUser = new XMPPUser(this);
	}

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#acceptContactInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.jabber.JabberId, java.util.Calendar)
     * 
     */
    public void acceptContactInvitation(final JabberId userId,
            final JabberId invitedBy, final Calendar acceptedOn) {
		xmppContact.acceptInvitation(userId, invitedBy, acceptedOn);
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#addListener(com.thinkparity.ophelia.model.util.xmpp.events.XMPPArtifactListener)
     * 
     */
	public void addListener(final ArtifactListener listener) {
		xmppArtifact.addListener(listener);
	}

	/**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#addListener(com.thinkparity.ophelia.model.util.xmpp.events.XMPPContactListener)
	 * 
	 */
	public void addListener(final ContactListener listener) {
		xmppContact.addListener(listener);
	}

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#addListener(com.thinkparity.ophelia.model.util.xmpp.events.XMPPContainerListener)
     * 
     */
    public void addListener(final ContainerListener listener) {
        xmppContainer.addListener(listener);
    }

	/**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#addListener(com.thinkparity.ophelia.model.util.xmpp.events.XMPPSessionListener)
	 */
	public void addListener(final SessionListener listener) {
        synchronized (listeners) {
            if (listeners.contains(listener)) {
                return;
            } else {
                listeners.add(listener);
            }
        }
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#addProfileEmail(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.model.profile.ProfileEMail)
     */
    public void addProfileEmail(final JabberId userId, final EMail email) {
        xmppProfile.addEmail(userId, email);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#addTeamMember(java.util.UUID,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
	public void addTeamMember(final UUID uniqueId, final JabberId jabberId)
            throws SmackException {
	    xmppArtifact.addTeamMember(uniqueId, jabberId);
	}

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPCore#assertContainsResult(java.lang.Object,
     *      com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse)
     * 
     */
    public void assertContainsResult(final Object assertion,
            final XMPPMethodResponse response) {
        Assert.assertTrue(assertion, response.containsResult());
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#clearListeners()
     */
    public void clearListeners() {
        listeners.clear();
        xmppArtifact.clearListeners();
        xmppContact.clearListeners();
        xmppContainer.clearListeners();
        xmppProfile.clearListeners();
        xmppUser.clearListeners();
    }

	/**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#closeArtifact(java.util.UUID)
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
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#confirmArtifactReceipt(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID, java.lang.Long,
     *      com.thinkparity.codebase.jabber.JabberId)
     */
    public void confirmArtifactReceipt(final JabberId userId,
            final UUID uniqueId, final Long versionId, final JabberId receivedBy) {
        xmppArtifact.confirmReceipt(userId, uniqueId, versionId, receivedBy);
    }

	/**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createArtifact(java.util.UUID)
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
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createDraft(java.util.UUID)
     */
    public void createDraft(final UUID uniqueId) {
        logger.info("[XMPP SESSION] [CREATE DRAFT]");
        logger.debug(uniqueId);
        xmppArtifact.createDraft(uniqueId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#declineInvitation(java.lang.String,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public void declineInvitation(final EMail invitedAs,
            final JabberId invitedBy) throws SmackException {
		xmppContact.decline(invitedAs, invitedBy);
	}

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#deleteArtifact(java.util.UUID)
     */
    public void deleteArtifact(final UUID uniqueId) {
        xmppArtifact.delete(uniqueId);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#deleteContact(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.codebase.jabber.JabberId)
     */
    public void deleteContact(final JabberId userId, final JabberId contactId) {
        xmppContact.delete(userId, contactId);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#deleteContactInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.email.EMail, java.util.Calendar)
     * 
     */
    public void deleteContactInvitation(final JabberId userId, final EMail invitedAs,
            final Calendar deletedOn) {
        xmppContact.deleteInvitation(userId, invitedAs, deletedOn);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#deleteDraft(java.util.UUID)
     */
    public void deleteDraft(final UUID uniqueId) {
        xmppArtifact.deleteDraft(uniqueId);
    }

	/**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#sendInvitation(com.thinkparity.codebase.jabber.JabberId)
	 * 
	 */
	public void extendInvitation(final JabberId userId, final EMail extendedTo,
            final Calendar extendedOn) {
		xmppContact.extendInvitation(userId, extendedTo, extendedOn);
	}

	/**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#flag(java.util.UUID,
	 *      com.thinkparity.codebase.model.artifact.ArtifactFlag)
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
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#getArtifactKeys()
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
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPCore#getConnection()
	 * 
	 */
	public XMPPConnection getConnection() { return xmppConnection; }

	/**
     * Obtain the connection's jabber id.
     * 
     * @return A jabber id.
     */
	public JabberId getJabberId() {
		return JabberIdBuilder.parseQualifiedJabberId(xmppConnection.getUser());
	}

    /**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#isLoggedIn()
	 */
	public Boolean isLoggedIn() {
		logger.info("isLoggedIn()");
		return (null != xmppConnection
				&& xmppConnection.isConnected()
				&& xmppConnection.isAuthenticated());
	}

    /**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#login(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	public void login(final Environment environment,
            final Credentials credentials) throws SmackException {
	    assertIsReachable(environment);
        try {
			if (Boolean.TRUE == isLoggedIn())
				logout();
            switch (environment.getServerProtocol()) {
            case XMPP:
                xmppConnection = new XMPPConnection(environment.getServerHost(),
                            environment.getServerPort());
                break;
            case XMPPS:
                xmppConnection =
                    new SSLXMPPConnection(environment.getServerHost(), environment.getServerPort());
                break;
            default:
                Assert.assertUnreachable("UNKNOWN ENVIRONMENT PROTOCOL");
			}

            xmppConnection.addConnectionListener(new ConnectionListener() {
			    public void connectionClosed() {
                    handleConnectionClosed();
			    }
			    public void connectionClosedOnError(final Exception x) {
			        handleConnectionClosed(x);
			    }
			});
            xmppConnection.addPacketListener(new PacketListener() {
                    public void processPacket(final Packet packet) {
                        logger.debug(packet);
                    }
                }, new PacketFilter() {
                    public boolean accept(final Packet packet) {
                        return true;
                    }
                });
			xmppArtifact.addEventHandlers();
			xmppContact.addEventHandlers();
            xmppContainer.addEventHandlers();
			xmppUser.addEventHandlers();
            xmppConnection.login(
                    credentials.getUsername(), credentials.getPassword(), Jabber.RESOURCE);
            handleConnectionEstablished();
		} catch (final XMPPException xmppx) {
			logger.error("login(String,Integer,String,String)", xmppx);
			throw XMPPErrorTranslator.translate(xmppx);
		}
	}

    /**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#logout()
	 */
	public void logout() throws SmackException {
		logger.info("logout()");
        clearListeners();
        xmppConnection.close();
        xmppConnection = null;
	}

	/**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#processOfflineQueue()
	 * 
	 */
	public void processOfflineQueue() throws SmackException {
		logger.info("processOfflineQueue()");
		final IQParity processOfflineQueue = new IQProcessOfflineQueue();
		sendAndConfirmPacket(processOfflineQueue);
	}

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#publish(com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.Map, java.util.List, com.thinkparity.codebase.jabber.JabberId,
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
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readArtifactTeamIds(java.util.UUID)
     */
	public List<JabberId> readArtifactTeamIds(final UUID uniqueId) {
		return xmppArtifact.readTeamIds(uniqueId);
	}

	/**
     * Read a contact.
     * 
     * @param contactId
     *            A contact id.
     * @return A contact.
     */
    public Contact readContact(final JabberId userId, final JabberId contactId) {
        return xmppContact.read(userId, contactId);
    }

    /**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#getContacts()
	 * 
	 */
	public List<Contact> readContacts(final JabberId userId) {
		return xmppContact.read(userId);
	}

    /**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readCurrentUser()
	 * 
	 */
	public User readCurrentUser() throws SmackException {
		assertLoggedIn("[LMODEL] [XMPP] [READ CURRENT USER] [NO SESSION]");
		final String qualifiedJabberId = xmppConnection.getUser();
        final JabberId jabberId =
            JabberIdBuilder.parseQualifiedJabberId(qualifiedJabberId);
		return xmppUser.read(jabberId);
	}

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readKeyHolder(java.util.UUID)
     * 
     */
	public JabberId readKeyHolder(final JabberId userId, final UUID uniqueId) {
		return xmppArtifact.readKeyHolder(userId, uniqueId);
	}

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readProfile()
     * 
     */
    public Profile readProfile() throws SmackException {
        assertLoggedIn("[LMODEL] [XMPP] [READ PROFILE] [USER NOT ONLINE]");
        return xmppProfile.read(getJabberId());
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readProfileEMails()
     */
    public List<EMail> readProfileEMails() {
        return xmppProfile.readEMails(getJabberId());
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readProfileSecurityQuestion(com.thinkparity.codebase.jabber.JabberId)
     */
    public String readProfileSecurityQuestion(final JabberId userId) {
        return xmppProfile.readSecurityQuestion(userId);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readUsers(java.util.Set)
     * 
     */
	public User readUser(final JabberId userId) {
		return xmppUser.read(userId);
	}

    /**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#removeListener(com.thinkparity.ophelia.model.util.xmpp.events.XMPPArtifactListener)
	 * 
	 */
	public void removeListener(final ArtifactListener listener) {
		xmppArtifact.removeListener(listener);
	}

	/**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#removeListener(com.thinkparity.ophelia.model.util.xmpp.events.XMPPContactListener)
	 * 
	 */
	public void removeListener(final ContactListener listener) {
	    xmppContact.removeListener(listener);
	}

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#removeListener(com.thinkparity.ophelia.model.util.xmpp.events.ContainerListener)
     */
    public void removeListener(final ContainerListener listener) {
        xmppContainer.removeListener(listener);
    }

    /**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#removeListener(com.thinkparity.ophelia.model.util.xmpp.events.XMPPSessionListener)
	 */
	public void removeListener(final SessionListener listener) {
        synchronized (listeners) {
            if (listeners.contains(listener)) {
                listeners.remove(listener);
            } else {
                return;
            }
        }
	}

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#removeProfileEmail(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.codebase.email.EMail)
     */
    public void removeProfileEmail(final JabberId userId, final EMail email) {
        xmppProfile.removeEmail(userId, email);
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
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#resetProfileCredentials(com.thinkparity.codebase.jabber.JabberId)
     */
    public String resetProfilePassword(final JabberId userId,
            final String securityAnswer) {
        return xmppProfile.resetPassword(userId, securityAnswer);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#send(ContainerVersion, Map,
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
            xmppConnection.createPacketCollector(new PacketIDFilter(packetId));
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
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#sendLogFileArchive(java.io.File)
	 */
	public void sendLogFileArchive(final File logFileArchive, final User user)
			throws SmackException {
		logger.info("sendLogFileArchive(File)");
		logger.debug(logFileArchive);
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#updateProfile(com.thinkparity.codebase.model.profile.Profile)
     */
    public void updateProfile(final JabberId userId, final Profile profile) {
        xmppProfile.update(userId, profile);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#updateCredentials(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.codebase.model.session.Credentials)
     */
    public void updateProfileCredentials(final JabberId userId,
            final Credentials credentials) {
        try {
            Assert.assertTrue("CAN ONLY UPDATE PROFILE CREDENTIALS",
                    userId.equals(getJabberId()));
            final AccountManager accountManager = new AccountManager(xmppConnection);
            accountManager.changePassword(credentials.getPassword());
        } catch (final Throwable t) {
            XMPPErrorTranslator.translateUnchecked(this,
                    "UPDATE PROFILE CREDENTIALS", t);
        }
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#verifyProfileEmail(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.codebase.email.EMail, java.lang.String)
     */
    public void verifyProfileEmail(final JabberId userId, final EMail email, final String key) {
        xmppProfile.verifyEmail(userId, email, key);
    }

	/**
     * Assert that the environment is online.
     * 
     * @param assertion
     *            An assertion.
     * @param environment
     *            An environment.
     */
    protected void assertIsReachable(final Environment environment) {
        Assert.assertTrue(environment.isReachable(),
                "Environment {0} not reachable.", environment);
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
                xmppConnection.isAuthenticated());
	}

	/**
	 * Event handler for the connectionClosed event generated by the smack
	 * connection listener impl. This will iterate through the
	 * xmppSessionListeners list and fire the sessionTerminated event.
	 */
	private void handleConnectionClosed() {
	    notifyListeners(new EventNotifier<SessionListener>() {
            public void notifyListener(final SessionListener listener) {
                listener.sessionTerminated();
            }
        });
	}

	/**
	 * Event handler for the connectionClosedOnerror event generated by the
	 * smack connection listener impl. This will iterate through the
	 * xmppSessionListeners list and fire the sessionTerminated event.
	 * 
	 * @param x
	 *            <code>java.lang.Exception</code>
	 */
	private void handleConnectionClosed(final Exception x) {
	    notifyListeners(new EventNotifier<SessionListener>() {
            public void notifyListener(final SessionListener listener) {
                listener.sessionTerminated(x);
            }
        });
	}

    /**
	 * Event handler for the connectionEstablished event generated by the smack
	 * connection listener impl. This will iterate through the
	 * xmppSessionListeners list and fire the sessionEstablished event.
	 * 
	 * @param xmppConnection
	 *            <code>org.jivesoftware.smack.XMPPConnection</code>
	 */
	private void handleConnectionEstablished() {
        notifyListeners(new EventNotifier<SessionListener>() {
            public void notifyListener(final SessionListener listener) {
                listener.sessionEstablished();
            }
        });
	}

    /**
     * Notify all session listeners.
     * 
     * @param notifier
     *            A session listener notifier.
     */
    private void notifyListeners(final EventNotifier<SessionListener> notifier) {
        synchronized (listeners) {
            for (final SessionListener listener : listeners) {
                notifier.notifyListener(listener);
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
			final Message message = xmppConnection.createChat(
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
        xmppConnection.sendPacket(packet);
		// this sleep has been inserted because when packets are sent within
		// x milliseconds of each other, they tend to get swallowed by the
		// smack library
		try { Thread.sleep(SEND_PACKET_SLEEP_DURATION); }
		catch(final InterruptedException ix) {
			throw XMPPErrorTranslator.translate(ix);
		}
	}
}
