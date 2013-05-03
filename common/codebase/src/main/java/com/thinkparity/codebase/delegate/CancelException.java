/*
 * Created On:  12-Aug-07 11:37:19 AM
 */
package com.thinkparity.codebase.delegate;

/**
 * <b>Title:</b>thinkParity Codebase Cancelable Delegate Cancel Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CancelException extends Exception {

    /**
     * Create CancelException.
     * 
     * @param message
     *            A <code>String</code>.
     */
    public CancelException(final String message) {
        super(message);
    }

    /**
     * Create CancelException.
     * 
     * @param cause
     *            The cause <code>Throwable</code>.
     */
    public CancelException(final Throwable cause) {
        super(cause);
    }
}
