/*
 * Jun 6, 2005
 */
package com.thinkparity.model.smackx;

/**
 * UnsupportedXTypeException
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UnsupportedXTypeException extends Exception {

	private static final long serialVersionUID = 1;

	/**
	 * Message for the exception.
	 */
	private static final String message =
		"Unsupported packet extension type:  ";

	/**
	 * Create a UnsupportedXTypeException
	 * @param type
	 */
	public UnsupportedXTypeException(final Object type) {
		this(new StringBuffer(message).append(type.getClass().getName()));
	}

	private UnsupportedXTypeException(final StringBuffer message) {
		super(message.toString());
	}
}
