/*
 * Created On: Sun Oct 22 2006 10:41 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.model.stream.Direction;

import com.thinkparity.desdemona.util.MD5Util;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class StreamServer {

    /** A <code>Session</code> list. */
    private static final Map<String, Session> SESSIONS;

    static {
        SESSIONS = new HashMap<String, Session>(50);
    }

    /** The <code>StreamFileServer</code>. */
    private final StreamFileServer fileServer;

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
        this.fileServer = new StreamFileServer(workingDirectory);
        this.socketServer = new StreamSocketServer(host, port);
    }

    /**
     * Create a stream session.
     * 
     * @param direction
     *            A stream <code>Direction</code>.
     * @return A stream session id <code>String</code>.
     */
    String createSession(final Direction direction) {
        synchronized (SESSIONS) {
            final Session session = new Session(direction);
            SESSIONS.put(session.id, session);
            return SESSIONS.get(session.id).id;
        }
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
        final Session session = SESSIONS.get(sessionId);
        final FileSystem fileSystem = fileServer.readWorkspace(session);
        final List<InputStream> streams = new ArrayList<InputStream>();
        for (final File file : fileSystem.listFiles("/", Boolean.FALSE)) {
            streams.add(new FileInputStream(file));
        }
        return streams;
    }

    /**
     * Open a number of output streams for a session.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @param count
     *            A count of the number of streams to open.
     * @return A list of output streams.
     * @throws IOException
     */
    List<OutputStream> openOutputStreams(final String sessionId,
            final Integer count) throws IOException {
        return Collections.emptyList();
    }

    /**
     * Start the stream server.
     * 
     * @throws IOException
     */
    void start() throws IOException {
        fileServer.start();
        socketServer.start();
    }

    /**
     * Stop the stream server.
     *
     */
    void stop() {
        socketServer.stop();
        fileServer.stop();
    }

    /** A stream server session. */
    final class Session {
        Long bandwidth;
        final Direction direction;
        final String id;
        private Session(final Direction direction) {
            super();
            this.bandwidth = 0L;
            this.direction = direction;
            this.id = MD5Util.md5Hex(String.valueOf(System.currentTimeMillis()).getBytes());
        }
    }
}
