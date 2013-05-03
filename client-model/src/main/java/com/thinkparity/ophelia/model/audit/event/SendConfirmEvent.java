/*
 * Created On: Apr 8, 2006
 * $Id$
 */
package com.thinkparity.ophelia.model.audit.event;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.audit.AuditEventType;

/**
 * An event triggered for UserX when UserX sends a document to UserY; and UserY
 * sends a confirmation back to UserX.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendConfirmEvent extends AuditVersionEvent {

    /** Send confirmed by. */
    private User confirmedBy;

    /** Create SendConfirmEvent. */
    public SendConfirmEvent() { super(AuditEventType.SEND_CONFIRM); }

    /**
     * Obtain the confirmed by user.
     * 
     * @return A user.
     */
    public User getConfirmedBy() { return confirmedBy; }

    /**
     * Set the confirmed by user.
     * 
     * @param confirmedBy
     *            The confirmed by user.
     */
    public void setConfirmedBy(final User confirmedBy) {
        this.confirmedBy = confirmedBy;
    }
}
