/*
 * Created On: Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * <b>Title:</b>thinkParity Common Null Pointer Assertion<br>
 * <b>Description:</b>An evaluation of an object reference has incorrectly
 * resolved to null.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NullPointerAssertion extends Assertion {

	/**
     * Create NullPointerAssertion.
     * 
     * @param message
     *            A message <code>String</code>.
     */
	NullPointerAssertion(final String message) {
        super(message);
    }
}
