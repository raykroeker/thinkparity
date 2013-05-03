/*
 * Created On: Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * <b>Title:</b>thinkParity Common Not Of Type Assertion<br>
 * <b>Description:</b>A type has been determined to be of an incorrect
 * hierarchy.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NotOfTypeAssertion extends Assertion {

	/**
     * Create NotOfTypeAssertion.
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
