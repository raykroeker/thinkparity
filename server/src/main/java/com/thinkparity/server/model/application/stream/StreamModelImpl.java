/*
 * Generated On: Oct 22 06 10:33:07 AM
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.Constants.JivePropertyNames;
import com.thinkparity.desdemona.util.MD5Util;

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

    /** The size of the stream buffer to use. */
    private static final Integer BUFFER_SIZE;

    /** A thinkParity <code>StreamServer</code>. */
    private static StreamServer streamServer;

    static {
        BUFFER_SIZE = 1024;
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
     * Initialize a session. If a previous session for the user exists; it will
     * be re-used to allow resume functionality.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>Session</code>.
     */
    StreamSession initializeSession(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        try {
            logger.logTraceId();
            assertIsAuthenticatedUser(userId);
            logger.logTraceId();
            final ServerSession session = createServerSession(userId);
            logger.logTraceId();
            streamServer.initializeSession(session);
            logger.logTraceId();
            return session;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * 
     * @param session
     * @return
     */
    List<InputStream> openStreams(final StreamSession session) {
        logApiId();
        logVariable("session", session);
        try {
            return streamServer.openInputStreams(session.getId());
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
            final Environment environment = readEnvironment();
            streamServer = new StreamServer(
                    new File((String) jiveProperties.get(JivePropertyNames.THINKPARITY_STREAM_ROOT)),
                    environment.getStreamHost(), environment.getStreamPort());
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
     * Create a new server stream session.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A new server stream session.
     */
    private ServerSession createServerSession(final JabberId userId) {
        final ServerSession serverSession = new ServerSession();
        serverSession.setCharset(CHARSET);
        serverSession.setBufferSize(BUFFER_SIZE);
        serverSession.setEnvironment(readEnvironment());
        serverSession.setId(createSessionId(userId));
        return serverSession;
    }

    /**
     * Create a stream session id.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A stream session id <code>String</code>.
     */
    private String createSessionId(final JabberId userId) {
        // TODO Generate a unique id per user id and store it in the user's
        // meta-data
        final String hashString = new StringBuffer(userId.toString())
                .append("LSAHD-QOIUQOE-ZXBVMNZNX-MZXXNCBVMX")
                .insert(0, "LKSJD-ZXVBNZM-QPWOEIURY-NXBCVMXNBC")
                .toString();
        return MD5Util.md5Hex(hashString.getBytes());
    }
}
