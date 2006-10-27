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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class StreamServer {

    /** A <code>Charset</code>. */
    private static final Charset CHARSET;

    /** A <code>Session</code> list. */
    private static final Map<String, StreamSession> SESSIONS;

    static {
        CHARSET = StreamModelImpl.CHARSET;
        SESSIONS = new HashMap<String, StreamSession>(50);
    }

    /** The <code>StreamFileServer</code>. */
    private final StreamFileServer fileServer;

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
    StreamServer(final File workingDirectory, final String host, final Integer port) {
        super();
        this.fileServer = new StreamFileServer(this, workingDirectory);
        this.logger = new Log4JWrapper();
        this.socketServer = new StreamSocketServer(this, host, port);
    }

    /**
     * Authenticate a session.
     * 
     * @param sessionId
     *            A session id.
     * @param sessionAddress
     *            A session <code>InetSocketAddress</code>.
     * @return A <code>StreamSession</code>.
     */
    StreamSession authenticate(final String sessionId) {
        synchronized (SESSIONS) {
            return SESSIONS.get(sessionId);
        }
    }

    Charset getCharset() {
        return CHARSET;
    }

    /**
     * Create a stream session.
     * 
     * @param A
     *            <code>StreamSession</code>.
     */
    void initializeSession(final StreamSession session) {
        synchronized (SESSIONS) {
            SESSIONS.put(session.getId(), session);
            fileServer.initialize(session);
            socketServer.initialize(session);
        }
    }

    void invalidate(final StreamSession streamSession) {
        synchronized (SESSIONS) {
            if (null == streamSession) {
                logger.logWarning("Stream session is null.");
            } else {
                fileServer.invalidate(streamSession);
                socketServer.invalidate(streamSession);
                if (SESSIONS.containsKey(streamSession.getId())) {
                    SESSIONS.remove(streamSession.getId());
                }
                streamSession.setId(null);
            }
        }
    }

    InputStream openInputStream(final StreamSession streamSession,
            final String streamId) throws IOException {
        return new FileInputStream(
                fileServer.readWorkspace(
                        authenticate(streamSession)).findFile(streamId));
    }

    /**
     * Open the input streams for a session.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @return An <code>InputStream</code> <code>List</code>.
     * @throws IOException
     */
    List<InputStream> openInputStreams(final String sessionId) throws IOException {
        final StreamSession session = SESSIONS.get(sessionId);
        final FileSystem fileSystem = fileServer.readWorkspace(session);
        final List<InputStream> streams = new ArrayList<InputStream>();
        for (final File file : fileSystem.listFiles("/", Boolean.FALSE)) {
            streams.add(new FileInputStream(file));
        }
        return streams;
    }

    /**
     * Open an output stream.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @return An output stream.
     * @throws IOException
     */
    OutputStream openOutputStream(final StreamSession streamSession,
            final String streamId) throws IOException {
        return new FileOutputStream(
                fileServer.readWorkspace(
                        authenticate(streamSession)).createFile(streamId));
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
     * @param sessionId
     *            A session id.
     * @param sessionAddress
     *            A session <code>InetSocketAddress</code>.
     * @return A <code>StreamSession</code>.
     */
    private StreamSession authenticate(final StreamSession streamSession) {
        synchronized (SESSIONS) {
            return SESSIONS.get(streamSession.getId());
        }
    }
}
