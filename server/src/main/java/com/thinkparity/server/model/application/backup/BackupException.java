/*
 * Created On:  5-Mar-07 10:00:38 AM
 */
package com.thinkparity.desdemona.model.backup;

import java.text.MessageFormat;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Backup Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class BackupException extends RuntimeException {

    /**
     * Create BackupException.
     * 
     * @param message
     *            An exception message <code>String</code>.
     * @param messageArguments
     *            An exception message arguments <code>Object[]</code>.
     */
    BackupException(final String message, final Object... messageArguments) {
        super(new MessageFormat(message).format(messageArguments));
    }

    /**
     * Create BackupException.
     * 
     * @param cause
     *            An exception cause <code>Throwable</code>.
     * @param message
     *            An exception message <code>String</code>.
     * @param messageArguments
     *            An exception message arguments <code>Object[]</code>.
     */
    BackupException(final Throwable cause, final String message,
            final Object... messageArguments) {
        super(new MessageFormat(message).format(messageArguments), cause);
    }
}
