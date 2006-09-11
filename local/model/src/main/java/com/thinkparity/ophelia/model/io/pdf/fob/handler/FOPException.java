/*
 * Mar 3, 2006
 */
package com.thinkparity.ophelia.model.io.pdf.fob.handler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class FOPException extends RuntimeException {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a FOPException.
	 * 
	 * @param message
	 *            The error message.
	 */
	public FOPException(final String message) { super(message); }

	/**
	 * Create a FOPException.
	 * 
	 * @param cause
	 *            The cause of the error.
	 */
	public FOPException(final Throwable cause) { super(cause); }
}
