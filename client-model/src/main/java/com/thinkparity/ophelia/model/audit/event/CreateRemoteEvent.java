/*
 * Created On: Created On: Thu May 04 2006 09:14 PDT
 * $Id$
 */
package com.thinkparity.ophelia.model.audit.event;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.audit.AuditEventType;

/**
 * This event is recorded when an artifact is created based upon a remote
 * event.
 *
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class CreateRemoteEvent extends AuditEvent {

    /** From whom the creation was received. */
    private User receivedFrom;

	/** Create CreateRemoteEvent. */
	public CreateRemoteEvent() {
        super(AuditEventType.CREATE_REMOTE);
    }

    /**
     * Obtain the recieved from user.
     *
     * @return A user.
     */
    public User getReceivedFrom() { return receivedFrom; }

    /**
     * Set the received from user.
     *
     * @param receivedFrom
     *      A user.
     */
    public void setReceivedFrom(final User receivedFrom) {
        this.receivedFrom = receivedFrom;
    }
}
