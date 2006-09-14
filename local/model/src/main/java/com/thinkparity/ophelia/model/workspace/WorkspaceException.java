/*
 * Apr 16, 2006
 */
package com.thinkparity.ophelia.model.workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class WorkspaceException extends RuntimeException {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /**
     * Create a WorkspaceException.
     * 
     * @param message
     *            The error message.
     * @param cause
     *            The error cause.
     */
    public WorkspaceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
