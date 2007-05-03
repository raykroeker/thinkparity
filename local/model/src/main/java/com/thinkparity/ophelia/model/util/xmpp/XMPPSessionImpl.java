/*
 * Created On: Feb 6, 2005
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.net.SocketFactory;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;
import com.thinkparity.codebase.log4j.Log4JContext;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.backup.Statistics;
import com.thinkparity.codebase.model.contact.Contact;
import com.thinkparity.codebase.model.contact.IncomingEMailInvitation;
import com.thinkparity.codebase.model.contact.IncomingUserInvitation;
import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;
import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.container.ContainerVersionArtifactVersionDelta.Delta;
import com.thinkparity.codebase.model.document.Document;
import com.thinkparity.codebase.model.document.DocumentVersion;
import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.ProfileEMail;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.session.Credentials;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.session.InvalidCredentialsException;
import com.thinkparity.codebase.model.session.TemporaryCredentials;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.Token;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.ophelia.model.util.ProcessAdapter;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.Step;
import com.thinkparity.ophelia.model.util.smack.SmackException;
import com.thinkparity.ophelia.model.util.smackx.packet.AbstractThinkParityIQ;
import com.thinkparity.ophelia.model.util.smackx.packet.AbstractThinkParityIQProvider;
import com.thinkparity.ophelia.model.util.xmpp.event.SessionListener;
import com.thinkparity.ophelia.model.util.xmpp.event.XMPPEventListener;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmlpull.v1.XmlPullParser;

/**
 * <b>Title:</b>thinkParity XMPP Session Implementation<br>
 * <b>Description:</b>The thinkParity application's xmpp interface
 * implementation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.34
 */
public final class XMPPSessionImpl implements XMPPCore, XMPPSession {

    /** An internal <code>ProcessMonitor</code> for the event queue. */
    private static final ProcessMonitor EVENT_QUEUE_MONITOR;

    /** An apache logger wrapper. */
	private static final Log4JWrapper logger = new Log4JWrapper();

    static {
        EVENT_QUEUE_MONITOR = new ProcessAdapter() {
            @Override
            public void beginProcess() {}
            @Override
            public void beginStep(final Step step, final Object data) {}
            @Override
            public void determineSteps(final Integer steps) {}
            @Override
            public void endProcess() {}
            @Override
            public void endStep(final Step step) {}
        };
        // TIMEOUT - XMPPSessionImpl#<cinit> - -1
        SmackConfiguration.setKeepAliveInterval(-1);
        logger.logInfo("Smack v{0}", SmackConfiguration.getVersion());
        // register a custom packet creator for remote events.
        ProviderManager.addIQProvider("query",
                "jabber:iq:parity:system:queueupdated",
                new AbstractThinkParityIQProvider() {
            public IQ parseIQ(final XmlPullParser parser) throws Exception {
                setParser2(parser);
                final HandleQueueUpdatedIQ query = new HandleQueueUpdatedIQ();
                boolean isComplete = false;
                while (false == isComplete) {
                    if (isStartTag("updatedOn")) {
                        readCalendar2();
                    } else {
                        isComplete = true;
                    }
                }
                return query;
            }
        });
	}

    /** The current login <code>Credentials</code>. */
    private transient Credentials credentials;

    /** An <code>XMPPSessionDebugger</code>. */
    private final Class<? extends XMPPSessionDebugger> debuggerClass;

    /** The current login <code>Environment</code>. */
    private Environment environment;

    /** A list of the session listeners. */
    private final List<SessionListener> listeners;

    /**
     * A <code>PacketListenerWrapper</code> used to monitor the xmpp
     * connection for remote events.
     */
    private PacketListenerWrapper queueListener;

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

    /** The xmpp event dispatcher. */
    private final XMPPEventDispatcher xmppEventDispatcher;

    /** The migrator xmpp interface. */
    private final XMPPMigrator xmppMigrator;

    /** An <code>XMPPNetworkUtil</code>. */
    private XMPPNetworkUtil xmppNetworkUtil;

    /** The thinkParity xmpp profile interface. */
    private final XMPPProfile xmppProfile;

    /** The thinkParity xmpp rules interface. */
    private final XMPPRules xmppRules;

    /** The stream xmpp interface. */
    private final XMPPStream xmppStream;

    /** The thinkParity xmpp system interface. */
    private final XMPPSystem xmppSystem;

    /** The thinkParity xmpp user interface. */
	private final XMPPUser xmppUser;

    /**
     * Create XMPPSessionImpl.
     *
     */
    public XMPPSessionImpl() {
        this(null);
    }

