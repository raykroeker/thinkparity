/*
 * Apr 16, 2006
 */
package com.thinkparity.ophelia.model.workspace;

import java.io.IOException;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class WorkspaceException extends RuntimeException {

    /** @see java.io.Serializable */
    private static final long serialVersionUID = 1;

    /**
     * Translate an io error into a workspace error.
     * 
     * @param message
     *            The error message.
     * @param iox
     *            The io error.
     */
    static WorkspaceException translate(final String message,
            final IOException iox) {
        return new WorkspaceException(message, iox);
    }

    /**
     * Create a WorkspaceException.
     * 
     * @param message
     *            The error message.
     * @param cause
     *            The error cause.
     */
    private WorkspaceException(final String message, final IOException cause) {
        super(message, cause);
    }
}
