/*
 * Created On: Nov 28, 2005
 */
package com.thinkparity.desdemona.model;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.Constants.Xml;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.Constants.JivePropertyNames;
import com.thinkparity.desdemona.model.archive.ArchiveModel;
import com.thinkparity.desdemona.model.archive.InternalArchiveModel;
import com.thinkparity.desdemona.model.artifact.ArtifactModel;
import com.thinkparity.desdemona.model.backup.BackupModel;
import com.thinkparity.desdemona.model.backup.InternalBackupModel;
import com.thinkparity.desdemona.model.contact.ContactModel;
import com.thinkparity.desdemona.model.io.sql.ConfigurationSql;
import com.thinkparity.desdemona.model.queue.QueueModel;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.stream.StreamModel;
import com.thinkparity.desdemona.model.user.UserModel;
import com.thinkparity.desdemona.util.xmpp.IQWriter;
import com.thinkparity.desdemona.wildfire.JIDBuilder;

import org.jivesoftware.util.JiveProperties;
import org.jivesoftware.wildfire.ClientSession;
import org.jivesoftware.wildfire.SessionManager;
import org.jivesoftware.wildfire.SessionResultFilter;
import org.jivesoftware.wildfire.XMPPServer;
import org.jivesoftware.wildfire.auth.UnauthorizedException;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModelImpl
    extends com.thinkparity.codebase.model.AbstractModelImpl {

    /** No session log statement. */
    private static final JabberId NO_SESSION;

    /** An apache xmpp iq logger wrapper. */
    private static final Log4JWrapper XMPP_IQ_LOGGER;

    static {
        NO_SESSION = User.THINK_PARITY.getId();
        XMPP_IQ_LOGGER = new Log4JWrapper("XMPP_IQ");
    }

    /** An apache logger. */
	protected final Log4JWrapper logger;

    /**
	 * Handle to the user's session.
	 */
	protected final Session session;

    /** A thinkParity configuration sql interface. */
    private final ConfigurationSql configurationSql;

    /**
	 * Create an AbstractModelImpl.
	 */
	protected AbstractModelImpl(final Session session) {
		super();
        this.configurationSql = new ConfigurationSql();
        this.logger = new Log4JWrapper(getClass());
		this.session = session;
	}

    /**
     * Add a to email recipient to a mime message.
     * 
     * @param mimeMessage
     *            A <code>MimeMessage</code>.
     * @param email
     *            An <code>EMail</code>.
     * @throws MessagingException
     */
    protected void addRecipient(final MimeMessage mimeMessage, final EMail email)
            throws MessagingException {
        mimeMessage.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(email.toString(), Boolean.TRUE));
    }

    /**
     * Assert that the actual and expected jabber id's are equal.
     * 
     * @param assertion
     *            The assertion.
     * @param expected
     *            The expected jabber id.
     * @param actual
     *            The actual jabber id.
     */
    protected void assertEquals(final Object assertion,
            final JabberId expected, final JabberId actual) {
        Assert.assertTrue(assertion, expected.equals(actual));
    }

	/**
     * Assert that the actual and expected jive id's are equal.
     * 
     * @param message
     *            The message.
     * @param actualJID
     *            The actual jive id.
     * @param expectedJID
     *            The expected jive id.
     */
    protected void assertEquals(final String message, final JID actualJID,
            final JID expectedJID) {
        Assert.assertTrue(message, actualJID.equals(expectedJID));
    }

	/**
     * Assert that the user id matched that of the authenticated user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @see #isAuthenticatedUser(JabberId)
     */
    protected void assertIsAuthenticatedUser(final JabberId userId) {
        Assert.assertTrue(isAuthenticatedUser(userId),
                "User {0} does not match authenticated user {1}.",
                userId, session.getJabberId());
    }

    /**
	 * Assert that the session user is the artifact key holder.
	 * 
	 * @param artifact
	 *            The artifact.
	 * @throws NotTrueAssertion
	 *             If the user is not the key holder.
	 */
	protected void assertIsKeyHolder(final UUID uniqueId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        final JabberId keyHolder = readKeyHolder(uniqueId);
        logVariable("keyHolder", keyHolder);
		Assert.assertTrue(keyHolder.equals(session.getJabberId()),
                "Session user {0} is not the key holder {1}.",
                session.getJabberId(), keyHolder);
	}

    /**
     * Assert that the thinkParity system is the current key holder.
     * 
     * @param assertion
     *            The assertion.
     * @param uniqueId
     *            The artifact unique id.
     */
    protected void assertSystemIsKeyHolder(final Object assertion,
            final UUID uniqueId) throws ParityServerModelException {
        Assert.assertTrue(assertion, isSystemKeyHolder(uniqueId));
    }

    /**
	 * Create a jabber id for the username.
	 * 
	 * @param username
	 *            The user node (username).
	 * @return The jabber id.
	 */
	protected JID buildJID(final String username) {
		return JIDBuilder.build(username);
	}

    /**
     * Create an iq writer for a query.
     * 
     * @param queryName
     *            A query name.
     * @return An iq writer.
     */
    protected IQWriter createIQWriter(final String queryName) {
        final IQ iq = new IQ();
        iq.setType(IQ.Type.set);
        iq.setChildElement(Xml.NAME, new StringBuffer(Xml.NAMESPACE)
                .append(":").append(queryName).toString());
        return new IQWriter(iq);
    }

    /**
     * Obtain the date and time.
     * 
     * @return A <code>Calendar</code>.
     */
    protected Calendar currentDateTime() {
        // TIME This is a global date
        return DateUtil.getInstance();
    }

    /**
     * Obtain the current time in milliseconds.
     * 
     * @return The current time <code>Long</code>.
     */
    protected Long currentTimeMillis() {
        // TIME This is a global timestamp
        return System.currentTimeMillis();
    }

	/**
     * Obtain a thinkParity archive interface.
     * 
     * @return A thinkParity archive interface.
     */
    protected InternalArchiveModel getArchiveModel() {
        return ArchiveModel.getInternalModel(getContext(), session);
    }

    /**
     * Obtain the parity artifact interface.
     * 
     * @return The parity artifact interface.
     */
	protected ArtifactModel getArtifactModel() {
		final ArtifactModel artifactModel = ArtifactModel.getModel(session);
		return artifactModel;
	}

    /**
     * Obtain a thinkParity backup interface.
     * 
     * @return A thinkParity backup interface.
     */
    protected InternalBackupModel getBackupModel() {
        return BackupModel.getInternalModel(getContext(), session);
    }

    /**
     * Obtain the client session for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A client session.
     */
    protected final List<ClientSession> getClientSessions(final JabberId userId) {
        final Collection<ClientSession> sessions =
            getSessionManager().getSessions(createClientSessionFilter(userId));
        final List<ClientSession> sessionList = new ArrayList<ClientSession>(sessions.size());
        sessionList.addAll(sessions);
        return sessionList;
    }

    /**
     * Obtain the parity contact interface.
     * 
     * @return The parity contact interface.
     */
	protected ContactModel getContactModel() {
		return ContactModel.getModel(session);
	}

	/**
     * Obtain an error id.
     * 
     * @return An error id.
     */
    protected final Object getErrorId(final Throwable t) {
        return MessageFormat.format("[{0}] [{1}] [{2}] - [{3}]",
                    null == session ? NO_SESSION : session.getJabberId(),
                    StackUtil.getFrameClassName(2),
                    StackUtil.getFrameMethodName(2),
                    t.getMessage());
    }

    protected StreamModel getStreamModel() {
        return StreamModel.getModel(session);
    }

	/**
     * Obtain the parity user interface.
     * 
     * @return The parity user interface.
     */
	protected UserModel getUserModel() {
		final UserModel uModel = UserModel.getModel(session);
		return uModel;
	}

	/**
     * Inject the fields of a user into a user type object.
     * 
     * @param <T>
     *            A type of <code>User</code>.
     * @param type
     *            A <code>T</code>.
     * @param user
     *            A <code>User</code>.
     * @return A <code>T</code>.
     */
    protected <T extends User> T inject(final T type, final User user) {
        type.setId(user.getId());
        type.setName(user.getName());
        type.setOrganization(user.getOrganization());
        type.setTitle(user.getTitle());
        return logVariable("type", type);
    }

	/**
     * Determine if the user id is the authenticated user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True if the user id matches the currently authenticated user.
     */
    protected Boolean isAuthenticatedUser(final JabberId userId) {
        return session.getJabberId().equals(userId);
    }

    /**
     * Determine whether or not the user represented by the jabber id is
     * currently online.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True; if the user is online; false otherwise.
     */
	protected Boolean isOnline(final JabberId jabberId) {
		if(0 < getSessionManager().getSessionCount(jabberId.getUsername())) {
			return Boolean.TRUE;
		}
		else { return Boolean.FALSE; }
	}

	protected Boolean isSessionUserKeyHolder(final UUID uniqueId) {
		return readKeyHolder(uniqueId).equals(session.getJabberId());
	}

    /** Log an api id. */
    protected final void logApiId() {
        logger.logApiId();
    }

	/**
     * Log an api id with a message.
     * 
     * @param message
     *            A message.
     */
    protected final void logApiId(final Object message) {
        logger.logApiId();
    }

    /**
     * Log an info message.
     * 
     * @param infoPattern
     *            An info pattern.
     * @param infoArguments
     *            Info arguments.
     */
    protected final void logInfo(final String infoPattern,
            final Object... infoArguments) {
        logger.logInfo(infoPattern, infoArguments);
    }

    /** Log a trace id. */
    protected final void logTraceId() {
        logger.logApiId();
    }

	/**
     * Log a named variable. Note that the logging renderer will be used only
     * for the value.
     * 
     * @param name
     *            A variable name.
     * @param value
     *            A variable.
     * @return The value.
     */
    protected final <T> T logVariable(final String name, final T value) {
        return logger.logVariable(name, value);
    }

	/**
     * Log a warning.
     * 
     * @param warning
     *            A warning.
     */
    protected final void logWarning(final String warningPattern,
            final Object... warningArguments) {
        logger.logWarning(warningPattern, warningArguments);
    }

    /**
     * Send a notification to an artifact team. Note that the user this session
     * belongs to will *NOT* be notified. This eases data integrity concerns on
     * the network; for example if a team member is added and the person adding
     * the team member is notified; they need to check for existing pending team
     * member row in the db before inserting it. A bit of a hack.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @param notification
     *            The notification internet query.
     * @throws ParityServerModelException
     * @throws UnauthorizedException
     */
    protected void notifyTeam(final UUID uniqueId, final IQ notification)
            throws UnauthorizedException {
        final List<JabberId> team = getArtifactModel().readTeamIds(uniqueId);
        send(team, notification);
    }

    /**
     * Process an xmpp internet query for a jive client session. The to portion
     * of the query will be set according to the session.
     * 
     * @param session
     *            A jive <code>ClientSession</code>.
     * @param query
     *            An xmpp <code>IQ</code>.
     * @see IQ#setTo(JID)
     * @see ClientSession#getAddress()
     * @see ClientSession#process(org.xmpp.packet.Packet)
     */
    protected void process(final ClientSession session, final IQ query) {
        query.setTo(session.getAddress());
        XMPP_IQ_LOGGER.logVariable("query", query);
        session.process(query);
    }

    /**
     * Read thinkParity configuration.
     * 
     * @return A configuration <code>Properties</code>.
     */
    protected Properties readConfiguration() {
        final Properties properties = new Properties();
        final List<String> keys = configurationSql.readKeys();
        for (final String key : keys) {
            properties.setProperty(key, configurationSql.read(key));
        }
        return properties;
    }

    /**
     * Read the jive property for the environment.
     * 
     * @return An environment.
     */
    protected Environment readEnvironment() {
        final String thinkParityEnvironment =
            (String) JiveProperties.getInstance().get(JivePropertyNames.THINKPARITY_ENVIRONMENT);
        return Environment.valueOf(thinkParityEnvironment);
    }

    /**
     * Read the key holder.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @return The key holder jabber id.
     * @throws ParityServerModelException
     */
    protected JabberId readKeyHolder(final UUID uniqueId) {
        return getArtifactModel().readKeyHolder(session.getJabberId(), uniqueId);
    }

    /**
     * Send a query to a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param query
     *            An xmpp internet query <code>IQ</code>.
     */
    protected void send(final JabberId toUserId, final IQ query) {
        logApiId();
        logVariable("toUserId", toUserId);
        logVariable("query", query);
        final List<JabberId> toUserIds = new ArrayList<JabberId>(1);
        send(toUserIds, query);
    }

    /**
     * Send a query to a list of users.
     * 
     * @param toUserIds
     *            A <code>List&lt;JabberId&gt;</code>.
     * @param query
     *            An xmpp internet query <code>IQ</code>.
     */
    protected void send(final List<JabberId> toUserIds, final IQ query) {
        logApiId();
        logVariable("toUserIds", toUserIds);
        logVariable("query", query);
        for (final JabberId toUserId : toUserIds) {
            query.setTo(getJID(toUserId));
            enqueue(toUserId, query);
            if (isOnline(toUserId)) {
                sendQueueUpdated(toUserId);
            }
            backup(toUserId, query);
        }
        // do not backup the same user twice
        if (!toUserIds.contains(session.getJabberId())) {
            backup(session.getJabberId(), query);
        }
    }

	/**
     * Set the to field in the query.
     * 
     * @param query
     *            The xmpp query <code>IQ</code>.
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    protected void setTo(final IQ query, final JabberId userId) {
        query.setTo(getJID(userId));
    }

	/**
     * Translate an error into a parity unchecked error.
     * 
     * @param t
     *            An error.
     */
    protected ParityModelException translateError(final Throwable t) {
        if(ParityModelException.class.isAssignableFrom(t.getClass())) {
            return (ParityModelException) t;
        }
        else {
            final String errorId = new ErrorHelper().getErrorId(t);
            logger.logError(t, errorId);
            return ParityErrorTranslator.translateUnchecked(session, errorId, t);
        }
    }

    /**
     * Archive a query for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param query
     *            An xmpp internet query.
     */
    private void backup(final JabberId userId, final IQ query) {
        final JabberId archiveId = getUserModel().readArchiveId(userId);
        if (null != archiveId) {
            query.setTo(getJID(archiveId));
            enqueue(archiveId, query);
            if (isOnline(archiveId)) {
                sendQueueUpdated(archiveId);
            } else {
                logWarning(MessageFormat.format("Archive {0} not online.", archiveId));
            }
        }
    }

    /**
     * Create a client session filter for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>SessionResultFilter</code>.
     */
    private SessionResultFilter createClientSessionFilter(final JabberId userId) {
        final SessionResultFilter filter = new SessionResultFilter();
        filter.setUsername(userId.getUsername());
        return filter;
    }

    /**
	 * Save the iq in the parity offline queue.
	 * 
	 * @param jid
	 *            The jid.
	 * @param iq
	 *            The iq packet.
	 */
	private void enqueue(final JabberId userId, final IQ iq) {
		final QueueModel queueModel = QueueModel.getModel(session);
		queueModel.enqueue(getJID(userId), iq);
	}

    /**
     * Create a JID from a jabber id.
     * 
     * @param jabberId
     *            A <code>JabberId</code>.
     * @return A <code>JID</code>.
     */
    private final JID getJID(final JabberId jabberId) {
        return JIDBuilder.buildQualified(jabberId.getQualifiedJabberId());
    }

    /**
	 * Obtain a handle to the xmpp server's session manager.
	 * @return The xmpp servers's session manager.
	 */
	private SessionManager getSessionManager() {
		return getXMPPServer().getSessionManager();
	}


    /**
	 * Obtain a handle to the underlying xmpp server.
	 * 
	 * @return The xmpp server.
	 */
	private XMPPServer getXMPPServer() { return XMPPServer.getInstance(); }

    /**
     * Determine whether or not the system account is the key holder.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @return True if the system account is the key holder; false otherwise.
     * @throws ParityServerModelException
     */
    private Boolean isSystemKeyHolder(final UUID uniqueId)
            throws ParityServerModelException {
        return readKeyHolder(uniqueId).equals(User.THINK_PARITY.getId());
    }

    /**
     * Send a queue updated event to a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    private void sendQueueUpdated(final JabberId userId) {
        final IQWriter queryWriter = createIQWriter("system:queueupdated");
        queryWriter.writeCalendar("updatedOn", currentDateTime());
        final IQ query = queryWriter.getIQ();
        for (final ClientSession session : getClientSessions(userId)) {
            process(session, query);
        }        
    }
}
