/*
 * Created On:  29-Sep-07 3:54:30 PM
 */
package com.thinkparity.adriana.backup.ui;

import java.text.MessageFormat;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NoSuchCommandException extends Exception {

    /** A message pattern. */
    private static final String MESSAGE_PATTERN;

    static {
        MESSAGE_PATTERN = "The command \"{0}\" does not exist.";
    }

    /**
     * Create NoSuchCommandException.
     * 
     * @param command
     *            A <code>String</code>.
     */
    public NoSuchCommandException(final String command) {
        super(MessageFormat.format(MESSAGE_PATTERN, command));
    }
}
