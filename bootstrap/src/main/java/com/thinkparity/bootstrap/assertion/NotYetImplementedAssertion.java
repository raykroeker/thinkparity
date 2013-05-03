/*
 * Created On: Apr 27, 2005
 */
package com.thinkparity.bootstrap.assertion;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public final class NotYetImplementedAssertion extends Assertion {

	/** The assertion message prefix. */
	private static final String MESSAGE_PREFIX =
		"This api method has not yet been implemented:  ";

	/**
     * Create NotTrueAssertion.
     * 
     * @param message
     *            An assertion message.
     */
    NotYetImplementedAssertion(final String message) {
        super(new StringBuffer(MESSAGE_PREFIX).append(message).toString());
    }
}
