/*
 * Feb 18, 2005
 */
package com.thinkparity.model.parity;

import java.io.Serializable;

/**
 * ParityException
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ParityException extends Exception {

	/**
	 * @see Serializable
	 */
	private static final long serialVersionUID = -1;

	/**
	 * Create a ParityException
	 * @param cause <code>Throwable</code>
	 */
	ParityException(final Throwable cause) {
		super(cause);
		fillInStackTrace();
	}
}
