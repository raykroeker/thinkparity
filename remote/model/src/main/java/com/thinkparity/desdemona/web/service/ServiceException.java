/*
 * Created On:  2-Jun-07 10:21:34 AM
 */
package com.thinkparity.desdemona.web.service;

import java.text.MessageFormat;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServiceException extends RuntimeException {

    /**
     * Create ServiceException.
     * 
     * @param message
     *            An error message <code>String</code>.
     */
    ServiceException(final String message) {
        super(message);
    }

    /**
     * Create ServiceException.
     * 
     * @param pattern
     *            An error message pattern <code>String</code>.
     * @param arguments
     *            An error message arguments.
     */
    ServiceException(final String pattern, final Object... arguments) {
        super(MessageFormat.format(pattern, arguments));
    }

    /**
     * Create ServiceException.
     * 
     * @param pattern
     *            An error message pattern <code>String</code>.
     * @param arguments
     *            An error message arguments.
     */
    ServiceException(final Throwable cause, final String pattern,
            final Object... arguments) {
        super(MessageFormat.format(pattern, arguments), cause);
    }

    /**
     * Create ServiceException.
     * 
     * @param cause
     *            An error cause <code>Throwable</code>.
     */
    ServiceException(final Throwable cause) {
        super(cause);
    }
}
