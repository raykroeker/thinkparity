/*
 * Feb 8, 2006
 */
package com.thinkparity.model.io.hsqldb;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HypersonicException extends RuntimeException {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a HypersonicException.
	 * 
	 * @param cause
	 *            The cause of the error.
	 */
	public HypersonicException(final Throwable cause) { super(cause); }

	/**
	 * Create a HypersonicException.
	 * @param message The error message.
	 */
	public HypersonicException(final String message) { super(message); }
}
