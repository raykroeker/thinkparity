/*
 * Created On:  4-Jun-07 2:36:09 PM
 */
package com.thinkparity.service.client;

import java.text.MessageFormat;

/**
 * <b>Title:</b>thinkParity Service Client Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ServiceException extends RuntimeException {

    /**
     * Create ServiceException.
     * 
     * @param pattern
     *            An error message pattern.
     * @param arguments
     *            An error message pattern arguments.
     */
    public ServiceException(final String pattern, final Object...arguments) {
        super(MessageFormat.format(pattern, arguments));
    }

    /**
     * Create ServiceException.
     *
     * @param cause
     */
    public ServiceException(final Throwable cause) {
        super(cause);
    }
}
