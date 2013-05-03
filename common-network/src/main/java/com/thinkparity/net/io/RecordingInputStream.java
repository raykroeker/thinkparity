/*
 * Created On:  19-Aug-07 2:43:10 PM
 */
package com.thinkparity.net.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * <b>Title:</b>thinkParity Network IO Recording Input Stream<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RecordingInputStream extends ProxyInputStream {

    /** A byte record. */
    private long record;

    /** The last read. */
    private long lastRead;

    /**
     * Create RecordingInputStream.
     * 
     * @param record
     *            An <code>InputStream</code>.
     */
    public RecordingInputStream(final InputStream record) {
        super(record);
        this.lastRead = System.currentTimeMillis();
    }

    /**
     * Obtain the byte record.
     * 
     * @return A <code>Long</code>.
     */
    public synchronized Long getRecord() {
        return Long.valueOf(record);
    }

    /**
     * Obtain the read record.
     * 
     * @return A <code>Long</code>.
     */
    public synchronized Long getLastRead() {
        return Long.valueOf(lastRead);
    }

    /**
     * @see java.io.FilterInputStream#read()
     *
     */
    @Override
    public int read() throws IOException {
        final int read = super.read();
        record += read > 0 ? read : 0;
        lastRead = System.currentTimeMillis();
        return read;
    }

    /**
     * @see java.io.FilterInputStream#read(byte[])
     *
     */
    @Override
    public int read(final byte[] b) throws IOException {
        final int read = super.read(b);
        record += read > 0 ? read : 0;
        lastRead = System.currentTimeMillis();
        return read;
    }

    /**
     * @see java.io.FilterInputStream#read(byte[], int, int)
     *
     */
    @Override
    public int read(final byte[] b, final int off, final int len)
            throws IOException {
        final int read = super.read(b, off, len);
        record += read > 0 ? read : 0;
        lastRead = System.currentTimeMillis();
        return read;
    }

    /**
     * Reset the record returning the original record.
     * 
     * @return A <code>Long</code>.
     */
    public synchronized void resetRecord() {
        this.lastRead = System.currentTimeMillis();
        this.record = 0;
    }

    /**
     * @see java.io.FilterInputStream#skip(long)
     *
     */
    @Override
    public long skip(final long n) throws IOException {
        final long skipped = super.skip(n);
        record += skipped > 0 ? skipped : 0;
        lastRead = System.currentTimeMillis();
        return skipped;
    }
}
