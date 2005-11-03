/*
 * Nov 2, 2005
 */
package com.thinkparity.model.parity.model.io.xml;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XmlIOConverterException extends RuntimeException {

	private static final long serialVersionUID = 1;

	/**
	 * Create a XmlIOConverterException.
	 */
	XmlIOConverterException() { super(); }

	/**
	 * Create a XmlIOConverterException.
	 * @param message
	 */
	XmlIOConverterException(String message) { super(message); }

	/**
	 * Create a XmlIOConverterException.
	 * @param message
	 * @param cause
	 */
	XmlIOConverterException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a XmlIOConverterException.
	 * @param cause
	 */
	XmlIOConverterException(Throwable cause) {
		super(cause);
	}
}
