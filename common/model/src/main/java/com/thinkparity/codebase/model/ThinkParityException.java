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
public final class ThinkParityException extends RuntimeException {

	/**
     * Create ParityException.
     * 
     * @param cause
     *            The cause of the error.
     */
	public ThinkParityException(final String message, final Throwable cause) {
		super(message, cause);
		fillInStackTrace();
	}
}
