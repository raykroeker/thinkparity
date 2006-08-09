/*
 * Created On: Apr 27, 2005
 */
package com.thinkparity.codebase.assertion;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.2
 */
public class UnreachableCodeAssertion extends Assertion {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

	/**
	 * Create UnreachableCodeAssertion.
	 * 
	 * @param message An assertion messsage.
	 */
	UnreachableCodeAssertion(final String message) { super(message); }

	/** Create UnreachableCodeAssertion. */
	private UnreachableCodeAssertion() { super(); }
}
