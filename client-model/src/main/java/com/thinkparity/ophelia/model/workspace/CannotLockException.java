/*
 * Created On:  13-Feb-07 7:52:34 PM
 */
package com.thinkparity.ophelia.model.workspace;

import java.text.MessageFormat;

/**
 * <b>Title:</b>thinkParity OpheliaModel Cannot Lock Exception<br>
 * <b>Description:</b>Thrown when an exclusive lock cannot be obtained on the
 * workspace.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CannotLockException extends Exception {

    /** Exception message format pattern <code>String</code>. */
    private static final String PATTERN;

    static {
        PATTERN = "Cannot obtain exclusive lock for workspace {0}.";
    }

    /**
     * Create CannotLockException.
     *
     */
    public CannotLockException(final String workspace) {
        super(MessageFormat.format(PATTERN, workspace));
    }
}
