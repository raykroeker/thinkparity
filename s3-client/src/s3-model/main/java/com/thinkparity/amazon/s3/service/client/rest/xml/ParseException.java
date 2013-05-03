/*
 * Created On:  20-Jun-07 10:55:55 AM
 */
package com.thinkparity.amazon.s3.service.client.rest.xml;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ParseException extends Exception {

    /**
     * Create ParserException.
     *
     * @param message
     * @param cause
     */
    public ParseException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Create ParserException.
     *
     * @param message
     * @param cause
     */
    public ParseException(final Throwable cause) {
        super(cause);
    }
}
