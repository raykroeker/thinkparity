/*
 * Created On:  16-Aug-07 12:22:22 PM
 */
package com.thinkparity.ophelia.model.util.service;

/**
 * <b>Title:</b>thinkParity Ophelia Model Service Client Retry Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface ServiceRetryHandler {

    /**
     * Determine service should be retried.
     * 
     * @return True if the service call should be retried.
     */
    Boolean retry();
}
