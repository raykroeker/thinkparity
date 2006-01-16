/*
 * Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * NotOfTypeAssertion
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class NotOfTypeAssertion extends RuntimeException {

	/**
	 * Required when inheriting from <code>java.lang.Exception</code>
	 */
	private static final long serialVersionUID = 1;

	/**
	 * Create a NotTrueAssertion
	 */
	private NotOfTypeAssertion() { super(); }

	/**
	 * Create a NotOfTypeAssertion.
	 * 
	 * @param message
	 *            The assertion message.
	 * @param type
	 *            The target type.
	 * @param instance
	 *            The instance.
	 */
	NotOfTypeAssertion(final String message, final Class<?> type,
			final Object instance) {
		super(message);
	}
}
