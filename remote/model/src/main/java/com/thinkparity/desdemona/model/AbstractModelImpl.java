/*
 * Created On: Nov 28, 2005
 */
package com.thinkparity.desdemona.model;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
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
import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.Constants.Xml;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.NotTrueAssertion;
import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.DownloadMonitor;
import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamException;
import com.thinkparity.codebase.model.stream.StreamMonitor;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.codec.MD5Util;
import com.thinkparity.codebase.model.util.xmpp.event.BackupStatisticsUpdatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;
import com.thinkparity.codebase.model.util.xstream.XStreamUtil;

import com.thinkparity.ophelia.model.user.UserUtils;

import com.thinkparity.desdemona.model.Constants.JivePropertyNames;
import com.thinkparity.desdemona.model.artifact.ArtifactModel;
import com.thinkparity.desdemona.model.artifact.InternalArtifactModel;
import com.thinkparity.desdemona.model.backup.InternalBackupModel;
import com.thinkparity.desdemona.model.contact.InternalContactModel;
import com.thinkparity.desdemona.model.container.InternalContainerModel;
import com.thinkparity.desdemona.model.io.sql.ConfigurationSql;
import com.thinkparity.desdemona.model.profile.InternalProfileModel;
import com.thinkparity.desdemona.model.queue.InternalQueueModel;
import com.thinkparity.desdemona.model.queue.QueueModel;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.stream.InternalStreamModel;
import com.thinkparity.desdemona.model.stream.StreamModel;
import com.thinkparity.desdemona.model.user.InternalUserModel;
import com.thinkparity.desdemona.model.user.UserModel;
import com.thinkparity.desdemona.util.xmpp.IQWriter;
import com.thinkparity.desdemona.wildfire.JIDBuilder;

