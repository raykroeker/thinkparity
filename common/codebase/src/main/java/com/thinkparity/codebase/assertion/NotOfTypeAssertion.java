/*
 * Created On: Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public class NotOfTypeAssertion extends Assertion {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

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

	/** Create NotTrueAssertion. */
	private NotOfTypeAssertion() { super(); }
}
