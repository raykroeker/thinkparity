/*
 * Created On:  16-Aug-07 4:27:44 PM
 */
package com.thinkparity.codebase.model.stream;

/**
 * <b>Title:</b>thinkParity Common Model Stream Retry Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface StreamRetryHandler {

    /**
     * Determine whether or not an attempt should be made to retry a stream
     * upload/download.
     * 
     * @return True if a retry should be attempted.
     */
    Boolean retry();
}
