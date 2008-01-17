/*
 * Created On: Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * <b>Title:</b>thinkParity Common True Assertion<br>
 * <b>Description:</b>An assertion of a conditional statement has incorrectly
 * evaluated to false.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class TrueAssertion extends Assertion {

	/**
     * Create TrueAssertion.
     * 
     * @param message
     *            A message <code>String</code>.
     */
	TrueAssertion(final String message) {
        super(message);
    }
}
