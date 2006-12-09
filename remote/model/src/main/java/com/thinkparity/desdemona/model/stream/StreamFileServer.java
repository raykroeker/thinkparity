/*
 * Created On: Sun Oct 22 2006 12:31 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class StreamFileServer {

    /** The file server's <code>FileSystem</code>. */
    private final FileSystem fileSystem;

    /** An apache logger wrapper. */
    private final Log4JWrapper logger;

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
        this.logger = new Log4JWrapper(getClass());
        this.fileSystem = new FileSystem(root);
    }

    /**
     * Invalidate a session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     */
    void destroy(final StreamSession session) {
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
        final File file = find(session, streamId);
        Assert.assertTrue(file.delete(), "Could not destroy stream {0}.", streamId);
    }

    /**
     * Find a file for a stream.
     * 
     * @param session
     *            A stream server <code>Session</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     * @return A workspace <code>FileSystem</code>.
     */
    File find(final StreamSession session, final String streamId) {
        final File streamFile = fileSystem.find(resolvePath(session, streamId));
        if (null == streamFile) {
            logger.logWarning("Could not locate stream {0}.", streamId);
        }
        return streamFile;
    }

    /**
     * Initialize the file server for a session.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     */
    void initialize(final StreamSession session) {
    }

    /**
     * Initialize the file server for a stream.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     *            @param streamId
     *            A stream id <code>String</code>.
     */
    void initialize(final StreamSession session, final String streamId) {
        final File streamFile = find(session, streamId);
        if (null == streamFile) {
            try {
                logger.logTrace("Initializing stream {0}.", streamId);
                fileSystem.createFile(resolvePath(session, streamId));
                logger.logTrace("Stream {0} initialized.  Resume not supported.", streamId);
            } catch (final IOException iox) {
                throw new StreamException(iox);
            }
        } else {
            logger.logTrace("Stream {0} initialized.  Resume supported.", streamId);
        }
    }

    /**
     * Log statistics regarding the stream file server.
     *
     */
    void logStatistics() {
        logger.logInfo("Streaming file server root:{0}", fileSystem.getRoot());
        logger.logInfo("Streaming file server file count:{0}", fileSystem.listFiles("/").length);
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
     * Resolve a file system path for a stream.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     * @return A <code>FileSystem</code> path <code>String</code>.
     */
    private String resolvePath(final StreamSession session,
            final String streamId) {
        return MessageFormat.format("/{0}", streamId);
    }
}
