/**
 * 
 */
package com.thinkparity.desdemona.model.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DownstreamHandler implements Runnable {
    
    /** An apache logger wrapper. */
    private final Log4JWrapper logger;

    /** The downstream <code>OutputStream</code>. */
    private final OutputStream output;

    /** The stream id <code>String<code>. */
    private final String streamId;

    /** The <code>StreamServer</code>. */
    private final StreamServer streamServer;

    /** The <code>StreamSession</code>. */
    private final StreamSession streamSession;

    /**
     * Create DownstreamSocketHandler.
     * 
     * @param streamServer
     *            A <code>StreamServer</code>.
     * @param streamSession
     *            A <code>StreamSession</code>.
     * @param streamId
     *            A stream id <code>String</code>.
     * @param output
     *            The downstream <code>OutputStream</code>.
     */
    DownstreamHandler(final StreamServer streamServer,
            final StreamSession streamSession, final String streamId,
            final OutputStream output) {
        super();
        this.logger = new Log4JWrapper();
        this.output = output;
        this.streamServer = streamServer;
        this.streamSession = streamSession;
        this.streamId = streamId;
    }


    /**
     * Run the downstream socket handler. This will read the socket chanel and
     * look for key headers to initiate a upstream file trasfer.
     * 
     * 
     */
    public void run() {
        logger.logApiId();
        try {
            doRun();
        } catch (final Throwable t) {
            throw new StreamException(t);
        }
    }

    /**
     * Download a stream to the downstream output stream.
     * 
     * @throws IOException
     */
    private void doRun() throws IOException {
        final Long total = streamServer.getSize(streamSession, streamId);
        final InputStream stream =
            streamServer.openInputStream(streamSession, streamId);

        int len;
        final byte[] b = new byte[streamSession.getBufferSize()];
        try {
            while((len = stream.read(b)) > 0) {
                logger.logDebug("{0}/{1}", len, total);
                output.write(b, 0, len);
                output.flush();
            }
        } finally {
            stream.close();
            output.flush();
            output.close();
        }
    }
}
