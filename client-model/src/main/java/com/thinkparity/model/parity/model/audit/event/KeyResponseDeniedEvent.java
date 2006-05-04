/*
 * Created On: Mar 21, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.xmpp.user.User;

/**
 * This event is recorded when a key request is denied.
 *
 * @author raykroeker@gmail.com
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
