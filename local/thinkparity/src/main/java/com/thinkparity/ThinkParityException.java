/*
 * Created On: May 25, 2006 11:00:48 PM
 * $Id$
 */
package com.thinkparity;

/**
 * A thinkParity error.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public final class ThinkParityException extends RuntimeException {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /**
     * Create ThinkParityException.
     * 
     * @param message
     *            The error message.
     * @param cause
     *            The cause of the error.
     */
    public ThinkParityException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
