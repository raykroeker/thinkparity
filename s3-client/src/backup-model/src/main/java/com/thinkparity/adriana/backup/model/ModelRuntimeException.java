/*
 * Created On:  29-Sep-07 6:00:08 PM
 */
package com.thinkparity.adriana.backup.model;

import java.text.MessageFormat;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ModelRuntimeException extends RuntimeException {

    /**
     * Create ModelRuntimeException.
     * 
     * @param code
     *            A <code>Code</code>.
     * @param arguments
     *            An <code>Object[]</code>.
     */
    public ModelRuntimeException(final Code code, final Object... arguments) {
        super(code.getMessage(arguments));
    }

    /**
     * Create ModelRuntimeException.
     * 
     * @param code
     *            A <code>Code</code>.
     * @param cause
     *            A <code>Throwable</code>.
     */
    public ModelRuntimeException(final Code code, final Throwable cause) {
        super(code.getMessage(), cause);
    }

    /** <b>Title:</b>Model Runtime Exception Code<br> */
    public enum Code {

        CANNOT_CREATE_TEMP_DIRECTORY("Cannot create temp directory \"{0}.\""),
        MODEL_INSTANTIATION("Could not instantiate model."),
        PANIC("An unexpected error has occured."),
        TEMP_DIRECTORY_EXISTS("Temp directory \"{0}\" already exists."),
        TEMP_FILE_EXISTS("Temp file \"{0}\" already exists.");

        /** A message. */
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
         * Obtain the message.
         * 
         * @return A <code>String</code>.
         */
        private String getMessage() {
            return message;
        }

        /**
         * Obtain the message.
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
