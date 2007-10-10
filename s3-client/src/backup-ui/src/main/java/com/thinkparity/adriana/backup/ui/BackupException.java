/*
 * Created On:  29-Sep-07 3:54:30 PM
 */
package com.thinkparity.adriana.backup.ui;

import java.text.MessageFormat;

/**
 * <b>Title:</b>thinkParity Backup Client Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class BackupException extends Exception {

    /**
     * Create BackupException.
     * 
     * @param code
     *            A <code>Code</code>.
     * @param arguments
     *            An <code>Object[]</code>.
     */
    public BackupException(final Code code, final Object... arguments) {
        super(code.getMessage(arguments));
    }

    /**
     * Create BackupException.
     *
     */
    public BackupException(final Code code, final Throwable cause) {
        super(code.getMessage(), cause);
    }

    /**
     * Create BackupException.
     *
     */
    public BackupException(final Code code, final Throwable cause,
            final Object... arguments) {
        super(code.getMessage(arguments), cause);
    }

    /** <b>Title:</b>Backup Exception Code<br> */
    public enum Code {

        INVALID_URI_SYNTAX("Invalid resource uri syntax {0}."),
        LOAD_PROPERTIES_FAILED("Could not load backup properties."),
        UNSUPPORTED_RESOURCE_SCHEME("Could not backup resource of type {0}.");

        /** An error message. */
        private final String message;

        /**
         * Create Code.
         * 
         * @param message
         *            A <code>String</code>.
         */
        private Code(final String message) {
            this.message = message;
        }

        /**
         * Obtain the error message.
         * 
         * @return A <code>String</code>.
         */
        private String getMessage() {
            return message;
        }

        /**
         * Obtain the error message.
         * 
         * @param arguments
         *            An <code>Object[]</code>.
         * @return A <code>String</code>.
         */
        private String getMessage(final Object... arguments) {
            return MessageFormat.format(message, arguments);
        }
    }
}
