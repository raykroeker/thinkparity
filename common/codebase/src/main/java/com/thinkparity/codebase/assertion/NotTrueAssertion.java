/*
 * Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * NotTrueAssertion
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class NotTrueAssertion extends RuntimeException {

	/**
	 * Required when inheriting from <code>java.lang.Exception</code>
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a NotTrueAssertion
	 */
	private NotTrueAssertion() { super(); }

	/**
	 * Create a NotTrueAssertion
	 * 
	 * @param message
	 *            <code>java.lang.String</code>
	 */
	NotTrueAssertion(final String message) { super(message); }
}
