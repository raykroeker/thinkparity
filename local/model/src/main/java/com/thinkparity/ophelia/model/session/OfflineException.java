/*
 * Created On:  2-Apr-07 1:48:20 PM
 */
package com.thinkparity.ophelia.model.session;

import com.thinkparity.codebase.model.ThinkParityException;

/**
 * <b>Title:</b>thinkParity OpheliaModel Offline Exception<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class OfflineException extends ThinkParityException {

    /** Exception message <code>String</code>. */
    private static final String MESSAGE;

    static {
        MESSAGE = "User session is offline.";
    }

    /**
     * Create OfflineException.
     *
     */
    public OfflineException() {
        super(MESSAGE);
    }
}
