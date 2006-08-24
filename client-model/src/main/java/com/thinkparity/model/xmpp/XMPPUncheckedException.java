/*
 * Created On: Aug 24, 2006 8:30:53 AM
 */
package com.thinkparity.model.xmpp;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class XMPPUncheckedException extends RuntimeException {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /**
     * Create XMPPUncheckedException.
     * 
     * @param message
     *            An error message.
     * @param cause
     *            The cause of the error.
     */
    XMPPUncheckedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
