/*
 * Nov 28, 2005
 */
package com.thinkparity.model;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityServerModelException extends Exception {

	private static final long serialVersionUID = -1;

	/**
	 * Create a ParityServerModelException.
	 */
	public ParityServerModelException() { super(); }

	/**
	 * Create a ParityServerModelException.
	 * @param message
	 */
	public ParityServerModelException(String message) { super(message); }

	/**
	 * Create a ParityServerModelException.
	 * @param message
	 * @param cause
	 */
	public ParityServerModelException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a ParityServerModelException.
	 * @param cause
	 */
	public ParityServerModelException(Throwable cause) {
		super(cause);
	}
}
