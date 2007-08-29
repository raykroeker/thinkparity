/*
 * Created On:  20-Aug-07 1:52:08 PM
 */
package com.thinkparity.network.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * <b>Title:</b>thinkParity Network Input Stream<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NetworkInputStream extends RecordingInputStream {

    /** A connection id. */
    private final String connectionId;

    /** An enabled flag. */
    private boolean enabled;

    /**
     * Create NetworkInputStream.
     * 
     * @param socketInputStream
     *            An <code>InputStream</code>.
     */
    public NetworkInputStream(final String connectionId,
            final InputStream socketInputStream) throws IOException {
        super(socketInputStream);
        this.connectionId = connectionId;
        this.enabled = true;
    }

    /**
     * @see java.io.FilterInputStream#available()
     *
     */
    @Override
    public int available() throws IOException {
        ensureEnabled();
        return super.available();
    }

    /**
     * @see java.io.FilterInputStream#close()
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
     * Obtain the connection id.
     *
     * @return A <code>String</code>.
     */
    public String getConnectionId() {
        return connectionId;
    }

    /**
     * @see com.thinkparity.network.io.RecordingInputStream#getLastRead()
     *
     */
    @Override
    public synchronized Long getLastRead() {
        ensureEnabled();
        return super.getLastRead();
    }

    /**
     * @see com.thinkparity.network.io.RecordingInputStream#getRecord()
     *
     */
    @Override
    public synchronized Long getRecord() {
        ensureEnabled();
        return super.getRecord();
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
     * @see java.io.FilterInputStream#mark(int)
     *
     */
    @Override
    public synchronized void mark(final int readlimit) {
        ensureEnabled();
        super.mark(readlimit);
    }

    /**
     * @see java.io.FilterInputStream#markSupported()
     *
     */
    @Override
    public boolean markSupported() {
        ensureEnabled();
        return super.markSupported();
    }

    /**
     * @see com.thinkparity.network.io.RecordingInputStream#read()
     *
     */
    @Override
    public int read() throws IOException {
        ensureEnabled();
        return super.read();
    }

    /**
     * @see com.thinkparity.network.io.RecordingInputStream#read(byte[])
     *
     */
    @Override
    public int read(final byte[] b) throws IOException {
        ensureEnabled();
        return super.read(b);
    }

    /**
     * @see com.thinkparity.network.io.RecordingInputStream#read(byte[], int, int)
     *
     */
    @Override
    public int read(final byte[] b, final int off, final int len)
            throws IOException {
        ensureEnabled();
        return super.read(b, off, len);
    }

    /**
     * @see java.io.FilterInputStream#reset()
     *
     */
    @Override
    public synchronized void reset() throws IOException {
        ensureEnabled();
        super.reset();
    }

    /**
     * @see com.thinkparity.network.io.RecordingInputStream#resetRecord()
     *
     */
    @Override
    public synchronized void resetRecord() {
        ensureEnabled();
        super.resetRecord();
    }

    /**
     * @see com.thinkparity.network.io.RecordingInputStream#skip(long)
     *
     */
    @Override
    public long skip(final long n) throws IOException {
        ensureEnabled();
        return super.skip(n);
    }

    /**
     * Ensure the stream is enabled; throwing an illegal argument error if not.
     * 
     */
    private void ensureEnabled() {
        if (false == enabled) {
            throw new IllegalStateException("Stream is disabled.");
        }
    }
}
