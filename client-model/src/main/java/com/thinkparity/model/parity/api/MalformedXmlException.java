/*
 * Apr 26, 2005
 */
package com.thinkparity.model.parity.api;

/**
 * MalformedXmlException
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class MalformedXmlException extends RuntimeException {

	/**
	 * Required when overriding an exception.
	 */
	private static final long serialVersionUID = 1;
	
	/**
	 * Create a new MalformedXmlException
	 */
	public MalformedXmlException() { super(); }

	/**
	 * Create a new MalformedXmlException
	 * 
	 * @param message
	 *            <code>java.lang.String</code>
	 */
	public MalformedXmlException(String message) { super(message); }

	/**
	 * Create a new MalformedXmlException
	 * 
	 * @param message
	 *            <code>java.lang.String</code>
	 * @param cause
	 *            <code>java.lang.Throwable</code>
	 */
	public MalformedXmlException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Create a new MalformedXmlException
	 * @param cause <code>java.lang.Throwable</code>
	 */
	public MalformedXmlException(Throwable cause) {
		super(cause);
	}
}
