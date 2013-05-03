/*
 * Created On: Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * <b>Title:</b>thinkParity Common Not True Assertion<br>
 * <b>Description:</b>A conditional expression has incorrectly evaluated to
 * false.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NotTrueAssertion extends Assertion {

	/**
     * Create NotTrueAssertion.
     * 
     * @param message
     *            A message <code>String</code>.
     */
	NotTrueAssertion(final String message) {
        super(message);
    }
}
