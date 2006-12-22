/*
 * Created On:  21-Dec-06 2:37:29 PM
 */
package com.thinkparity.ophelia.model;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface DownloadMonitor {
    public void chunkDownloaded(final int chunkSize);
}
