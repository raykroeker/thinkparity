/*
 * Created On: Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public class NotYetImplementedAssertion extends Assertion {

	/** The assertion message prefix. */
	private static final String MESSAGE_PREFIX =
		"This api method has not yet been implemented:  ";

	/** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

	/**
     * Create NotTrueAssertion.
     * 
     * @param message
     *            An assertion message.
     */
	NotYetImplementedAssertion(final String message) {
		super(new StringBuffer(MESSAGE_PREFIX).append(message).toString());
	}

	/** Create NotYetImplementedAssertion. */
	private NotYetImplementedAssertion() { super(); }
}
