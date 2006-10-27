/**
 * 
 */
package com.thinkparity.desdemona.model.stream;

/**
 * <b>Title:</b>thinkParity StreamException<br>
 * <b>Description:</b>Used by the stream model to ease error handling within
 * the package.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class StreamException extends RuntimeException {

    /**
     * Create StreamException.
     * 
     * @param cause
     *            The cause of the error.
     */
    StreamException(final Throwable cause) {
        super(cause);
    }
}
