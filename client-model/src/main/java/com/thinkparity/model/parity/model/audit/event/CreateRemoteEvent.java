/*
 * Created On: Created On: Thu May 04 2006 09:14 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.xmpp.user.User;

/**
 * This event is recorded when an artifact is created based upon a remote
 * event.
 *
 * @author raykroeker@gmail.com
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
