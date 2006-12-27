/*
 * Created On: Nov 28, 2005
 */
package com.thinkparity.desdemona.model;

import java.io.IOException;
import java.io.InputStream;
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
import com.thinkparity.codebase.model.stream.StreamException;
import com.thinkparity.codebase.model.stream.StreamMonitor;
import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.stream.StreamWriter;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.desdemona.model.Constants.JivePropertyNames;
import com.thinkparity.desdemona.model.archive.ArchiveModel;
import com.thinkparity.desdemona.model.archive.InternalArchiveModel;
import com.thinkparity.desdemona.model.artifact.ArtifactModel;
import com.thinkparity.desdemona.model.backup.BackupModel;
import com.thinkparity.desdemona.model.backup.InternalBackupModel;
import com.thinkparity.desdemona.model.contact.ContactModel;
import com.thinkparity.desdemona.model.io.sql.ConfigurationSql;
import com.thinkparity.desdemona.model.queue.InternalQueueModel;
import com.thinkparity.desdemona.model.queue.QueueModel;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.stream.InternalStreamModel;
import com.thinkparity.desdemona.model.stream.StreamModel;
import com.thinkparity.desdemona.model.user.UserModel;
import com.thinkparity.desdemona.util.MD5Util;
import com.thinkparity.desdemona.util.xmpp.IQWriter;
import com.thinkparity.desdemona.wildfire.JIDBuilder;
import com.thinkparity.desdemona.wildfire.util.SessionUtil;

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

    /** No session log statement. */
    private static final JabberId NO_SESSION;

    /** An apache xmpp iq logger wrapper. */
    private static final Log4JWrapper XMPP_IQ_LOGGER;

    /** A <code>SessionUtil</code>. */
    private static final SessionUtil SESSION_UTIL;

    static {
        NO_SESSION = User.THINK_PARITY.getId();
        SESSION_UTIL = SessionUtil.getInstance();
        XMPP_IQ_LOGGER = new Log4JWrapper("DESDEMONA_XMPP_DEBUGGER");
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
       return MD5Util.md5Hex(hashString.getBytes());
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

    protected void enqueueEvent(final JabberId userId,
            final JabberId eventUserId, final XMPPEvent event) {
        final List<JabberId> eventUserIds = new ArrayList<JabberId>(1);
        eventUserIds.add(eventUserId);
        enqueueEvent(userId, eventUserIds, event);
    }

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
            backupEvent(userId, eventUserId, event);
        }
        // do not backup the same user twice
        if (!eventUserIds.contains(userId)) {
            backupEvent(userId, userId, event);
        }
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

    protected InternalQueueModel getQueueModel() {
        return QueueModel.getInternalModel(getContext(), session);
    }

	protected InternalStreamModel getStreamModel() {
        return StreamModel.getInternalModel(getContext(), session);
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
     * Upload a stream to the stream server using an existing session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @param iStream
     *            A <code>Iterable</code> series of <code>InputStream</code>.
     * @throws IOException
     */
    protected final String uploadStream(final UploadMonitor uploadMonitor,
            final StreamMonitor streamMonitor, final String streamId,
            final StreamSession session, final InputStream stream,
            final Long streamSize, final Long streamOffset) throws IOException {
        stream.reset();
        long skipped = stream.skip(streamOffset);
        while (skipped < streamOffset && 0 < skipped) {
            skipped += stream.skip(streamOffset.longValue() - skipped);
        }
        final Long actualStreamOffset;
        if (skipped == streamOffset.longValue()) {
            logger.logInfo("Resuming upload for {0} at {1}.",
                    streamId, streamOffset);
            actualStreamOffset = streamOffset;
        } else {
            logger.logWarning("Could not resume upload for {0} at {1}.  Starting over.",
                    streamId, streamOffset);
            actualStreamOffset = 0L;
        }
        final StreamWriter writer = new StreamWriter(streamMonitor, session);
        writer.open();
        try {
            writer.write(streamId, stream, streamSize, actualStreamOffset);
            return streamId;
        } finally {
            writer.close();
        }
    }

	/**
     * Upload a stream to the stream server using an existing session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @param iStream
     *            A <code>Iterable</code> series of <code>InputStream</code>.
     * @throws IOException
     */
    protected final String uploadStream(final UploadMonitor uploadMonitor,
            final String streamId, final StreamSession session,
            final InputStream stream, final Long streamSize) throws IOException {
        final StreamMonitor streamMonitor = new StreamMonitor() {
            long recoverChunkOffset = 0;
            long totalChunks = 0;
            public void chunkReceived(final int chunkSize) {}
            public void chunkSent(final int chunkSize) {
                totalChunks += chunkSize;
                uploadMonitor.chunkUploaded(chunkSize);
            }
            public void headerReceived(final String header) {}
            public void headerSent(final String header) {}
            public void streamError(final StreamException error) {
                if (error.isRecoverable()) {
                    if (recoverChunkOffset <= totalChunks) {
                        // attempt to resume the upload
                        recoverChunkOffset = totalChunks;
                        try {
                            uploadStream(uploadMonitor, this, streamId,
                                    session, stream, streamSize, Long
                                            .valueOf(recoverChunkOffset));
                        } catch (final IOException iox) {
                            throw translateError(iox);
                        }
                    } else {
                        throw error;
                    }
                } else {
                    throw error;
                }
            }
        };
        return uploadStream(uploadMonitor, streamMonitor, streamId, session,
                stream, streamSize, 0L);
    }

    private void backupEvent(final JabberId userId, final JabberId eventUserId,
            final XMPPEvent event) {
        final JabberId archiveId = getUserModel().readArchiveId(eventUserId);
        if (null == archiveId) {
            logger.logInfo("No archive exists for user {0}", eventUserId);
        } else {
            createEvent(userId, archiveId, event);
            if (isOnline(archiveId)) {
                sendQueueUpdated(archiveId);
            } else {
                logWarning("Archive {0} for user {1} is not online.",
                        archiveId, eventUserId);
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

    private void createEvent(final JabberId userId,
            final JabberId eventUserId, final XMPPEvent event) {
        QueueModel.getModel(session).createEvent(userId, eventUserId, event);
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
        return readKeyHolder(uniqueId).equals(User.THINK_PARITY.getId());
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
