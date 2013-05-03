/*
 * Created On:  6-Jun-07 9:40:35 AM
 */
package com.thinkparity.desdemona.web;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class WebException extends RuntimeException {

    /**
     * Create WebException.
     *
     */
    WebException(final Throwable cause) {
        super(cause);
    }

    /**
     * Create WebException.
     * 
     * @param message
     *            An error message <code>String</code>.
     */
    WebException(final String message) {
        super(message);
    }
}
