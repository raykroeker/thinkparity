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
