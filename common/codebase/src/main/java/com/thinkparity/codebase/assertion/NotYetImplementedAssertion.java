/*
 * Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * NotTrueAssertion
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class NotYetImplementedAssertion extends RuntimeException {

	/**
	 * Prefix to the assertion message.
	 */
	private static final String prefix =
		"This api method has not yet been implemented:  ";

	/**
	 * Required when inheriting from <code>java.lang.Exception</code>
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a NotTrueAssertion
	 */
	private NotYetImplementedAssertion() { super(); }

	/**
	 * Create a NotTrueAssertion
	 * 
	 * @param message
	 *            <code>java.lang.String</code>
	 */
	NotYetImplementedAssertion(final String message) {
		super(new StringBuffer(prefix).append(message).toString());
	}
}
