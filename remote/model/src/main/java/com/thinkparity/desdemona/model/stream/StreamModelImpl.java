/*
 * Generated On: Oct 22 06 10:33:07 AM
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.net.InetAddress;
import java.nio.charset.Charset;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.stream.StreamSession;
import com.thinkparity.codebase.model.util.codec.MD5Util;

import com.thinkparity.ophelia.model.util.UUIDGenerator;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants.JivePropertyNames;

import org.jivesoftware.util.JiveProperties;

/**
 * <b>Title:</b>thinkParity Stream Model Implementation</br>
 * <b>Description:</b>
 *
 * @author CreateModel.groovy
 * @version 1.1
 */
final class StreamModelImpl extends AbstractModelImpl {

    /** The character set used by the stream server. */
    static final Charset CHARSET;

    /** A thinkParity <code>StreamServer</code>. */
    private static StreamServer streamServer;

    static {
        CHARSET = Charset.forName("ISO-8859-1");
    }

    /** A <code>JiveProperties</code> instance. */
    private final JiveProperties jiveProperties;

    StreamModelImpl() {
        this(null);
    }

    /**
     * Create StreamModelImpl.
     *
     * @param session
     *		The user's session.
     */
    StreamModelImpl(final com.thinkparity.desdemona.model.session.Session session) {
        super(session);
        this.jiveProperties = JiveProperties.getInstance();
    }

    /**
     * Create a stream.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param sessionId
     *            A session id <code>String</code>.
     * @return A stream id <code>String</code>.
     */
    String create(final JabberId userId, final String sessionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("sessionId", sessionId);
        try {
            assertIsAuthenticatedUser(userId);
            final StreamSession session = streamServer.authenticate(
                    sessionId, this.session.getInetAddress());
            final String streamId = buildStreamId();
            streamServer.initialize(session, streamId);
            return streamId;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Create a stream session for an achive user.
     * 
     * @param archiveId
     *            An archive user id <code>JabberId</code>.
     * @return A stream session.
     */
    StreamSession createArchiveSession(final JabberId archiveId) {
        logger.logApiId();
        logger.logVariable("archiveId", archiveId);
        try {
            final ServerSession streamSession = buildSession(archiveId, null);
            streamServer.initialize(streamSession);
            return streamSession;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Create a session.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>Session</code>.
     */
    StreamSession createSession(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        try {
            assertIsAuthenticatedUser(userId);
            final ServerSession session = buildSession(userId,
                    this.session.getInetAddress());
            streamServer.initialize(session);
            return session;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Delete a stream.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param sessionId
     *            A stream session id <code>String</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    void delete(final JabberId userId, final String sessionId,
            final String streamId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("sessionId", sessionId);
        logger.logVariable("streamId", streamId);
        try {
            assertIsAuthenticatedUser(userId);
            final StreamSession session = streamServer.authenticate(
                    sessionId, this.session.getInetAddress());
            streamServer.destroy(session, streamId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Delete a session.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param sessionId
     *            A session id <code>String</code>.
     */
    void deleteSession(final JabberId userId, final String sessionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        try {
            assertIsAuthenticatedUser(userId);
            final StreamSession session =
                streamServer.authenticate(sessionId,
                        this.session.getInetAddress());
            streamServer.destroy(session);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read a session.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param sessionId
     *            A session id <code>String</code>.
     * @return A <code>StreamSession</code>.
     */
    StreamSession readSession(final JabberId userId, final String sessionId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("sessionId", sessionId);
        try {
            assertIsAuthenticatedUser(userId);
            return streamServer.authenticate(
                    sessionId, this.session.getInetAddress());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Start the stream service.
     *
     */
    void start() {
        logApiId();
        try {
            Assert.assertIsNull("Stream server has been started.", streamServer);
            streamServer = new StreamServer(
                    new File((String) jiveProperties.get(JivePropertyNames.THINKPARITY_STREAM_ROOT)),
                    readEnvironment());
            streamServer.start();
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Stop the stream service.
     *
     */
    void stop() {
        logApiId();
        try {
            Assert.assertNotNull("Stream server has not been started.", streamServer);
            streamServer.stop(Boolean.TRUE);
            streamServer = null;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Build a server session.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param inetAddress
     *            An <code>InetAddress</code>.
     * @return A new server stream session.
     */
    private ServerSession buildSession(final JabberId userId,
            final InetAddress inetAddress) {
        final ServerSession session = new ServerSession();
        session.setCharset(CHARSET);
        session.setBufferSize(getBufferSize("stream-session"));
        session.setEnvironment(readEnvironment());
        session.setId(buildSessionId(userId));
        session.setInetAddress(inetAddress);
        return session;
    }

    /**
     * Create a stream session id.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A stream session id <code>String</code>.
     */
    private String buildSessionId(final JabberId userId) {
        return buildUserTimestampId(userId);
    }

    /**
     * Build a stream id.
     * 
     * @return A stream id <code>String</code>.
     */
    private String buildStreamId() {
        /*
         * NOTE A stream id is a UUID
         */
        final StringBuffer hashString = new StringBuffer()
            .append(UUIDGenerator.nextUUID());
        return MD5Util.md5Hex(hashString.toString());
    }
}
