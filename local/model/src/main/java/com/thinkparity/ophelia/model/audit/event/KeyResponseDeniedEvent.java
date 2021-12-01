/*
 * Created On: Mar 21, 2006
 * $Id$
 */
package com.thinkparity.ophelia.model.audit.event;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.audit.AuditEventType;

/**
 * This event is recorded when a key request is denied.
 *
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class KeyResponseDeniedEvent extends AuditEvent {

    /** From whom the key request was issued. */
	private User requestedBy;

	/** Create KeyResponseDeniedEvent. */
	public KeyResponseDeniedEvent() { 
        super(AuditEventType.KEY_RESPONSE_DENIED);
    }

	/**
     * Obtain the requested by user.
     *
	 * @return A user.
	 */
	public User getRequestedBy() { return requestedBy; }

	/**
     * Set the requested by user.
     *
	 * @param requestedBy A user.
	 */
	public void setRequestedBy(final User requestedBy) {
        this.requestedBy = requestedBy;
	}
}
