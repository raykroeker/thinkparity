/*
 * Feb 18, 2005
 */
package com.thinkparity.model.parity;

/**
 * ParityException
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityException extends Exception {

	private static final long serialVersionUID = -1;

	/**
	 * Create a ParityException
	 */
	public ParityException() { super(); }

	/**
	 * Create a ParityException
	 * @param message <code>String</code>
	 */
	public ParityException(String message) { super(message); }

	/**
	 * Create a ParityException
	 * @param message <code>String</code>
	 * @param cause <code>Throwable</code>
	 */
	public ParityException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a ParityException
	 * @param cause <code>Throwable</code>
	 */
	public ParityException(Throwable cause) { super(cause); }
}
