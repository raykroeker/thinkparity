/*
 * Created On:  14-Dec-07 3:27:51 PM
 */
package com.thinkparity.network.protocol.http;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HttpException extends Exception {

    /**
     * Create HttpException.
     * 
     * @param cause
     *            A <code>Throwable</code>.
     */
    public HttpException(final Throwable cause) {
        super(cause);
    }
}
