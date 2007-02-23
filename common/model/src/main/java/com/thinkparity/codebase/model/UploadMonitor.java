/*
 * Created On:  21-Dec-06 2:37:29 PM
 */
package com.thinkparity.codebase.model;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface UploadMonitor {
    public void chunkUploaded(final int chunkSize);
}
