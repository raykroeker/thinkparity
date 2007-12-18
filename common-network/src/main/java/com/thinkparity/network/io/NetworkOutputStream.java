/*
 * Created On:  20-Aug-07 1:53:33 PM
 */
package com.thinkparity.network.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <b>Title:</b>thinkParity Network Output Stream<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NetworkOutputStream extends RecordingOutputStream {

    /** The connection id. */
    private final String connectionId;

    /** An enabled flag. */
    private boolean enabled;

    /**
     * Create NetworkOutputStream.
     * 
     * @param socketOutputStream
     *            An <code>OutputStream</code>.
     */
    public NetworkOutputStream(final String connectionId,
            final OutputStream socketOutputStream) throws IOException {
        super(socketOutputStream);
        this.connectionId = connectionId;
        this.enabled = true;
    }

    /**
     * @see java.io.FilterOutputStream#close()
     *
     */
    @Override
    public void close() throws IOException {
        ensureEnabled();
        super.close();
    }

    /**
     * Disable the output stream.
     * 
     */
    public void disable() {
        ensureEnabled();
        enabled = false;
    }

    /**
     * Enable the output stream.
     * 
     */
    public void enable() {
        if (enabled) {
            throw new IllegalStateException();
        }
        enabled = true;
    }

    /**
     * @see java.io.FilterOutputStream#flush()
     *
     */
    @Override
    public void flush() throws IOException {
        ensureEnabled();
        super.flush();
    }

    /**
     * Obtain the connection id.
     *
     * @return A <code>String</code>.
     */
    public String getConnectionId() {
        return connectionId;
    }

    /**
     * Determine if the network stream is enabled.
     * 
     * @return True if it is enabled.
     */
    public Boolean isEnabled() {
        return Boolean.valueOf(enabled);
    }

    /**
     * @see com.thinkparity.network.io.RecordingOutputStream#write(byte[])
     *
     */
    @Override
    public void write(final byte[] b) throws IOException {
        ensureEnabled();
        super.write(b);
    }

    /**
     * @see com.thinkparity.network.io.RecordingOutputStream#write(byte[], int, int)
     *
     */
    @Override
    public void write(final byte[] b, final int off, final int len) throws IOException {
        ensureEnabled();
        super.write(b, off, len);
    }

    /**
     * @see com.thinkparity.network.io.RecordingOutputStream#write(int)
     *
     */
    @Override
    public void write(final int b) throws IOException {
        ensureEnabled();
        super.write(b);
    }

    /**
     * Ensure the stream is enabled; throwing an illegal state error if not.
     * 
     */
    private void ensureEnabled() {
        if (false == enabled) {
            throw new IllegalStateException("Stream is disabled.");
        }
    }
}
