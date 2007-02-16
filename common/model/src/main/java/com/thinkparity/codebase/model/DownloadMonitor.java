/*
 * Created On:  21-Dec-06 2:37:29 PM
 */
package com.thinkparity.codebase.model;

/**
 * <b>Title:</b>thinkParity Download Monitor<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface DownloadMonitor {

    /**
     * A chunk of the stream was downloaded.
     * 
     * @param chunkSize
     *            The <code>int</code> size of the chunk.
     */
    public void chunkDownloaded(final int chunkSize);
}
