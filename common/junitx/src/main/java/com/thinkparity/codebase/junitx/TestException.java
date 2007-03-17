/*
 * Created On:  15-Mar-07 4:58:17 PM
 */
package com.thinkparity.codebase.junitx;

import java.text.MessageFormat;

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

    /**
     * Create TestException.
     * 
     * @param cause
     *            A <code>Throwable</code> cause.
     * @param message
     *            An exception message <code>String</code>.
     * @param arguments
     *            An exception message's <code>Object[]</code> arguments.
     */
    public TestException(final Throwable cause, final String message,
            final Object... arguments) {
        super(MessageFormat.format(message, arguments), cause);
    }
}
