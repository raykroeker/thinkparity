/*
 * Created On:  7-Nov-06 3:49:17 PM
 */
package com.thinkparity.codebase.model.stream;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface StreamMonitor {
    public void chunkSent(final int chunkSize);
    public void chunkReceived(final int chunkSize);
    public void headerSent(final String header);
    public void headerReceived(final String header);
}
