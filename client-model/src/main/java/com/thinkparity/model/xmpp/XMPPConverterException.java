/*
 * Nov 15, 2005
 */
package com.thinkparity.model.xmpp;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XMPPConverterException extends RuntimeException {

	private static final long serialVersionUID = 1;

	/**
	 * Create a XMPPConverterException.
	 */
	public XMPPConverterException() {
		super();
	}

	/**
	 * Create a XMPPConverterException.
	 * @param message
	 */
	public XMPPConverterException(String message) {
		super(message);
	}

	/**
	 * Create a XMPPConverterException.
	 * @param message
	 * @param cause
	 */
	public XMPPConverterException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a XMPPConverterException.
	 * @param cause
	 */
	public XMPPConverterException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
