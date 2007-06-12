/*
 * Created On:  7-Nov-06 3:49:17 PM
 */
package com.thinkparity.codebase.model.queue.notification;

/**
 * <b>Title:</b>thinkParity Stream Monitor<br>
 * <b>Description:</b>A stream monitor is notified by the core stream
 * manipulation implementation of the chunks as they are sent and received as
 * well as headers and errors.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface NotificationMonitor {

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
     * A header was received.
     * 
     * @param header
     *            A <code>String</code> header.
     */
    public void headerReceived(final String header);

    /**
     * A header was sent.
     * 
     * @param header
     *            A <code>String</code> header.
     */
    public void headerSent(final String header);

    /**
     * A notification error has occured.
     * 
     * @param error
     *            A <code>NotificationException</code>.
     */
    public void streamError(final NotificationException error);
}
