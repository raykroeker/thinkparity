/**
 * 
 */
package com.thinkparity.desdemona.model.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class UpstreamHandler extends AbstractStreamHandler implements
        Runnable {
    
    /** The <code>InputStream</code> to upload. */
    private final InputStream input;

    /**
     * Create UpstreamHandler.
     * 
     * @param streamServer
     *            The <code>StreamServer</code>.
     * @param streamSession
     *            The <code>StreamSession</code>.
     * @param streamId
     *            The stream id <code>String</code>.
     * @param streamOffset
     *            The offset <code>Long</code> within the stream to upload
     *            from.
     * @param streamSize
     *            The size <code>Long</code> of the stream to upload.
     * @param input
     *            The <code>InputStream</code> to upload.
     */
    UpstreamHandler(final StreamServer streamServer,
            final StreamSession streamSession, final String streamId,
            final Long streamOffset, final Long streamSize,
            final InputStream input) {
        super(streamServer, streamSession, streamId, streamOffset, streamSize);
        this.input = input;
    }

    /**
     * Run the upstream socket handler. This will read the socket chanel and
     * look for key headers to initiate a upstream file trasfer.
     * 
     * 
     */
    public void run() {
        LOGGER.logApiId();
        LOGGER.logVariable("streamId", streamId);
        LOGGER.logVariable("streamOffset", streamOffset);
        LOGGER.logVariable("streamSize", streamSize);
        try {
            final OutputStream output =
                streamServer.openOutputStream(streamSession, streamId, streamOffset);

            int len;
            long total = streamOffset.longValue();
            final byte[] b = new byte[streamSession.getBufferSize()];
            try {
                while((len = input.read(b)) > 0) {
                    LOGGER.logDebug("Upstream recieved:  {0}/{1}", total += len, streamSize);
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
