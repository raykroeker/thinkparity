/*
 * Created On: Sun Oct 22 2006 12:31 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.text.MessageFormat;

import com.thinkparity.codebase.FileSystem;

import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class StreamFileServer {

    /** The file server's <code>FileSystem</code>. */
    private final FileSystem fileSystem;

    /**
     * Create StreamFileServer.
     * 
     * @param streamServer
     *            A handle to the <code>StreamServer</code>.
     * @param root
     *            The file server's root directory.
     */
    StreamFileServer(final StreamServer streamServer, final File root) {
        super();
        this.fileSystem = new FileSystem(root);
    }

    /**
     * Invalidate a stream session.
     * 
     * @param streamSession
     *            A <code>StreamSession</code>.
     */
    void invalidate(final StreamSession streamSession) {
    }

    /**
     * Initialize a stream session.
     * 
     * @param streamSession
     *            A <code>StreamSession</code>.
     */
    void initialize(final StreamSession streamSession) {
        final String path = resolvePath(streamSession);
        if (null == fileSystem.findDirectory(path))
            fileSystem.createDirectory(resolvePath(streamSession));
    }

    /**
     * Read the workspace for a stream session.
     * 
     * @param session
     *            A stream server <code>Session</code>.
     * @return A workspace <code>FileSystem</code>.
     */
    FileSystem readWorkspace(final StreamSession session) {
        return fileSystem.cloneChild(resolvePath(session));
    }

    /**
     * Start the stream file server.
     *
     */
    void start() {}

    /**
     * Stop the stream file server.
     *
     */
    void stop() {}

    /**
     * Resolve a file system path for a stream session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @return A <code>FileSystem</code> path <code>String</code>.
     */
    private String resolvePath(final StreamSession session) {
        return MessageFormat.format("/{0}", session.getId());
    }
}
