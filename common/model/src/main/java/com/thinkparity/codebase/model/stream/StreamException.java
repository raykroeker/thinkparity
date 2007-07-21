/*
 * Created On: Oct 27, 2006 10:01
 */
package com.thinkparity.codebase.model.stream;

import java.text.MessageFormat;

/**
 * <b>Title:</b>thinkParity Stream Exception<br>
 * <b>Description:</b>An exception identifying an error during a stream reader
 * or writer's read or write operation.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class StreamException extends RuntimeException {

    /**
     * A <code>boolean</code> flag indicating whether or not the client can
     * recover from this error.
     */
    private final Boolean recoverable;

    /**
     * Create StreamException.
     * 
     * @param cause
     *            The cause of the error.
     */
    StreamException(final Boolean recoverable, final String messagePattern,
            final Object... messageArguments) {
        super(MessageFormat.format(messagePattern, messageArguments));
        this.recoverable = recoverable;
    }

    /**
     * Create StreamException.
     * 
     * @param cause
     *            The cause of the error.
     */
    StreamException(final Boolean recoverable, final Throwable cause) {
        super(cause);
        this.recoverable = recoverable;
    }

    /**
     * Create StreamException.
     * 
     * @param cause
     *            The cause of the error.
     */
    StreamException(final String messagePattern,
            final Object... messageArguments) {
        super(MessageFormat.format(messagePattern, messageArguments));
        this.recoverable = Boolean.FALSE;
    }

    /**
     * Create StreamException.
     * 
     * @param cause
     *            The cause of the error.
     */
    StreamException(final Throwable cause) {
        super(cause);
        this.recoverable = Boolean.FALSE;
    }

    /**
     * Determine whether or not the client can recover from this error.
     * 
     * @return True if the client can recover from this error.
     */
    public Boolean isRecoverable() {
        return recoverable;
    }
}
