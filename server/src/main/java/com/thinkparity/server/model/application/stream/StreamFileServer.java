/*
 * Created On: Sun Oct 22 2006 12:31 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.text.MessageFormat;

import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.model.stream.Direction;

import com.thinkparity.desdemona.model.stream.StreamServer.Session;

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
     * @param root
     *            The file server's root directory.
     */
    StreamFileServer(final File root) {
        super();
        this.fileSystem = new FileSystem(root);
    }

    /**
     * Create a workspace for a stream session.
     * 
     * @param session
     *            A stream server <code>Session</code>.
     */
    void createWorkspace(final Session session) {
        createDirectory(session.direction, session.id);
    }

    /**
     * Read the workspace for a stream session.
     * 
     * @param session
     *            A stream server <code>Session</code>.
     * @return A workspace <code>FileSystem</code>.
     */
    FileSystem readWorkspace(final Session session) {
        return fileSystem.cloneChild(resolvePath(session.direction, session.id));
    }

    /**
     * Start the stream file server.
     *
     */
    void start() {
        for (final Direction direction : Direction.values()) {
            createDirectory(direction);
        }
    }

    /**
     * Stop the stream file server.
     *
     */
    void stop() {}

    /**
     * Create a directory for a stream direction.
     * 
     * @param direction
     *            A stream <code>Direction</code>.
     * @return A directory <code>File</code>.
     */
    private File createDirectory(final Direction direction) {
        return fileSystem.createDirectory(resolvePath(direction));
    }

    /**
     * Create a directory for a direction child.
     * 
     * @param direction
     *            A stream <code>Direction</code>.
     * @param child
     *            A child path.
     * @return A directory <code>File</code>.
     */
    private File createDirectory(final Direction direction, final String childPath) {
        return fileSystem.createDirectory(resolvePath(direction, childPath));
    }

    private String resolvePath(final Direction direction) {
        return MessageFormat.format("/{0}", direction.toString().toLowerCase());
    }
    private String resolvePath(final Direction direction, final String childPath) {
        return MessageFormat.format("/{0}/{1}",
                direction.toString().toLowerCase(), childPath);
    }
}
