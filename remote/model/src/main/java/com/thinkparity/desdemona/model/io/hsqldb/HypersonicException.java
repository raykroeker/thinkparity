/*
 * Feb 8, 2006
 */
package com.thinkparity.desdemona.model.io.hsqldb;

import java.text.MessageFormat;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Hypersonic Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class HypersonicException extends RuntimeException {

	/**
     * Create HypersonicException.
     * 
     * @param message
     *            An error message.
     */
	public HypersonicException(final String message) {
        super(message);
	}

    /**
     * Create HypersonicException.
     * 
     * @param message
     *            An error message.
     * @param messageArguments
     *            An error message arguments.
     */
    public HypersonicException(final String message,
            final Object... messageArguments) {
        super(new MessageFormat(message).format(messageArguments));
    }

    /**
	 * Create a HypersonicException.
	 * 
	 * @param cause
	 *            The cause of the error.
	 */
	public HypersonicException(final Throwable cause) {
        super(cause);
    }

    /**
     * Create HypersonicException.
     * 
     * @param cause
     *            An error cause.
     * @param message
     *            An error message.
     * @param messageArguments
     *            An error message arguments.
     */
    public HypersonicException(final Throwable cause, final String message,
            final Object... messageArguments) {
        super(new MessageFormat(message).format(messageArguments), cause);
    }
}
