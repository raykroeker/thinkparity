/*
 * Created On: Fri May 26 2006 16:16 PDT
 * $Id$
 */
package com.thinkparity.browser;

/**
 * A browser error.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class BrowserException extends RuntimeException {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /**
     * Create BrowserException.
     *
     * @param message
     *      The error message.
     * @param cause
     *      The cause of the error.
     */
    public BrowserException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
