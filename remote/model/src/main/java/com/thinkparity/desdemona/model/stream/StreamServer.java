/*
 * Created On: Sun Oct 22 2006 10:41 PDT
 */
package com.thinkparity.desdemona.model.stream;

import java.io.File;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

import com.thinkparity.codebase.log4j.Log4JWrapper;
import com.thinkparity.codebase.nio.ChannelUtil;

import com.thinkparity.codebase.model.session.Environment;

/**
 * <b>Title:</b>thinkParity Stream Server<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class StreamServer {

    /** The <code>StreamFileServer</code>. */
    private final StreamFileServer fileServer;

    /** An apache logger wrapper. */
    private final Log4JWrapper logger;

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
    }

    /**
     * Destroy a stream.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     */
    void destroy(final String streamId) {
        fileServer.destroy(streamId);
    }

    /**
     * Determine if a stream exists.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @return True if the stream exists; ie it can be downloaded.
     */
    Boolean doesExist(final String streamId) {
        final File downstreamFile = fileServer.findForDownstream(streamId);
        if (null != downstreamFile && downstreamFile.exists()) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Finalize the persistence of the stream.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     */
    void finalizeStream(final String streamId) {
        fileServer.finalizeStream(streamId);
    }

    /**
     * Obtain the size of a stream.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @return The size of the stream in bytes.
     */
    Long getDownstreamSize(final String streamId) {
        return fileServer.findForDownstream(streamId).length();
    }

    /**
     * Initialize the stream server for a given stream.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     */
    void initialize(final String streamId) {
        fileServer.initialize(streamId);
    }

    /**
     * Open a channel from persistence to be read.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @return A <code>ReadableByteChannel</code>.
     * @throws IOException
     */
    ReadableByteChannel openForDownstream(final String streamId) throws IOException {
        final File downstreamFile = fileServer.findForDownstream(streamId);
        return ChannelUtil.openReadChannel(downstreamFile);
    }

    /**
     * Open a stream for persistence to be written to.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @return A <code>WritableByteChannel</code>.
     * @throws IOException
     */
    WritableByteChannel openForUpstream(final String streamId) throws IOException {
        final File upstreamFile = fileServer.findForUpstream(streamId);
        return ChannelUtil.openWriteChannel(upstreamFile);
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
    }

    /**
     * Stop the stream server.
     * 
     * @param wait
     *            Whether or not to wait for connected users to disconnect.
     */
    void stop(final Boolean wait) {
        fileServer.stop();
    }
}
