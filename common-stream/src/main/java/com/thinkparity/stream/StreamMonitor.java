/*
 * Created On:  7-Nov-06 3:49:17 PM
 */
package com.thinkparity.stream;

/**
 * <b>Title:</b>thinkParity Stream Monitor<br>
 * <b>Description:</b>A stream monitor is notified by the core stream
 * manipulation implementation of the chunks as they are sent and received as
 * well as headers and errors.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface StreamMonitor {

    /**
     * A chunk of the stream was received.
     * 
     * @param chunkSize
     *            The <code>int</code> size of the chunk.
     */
    public void chunkReceived(final int chunkSize);

    /**
     * A chunk of the stream was sent.
     * 
     * @param chunkSize
     *            The <code>int</code> size of the chunk.
     */
    public void chunkSent(final int chunkSize);

    /**
     * Obtain the stream monitor name.
     * 
     * @return A stream monitor name <code>String</code>.
     */
    public String getName();

    /**
     * Reset the stream monitor.
     * 
     */
    public void reset();
}
