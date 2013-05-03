/*
 * Created On:  19-Aug-07 2:43:10 PM
 */
package com.thinkparity.network.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * <b>Title:</b>thinkParity Network IO Recording Output Stream<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RecordingOutputStream extends ProxyOutputStream {

    /** A byte record. */
    private long record;

    /** A write time. */
    private long lastWrite;

    /**
     * Create RecordingOutputStream.
     * 
     * @param record
     *            An <code>OutputStream</code>.
     */
    public RecordingOutputStream(final OutputStream record) {
        super(record);
        this.lastWrite = System.currentTimeMillis();
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
     * Obtain the write record.
     * 
     * @return A <code>Long</code>.
     */
    public synchronized Long getLastWrite() {
        return Long.valueOf(lastWrite);
    }

    /**
     * Reset the record returning the original record.
     * 
     * @return A <code>Long</code>.
     */
    public synchronized void resetRecord() {
        this.lastWrite = System.currentTimeMillis();
        this.record = 0;
    }

    /**
     * @see java.io.FilterOutputStream#write(byte[])
     *
     */
    @Override
    public void write(final byte[] b) throws IOException {
        super.write(b);
        record += b.length;
        lastWrite = System.currentTimeMillis();
    }

    /**
     * @see java.io.FilterOutputStream#write(byte[], int, int)
     *
     */
    @Override
    public void write(final byte[] b, final int off, final int len)
            throws IOException {
        super.write(b, off, len);
        record += len;
        lastWrite = System.currentTimeMillis();
    }

    /**
     * @see java.io.FilterOutputStream#write(int)
     *
     */
    @Override
    public void write(final int b) throws IOException {
        super.write(b);
        record++;
        lastWrite = System.currentTimeMillis();
    }
}
