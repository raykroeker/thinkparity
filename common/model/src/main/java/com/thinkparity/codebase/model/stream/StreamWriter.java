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
     */
    public void close() throws IOException {
        waitForWriteCompletion();
        disconnect();
    }

    /**
	 * Determine whether or not the stream writer is open.
	 * 
	 * @return True if the stream writer is open.
	 */
    public Boolean isOpen() {
    	return super.isOpen();
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
     * @param streamId
     *            A stream id <code>String</code>.
     * @param stream
     *            An <code>InputStream</code>.
     * @param streamSize
     *            The stream size <code>Long</code>.
     */
    public void write(final String streamId, final InputStream stream,
            final Long streamSize) {
        write(streamId, stream, streamSize, 0L);
    }

    /**
     * Write a stream.
     * 
     * @param streamId
     *            A stream id <code>String</code>.
     * @param stream
     *            An <code>InputStream</code>.
     * @param streamSize
     *            The stream size <code>Long</code>.
     * @param streamOffset
     *            The relative offset <code>Long</code> within the stream to
     *            begin writing.
     */
    public void write(final String streamId, final InputStream stream,
            final Long streamSize, final Long streamOffset) {
        initializeWrite(streamId, streamSize, streamOffset);
        write(stream, streamSize);
    }
}
