/*
 * Created On: Apr 27, 2005
 */
package com.thinkparity.bootstrap.assertion;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
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
