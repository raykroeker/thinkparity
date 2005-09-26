/*
 * Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * NullPointerAssertion
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class NullPointerAssertion extends RuntimeException {

	/**
	 * Required when inheriting from <code>java.lang.Exception</code>
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a NullPointerAssertion
	 */
	private NullPointerAssertion() { super(); }

	/**
	 * Create a NullPointerAssertion
	 * @param message <code>java.lang.String</code>
	 */
	NullPointerAssertion(final String message) { super(message); }
}
