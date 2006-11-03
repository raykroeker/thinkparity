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
public final class UpstreamHandler implements Runnable {
    
    private final InputStream input;

    /** An apache logger wrapper. */
    private final Log4JWrapper logger;

    private final String streamId;

    /** The <code>StreamServer</code>. */
    private final StreamServer streamServer;

    private final StreamSession streamSession;

    private final Long streamSize;

    /**
     * Create UpstreamSocketHandler.
     * 
     * @param streamServer
     *            The <code>StreamServer</code>.
     * @param socketChannel
     *            The client <code>SocketChannel</code>.
     */
    UpstreamHandler(final StreamServer streamServer,
            final StreamSession streamSession, final String streamId,
            final Long streamSize, final InputStream input) {
        super();
        this.logger = new Log4JWrapper();
        this.streamServer = streamServer;
        this.streamSession = streamSession;
        this.streamId = streamId;
        this.streamSize = streamSize;
        this.input = input;
    }

    /**
     * Run the upstream socket handler. This will read the socket chanel and
     * look for key headers to initiate a upstream file trasfer.
     * 
     * 
     */
    public void run() {
        logger.logApiId();
        logger.logVariable("streamSize", streamSize);
        try {
            final OutputStream output =
                streamServer.openOutputStream(streamSession, streamId);

            int len, total = 0;
            final byte[] b = new byte[streamSession.getBufferSize()];
            try {
                while((len = input.read(b)) > 0) {
                    logger.logDebug("UPSTREAM RECEIVED {0}/{1}", total += len, streamSize);
                    output.write(b, 0, len);
                    output.flush();
                }
            } finally {
                output.flush();
                output.close();
            }
        } catch (final IOException iox) {
            throw new StreamException(iox);
        }
    }
}
