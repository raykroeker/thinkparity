/*
 * Created On: Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public class NotNullPointerAssertion extends Assertion {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

	/**
	 * Create a NullPointerAssertion
	 * @param message <code>java.lang.String</code>
	 */
	NotNullPointerAssertion(final String message) { super(message); }

	/** Create NotNullPointerAssertion. */
	private NotNullPointerAssertion() { super(); }
}
