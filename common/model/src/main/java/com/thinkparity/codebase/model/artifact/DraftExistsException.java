/*
 * Created On:  27-Jun-07 4:07:32 PM
 */
package com.thinkparity.codebase.model.artifact;

/**
 * <b>Title:</b>thinkParity Common Model Draft Exists Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DraftExistsException extends Exception {

    /** The message. */
    private static final String MESSAGE;

    static {
        MESSAGE = "A draft for the given artifact already exists.";
    }

    /**
     * Create DraftExistsException.
     *
     */
    public DraftExistsException() {
        super(MESSAGE);
    }
}
