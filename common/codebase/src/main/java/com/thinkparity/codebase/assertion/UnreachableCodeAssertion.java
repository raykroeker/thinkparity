/*
 * Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * UnreachableCodeAssertion
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UnreachableCodeAssertion extends RuntimeException {

	/**
	 * Required when extending <code>java.lang.Exception</code>
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a new UnreachableCodeAssertion
	 */
	private UnreachableCodeAssertion() { super(); }

	/**
	 * Create a new UnreachableCodeAssertion
	 * 
	 * @param message
	 *            <code>java.lang.String</code>
	 */
	UnreachableCodeAssertion(String message) { super(message); }
}
