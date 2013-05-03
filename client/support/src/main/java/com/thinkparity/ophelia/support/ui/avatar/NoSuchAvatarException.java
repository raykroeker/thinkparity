/*
 * Created On:  Nov 19, 2007 9:58:00 AM
 */
package com.thinkparity.ophelia.support.ui.avatar;

import java.text.MessageFormat;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NoSuchAvatarException extends RuntimeException {

    /** A message format pattern. */
    private static final String PATTERN;

    static {
        PATTERN = "Action \"{0}\" does not exist.";
    }

    /**
     * Format the message.
     * 
     * @param actionClass
     *            A <code>Class<?></code>.
     * @return A <code>String</code>.
     */
    private static String format(final Class<?> actionClass) {
        return MessageFormat.format(PATTERN, actionClass);
    }

    /**
     * Create NoSuchActionException.
     *
     */
    NoSuchAvatarException(final Class<?> actionClass) {
        super(format(actionClass));
    }
}
