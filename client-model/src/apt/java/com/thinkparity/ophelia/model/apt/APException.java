/*
 * Created On:  11-Jan-07 8:53:51 PM
 */
package com.thinkparity.ophelia.model.apt;

import java.text.MessageFormat;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class APException extends RuntimeException {

    /**
     * Create APException.
     *
     */
    public APException() {
        super();
    }

    /**
     * Create APException.
     *
     * @param message
     */
    public APException(final String message) {
        super(message);
    }

    /**
     * Create APException.
     *
     * @param cause
     */
    public APException(final Throwable cause) {
        super();
    }

    /**
     * Create APException.
     *
     * @param message
     * @param cause
     */
    public APException(final Throwable cause, final String message,
            final Object... messageArguments) {
        super(MessageFormat.format(message, messageArguments), cause);
    }
}
