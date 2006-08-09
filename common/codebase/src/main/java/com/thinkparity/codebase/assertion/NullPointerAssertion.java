/*
 * Created On: Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public class NullPointerAssertion extends Assertion {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

	/**
     * Create NullPointerAssertion.
     * 
     * @param message
     *            An assertion message.
     */
	NullPointerAssertion(final String message) { super(message); }

	/** Create NullPointerAssertion. */
	private NullPointerAssertion() { super(); }
}
