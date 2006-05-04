/*
 * Created On: Mar 3, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class RequestKeyEvent extends AuditEvent {

    /** By whom the key was requested. */
	private User requestedBy;

    /** From whom the key was requested. */
	private User requestedFrom;

	/** Create RequestKeyEvent. */
	public RequestKeyEvent() { super(AuditEventType.REQUEST_KEY); }

	/**
     * Obtain the requested by user.
     *
	 * @return A user.
	 */
	public User getRequestedBy() { return requestedBy; }

	/**
     * Obtain the requested from user.
     *
	 * @return A user.
	 */
	public User getRequestedFrom() { return requestedFrom; }

	/**
     * Set the requested by user.
     *
	 * @param requestedBy A user.
	 */
	public void setRequestedBy(final User requestedBy) {
		this.requestedBy = requestedBy;
	}

	/**
     * Set the requested from user.
     *
	 * @param requestedFrom A user.
	 */
	public void setRequestedFrom(final User requestedFrom) {
		this.requestedFrom = requestedFrom;
	}
}
