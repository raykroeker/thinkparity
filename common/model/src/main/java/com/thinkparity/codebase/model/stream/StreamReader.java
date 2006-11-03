/*
 * Created On:  26-Oct-06 6:22:07 PM
 */
package com.thinkparity.codebase.model.stream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class StreamReader extends StreamClient {

    /**
     * Create StreamReader.
     * 
     * @param session
     *            A <code>StreamSession</code>.
     */
    public StreamReader(StreamSession session) {
        super(session);
    }

    /**
     * Open the reader.
     *
     */
    public void open() throws IOException {
        connect(Type.DOWNSTREAM);
    }

    /**
     * Close the reader.
     *
     */
    public void close() throws IOException {
        disconnect();
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
        write(new StreamHeader(StreamHeader.Type.STREAM_ID, streamId));
        read(stream);
    }
}
