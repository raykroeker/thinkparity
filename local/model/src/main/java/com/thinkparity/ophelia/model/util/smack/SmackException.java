/*
 * Feb 5, 2005
 */
package com.thinkparity.ophelia.model.util.smack;

import org.jivesoftware.smack.packet.XMPPError;

/**
 * SmackException
 * This class is used as a wrapper class for any exceptions that are thrown by
 * the Smack library.
 * @author raykroeker@gmail.com
 * @version 1.3
 */
public class SmackException extends Exception {

	static final long serialVersionUID = 1;

	/**
	 * Create a SmackException
	 */
	SmackException() { super(); }

	/**
	 * Create a SmackException
	 * @param string <code>String</code>
	 */
	public SmackException(String string) { super(string); }

	/**
	 * Create a SmackException
	 * @param string <code>String</code>
	 * @param throwable <code>Throwable</code>
	 */
	public SmackException(String string, Throwable throwable) {
		super(string, throwable);
	}

	/**
	 * Create a SmackException
	 * @param throwable <code>Throwable</code>
	 */
	public SmackException(Throwable throwable) { super(throwable); }

	/**
	 * Create a SmackException.
	 * 
	 * @param xmppError
	 *            The network xmpp error causing the exception.
	 */
	public SmackException(final XMPPError xmppError) {
		this(new StringBuffer(xmppError.getCode())
				.append(":  ")
				.append(xmppError.getMessage()).toString());
	}
}
