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
    public StreamWriter(final StreamSession session) {
        super(session);
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
        connect(Type.UPSTREAM);
    }

    /**
     * Write input.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @param input
     *            An <code>InputStream</code>.
     */
    public void write(final String streamId, final InputStream stream, final Long streamSize) {
        write(new StreamHeader(StreamHeader.Type.STREAM_ID, streamId));
        write(new StreamHeader(StreamHeader.Type.STREAM_SIZE, String.valueOf(streamSize)));
        write(stream, streamSize);
    }
}
