/*
 * Feb 5, 2005
 */
package com.thinkparity.ophelia.model.util.smack;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SmackException extends RuntimeException {

	/**
     * Create SmackException.
     * 
     * @param message
     *            The error message <code>String</code>.
     * @param cause
     *            The error cause <code>Throwable</code>.
     */
    SmackException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /** Create SmackException */
	private SmackException() { super(); }
}
