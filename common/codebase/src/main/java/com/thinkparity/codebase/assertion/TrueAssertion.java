/*
 * Created On: Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public class TrueAssertion extends Assertion {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

	/**
     * Create NotTrueAssertion.
     * 
     * @param message
     *            An assertion message.
     */
	TrueAssertion(final String message) { super(message); }

	/** Create TrueAssertion. */
	private TrueAssertion() { super(); }
}