    /**
	 * Create XMPPSessionImpl.
	 * 
	 */
	public XMPPSessionImpl(
            final Class<? extends XMPPSessionDebugger> debuggerClass) {
        super();
        this.debuggerClass = debuggerClass;
		this.listeners = new ArrayList<SessionListener>();
        this.xmppArchive = new XMPPArchive(this);
        this.xmppBackup = new XMPPBackup(this);
		this.xmppArtifact = new XMPPArtifact(this);
        this.xmppContainer = new XMPPContainer(this);
		this.xmppContact = new XMPPContact(this);
        this.xmppMigrator = new XMPPMigrator(this);
        this.xmppProfile = new XMPPProfile(this);
        this.xmppRules = new XMPPRules(this);
        this.xmppStream = new XMPPStream(this);
        this.xmppSystem = new XMPPSystem(this);
		this.xmppUser = new XMPPUser(this);
        this.xmppEventDispatcher = new XMPPEventDispatcher(this);
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#acceptInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.IncomingEMailInvitation,
     *      java.util.Calendar)
     * 
     */
    public void acceptInvitation(final JabberId userId,
            final IncomingEMailInvitation invitation, final Calendar acceptedOn) {
        xmppContact.acceptInvitation(userId, invitation, acceptedOn);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#acceptInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.IncomingUserInvitation,
     *      java.util.Calendar)
     * 
     */
    public void acceptInvitation(final JabberId userId,
            final IncomingUserInvitation invitation, final Calendar acceptedOn) {
		xmppContact.acceptInvitation(userId, invitation, acceptedOn);
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#addListener(com.thinkparity.ophelia.model.util.xmpp.event.XMPPEventListener)
     *
     */
    public <T extends XMPPEvent> void addListener(final Class<T> eventClass,
            final XMPPEventListener<T> listener) {
        xmppEventDispatcher.addListener(eventClass, listener);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#addListener(com.thinkparity.ophelia.model.util.xmpp.events.XMPPSessionListener)
     * 
     */
	public void addListener(final SessionListener listener) {
        logger.logApiId();
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
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#addTeamMember(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.List, java.util.UUID,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
	public void addTeamMember(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final JabberId teamMemberId) {
	    xmppArtifact.addTeamMember(userId, team, uniqueId, teamMemberId);
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#archiveArtifact(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID)
     * 
     */
    public void archiveArtifact(final JabberId userId, final UUID uniqueId) {
        xmppBackup.archive(userId, uniqueId);
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
     * 
     */
    public void clearListeners() {
        // clear remote queue listener
        if (null != queueListener)
            xmppConnection.removePacketListener(queueListener.listener);
        queueListener = null;
        // clear event listeners
        synchronized (listeners) {
            listeners.clear();
        }
        xmppEventDispatcher.clearListeners();
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#confirmArtifactReceipt(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.List, java.util.UUID, java.lang.Long,
     *      com.thinkparity.codebase.jabber.JabberId, java.util.Calendar)
     * 
     */
    public void confirmArtifactReceipt(final JabberId userId,
            final UUID uniqueId, final Long versionId,
            final JabberId publishedBy, final Calendar publishedOn,
            final List<JabberId> publishedTo, final JabberId receivedBy,
            final Calendar receivedOn) {
        xmppArtifact.confirmReceipt(userId, uniqueId, versionId, publishedBy,
                publishedOn, publishedTo, receivedBy, receivedOn);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createArtifact(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID)
     */
	public void createArtifact(final JabberId userId, final UUID uniqueId,
            final Calendar createdOn) {
	    xmppArtifact.create(userId, uniqueId, createdOn);
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#openBackupDocumentVersion(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID, java.lang.Long)
     * 
     */
    public void createBackupStream(final JabberId userId,
            final String streamId, final UUID uniqueId, final Long versionId) {
        xmppBackup.createStream(userId, streamId, uniqueId, versionId);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createDraft(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.List, java.util.UUID)
     * 
     */
    public void createDraft(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final Calendar createdOn) {
        xmppArtifact.createDraft(userId, team, uniqueId, createdOn);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.OutgoingEMailInvitation)
     * 
     */
    public void createInvitation(final JabberId userId,
            final OutgoingEMailInvitation invitation) {
        xmppContact.createInvitation(userId, invitation);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.OutgoingUserInvitation)
     * 
     */
    public void createInvitation(final JabberId userId,
            final OutgoingUserInvitation invitation) {
        xmppContact.createInvitation(userId, invitation);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPCore#createMethod(java.lang.String)
     * 
     */
    public XMPPMethod createMethod(final String name) {
    	if ("backup:createstream".equals(name)
                || "container:publish".equals(name)
				|| "container:publish".equals(name)
				|| "system:readqueueevents".equals(name)
                || "contact:acceptincomingemailinvitation".equals(name)
                || "migrator:deploy".equals(name)
                || "migrator:createstream".equals(name)) {
    		return new XMPPMethod(name);
    	} else {
    		return new XMPPMethod(name, xmppNetworkUtil);
    	}
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createMigratorProduct(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.codebase.model.migrator.Product)
     *
     */
    public void createMigratorProduct(final JabberId userId, final Product product) {
        xmppMigrator.createProduct(userId, product);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createMigratorStream(com.thinkparity.codebase.jabber.JabberId,
     *      java.lang.String, com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release, java.util.List)
     * 
     */
    public void createMigratorStream(final JabberId userId,
            final String streamId, final Product product,
            final Release release, final List<Resource> resources) {
        xmppMigrator.createStream(userId, streamId, product, release, resources);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createProfile(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.profile.UsernameReservation,
     *      com.thinkparity.codebase.model.profile.EMailReservation,
     *      com.thinkparity.codebase.model.session.Credentials,
     *      com.thinkparity.codebase.model.profile.Profile,
     *      com.thinkparity.codebase.email.EMail, java.lang.String,
     *      java.lang.String)
     * 
     */
    public void createProfile(final JabberId userId,
            final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final String securityQuestion,
            final String securityAnswer) {
        xmppProfile.create(userId, usernameReservation, emailReservation,
                credentials, profile, email, securityQuestion, securityAnswer);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createProfileCredentials(java.lang.String, java.lang.String)
     *
     */
    public TemporaryCredentials createProfileCredentials(
            final String profileKey, final String securityAnswer,
            final Calendar createdOn) {
        return xmppProfile.createProfileCredentials(profileKey, securityAnswer,
                createdOn);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createProfileEMailReservation(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.codebase.email.EMail, java.util.Calendar)
     *
     */
    public EMailReservation createProfileEMailReservation(
            final JabberId userId, final EMail email,
            final Calendar reservedOn) {
        return xmppProfile.createEMailReservation(userId, email, reservedOn);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createProfileUsernameReservation(com.thinkparity.codebase.jabber.JabberId,
     *      java.lang.String, java.util.Calendar)
     * 
     */
    public UsernameReservation createProfileUsernameReservation(
            final JabberId userId, final String username,
            final Calendar reservedOn) {
        return xmppProfile.createUsernameReservation(userId, username, reservedOn);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createStream(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.stream.StreamSession)
     * 
     */
	public String createStream(final JabberId userId,
            final StreamSession session) {
        return xmppStream.create(userId, session);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createStreamSession(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
	public StreamSession createStreamSession(final JabberId userId) {
        return xmppStream.createSession(userId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#createToken(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public Token createToken(final JabberId userId) {
        return xmppProfile.createToken(userId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#declineInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.IncomingEMailInvitation,
     *      java.util.Calendar)
     * 
     */
    public void declineInvitation(final JabberId userId,
            final IncomingEMailInvitation invitation, final Calendar declinedOn) {
        xmppContact.declineInvitation(userId, invitation, declinedOn);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#declineInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.IncomingUserInvitation,
     *      java.util.Calendar)
     * 
     */
    public void declineInvitation(final JabberId userId,
            final IncomingUserInvitation invitation, final Calendar declinedOn) {
        xmppContact.declineInvitation(userId, invitation, declinedOn);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#delete(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public void delete(final JabberId userId, final JabberId contactId) {
        xmppContact.delete(userId, contactId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#deleteArtifact(com.thinkparity.codebase.jabber.JabberId, java.util.UUID)
     *
     */
    public void deleteArtifact(final JabberId userId, final UUID uniqueId) {
        xmppBackup.delete(userId, uniqueId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#deleteArtifact(java.util.UUID)
     * 
     */
    public void deleteArtifact(final UUID uniqueId) {
        xmppArtifact.delete(uniqueId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#deleteDraft(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.List, java.util.UUID)
     * 
     */
    public void deleteDraft(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final Calendar deletedOn) {
        xmppArtifact.deleteDraft(userId, team, uniqueId, deletedOn);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#deleteInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.OutgoingEMailInvitation,
     *      java.util.Calendar)
     * 
     */
    public void deleteInvitation(final JabberId userId,
            final OutgoingEMailInvitation invitation, final Calendar deletedOn) {
        xmppContact.deleteInvitation(userId, invitation, deletedOn);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#deleteInvitation(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.contact.OutgoingUserInvitation,
     *      java.util.Calendar)
     * 
     */
    public void deleteInvitation(final JabberId userId,
            final OutgoingUserInvitation invitation, final Calendar deletedOn) {
        xmppContact.deleteInvitation(userId, invitation, deletedOn);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#deleteStream(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.stream.StreamSession,
     *      java.lang.String)
     * 
     */
    public void deleteStream(final JabberId userId, final StreamSession session,
            final String streamId) {
        xmppStream.delete(userId, session, streamId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#deleteStreamSession(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.stream.StreamSession)
     * 
     */
    public void deleteStreamSession(final JabberId userId,
            final StreamSession session) {
        xmppStream.deleteSession(userId, session);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#deployMigrator(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.codebase.model.migrator.Product, com.thinkparity.codebase.model.migrator.Release, java.util.List, java.lang.String)
     *
     */
    public void deployMigrator(final JabberId userId, final Product product,
            final Release release, final List<Resource> resources,
            final String streamId) {
        xmppMigrator.deploy(userId, product, release, resources, streamId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPCore#execute(com.thinkparity.ophelia.model.io.xmpp.XMPPMethod)
     * 
     */
    public XMPPMethodResponse execute(final XMPPMethod method) {
        return execute(method, Boolean.FALSE);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPCore#execute(com.thinkparity.ophelia.model.io.xmpp.XMPPMethod,
     *      java.lang.Boolean)
     *
     */
    public XMPPMethodResponse execute(final XMPPMethod method,
            final Boolean assertResponse) {
        return execute(method, xmppConnection, assertResponse);
    }

    /**
     * Obtain the connection's jabber id.
     * 
     * @return A jabber id.
     */
	public JabberId getUserId() {
        logger.logApiId();
		return JabberIdBuilder.parse(xmppConnection.getUser());
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPCore#handleError(java.lang.Throwable)
     *
     */
    public void handleError(final Throwable t) {
        notifyListeners(new EventNotifier<SessionListener>() {
            public void notifyListener(final SessionListener listener) {
                listener.sessionError(t);
            }
        });
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPCore#handleEvent(com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent)
     *
	 */
    public <T extends XMPPEvent> void handleEvent(final T event) {
        xmppEventDispatcher.handleEvent(event);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#isEmailAvailable(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.email.EMail)
     * 
     */
    public Boolean isEmailAvailable(final JabberId userId, final EMail email) {
        return xmppProfile.isEmailAvailable(userId, email);
    }

    /**
	 * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#isLoggedIn()
	 */
	public Boolean isOnline() {
		logger.logApiId();
		return null != xmppConnection
				&& xmppConnection.isConnected()
				&& xmppConnection.isAuthenticated();
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#isPublishRestricted(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public Boolean isPublishRestricted(final JabberId userId,
            final JabberId publishFrom, final JabberId publishTo) {
        return xmppRules.isPublishRestricted(userId, publishFrom, publishTo);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#logError(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release,
     *      com.thinkparity.codebase.model.migrator.Error)
     * 
     */
    public void logError(final JabberId userId, final Product product,
            final Release release, final Error error) {
        xmppMigrator.logError(userId, product, release, error);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#login(com.thinkparity.codebase.model.session.Environment,
     *      com.thinkparity.codebase.model.session.Credentials)
     * 
     */
	public void login(final Environment environment,
            final Credentials credentials) throws InvalidCredentialsException {
        login(1, environment, credentials);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#logout()
     * 
     */
	public void logout() {
        disconnect();
	}

    /**
     * Translate an error into a parity unchecked error.
     * 
     * @param t
     *            An error.
     */
    public com.thinkparity.ophelia.model.io.xmpp.XMPPException panic(final Throwable t) {
        if (com.thinkparity.ophelia.model.io.xmpp.XMPPException.class.isAssignableFrom(t.getClass())) {
            return (com.thinkparity.ophelia.model.io.xmpp.XMPPException) t;
        } else {
            final String errorId = new ErrorHelper().getErrorId(t);
            logger.logError(t, "{0}", errorId);
            return new com.thinkparity.ophelia.model.io.xmpp.XMPPException(
                    errorId.toString(), t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#processEventQueue(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public void processEventQueue(final ProcessMonitor monitor,
            final JabberId userId) {
        logger.pushContext(new Log4JContext() {
            public String getContext() {
                return userId.getUsername();
            }
        });
        try {
            xmppSystem.processEventQueue(monitor, userId);
        } catch (final Throwable t) {
            logger.logFatal(t, "A fatal error occured whilst processing a remote event.  Re-establishing connection now.");
            logout();
            try {
                login(0, environment, credentials);
            } catch (final InvalidCredentialsException icx) {
                logger.logFatal("Could not re-establish connection.");
                logout();
            }
        } finally {
            logger.popContext();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#publish(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.Map, java.util.List,
     *      com.thinkparity.codebase.jabber.JabberId, java.util.Calendar,
     *      java.util.List, java.util.List)
     * 
     */
    public void publish(final JabberId userId, final ContainerVersion version,
            final Map<DocumentVersion, String> documentVersions,
            final List<TeamMember> teamMembers,
            final JabberId publishedBy, final Calendar publishedOn,
            final List<EMail> publishToEMails, final List<User> publishToUsers) {
        xmppContainer.publish(userId, version, documentVersions, teamMembers,
                publishedBy, publishedOn, publishToEMails, publishToUsers);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#publishVersion(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.model.container.ContainerVersion,
     *      java.util.Map, java.util.List, java.util.List,
     *      com.thinkparity.codebase.jabber.JabberId, java.util.Calendar,
     *      java.util.List, java.util.List)
     * 
     */
    public void publishVersion(JabberId userId, ContainerVersion version,
            Map<DocumentVersion, String> documentVersions,
            List<TeamMember> teamMembers, List<ArtifactReceipt> receivedBy,
            JabberId publishedBy, Calendar publishedOn,
            List<EMail> publishToEMails, List<User> publishToUsers) {
        xmppContainer.publishVersion(userId, version, documentVersions,
                teamMembers, receivedBy, publishedBy, publishedOn,
                publishToEMails, publishToUsers);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readArchiveContainer(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID)
     * 
     */
    public Container readArchiveContainer(final JabberId userId,
            final UUID uniqueId) {
        return xmppArchive.readContainer(userId, uniqueId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readArchiveContainers(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public List<Container> readArchiveContainers(final JabberId userId) {
        return xmppArchive.readContainers(userId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readArchiveContainerVersions(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID)
     * 
     */
    public List<ContainerVersion> readArchiveContainerVersions(
            final JabberId userId, final UUID uniqueId) {
        return xmppArchive.readContainerVersions(userId, uniqueId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readArchiveDocuments(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID, java.lang.Long)
     * 
     */
    public List<Document> readArchiveDocuments(final JabberId userId,
            final UUID uniqueId, final Long versionId) {
        return xmppArchive.readDocuments(userId, uniqueId, versionId);
    }

    public DocumentVersion readArchiveDocumentVersion(final JabberId userId,
            final UUID uniqueId, final UUID documentUniqueId,
            final Long documentVersionId) {
        return xmppArchive.readDocumentVersion(userId, uniqueId,
                documentUniqueId, documentVersionId);
    }

    public Map<DocumentVersion, Delta> readArchiveDocumentVersionDeltas(
            final JabberId userId, final UUID uniqueId, final Long compareVersionId) {
        return xmppArchive.readDocumentVersionDeltas(userId, uniqueId, compareVersionId);
    }
    public Map<DocumentVersion, Delta> readArchiveDocumentVersionDeltas(
            final JabberId userId, final UUID uniqueId,
            final Long compareVersionId, final Long compareToVersionId) {
        return xmppArchive.readDocumentVersionDeltas(userId, uniqueId,
                compareVersionId, compareToVersionId);
    }
    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readArchiveDocumentVersions(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID, java.lang.Long, java.util.UUID)
     * 
     */
    public List<DocumentVersion> readArchiveDocumentVersions(
            final JabberId userId, final UUID uniqueId, final Long versionId) {
        return xmppArchive.readDocumentVersions(userId, uniqueId, versionId);
    }
    public List<TeamMember> readArchiveTeam(final JabberId userId,
            final UUID uniqueId) {
        return xmppArchive.readTeam(userId, uniqueId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readArchiveTeamIds(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID)
     * 
     */
    public List<JabberId> readArchiveTeamIds(final JabberId userId,
            final UUID uniqueId) {
        return xmppArchive.readTeamIds(userId, uniqueId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readBackupContainer(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID)
     * 
     */
    public Container readBackupContainer(final JabberId userId, final UUID uniqueId) {
        return xmppBackup.readContainer(userId, uniqueId);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readBackupContainers(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public List<Container> readBackupContainers(final JabberId userId) {
        return xmppBackup.readContainers(userId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readBackupContainerVersions(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID)
     * 
     */
    public List<ContainerVersion> readBackupContainerVersions(final JabberId userId, final UUID uniqueId) {
        return xmppBackup.readContainerVersions(userId, uniqueId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readBackupDocuments(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID, java.lang.Long)
     * 
     */
    public List<Document> readBackupDocuments(final JabberId userId, final UUID uniqueId, final Long versionId) {
        return xmppBackup.readDocuments(userId, uniqueId, versionId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readBackupDocumentVersions(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID, java.lang.Long, java.util.UUID)
     * 
     */
    public List<DocumentVersion> readBackupDocumentVersions(
            final JabberId userId, final UUID uniqueId, final Long versionId) {
        return xmppBackup.readDocumentVersions(userId, uniqueId, versionId);
    }

    public List<ArtifactReceipt> readBackupPublishedTo(
            final JabberId userId, final UUID uniqueId, final Long versionId) {
        return xmppBackup.readPublishedTo(userId, uniqueId, versionId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readBackupTeamIds(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID)
     * 
     */
    public List<JabberId> readBackupTeamIds(final JabberId userId, final UUID uniqueId) {
        return xmppBackup.readTeamIds(userId, uniqueId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readContact(com.thinkparity.codebase.jabber.JabberId,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public Contact readContact(final JabberId userId, final JabberId contactId) {
        return xmppContact.read(userId, contactId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readContactIds(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public List<JabberId> readContactIds(final JabberId userId) {
        return xmppContact.readIds(userId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readContacts(com.thinkparity.codebase.jabber.JabberId)
     * 
     */
	public List<Contact> readContacts(final JabberId userId) {
		return xmppContact.read(userId);
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readDateTime(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public Calendar readDateTime(final JabberId userId) {
        return xmppSystem.readDateTime(userId);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#getQueueSize()
     *
     */
    public Integer readEventQueueSize(final JabberId userId) {
        return xmppSystem.readEventQueueSize(userId);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readIncomingInvitations(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public List<IncomingEMailInvitation> readIncomingEMailInvitations(
            final JabberId userId) {
        return xmppContact.readIncomingEMailInvitations(userId);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readIncomingUserInvitations(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public List<IncomingUserInvitation> readIncomingUserInvitations(
            final JabberId userId) {
        return xmppContact.readIncomingUserInvitations(userId);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readKeyHolder(java.util.UUID)
     * 
     */
	public JabberId readKeyHolder(final JabberId userId, final UUID uniqueId) {
		return xmppArtifact.readKeyHolder(userId, uniqueId);
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readMigratorLatestRelease(com.thinkparity.codebase.jabber.JabberId,
     *      java.lang.String, com.thinkparity.codebase.OS)
     * 
     */
    public Release readMigratorLatestRelease(final JabberId userId,
            final String productName, final OS os) {
        return xmppMigrator.readLatestRelease(userId, productName, os);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readMigratorProduct(java.lang.String)
     *
     */
    public Product readMigratorProduct(final JabberId userId, final String name) {
        return xmppMigrator.readProduct(userId, name);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readMigratorProductFeatures(com.thinkparity.codebase.jabber.JabberId, java.lang.String)
     *
     */
    public List<Feature> readMigratorProductFeatures(final JabberId userId,
            final String name) {
        return xmppMigrator.readProductFeatures(userId, name);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readMigratorRelease(com.thinkparity.codebase.jabber.JabberId,
     *      java.lang.String, java.lang.String, com.thinkparity.codebase.OS)
     * 
     */
    public Release readMigratorRelease(final JabberId userId,
            final String productName, final String name, final OS os) {
        return xmppMigrator.readRelease(userId, productName, name, os);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readMigratorResources(com.thinkparity.codebase.jabber.JabberId,
     *      java.lang.String, java.lang.String, com.thinkparity.codebase.OS)
     * 
     */
    public List<Resource> readMigratorResources(final JabberId userId,
            final String productName, final String releaseName, final OS os) {
        return xmppMigrator.readResources(userId, productName, releaseName, os);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readOutgoingEMailInvitations(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public List<OutgoingEMailInvitation> readOutgoingEMailInvitations(
            final JabberId userId) {
        return xmppContact.readOutgoingEMailInvitations(userId);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readOutgoingUserInvitations(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public List<OutgoingUserInvitation> readOutgoingUserInvitations(
            final JabberId userId) {
        return xmppContact.readOutgoingUserInvitations(userId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readProfile()
     * 
     */
    public Profile readProfile() {
        assertLoggedIn("[LMODEL] [XMPP] [READ PROFILE] [USER NOT ONLINE]");
        return xmppProfile.read(getUserId());
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readProfileEMails()
     * 
     */
    public List<ProfileEMail> readProfileEMails() {
        return xmppProfile.readEMails(getUserId());
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readProfileFeatures(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public List<Feature> readProfileFeatures(final JabberId userId) {
        return xmppProfile.readFeatures(userId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readProfileSecurityQuestion(java.lang.String)
     * 
     */
    public String readProfileSecurityQuestion(final String profileKey) {
        return xmppProfile.readSecurityQuestion(profileKey);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readStatistics(com.thinkparity.codebase.jabber.JabberId)
     *
     */
    public Statistics readStatistics(final JabberId userId) {
        return xmppBackup.readStatistics(userId);
    }

	public StreamSession readStreamSession(final JabberId userId,
            final String sessionId) {
        return xmppStream.readSession(userId, sessionId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readToken(com.thinkparity.codebase.jabber.JabberId)
     */
    public Token readToken(final JabberId userId) {
        return xmppProfile.readToken(userId);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#readUsers(java.util.Set)
     * 
     */
	public User readUser(final JabberId userId) {
		return xmppUser.read(userId);
	}

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#registerQueueListener()
     * 
     */
    public void registerQueueListener() {
        Assert.assertIsNull("Queue listener already registered.", queueListener);
        queueListener = new PacketListenerWrapper(new PacketListener() {
            public void processPacket(final Packet packet) {
                processEventQueue();
            }
        }, new PacketTypeFilter(HandleQueueUpdatedIQ.class));
        // register a packet listener to monitor for remote event notification 
        xmppConnection.addPacketListener(queueListener.listener,
                queueListener.filter);
    }

	/**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#removeListener(com.thinkparity.ophelia.model.util.xmpp.event.SessionListener)
     * 
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
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#removeTeamMember(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.List, java.util.UUID,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public void removeTeamMember(final JabberId userId,
            final List<JabberId> team, final UUID uniqueId,
            final JabberId teamMemberId) {
        xmppArtifact.removeTeamMember(userId, team, uniqueId, teamMemberId);
	}

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#restoreArtifact(com.thinkparity.codebase.jabber.JabberId, java.util.UUID)
     *
     */
    public void restoreArtifact(final JabberId userId, final UUID uniqueId) {
        xmppBackup.restore(userId, uniqueId);
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
    public void updateProfilePassword(final JabberId userId,
            final Credentials credentials, final String newPassword) {
        xmppProfile.updatePassword(userId, credentials, newPassword);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#updateProfilePassword(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.codebase.model.session.TemporaryCredentials, java.lang.String)
     *
     */
    public void updateProfilePassword(final JabberId userId,
            final TemporaryCredentials credentials, final String newPassword) {
        xmppProfile.updatePassword(userId, credentials, newPassword);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.XMPPSession#verifyProfileEmail(com.thinkparity.codebase.jabber.JabberId, com.thinkparity.codebase.email.EMail, java.lang.String)
     */
    public void verifyProfileEmail(final JabberId userId, final EMail email, final String key) {
        xmppProfile.verifyEmail(userId, email, key);
    }

    /**
     * Assert that the xmpp service is online.
     * 
     * @param environment
     *            A thinkParity <code>Environment</code>.
     */
    protected void assertXMPPIsReachable(final Environment environment) {
        Assert.assertTrue(environment.isXMPPReachable(),
                "XMPP environment {0} is not reachable.",
                environment.getXMPPService());
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
	    disconnect();
	}

    /**
     * Disconnect the underlying connections; and clear all of the listeners.
     *
     */
    private void disconnect() {
        clearListeners();
        credentials = null;

        environment = null;

        try {
            xmppConnection.close();
        } catch (final Throwable t) {
            logger.logWarning(t, "Error closing XMPP connection.");
        }
        xmppConnection = null;

        xmppNetworkUtil = null;
    }

    /**
     * Event handler for the connectionClosedOnerror event generated by the
     * smack connection listener impl. This will iterate through the
     * xmppSessionListeners list and fire the sessionTerminated event. There is
     * nothing the user nor the client can do about the error; so it is not
     * bubbled up.
     * 
     * @param x
     *            An <code>Exception</code>
     */
	private void handleConnectionClosed(final Exception x) {
	    logger.logError(x, "A connection error has occured.");
	    notifyListeners(new EventNotifier<SessionListener>() {
            public void notifyListener(final SessionListener listener) {
                listener.sessionTerminated();
            }
        });
        disconnect();
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
            final Credentials credentials) throws InvalidCredentialsException {
        logVariable("attempt", attempt);
        logVariable("environment", environment);
        logVariable("credentials", credentials);
	    assertXMPPIsReachable(environment);
        Assert.assertTrue(attempt < 4, "Cannot login after 3 failed attempts.");
        try {
			if (Boolean.TRUE == isOnline())
				logout();
			// configure socket factory and xmpp
            final SocketFactory socketFactory;
            final ConnectionConfiguration configuration = new ConnectionConfiguration(
                    environment.getXMPPHost(), environment.getXMPPPort(),
                    environment.getXMPPService());
            configuration.setCompressionEnabled(false);
            configuration.setDebuggerEnabled(null != debuggerClass);
            if (configuration.isDebuggerEnabled()) {
                System.setProperty("smack.debuggerClass", debuggerClass.getName());
            }
            configuration.setTLSEnabled(environment.isXMPPTLSEnabled());
            if (configuration.isTLSEnabled()) {
                configuration.setExpiredCertificatesCheckEnabled(configuration.isTLSEnabled());
                configuration.setNotMatchingDomainCheckEnabled(configuration.isTLSEnabled());
                configuration.setSASLAuthenticationEnabled(configuration.isTLSEnabled());
                configuration.setSelfSignedCertificateEnabled(configuration.isTLSEnabled());
                configuration.setTruststorePassword("password");
                configuration.setTruststorePath("security/xmpp_client_keystore");
                configuration.setTruststoreType("JKS");
                configuration.setVerifyChainEnabled(configuration.isTLSEnabled());
                configuration.setVerifyRootCAEnabled(configuration.isTLSEnabled());
                socketFactory = com.thinkparity.codebase.net.SocketFactory.getSecureInstance(
                        "security/xmpp_client_keystore", "password".toCharArray(),
                        "security/xmpp_client_keystore", "password".toCharArray()); 
            } else {
                socketFactory = com.thinkparity.codebase.net.SocketFactory.getInstance();
            }
            // establish connection
            xmppConnection = new XMPPConnection(configuration, socketFactory);
            // register a connection listener
            xmppConnection.addConnectionListener(new ConnectionListener() {
			    public void connectionClosed() {
                    logger.logApiId();
                    handleConnectionClosed();
			    }
			    public void connectionClosedOnError(final Exception x) {
                    logger.logApiId();
			        handleConnectionClosed(x);
			    }
			});
            // login anonymously
            if (!credentials.isSetResource()) {
                final XMPPConnection xmppAnonymousConnection = new XMPPConnection(
                		configuration, socketFactory);
            	xmppAnonymousConnection.loginAnonymously();
                final XMPPMethod createResource = new XMPPMethod("system:createresource");
                credentials.setResource(execute(createResource,
                		xmppAnonymousConnection, Boolean.TRUE).readResultString(
                				"resource"));
            }
            // login
            xmppConnection.login(credentials.getUsername(),
                    credentials.getPassword(), credentials.getResource());
            // initialize a baseline for xmpp method invocation
            xmppNetworkUtil = new XMPPNetworkUtil(this);
            xmppNetworkUtil.initializeBaseline();
            logger.logInfo("{0}", readVersion());
            // save credentials/environment
            this.credentials = credentials;
            this.environment = environment;
            // fire event
            handleConnectionEstablished();
        } catch (final IllegalStateException isx) {
            logError(isx, "Login attempt {0} failed.", attempt);
            // this smack library is so flaky; a delay has to be used between
            // creating the connection and logging in
            // JIRA http://www.jivesoftware.org/issues/browse/SMACK-141
            if ("Not connected to server.".equals(isx.getMessage())) {
                login(attempt + 1, environment, credentials);
            }
        } catch (final XMPPException xmppx) {
            if ("SASL authentication failed".equals(xmppx.getMessage())) {
                throw new InvalidCredentialsException();
            } else if ("No response from the server.".equals(xmppx.getMessage())) {
                logError(xmppx, "Login attempt {0} failed.", attempt);
                login(attempt + 1, environment, credentials);
            } else {
                throw panic(xmppx);
            }
		} catch (final Throwable t) {
			throw panic(t);
		}
	}

    /**
     * Notify all session listeners.
     * 
     * @param notifier
     *            A session listener notifier.
     */
    private void notifyListeners(final EventNotifier<SessionListener> notifier) {
        logger.logVariable("listeners.size()", listeners.size());
        synchronized (listeners) {
            for (final SessionListener listener : listeners) {
                notifier.notifyListener(listener);
            }
        }
    }

	/**
     * Process the event queue for the logged in user.
     *
     */
    private void processEventQueue() {
        EVENT_QUEUE_MONITOR.beginProcess();
        processEventQueue(EVENT_QUEUE_MONITOR, getUserId());
        EVENT_QUEUE_MONITOR.endProcess();
    }

	/**
     * Read the server version.
     *
     * @return The server version <code>String</code>.
     */
    private String readVersion() {
        return xmppSystem.readVersion();
    }

    /** A queue update event. */
    private static final class HandleQueueUpdatedIQ extends
            AbstractThinkParityIQ {}

    /**
     * <b>Title:</b>Session Implementation Packet Listener Wrapper<br>
     * <b>Description:</b>Combines the packet listener with a packet filter.<br>
     */
    private static class PacketListenerWrapper {
        private final PacketFilter filter;
        private final PacketListener listener;
        private PacketListenerWrapper(final PacketListener listener, final PacketFilter filter) {
            super();
            this.listener = listener;
            this.filter = filter;
        }
    } 
}
