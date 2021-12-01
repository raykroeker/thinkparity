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
public class KeyRequestDeniedEvent extends AuditEvent {

    /** By whom the request was denied. */
	private User deniedBy;

	/** Create KeyRequestDeniedEvent. */
	public KeyRequestDeniedEvent() { super(AuditEventType.KEY_REQUEST_DENIED); }

	/**
     * Obtain the denied by user.
     *
	 * @return A user.
	 */
	public User getDeniedBy() { return deniedBy; }

	/**
     * Set the denied by user.
     *
	 * @param deniedBy
     *      A user.
	 */
	public void setDeniedBy(final User deniedBy) { this.deniedBy = deniedBy; }
}
