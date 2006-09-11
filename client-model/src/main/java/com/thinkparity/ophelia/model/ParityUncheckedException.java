/*
 * Created On: Feb 18, 2005
 */
package com.thinkparity.ophelia.model;

/**
 * <b>Title:</b>thinkParity Exception <br>
 * <b>Description:</b>This error is thrown by the thinkParity model api.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityUncheckedException extends RuntimeException {

    /** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	/**
     * Create ParityException.
     * 
     * @param cause
     *            The cause of the error.
     */
	ParityUncheckedException(final String message, final Throwable cause) {
		super(message, cause);
		fillInStackTrace();
	}
}
