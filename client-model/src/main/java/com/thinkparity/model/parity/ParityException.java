/*
 * Created On: Jul 13, 2006 10:00:05 AM
 */
package com.thinkparity.model.parity;

/**
 * <b>Title:</b>thinkParity Unchecked Exception <br>
 * <b>Description:</b>An unchecked thinkParity error.
 * 
 * @author raymond@thinkparity.com
 * @version 1.1
 */
public class ParityException extends Exception {

    /** @see java.io.Serializable */
	private static final long serialVersionUID = 1;

	/**
     * Create ParityException.
     * 
     * @param cause
     *            The cause of the error.
     */
	ParityException(final Throwable cause) {
		super(cause);
		fillInStackTrace();
	}
}
