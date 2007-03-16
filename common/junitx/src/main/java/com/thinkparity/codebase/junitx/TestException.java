/*
 * Created On:  15-Mar-07 4:58:17 PM
 */
package com.thinkparity.codebase.junitx;

/**
 * <b>Title:</b>thinkParity JUnit eXtensions Test Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class TestException extends RuntimeException {

    /**
     * Create TestException.
     *
     */
    public TestException() {
        super();
    }

    /**
     * Create TestException.
     * 
     * @param message
     *            An exception message <code>String</code>.
     */
    public TestException(final String message) {
        super(message);
    }

    /**
     * Create TestException.
     *
     * @param message
     *            An exception message <code>String</code>.
     * @param cause
     *            A <code>Throwable</code> cause.
     */
    public TestException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Create TestException.
     * 
     * @param cause
     *            A <code>Throwable</code> cause.
     */
    public TestException(final Throwable cause) {
        super(cause);
    }
}
