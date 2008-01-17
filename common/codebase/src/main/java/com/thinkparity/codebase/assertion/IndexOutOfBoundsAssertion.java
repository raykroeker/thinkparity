/*
 * Created On: Aug 14, 2006 11:38:01 AM
 */
package com.thinkparity.codebase.assertion;

/**
 * <b>Title:</b>thinkParity Common Index Out Of Bounds Assertion<br>
 * <b>Description:</b>An index has been determined to lie outside a bound
 * range.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class IndexOutOfBoundsAssertion extends Assertion {

    /**
     * Create IndexOutOfBoundsAssertion.
     * 
     * @param message
     *            A message <code>String</code>.
     */
	IndexOutOfBoundsAssertion(final String message) {
        super(message);
    }
}
