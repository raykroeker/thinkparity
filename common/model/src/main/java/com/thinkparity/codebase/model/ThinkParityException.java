/*
 * Created On: Feb 18, 2005
 */
package com.thinkparity.codebase.model;

/**
 * <b>Title:</b>thinkParity Exception<br>
 * <b>Description:</b>This error is thrown by the various thinkParity models
 * when nothing else can be done to correct the issue.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ThinkParityException extends RuntimeException {

	/**
     * Create ParityException.
     * 
     * @param message
     *            An exception message <code>String</code>.
     * @param cause
     *            The <code>Throwable</code> cause of the error.
     */
	public ThinkParityException(final String message, final Throwable cause) {
		super(message, cause);
		fillInStackTrace();
	}

    /**
     * Create ThinkParityException.
     * 
     * @param cause
     *            The cause <code>Throwable</code> of the error.
     */
    public ThinkParityException(final Throwable cause) {
        super(cause);
        fillInStackTrace();
    }

    /**
     * Create ThinkParityException.
     * 
     * @param message
     *            An error message <code>String</code>.
     */
    protected ThinkParityException(final String message) {
        super(message);
        fillInStackTrace();
    }
}
