/*
 * Created On:  26-Oct-06 6:22:07 PM
 */
package com.thinkparity.codebase.model.stream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <b>Title:</b>thinkParity Stream Reader<br>
 * <b>Description:</b>A stream client used to download streams from the server.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.8
 */
public final class StreamReader extends StreamClient {

    /**
     * Create StreamReader.
     * 
     * @param monitor
     *            A <code>StreamMonitor</code>.
     * @param session
     *            A <code>StreamSession</code>.
     */
    public StreamReader(final StreamMonitor monitor, final StreamSession session) {
        super(Type.DOWNSTREAM, monitor, session);
    }

    /**
     * Create StreamReader.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     */
    public StreamReader(final StreamSession session) {
        super(Type.DOWNSTREAM, session);
    }

    /**
     * Close the reader.
     *
     */
    public void close() throws IOException {
        disconnect();
    }

    /**
     * Open the reader.
     *
     */
    public void open() throws IOException {
        connect();
    }

    /**
     * Read a stream into the stream.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @param stream
     *            A target <code>OutputStream</code>.
     */
    public void read(final String streamId, final OutputStream stream) {
        read(streamId, stream, 0L);
    }

    /**
     * Read a stream.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @param stream
     *            A target <code>OutputStream</code>.
     * @param streamOffset
     *            The offset <code>Long</code> at which to start reading.
     */
    public void read(final String streamId, final OutputStream stream,
            final Long streamOffset) {
        initializeRead(streamId, streamOffset);
        read(stream);
    }
}
