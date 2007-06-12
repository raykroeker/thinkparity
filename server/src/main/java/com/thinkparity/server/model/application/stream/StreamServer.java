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
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.stream.StreamSession;

import com.thinkparity.desdemona.util.DesdemonaProperties;

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
        /*
         * TODO add timeout information to the session such that x duration of
         * in-activity will cause the session to be destroyed
         */
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
        final DesdemonaProperties properties = DesdemonaProperties.getInstance();
        this.fileServer = new StreamFileServer(this, workingDirectory);
        this.logger = new Log4JWrapper();
        final String bindHost = properties.getProperty("thinkparity.stream.bind-host");
        final int port = Integer.valueOf(properties.getProperty("thinkparity.stream-port"));
        this.socketServer = new SecureStreamSocketServer(this, bindHost, port);
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
    ServerSession authenticate(final String sessionId) {
        synchronized (SESSIONS) {
            // HACK - StreamSession#authenticate() - insecure
            return SESSIONS.get(sessionId);
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
    ServerSession authenticate(final String sessionId,
            final InetAddress sessionAddress) {
        return authenticate(sessionId);
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
     * Finalize the persistence of the stream.
     * 
     * @param streamSession
     *            A <code>StreamSession</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     */
    void finalizeStream(final StreamSession session, final String streamId) {
        fileServer.finalizeStream(session, streamId);
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
    Long getDownstreamSize(final StreamSession session, final String streamId) {
        final ServerSession serverSession = authenticate(session);
        if (null == serverSession)
            throw new StreamException("Illegal access to stream {0}.", streamId);
        return fileServer.findForDownstream(session, streamId).length();
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
        Assert.assertNotNull("Session id is null.", session.getId());
        Assert.assertNotNull("Session server host is null.", session.getServerHost());
        Assert.assertNotNull("Session server port is null.", session.getServerPort());
        synchronized (SESSIONS) {
            SESSIONS.put(session.getId(), session);
            fileServer.initialize(session.getClientSession());
            socketServer.initialize(session.getClientSession());
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
     * @param streamOffset
     *            The point at which to set the read position within the stream.
     * @return An <code>InputStream</code>.
     * @throws IOException
     */
    InputStream openForDownstream(final StreamSession session,
            final String streamId, final Long streamOffset) throws IOException {
        final ServerSession serverSession = authenticate(session);
        if (null == serverSession)
            throw new StreamException("Illegal access to stream {0}.", streamId);
        final File downstreamFile = fileServer.findForDownstream(session, streamId);
        Assert.assertTrue(streamOffset.longValue() < downstreamFile.length(),
            "Downstream offset {0} cannot be set for {1} of size {2}.",
            streamOffset, streamId, downstreamFile.length());
        final InputStream inputStream = new FileInputStream(downstreamFile);
        inputStream.skip(streamOffset);
        return inputStream;
    }

    /**
     * Open a stream for persistence to be written to.
     * 
     * @param streamSession
     *            A <code>StreamSession</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     * @param streamOffset
     *            At which point to set the write position within the stream.
     * @return An output stream.
     * @throws IOException
     */
    OutputStream openForUpstream(final StreamSession session,
            final String streamId, final Long streamOffset) throws IOException {
        final ServerSession serverSession = authenticate(session);
        if (null == serverSession)
            throw new StreamException("Illegal access to stream {0}.", streamId);
        final File upstreamFile = fileServer.findForUpstream(session, streamId);
        Assert.assertTrue(streamOffset.longValue() == upstreamFile.length(),
                "Upstream offset cannot be set for {0}.", streamId);
        return new FileOutputStream(upstreamFile, true);
    }

    /**
     * Start the stream server.
     * 
     * @throws IOException
     */
    void start() {
        fileServer.start();
        logger.logInfo("Streaming file server has been started.");
        fileServer.logStatistics();

        socketServer.start();
        logger.logInfo("Streaming socket server has been started.");
        socketServer.logStatistics();
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
    private ServerSession authenticate(final StreamSession session) {
        synchronized (SESSIONS) {
            return SESSIONS.get(session.getId());
        }
    }
}
