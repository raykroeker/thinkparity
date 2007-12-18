/*
 * Created On:  2-Aug-07 9:13:26 AM
 */
package com.thinkparity.codebase.model.artifact;

/**
 * <b>Title:</b>thinkParity Common Model Illegal Version Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class IllegalVersionException extends Exception {

    /** The message. */
    private static final String MESSAGE;

    static {
        MESSAGE = "The version provided must be the latest version.";
    }

    /**
     * Create IllegalVersionException.
     *
     */
    public IllegalVersionException() {
        super(MESSAGE);
    }
}
