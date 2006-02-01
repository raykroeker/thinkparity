/*
 * Nov 15, 2005
 */
package com.thinkparity.model.xmpp;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XMPPConverterException extends RuntimeException {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a XMPPConverterException.
	 */
	public XMPPConverterException() { super(); }

	/**
	 * Create a XMPPConverterException.
	 * @param message
	 */
	public XMPPConverterException(final String message) { super(message); }

	/**
	 * Create a XMPPConverterException.
	 * @param message
	 * @param cause
	 */
	public XMPPConverterException(final String message, final Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a XMPPConverterException.
	 * @param cause
	 */
	public XMPPConverterException(final Throwable cause) { super(cause); }
}
