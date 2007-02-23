/**
 * 
 */
package com.thinkparity.desdemona.model.stream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.thinkparity.codebase.model.stream.StreamSession;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Downstream Handler<br>
 * <b>Description:</b>A runnable that will open a stream from the stream server
 * and deliver it to an output stream provided in the constructor.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DownstreamHandler extends AbstractStreamHandler implements
        Runnable {
    
    /** The <code>OutputStream</code> to download. */
    private final OutputStream output;

    /**
     * Create DownstreamHandler.
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
     *            The size <code>Long</code> of the stream.
     * @param output
     *            The <code>OutputStream</code> to download.
     */
    DownstreamHandler(final StreamServer streamServer,
            final StreamSession streamSession, final String streamId,
            final Long streamOffset, final Long streamSize,
            final OutputStream output) {
        super(streamServer, streamSession, streamId, streamOffset, streamSize);
        this.output = output;
    }

    /**
     * Run the downstream socket handler. This will read the socket chanel and
     * look for key headers to initiate a upstream file trasfer.
     * 
     * 
     */
    public void run() {
        LOGGER.logDebug("{0} Begin downstream session.", streamId);
        try {
            final InputStream input =
                streamServer.openForDownstream(session, streamId, streamOffset);
            int len;
            long total = streamOffset.longValue();
            final byte[] b = new byte[session.getBufferSize()];
            try {
                /* NOTE we're using > 0 instead of != -1 because the stream will
                 * become empty before it ends */
                while (total < streamSize) {
                    len = input.read(b);
                    output.write(b, 0, len);
                    output.flush();
                    LOGGER.logDebug("{0}  Downstream bytes sent:  {1}/{2}",
                            streamId, total += len, streamSize);
                }
            } finally {
                try {
                    input.close();
                } finally {
                    try {
                        output.flush();
                    } finally {
                        output.close();
                    }
                }
            }
            LOGGER.logDebug("{0}  Downstream session complete.", streamId);
        } catch (final IOException iox) {
            throw new StreamException(iox);
        }
    }
}
