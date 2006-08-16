/*
 * Created On: Aug 14, 2006 11:38:01 AM
 */
package com.thinkparity.codebase.assertion;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class IndexOutOfBoundsAssertion extends Assertion {

    /** A default assertion message. */
    private static final String DEFAULT_MESSAGE =
            "Assert:  Index out of bounds.";

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

	/**
     * Create IndexOutOfBoundsAssertion.
     */
    IndexOutOfBoundsAssertion() { super(DEFAULT_MESSAGE); }

    /**
     * Create IndexOutOfBoundsAssertion.
     * 
     * @param message
     *            An assertion message.
     */
	IndexOutOfBoundsAssertion(final String message) { super(message); }
}
