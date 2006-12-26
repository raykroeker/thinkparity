/*
 * Created On: Oct 22, 2006 3:46:10 PM
 */
package com.thinkparity.codebase.model.stream;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class StreamWriter extends StreamClient {

    /**
     * Create StreamWriter.
     * 
     * @param session
     *            A stream <code>Session</code>.
     */
    public StreamWriter(final StreamMonitor monitor, final StreamSession session) {
        super(Type.UPSTREAM, monitor, session);
    }

    /**
     * Create StreamWriter.
     * 
     * @param session
     *            A stream <code>Session</code>.
     */
    public StreamWriter(final StreamSession session) {
        super(Type.UPSTREAM, session);
    }

    /**
     * Close the stream writer.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        disconnect();
    }

    /**
     * Open the stream writer.
     * 
     */
    public void open() throws IOException {
        connect();
    }

    /**
     * Write a stream.
     * 
     * @param monitor
     *            A progress monitor.
     * @param streamId
     *            A stream id <code>String</code>.
     * @param stream
     *            An <code>InputStream</code>.
     * @param streamSize
     *            The size of the stream in bytes.
     */
    public void write(final String streamId, final InputStream stream,
            final Long streamSize) {
        write(streamId, stream, streamSize, 0L);
    }


    /**
     * Write a stream.
     * 
     * @param monitor
     *            A progress monitor.
     * @param streamId
     *            A stream id <code>String</code>.
     * @param stream
     *            An <code>InputStream</code>.
     * @param streamSize
     *            The size of the stream in bytes.
     */
    public void write(final String streamId, final InputStream stream,
            final Long streamSize, final Long streamOffset) {
        write(new StreamHeader(StreamHeader.Type.STREAM_ID, streamId));
        write(new StreamHeader(StreamHeader.Type.STREAM_OFFSET, String.valueOf(streamOffset)));
        write(new StreamHeader(StreamHeader.Type.STREAM_SIZE, String.valueOf(streamSize)));
        write(stream, streamSize);
    }
}
