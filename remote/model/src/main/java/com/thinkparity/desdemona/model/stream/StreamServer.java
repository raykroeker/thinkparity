/*
 * Created On: Sun Oct 22 2006 10:41 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.NetworkUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class StreamServer {

    /** A <code>Charset</code>. */
    private static final Charset CHARSET;

    /** A <code>Session</code> list. */
    private static final Map<String, ServerSession> SESSIONS;

    static {
        CHARSET = StreamModelImpl.CHARSET;
        SESSIONS = new HashMap<String, ServerSession>(50);
    }

    /** The <code>StreamFileServer</code>. */
    private final StreamFileServer fileServer;

    /** An apache logger wrapper. */
    private final Log4JWrapper logger;

    /** The <code>StreamSocketServer</code>.*/
    private final StreamSocketServer socketServer;

    /**
     * Create StreamServer.
     * 
     * @param workingDirectory
     *            A directory within which the stream server will work.
     * @param host
     *            A host to which the stream server will bind.
     * @param port
     *            A port on which the stream server will listen.
     */
    StreamServer(final File workingDirectory, final Environment environment) {
        super();
        this.fileServer = new StreamFileServer(this, workingDirectory);
        this.logger = new Log4JWrapper();
        final String host;
        try {
            host = NetworkUtil.getMachine();
        } catch (final UnknownHostException uhx) {
            throw new StreamException(uhx);
        }
        final int port = environment.getStreamPort();
        if (environment.isStreamTLSEnabled()) {
            logger.logInfo("Stream Server - {0}:{1} - Secure", host, port);
            this.socketServer = new SecureStreamSocketServer(this, host, port);
        } else {
            logger.logInfo("Stream Server - {0}:{1}", host, port);
            this.socketServer = new StreamSocketServer(this, host, port);
        }
    }

    /**
     * Authenticate a session.
     * 
     * @param sessionId
     *            A session id.
     * @param sessionAddress
     *            A session <code>InetAddress</code>.
     * @return A <code>StreamSession</code>.
     */
    StreamSession authenticate(final String sessionId,
            final InetAddress sessionAddress) {
        synchronized (SESSIONS) {
            final ServerSession session = SESSIONS.get(sessionId);
            if (session.getInetAddress().equals(sessionAddress)) {
                return session;
            } else {
                logger.logWarning("Session address {0} does not match original.",
                        sessionAddress);
                return null;
            }
        }
    }

    /**
     * Destroy a session.
     * 
     * @param streamSession
     *            A <code>StreamSession</code>.
     */
    void destroy(final StreamSession session) {
        authenticate(session);
        synchronized (SESSIONS) {
            SESSIONS.remove(session.getId());

            fileServer.destroy(session);
            socketServer.destroy(session);
            session.setId(null);
        }
    }

    /**
     * Destroy a stream.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    void destroy(final StreamSession session, final String streamId) {
        authenticate(session);
        fileServer.destroy(session, streamId);
        socketServer.destroy(session, streamId);
    }

    /**
     * Obtain the stream server character set.
     * 
     * @return A <code>Charset</code>.
     */
    Charset getCharset() {
        return CHARSET;
    }

    /**
     * Obtain the size of a stream.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     * @return The size of the stream in bytes.
     */
    Long getSize(final StreamSession session, final String streamId) {
        return fileServer.find(authenticate(session), streamId).length();
    }

    /**
     * Initialize the stream server for a given stream session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     */
    void initialize(final ServerSession session) {
        Assert.assertNotNull("Session is null.", session);
        Assert.assertNotNull("Session buffer size is null.", session.getBufferSize());
        Assert.assertNotNull("Session charset is null.", session.getCharset());
        Assert.assertNotNull("Session environment is null.", session.getEnvironment());
        Assert.assertNotNull("Session id is null.", session.getId());
        Assert.assertNotNull("Session inet address is null.", session.getInetAddress());
        synchronized (SESSIONS) {
            SESSIONS.put(session.getId(), session);
            fileServer.initialize(session);
            socketServer.initialize(session);
        }
    }

    /**
     * Initialize the stream server for a given stream.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    void initialize(final StreamSession session, final String streamId) {
        authenticate(session);
        fileServer.initialize(session, streamId);
        socketServer.initialize(session, streamId);
    }

    /**
     * Open a stream from persistence to be read.
     * 
     * @param streamSession
     *            A <code>StreamSession</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     * @return An <code>InputStream</code>.
     * @throws IOException
     */
    InputStream openInputStream(final StreamSession session,
            final String streamId) throws IOException {
        return new FileInputStream(
                fileServer.find(authenticate(session), streamId));
    }

    /**
     * Open a stream for persistence to be written to.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     * @return An output stream.
     * @throws IOException
     */
    OutputStream openOutputStream(final StreamSession session,
            final String streamId) throws IOException {
        return new FileOutputStream(
                fileServer.find(authenticate(session), streamId));
    }

    /**
     * Start the stream server.
     * 
     * @throws IOException
     */
    void start() {
        fileServer.start();
        socketServer.start();
    }

    /**
     * Stop the stream server.
     * 
     * @param wait
     *            Whether or not to wait for connected users to disconnect.
     */
    void stop(final Boolean wait) {
        socketServer.stop(wait);
        fileServer.stop();
    }

    /**
     * Authenticate a session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @return A <code>StreamSession</code>.
     */
    private StreamSession authenticate(final StreamSession session) {
        synchronized (SESSIONS) {
            return SESSIONS.get(session.getId());
        }
    }
}