import org.jivesoftware.util.JiveProperties;
import org.jivesoftware.wildfire.ClientSession;
import org.jivesoftware.wildfire.SessionManager;
import org.jivesoftware.wildfire.SessionResultFilter;
import org.jivesoftware.wildfire.XMPPServer;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModelImpl
    extends com.thinkparity.codebase.model.AbstractModelImpl {

    /** A <code>UserUtils</code> utility. */
    protected static final UserUtils USER_UTIL;

    /** A <code>XStreamUtil</code> xml serialization utility. */
    protected static final XStreamUtil XSTREAM_UTIL;

    /** No session log statement. */
    private static final JabberId NO_SESSION;

    /** An apache xmpp iq logger wrapper. */
    private static final Log4JWrapper XMPP_IQ_LOGGER;

    static {
        NO_SESSION = User.THINKPARITY.getId();
        XMPP_IQ_LOGGER = new Log4JWrapper("DESDEMONA_XMPP_DEBUGGER");
        XSTREAM_UTIL = XStreamUtil.getInstance();
        USER_UTIL = UserUtils.getInstance();
    }

    /**
	 * Handle to the user's session.
	 */
	protected Session session;

    /** A thinkParity configuration sql interface. */
    private final ConfigurationSql configurationSql;

    /** An instance of the jive properties. */
    private JiveProperties jiveProperties;

    /** A <code>ModelConfiguration</code>. */
    private ModelConfiguration modelConfiguration;

    /**
     * Create AbstractModelImpl.
     *
     */
     protected AbstractModelImpl() {
        this(null);
     }

    /**
     * Create AbstractModelImpl.
     *
     */
     protected AbstractModelImpl(final Session session) {
        super();
        this.configurationSql = new ConfigurationSql();
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
        addRecipient(mimeMessage, Message.RecipientType.TO, email);
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
    protected void addRecipient(final MimeMessage mimeMessage,
            final Message.RecipientType recipientType, final EMail email)
            throws MessagingException {
        mimeMessage.addRecipient(recipientType, new InternetAddress(
                email.toString(), Boolean.TRUE));
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
     * Assert that the session user id matches that of a system user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @see #isSystemUser(JabberId)
     */
    protected void assertIsSystemUser() {
        Assert.assertTrue(isSystemUser(session.getJabberId()),
                "User {0} is not a system user.", session.getJabberId());
    }

	/**
     * Assert that the thinkParity system is the current key holder.
     * 
     * @param assertion
     *            The assertion.
     * @param uniqueId
     *            The artifact unique id.
     */
    protected void assertSystemIsKeyHolder(final UUID uniqueId) {
        Assert.assertTrue(isSystemKeyHolder(uniqueId),
                "The thinkParity system is not the key holder for artifact {0}.",
                uniqueId);
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
     * Build a unique id for a user in time. Use the user id plus the current
     * timestamp to generate a unique id.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A unique id <code>String</code>.
     */
    protected String buildUserTimestampId(final JabberId userId) {
        /*
         * NOTE A user timestamp id is unique per user per timestamp
         */
       // TIME A global timestamp
       final String hashString = new StringBuffer(userId.toString())
           .append(currentTimeMillis()).toString();
       return MD5Util.md5Hex(hashString);
    }

    /**
     * Determine if a list contains a user.
     * 
     * @param <U>
     *            A type of <code>User</code>.
     * @param list
     *            A <code>User</code> <code>List</code>.
     * @param o
     *            A <code>User</code>
     * @return True if the list contains the user.
     */
    protected final <T extends User> boolean contains(final List<T> list,
            final JabberId id) {
        return -1 < indexOf(list, id);
    }

    /**
     * Determine if a list contains a user.
     * 
     * @param <U>
     *            A type of <code>User</code>.
     * @param list
     *            A <code>User</code> <code>List</code>.
     * @param o
     *            A <code>User</code>
     * @return True if the list contains the user.
     */
    protected final <T extends User, U extends User> boolean contains(
            final List<T> list, final U o) {
        return -1 < indexOf(list, o);
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
     * Start a download from the stream server.
     * 
     * @param downloadMonitor
     *            A download monitor.
     * @param streamId
     *            A stream id <code>String</code>.
     * @return The downloaded <code>File</code>.
     * @throws IOException
     */
    protected final File downloadStream(final DownloadMonitor downloadMonitor,
            final JabberId userId, final String streamId) throws IOException {
        final File streamFile = buildStreamFile(streamId);
        final StreamSession streamSession = getStreamModel().createSession(userId);
        final StreamMonitor streamMonitor = new StreamMonitor() {
            long recoverChunkOffset = 0;
            long totalChunks = 0;
            public void chunkReceived(final int chunkSize) {
                logger.logApiId();
                logger.logVariable("chunkSize", chunkSize);
                totalChunks += chunkSize;
                downloadMonitor.chunkDownloaded(chunkSize);
            }
            public void chunkSent(final int chunkSize) {}
            public void headerReceived(final String header) {}
            public void headerSent(final String header) {}
            public void streamError(final StreamException error) {
                if (error.isRecoverable()) {
                    if (recoverChunkOffset <= totalChunks) {
                        logger.logWarning(error, "Network error.");
                        recoverChunkOffset = totalChunks;
                        try {
                            // attempt to resume the download
                            downloadStream(downloadMonitor, this,
                                    streamSession, streamId, streamFile,
                                    Long.valueOf(recoverChunkOffset));
                        } catch (final IOException iox) {
                            throw translateError(iox);
                        }
                    } else {
                        throw translateError(error);
                    }
                } else {
                    throw translateError(error);
                }
            }
        };
        downloadStream(downloadMonitor, streamMonitor, streamSession,
                streamId, streamFile, 0L);
        return streamFile;
    }

    /**
     * Enqueue an event for a user.
     * 
     * @param userId
     *            The event creator user id <code>JabberId</code>.
     * @param eventUserId
     *            The event target user id <code>JabberId</code>.
     * @param event
     *            The <code>XMPPEvent</code>.
     */
    protected void enqueueEvent(final JabberId userId,
            final JabberId eventUserId, final XMPPEvent event) {
        final List<JabberId> eventUserIds = new ArrayList<JabberId>(1);
        eventUserIds.add(eventUserId);
        enqueueEvent(userId, eventUserIds, event);
    }

    /**
     * Enqueue an event for a user.
     * 
     * @param userId
     *            The event creator user id <code>JabberId</code>.
     * @param eventUserIds
     *            The event target user ids <code>JabberId</code>.
     * @param event
     *            The <code>XMPPEvent</code>.
     */
    protected void enqueueEvent(final JabberId userId,
            final List<JabberId> eventUserIds, final XMPPEvent event) {
        logApiId();
        logVariable("userId", userId);
        logVariable("eventUserIds", eventUserIds);
        logVariable("event", event);
        for (final JabberId eventUserId : eventUserIds) {
            createEvent(userId, eventUserId, event);
            if (isOnline(eventUserId)) {
                sendQueueUpdated(eventUserId);
            }
        }
        // backup
        backupEvent(userId, userId, event);
    }

    /**
     * Enqueue a priority event for a user.
     * 
     * @param userId
     *            The event creator user id <code>JabberId</code>.
     * @param eventUserId
     *            The event target user id <code>JabberId</code>.
     * @param event
     *            The <code>XMPPEvent</code>.
     */
    protected void enqueuePriorityEvent(final JabberId userId,
            final JabberId eventUserId, final XMPPEvent event) {
        final List<JabberId> eventUserIds = new ArrayList<JabberId>(1);
        eventUserIds.add(eventUserId);
        enqueuePriorityEvent(userId, eventUserIds, event);
    }
    /**
     * Enqueue a priority event for a user.
     * 
     * @param userId
     *            The event creator user id <code>JabberId</code>.
     * @param eventUserIds
     *            The event target user ids <code>JabberId</code>.
     * @param event
     *            The <code>XMPPEvent</code>.
     */
    protected void enqueuePriorityEvent(final JabberId userId,
            final List<JabberId> eventUserIds, final XMPPEvent event) {
        logApiId();
        logVariable("userId", userId);
        logVariable("eventUserIds", eventUserIds);
        logVariable("event", event);
        for (final JabberId eventUserId : eventUserIds) {
            createPriorityEvent(userId, eventUserId, event);
            if (isOnline(eventUserId)) {
                sendQueueUpdated(eventUserId);
            }
        }
        // backup
        backupEvent(userId, userId, event);
    }

    /**
     * Obtain the parity artifact interface.
     * 
     * @return The parity artifact interface.
     */
	protected final InternalArtifactModel getArtifactModel() {
		return ArtifactModel.getInternalModel(getContext(), session);
	}

    /**
     * Obtain an internal backup model.
     * 
     * @return An instance of <code>InternalBackupModel</code>.
     */
    protected final InternalBackupModel getBackupModel() {
        return InternalModelFactory.getInstance(getContext(), session).getBackupModel();
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
     * Obtain configuration.
     * 
     * @param key
     *            A configuration key <code>String</code>.
     * @return A configuration value <code>String</code>.
     */
    protected final String getConfiguration(final String key) {
        return modelConfiguration.getConfiguration(key);
    }

    /**
     * Obtain an internal contact model.
     * 
     * @return An instance of <code>InternalContactModel</code>.
     */
    protected final InternalContactModel getContactModel() {
        return InternalModelFactory.getInstance(getContext(), session).getContactModel();
    }

    /**
     * Obtain an internal container model.
     * 
     * @return An instance of <code>InternalContainerModel</code>.
     */
    protected final InternalContainerModel getContainerModel() {
        return InternalModelFactory.getInstance(getContext(), session).getContainerModel();
    }

	/**
     * Obtain the default buffer size.
     * 
     * @return A buffer size <code>Integer</code>.
     */
    protected final ByteBuffer getDefaultBuffer() {
        return ByteBuffer.allocate(getDefaultBufferSize());
    }

    /**
     * Obtain the default buffer size.
     * 
     * @return A buffer size <code>Integer</code>.
     */
    protected final Integer getDefaultBufferSize() {
        return 1024 * 1024 * 8; // BUFFER 8MB  - AbstractModelImpl#getDefaultBufferSize()
    }

    /**
     * Obtain the default buffer size.
     * 
     * @return A buffer size <code>Integer</code>.
     */
    protected final Integer getDefaultBufferSize(final String context) {
        if ("stream-session".equals(context)) {
            return 1024 * 1; // BUFFER 1KB - AbstractModelImpl#getDefaultBufferSize(String)
        } else {
            return getDefaultBufferSize();
        }
    }

    /**
     * Obtain the local environment.
     * 
     * @return An instance of <code>Environment</code>.
     */
    protected Environment getEnvironment() {
        return Environment.valueOf(getJiveProperty("thinkparity.environment"));
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

	protected final InternalProfileModel getProfileModel() {
        return InternalModelFactory.getInstance(getContext(), session).getProfileModel();
    }

	protected final InternalQueueModel getQueueModel() {
        return QueueModel.getInternalModel(getContext(), session);
    }

    protected final InternalStreamModel getStreamModel() {
        return StreamModel.getInternalModel(getContext(), session);
    }

    protected final InternalUserModel getUserModel() {
		return UserModel.getInternalModel(getContext(), session);
	}

	/**
     * Obtain the index of a user in the list.
     * 
     * @param <U>
     *            A type of <code>User</code>.
     * @param list
     *            A user <code>List</code>.
     * @param o
     *            A <code>User</code>
     * @return The index of the first user in the list with a matching id; or -1
     *         if no such user exists.
     */
    protected final <T extends User, U extends User> int indexOf(
            final List<T> users, final U user) {
        return indexOf(users, user.getId());
    }

    /**
     * Obtain the index of a user in the list with the given id.
     * 
     * @param <U>
     *            A type of <code>User</code>.
     * @param list
     *            A user <code>List</code>.
     * @param id
     *            A user id <code>JabberId</code>.
     * @return The index of the first user in the list with a matching id; or -1
     *         if no such user exists.
     */
    protected final <U extends User> int indexOf(final List<U> list,
            final JabberId o) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equals(o))
                return i;
        }
        return -1;
    }

    /**
     * Initialize the model.
     * 
     * @param session
     *            A user <code>Session</code>.
     */
    protected final void initialize(final Session session) {
        setContext(new Context());
        this.session = session;
        this.modelConfiguration = ModelConfiguration.getInstance(getClass());
    }

    /**
     * Intialize the model.
     * 
     * @param session
     *            A user <code>Session</code>.
     */
    protected void initializeModel(final Session session) {}

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
        type.setLocalId(user.getLocalId());
        type.setName(user.getName());
        type.setOrganization(user.getOrganization());
        type.setTitle(user.getTitle());
        return logger.logVariable("type", type);
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

    /**
     * Determine if the user id is a system user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True if the user is a system user.
     */
    protected Boolean isSystemUser(final JabberId userId) {
        return userId.equals(User.THINKPARITY.getId());
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
     * @see com.thinkparity.codebase.model.AbstractModelImpl#panic(java.lang.Throwable)
     *
     */
    @Override
    protected ThinkParityException panic(final Throwable t) {
        return super.panic(t);
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
    protected ThinkParityException translateError(final Throwable t) {
        return panic(t);
    }

    /**
     * Send an event to the user's backup.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param eventUserId
     *            An event user id <code>JabberId</code>.
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    private void backupEvent(final JabberId userId, final JabberId eventUserId,
            final XMPPEvent event) {
        final JabberId backupUserId = getUserModel().readBackupUserId();

        // create a backup statistics event in the database
        final BackupStatisticsUpdatedEvent bsue = new BackupStatisticsUpdatedEvent();
        bsue.setStatistics(getBackupModel().readStatistics(eventUserId));
        createEvent(userId, eventUserId, bsue);
        if (isOnline(eventUserId)) {
            // send the user a notification that an event is pending
            sendQueueUpdated(eventUserId);
        }

        // create the backup event in the database
        createEvent(userId, backupUserId, event);
        if (isOnline(backupUserId)) {
            // send the backup user a notification that an event is pending
            sendQueueUpdated(backupUserId);
        } else {
            logWarning("Backup service is not online.");
        }
    }

    /**
     * Build a local file to back a stream. Note that the file is transient in
     * nature and will be deleted when the user logs out or the next time the
     * session is established.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @return A <code>File</code>.
     * @throws IOException
     */
    private File buildStreamFile(final String streamId) throws IOException {
        return session.createTempFile(streamId);
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
     * Create an event for user.
     * 
     * @param userId
     *            The event creator user id <code>JabberId</code>.
     * @param eventUserId
     *            The event destination user id <code>JabberId</code>.
     * @param event
     *            The <code>XMPPEvent</code>.
     */
    private void createEvent(final JabberId userId,
            final JabberId eventUserId, final XMPPEvent event) {
        getQueueModel().createEvent(userId, eventUserId, event);
    }

    /**
     * Create a priority event for user.
     * 
     * @param userId
     *            The event creator user id <code>JabberId</code>.
     * @param eventUserId
     *            The event destination user id <code>JabberId</code>.
     * @param event
     *            The <code>XMPPEvent</code>.
     */
    private void createPriorityEvent(final JabberId userId,
            final JabberId eventUserId, final XMPPEvent event) {
        getQueueModel().createEvent(userId, eventUserId, event,
                XMPPEvent.Priority.HIGH);
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
     * Obtain an instance of the jive properties.
     * 
     * @return An instance of <code>JiveProperties</code>.
     */
    private JiveProperties getJiveProperties() {
        if (null == jiveProperties) {
            jiveProperties = JiveProperties.getInstance();
        }
        return jiveProperties;
    }

    /**
     * Obtain a jive property.
     * 
     * @param name
     *            A property name.
     * @return A property value.
     */
    private String getJiveProperty(final String name) {
        return (String) getJiveProperties().get(name);
    }

    /**
     * Obtain a handle to the xmpp server's session manager.
     * 
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
	private XMPPServer getXMPPServer() {
        return XMPPServer.getInstance();
    }

    /**
     * Determine whether or not the system account is the key holder.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @return True if the system account is the key holder; false otherwise.
     */
    private Boolean isSystemKeyHolder(final UUID uniqueId) {
        return readKeyHolder(uniqueId).equals(User.THINKPARITY.getId());
    }

    /**
     * Send a queue updated event to a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    private void sendQueueUpdated(final JabberId userId) {
        final IQ iq = new IQ();
        iq.setType(IQ.Type.set);
        iq.setChildElement(Xml.NAME, new StringBuffer(Xml.NAMESPACE)
                .append(":system:queueupdated").toString());

        final IQWriter queryWriter = new IQWriter(iq);
        queryWriter.writeCalendar("updatedOn", currentDateTime());
        final IQ query = queryWriter.getIQ();
        for (final ClientSession session : getClientSessions(userId)) {
            process(session, query);
        }        
    }
}
