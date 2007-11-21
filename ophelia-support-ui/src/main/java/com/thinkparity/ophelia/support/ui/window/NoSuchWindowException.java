/*
 * Created On:  Nov 19, 2007 9:58:00 AM
 */
package com.thinkparity.ophelia.support.ui.window;

import java.text.MessageFormat;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NoSuchWindowException extends RuntimeException {

    /** A message format pattern. */
    private static final String PATTERN;

    static {
        PATTERN = "Window \"{0}\" does not exist.";
    }

    /**
     * Format the message.
     * 
     * @param windowClass
     *            A <code>Class<?></code>.
     * @return A <code>String</code>.
     */
    private static String format(final Class<?> windowClass) {
        return MessageFormat.format(PATTERN, windowClass);
    }

    /**
     * Create NoSuchWindowException.
     *
     */
    NoSuchWindowException(final Class<?> windowClass) {
        super(format(windowClass));
    }
}
