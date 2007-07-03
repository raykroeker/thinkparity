/*
 * Created On:  19-Jun-07 8:18:51 PM
 */
package com.thinkparity.amazon.s3.service.client;

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
     * @param cause
     */
    public ServiceException(final String message, final Object... arguments) {
        super(MessageFormat.format(message, arguments));
    }

    /**
     * Create ServiceException.
     *
     * @param message
     * @param cause
     */
    public ServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ServiceException(final Throwable cause) {
        super(cause);
    }
}
