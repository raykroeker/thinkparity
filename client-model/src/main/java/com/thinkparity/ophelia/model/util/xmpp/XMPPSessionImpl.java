/*
 * Created On: Feb 6, 2005
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jivesoftware.smack.*;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Packet;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.Assertion;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.ophelia.model.util.smack.SmackErrorTranslator;
import com.thinkparity.ophelia.model.util.smack.SmackException;
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
	private static final Log4JWrapper logger = new Log4JWrapper();

    /**
     * The number of milliseconds to sleep immediately prior to executing the
     * login api.
     */
    private static final int LOGIN_SLEEP_DURATION = 3 * 1000;

    static {
		// set the subscription mode such that all requests are manual
		Roster.setDefaultSubscriptionMode(Roster.SUBSCRIPTION_MANUAL);
        logger.logInfo("Smack v{0}", SmackConfiguration.getVersion());
	}

    /** A list of the session listeners. */
    private final List<SessionListener> listeners;

    /** An unauthenticated xmpp connection. */
    private XMPPConnection xmppAnonymousConnection;

    /** The archive xmpp interface. */
    private final XMPPArchive xmppArchive;

    /** The artifact xmpp interface. */
	private final XMPPArtifact xmppArtifact;

    /** The backup xmpp interface. */
    private final XMPPBackup xmppBackup;

    /** An authenticated xmpp connection. */
    private XMPPConnection xmppConnection;

    /** The contact xmpp interface. */
	private final XMPPContact xmppContact;

    /** The container xmpp interface. */
    private final XMPPContainer xmppContainer;

    /** The thinkParity xmpp profile interface. */
    private final XMPPProfile xmppProfile;

    /** The thinkParity xmpp system interface. */
    private final XMPPSystem xmppSystem;

    /** The thinkParity xmpp user interface. */
	private final XMPPUser xmppUser;

    /**
	 * Create a XMPPSessionImpl
	 * 
	 */
	public XMPPSessionImpl() {
		this.listeners = new ArrayList<SessionListener>();
        this.xmppArchive = new XMPPArchive(this);
        this.xmppBackup = new XMPPBackup(this);
		this.xmppArtifact = new XMPPArtifact(this);
        this.xmppContainer = new XMPPContainer(this);
		this.xmppContact = new XMPPContact(this);
        this.xmppProfile = new XMPPProfile(this);
        this.xmppSystem = new XMPPSystem(this);
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
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPCore#addPacketListener(org.jivesoftware.smack.PacketListener, org.jivesoftware.smack.filter.PacketFilter)
     */
    public void addPacketListener(final PacketListener listener, final PacketFilter filter) {
        logger.logApiId();
        logger.logVariable("listener", listener);
        logger.logVariable("filter", filter);
        xmppConnection.addPacketListener(listener, filter);
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
	public void addTeamMember(final UUID uniqueId, final JabberId jabberId) {
	    xmppArtifact.addTeamMember(uniqueId, jabberId);
	}

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#archiveArtifact(com.thinkparity.codebase.jabber.JabberId, java.util.UUID)
     */
    public void archiveArtifact(final JabberId userId, final UUID uniqueId) {
        xmppArchive.archive(userId, uniqueId);
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
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#confirmArtifactReceipt(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID, java.lang.Long,
     *      com.thinkparity.codebase.jabber.JabberId)
     */
    public void confirmArtifactReceipt(final JabberId userId,
            final UUID uniqueId, final Long versionId,
            final JabberId receivedBy, final Calendar receivedOn) {
        xmppArtifact.confirmReceipt(userId, uniqueId, versionId, receivedBy, receivedOn);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createArtifact(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID)
     */
	public void createArtifact(final JabberId userId, final UUID uniqueId) {
	    xmppArtifact.create(userId, uniqueId);
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createDraft(java.util.UUID)
     */
    public void createDraft(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        xmppArtifact.createDraft(uniqueId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#declineInvitation(java.lang.String,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public void declineInvitation(final EMail invitedAs,
            final JabberId invitedBy) {
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
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPCore#execute(com.thinkparity.ophelia.model.io.xmpp.XMPPMethod)
     */
    public XMPPMethodResponse execute(final XMPPMethod method) {
        return execute(method, Boolean.FALSE);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPCore#execute(com.thinkparity.ophelia.model.io.xmpp.XMPPMethod,
     *      java.lang.Boolean)
     */
    public XMPPMethodResponse execute(final XMPPMethod method,
            final Boolean assertResponse) {
        return execute(method, xmppConnection, assertResponse);
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
		logger.logApiId();
		return (null != xmppConnection
				&& xmppConnection.isConnected()
				&& xmppConnection.isAuthenticated());
	}

    /**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#login(java.lang.String, java.lang.Integer, java.lang.String, java.lang.String)
	 */
	public void login(final Environment environment,
            final Credentials credentials) {
        login(1, environment, credentials);
    }

    /**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#logout()
	 */
	public void logout() {
		logger.logApiId();
        clearListeners();
        xmppConnection.close();
        xmppConnection = null;
        xmppAnonymousConnection = null;
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#openArchiveDocumentVersionContent(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID, java.lang.Long)
     */
    public InputStream openArchiveDocumentVersion(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        return xmppArchive.openDocumentVersion(userId, uniqueId, versionId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#openBackupDocumentVersion(com.thinkparity.codebase.jabber.JabberId, java.util.UUID, java.lang.Long)
     */
    public InputStream openBackupDocumentVersion(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        return xmppBackup.openDocumentVersion(userId, uniqueId, versionId);
    }

	/**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#processOfflineQueue()
	 * 
	 */
	public void processOfflineQueue(final JabberId userId) {
        xmppSystem.processOfflineQueue(userId);
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#publish(com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.Map, java.util.List, com.thinkparity.codebase.jabber.JabberId,
     *      java.util.Calendar)
     * 
     */
    public void publish(ContainerVersion container,
            Map<DocumentVersion, InputStream> documents,
            List<JabberId> publishTo, JabberId publishedBy, Calendar publishedOn) {
        try {
            xmppContainer.publish(container, documents, publishTo, publishedBy,
                    publishedOn);
        } catch (final IOException iox) {
            throw translateError(iox);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readArchiveContainer(com.thinkparity.codebase.jabber.JabberId, java.util.UUID)
     */
    public Container readArchiveContainer(final JabberId userId,
            final UUID uniqueId) {
        return xmppArchive.readContainer(userId, uniqueId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readArchiveContaines(com.thinkparity.codebase.jabber.JabberId)
     */
    public List<Container> readArchiveContainers(final JabberId userId) {
        return xmppArchive.readContainers(userId);
    }

    /**
     * Read the archived containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>List&lt;ContainerVersion&gt;</code>.
     */
    public List<ContainerVersion> readArchiveContainerVersions(
            final JabberId userId, final UUID uniqueId) {
        return xmppArchive.readContainerVersions(userId, uniqueId);
    }

    /**
     * Read the archived containers.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @return A <code>List&lt;Document&gt;</code>.
     */
    public List<Document> readArchiveDocuments(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        return xmppArchive.readDocuments(userId, uniqueId, versionId);
    }

    /**
     * Read the archived document versions.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param documentUniqueId
     *            A document unique id <code>UUID</code>.
     * @return A <code>List&lt;DocumentVersion&gt;</code>.
     */
    public List<DocumentVersion> readArchiveDocumentVersions(
            final JabberId userId, final UUID uniqueId, final Long versionId,
            final UUID documentUniqueId) {
        return xmppArchive.readDocumentVersions(userId, uniqueId, versionId,
                documentUniqueId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readArchiveTeamIds(com.thinkparity.codebase.jabber.JabberId, java.util.UUID)
     */
    public List<JabberId> readArchiveTeamIds(final JabberId userId,
            final UUID uniqueId) {
        return xmppArchive.readTeamIds(userId, uniqueId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readArtifactTeamIds(java.util.UUID)
     */
	public List<JabberId> readArtifactTeamIds(final UUID uniqueId) {
		return xmppArtifact.readTeamIds(uniqueId);
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readBackupContainer(com.thinkparity.codebase.jabber.JabberId, java.util.UUID)
     */
    public Container readBackupContainer(final JabberId userId, final UUID uniqueId) {
        return xmppBackup.readContainer(userId, uniqueId);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readBackupContainers(com.thinkparity.codebase.jabber.JabberId)
     */
    public List<Container> readBackupContainers(final JabberId userId) {
        return xmppBackup.readContainers(userId);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readBackupContainerVersions(com.thinkparity.codebase.jabber.JabberId, java.util.UUID)
     */
    public List<ContainerVersion> readBackupContainerVersions(final JabberId userId, final UUID uniqueId) {
        return xmppBackup.readContainerVersions(userId, uniqueId);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readBackupDocuments(com.thinkparity.codebase.jabber.JabberId, java.util.UUID, java.lang.Long)
     */
    public List<Document> readBackupDocuments(final JabberId userId, final UUID uniqueId, final Long versionId) {
        return xmppBackup.readDocuments(userId, uniqueId, versionId);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readBackupDocumentVersions(com.thinkparity.codebase.jabber.JabberId, java.util.UUID, java.lang.Long, java.util.UUID)
     */
    public List<DocumentVersion> readBackupDocumentVersions(
            final JabberId userId, final UUID uniqueId, final Long versionId,
            final UUID documentUniqueId) {
        return xmppBackup.readDocumentVersions(userId, uniqueId, versionId,
                documentUniqueId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readBackupTeamIds(com.thinkparity.codebase.jabber.JabberId, java.util.UUID)
     */
    public List<JabberId> readBackupTeamIds(final JabberId userId, final UUID uniqueId) {
        return xmppBackup.readTeamIds(userId, uniqueId);
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
	public User readCurrentUser() {
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
    public Profile readProfile() {
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
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#restoreArtifact(com.thinkparity.codebase.jabber.JabberId, java.util.UUID)
     */
    public void restoreArtifact(final JabberId userId, final UUID uniqueId) {
        xmppArchive.restore(userId, uniqueId);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#send(ContainerVersion, Map,
     *      User, JabberId, Calendar)
     *
     */
    public void send(final ContainerVersion container,
            final Map<DocumentVersion, InputStream> documents,
            final List<JabberId> sendTo, final JabberId sentBy,
            final Calendar sentOn) {
        try {
            xmppContainer.send(container, documents, sendTo, sentBy, sentOn);
        } catch (final IOException iox) {
            throw translateError(iox);
        }
    }

	/**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#sendLogFileArchive(java.io.File)
	 */
	public void sendLogFileArchive(final File logFileArchive, final User user) {
		logger.logApiId();
        logger.logVariable("logFileArchive", logFileArchive);
        logger.logVariable("user", user);
	}

	/**
     * Translate an error into a parity unchecked error.
     * 
     * @param t
     *            An error.
     */
    public RuntimeException translateError(final Throwable t) {
        if (SmackException.class.isAssignableFrom(t.getClass())) {
            return (SmackException) t;
        } else if (Assertion.class.isAssignableFrom(t.getClass())) {
            final String errorId = new ErrorHelper().getErrorId(t);
            logger.logError(t, errorId);
            return (Assertion) t;
        }
        else {
            final String errorId = new ErrorHelper().getErrorId(t);
            logger.logError(t, errorId);
            return SmackErrorTranslator.translate(this, errorId, t);
        }
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
            throw translateError(t);
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
     * Log an error.
     * 
     * @param message
     *            An error message.
     */
    protected final <E extends Throwable> E logError(final E e,
            final String errorPattern, final Object... errorArguments) {
        return logger.logError(e, errorPattern, errorArguments);
    }

    /**
     * Log a variable.  Note that only the variable value will be rendered.
     * 
     * @param name
     *            The variable name.
     * @param value
     *            The variable value.
     */
    protected final <V> V logVariable(final String name, final V value) {
        return logger.logVariable(name, value);
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
     * Execute a method using the connection.
     * 
     * @param method
     *            An xmpp remote method.
     * @param connection
     *            An xmpp connection.
     * @param assertResponse
     *            Whether or not to assert the response of the method.
     * @return An xmpp remote method response.
     */
    private XMPPMethodResponse execute(final XMPPMethod method,
            final XMPPConnection connection, final Boolean assertResponse) {
        final XMPPMethodResponse response = method.execute(connection);
        if (assertResponse) {
            assertContainsResult("Method response is empty.", response);
        }
        return response;
    }

	/**
     * Execute the method using an anonymous connection.
     * 
     * @param method
     *            An <code>XMPPMethod</code>.
     * @param assertResponse
     *            A <code>Boolean</code> flag indicating whether or not to
     *            asssert the response contains values.
     */
    private XMPPMethodResponse executeAnonymously(final XMPPMethod method,
            final Boolean assertResponse) {
        return execute(method, xmppAnonymousConnection, assertResponse);
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
     * Login to the xmpp server.
     * 
     * @param attempt
     *            The attempt count.
     * @param environment
     *            The xmpp server environment.
     * @param credentials
     *            The login credentials.
     * @throws SmackException
     */
    private void login(final Integer attempt, final Environment environment,
            final Credentials credentials) {
        logVariable("attempt", attempt);
        logVariable("environment", environment);
        logVariable("credentials", credentials);
	    assertIsReachable(environment);
        Assert.assertTrue(attempt < 4, "Cannot login after 3 attempts.");
        try {
			if (Boolean.TRUE == isLoggedIn())
				logout();
            switch (environment.getServerProtocol()) {
            case XMPP:
                xmppConnection = new XMPPConnection(
                        environment.getServerHost(), environment.getServerPort());
                xmppAnonymousConnection = new XMPPConnection(
                        environment.getServerHost(), environment.getServerPort());
                break;
            case XMPPS:
                xmppConnection =
                    new SSLXMPPConnection(environment.getServerHost(), environment.getServerPort());
                xmppAnonymousConnection =
                    new SSLXMPPConnection(environment.getServerHost(), environment.getServerPort());
                break;
            default:
                Assert.assertUnreachable("UNKNOWN ENVIRONMENT PROTOCOL");
			}
            // this smack library is so flaky; a delay has to be used between
            // creating the connection and logging in
            // JIRA http://www.jivesoftware.org/issues/browse/SMACK-141
            Thread.sleep(LOGIN_SLEEP_DURATION);

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
                        logger.logVariable("packet", packet);
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

            xmppAnonymousConnection.loginAnonymously();
            if (!credentials.isSetResource())
                setResource(credentials);

            xmppConnection.login(credentials.getUsername(),
                    credentials.getPassword(), credentials.getResource());
            logger.logInfo("{0}", readVersion());
            handleConnectionEstablished();
        } catch (final IllegalStateException isx) {
            logError(isx, "Login attempt {0} failed.", attempt);
            // this smack library is so flaky; a delay has to be used between
            // creating the connection and logging in
            // JIRA http://www.jivesoftware.org/issues/browse/SMACK-141
            if ("Not connected to server.".equals(isx.getMessage())) {
                login(attempt + 1, environment, credentials);
            }
		} catch (final Throwable t) {
			throw translateError(t);
		}
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
     * Read the server version.
     *
     * @return The server version <code>String</code>.
     */
    private String readVersion() {
        return xmppSystem.readVersion();
    }

    /**
     * Query the server for a new resource; and set the value within the
     * credentials.
     * 
     * @param credentials
     *            A user's <code>Credentials</code>.
     */
    private void setResource(final Credentials credentials) {
        final XMPPMethod createResource = new XMPPMethod("system:createresource");
        credentials.setResource(executeAnonymously(createResource, Boolean.TRUE).readResultString("resource"));
    }
}
