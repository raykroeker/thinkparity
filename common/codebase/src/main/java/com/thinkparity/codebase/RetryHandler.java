/*
 * Created On:  20-Sep-07 1:37:00 PM
 */
package com.thinkparity.codebase;

/**
 * <b>Title:</b>thinkParity Codebase Retry Handler<br>
 * <b>Description:</b>An interface used when determining whether or not retry a
 * timeout-bound invocation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface RetryHandler {

    /**
     * Determine whether or not an attempt should be made to retry.
     * 
     * @return True if a retry should be attempted.
     */
    Boolean retry();

    /**
     * Determine the period to wait between failed attempts.
     * 
     * @return A <code>Long</code>.
     */
    Long waitPeriod();
}
