/*
 * Created On: Sun Oct 22 2006 12:31 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.text.MessageFormat;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class StreamFileServer {

    private static final String UPSTREAM_PATH_EXTENSION;

    static {
        UPSTREAM_PATH_EXTENSION = "part";
    }

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
     * Destroy a stream.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     */
    void destroy(final String streamId) {
        final File downstreamFile = find(resolveDownstreamPath(streamId));
        final File upstreamFile = find(resolveUpstreamPath(streamId));
        boolean didDestroyDownstream = true;
        if (null != downstreamFile)
            didDestroyDownstream = downstreamFile.delete();
        boolean didDestroyUpstream = true;
        if (null != upstreamFile)
            didDestroyUpstream = upstreamFile.delete();

        if (!didDestroyDownstream && !didDestroyUpstream) {
            Assert.assertTrue(didDestroyUpstream, "Could not destroy files {0},{1}.",
                    downstreamFile, upstreamFile);
        } else {
            if (!didDestroyDownstream)
                Assert.assertTrue(didDestroyDownstream, "Could not destroy file {0}.", downstreamFile);
            if (!didDestroyUpstream)
                Assert.assertTrue(didDestroyUpstream, "Could not destroy file {0}.", upstreamFile);
        }
    }

    /**
     * Finalize a stream. Resolve the upstream file and rename it such that it
     * is suitable for download.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     */
    void finalizeStream(final String streamId) {
        final String upstreamPath = resolveUpstreamPath(streamId);
        final String downstreamPath = resolveDownstreamPath(streamId);
        final File file = fileSystem.rename(upstreamPath, downstreamPath);
        Assert.assertTrue(file.setReadOnly(),
                "Cannot mark file as read-only {0}.", file);
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
    File findForDownstream(final String streamId) {
        final File streamFile = find(resolveDownstreamPath(streamId));
        if (null == streamFile) {
            logger.logWarning("Could not locate downstream file {0}.", streamId);
        }
        return streamFile;
    }

    /**
     * Find the file for a stream for an upstream transaction. The upstream file
     * has a slightly different name than the downstream file so that we can
     * ensure a complete upload before download.  If the file does not exist it
     * will be created.
     * 
     * @param session
     *            A stream server <code>Session</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     * @return A <code>File</code>.
     */
    File findForUpstream(final String streamId) {
        File upstreamFile = fileSystem.find(resolveUpstreamPath(streamId));
        if (null == upstreamFile) {
            try {
                upstreamFile = fileSystem.createFile(resolveUpstreamPath(
                        streamId));
            } catch (final IOException iox) {
                throw new StreamException(iox);
            }
        }
        return upstreamFile;
    }

    /**
     * Initialize the file server for a stream.
     * 
     */
    void initialize(final String streamId) {
    }

    /**
     * Log statistics regarding the stream file server.
     *
     */
    void logStatistics() {
        logger.logInfo("Streaming file server root:{0}", fileSystem.getRoot());
        logger.logInfo("Streaming file server incomplete upstream file count:{0}", fileSystem.list("/", new FileFilter() {
            public boolean accept(final File pathname) {
                return pathname.getName().endsWith(UPSTREAM_PATH_EXTENSION);
            }
        }, Boolean.TRUE).length);
        logger.logInfo("Streaming file server complete file count:{0}", fileSystem.list("/", new FileFilter() {
            public boolean accept(final File pathname) {
                return !pathname.getName().endsWith(UPSTREAM_PATH_EXTENSION);
            }
        }, Boolean.TRUE).length);
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
     * Find a file for a given path.
     * 
     * @param path
     *            A path <code>String</code>.
     * @return A <code>File</code> or null if the file does not exist.
     */
    private File find(final String path) {
        return fileSystem.find(path);
    }

    /**
     * Resolve a file system path for a stream.
     * 
     * @return A <code>FileSystem</code> path <code>String</code>.
     */
    private String resolveDownstreamPath(final String streamId) {
        return MessageFormat.format("/{0}", streamId);
    }

    /**
     * Resolve a file system path for an upstream file.
     * 
     * @return A <code>FileSystem</code> path <code>String</code>.
     */
    private String resolveUpstreamPath(final String streamId) {
        return MessageFormat.format("/{0}.{1}", streamId, UPSTREAM_PATH_EXTENSION);
    }
}
