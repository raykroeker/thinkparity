/*
 * Created On: Aug 9, 2006 8:30:42 AM
 */
package com.thinkparity.codebase.assertion;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class Assertion extends RuntimeException {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /** Create Assertion. */
    protected Assertion() { super(); }

    /** Create Assertion. */
    protected Assertion(final String message) {
        super(message);
    }
}
