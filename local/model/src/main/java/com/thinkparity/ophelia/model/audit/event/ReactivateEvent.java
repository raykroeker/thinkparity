/*
 * Created On: Feb 21, 2006
 * $Id$
 */
package com.thinkparity.ophelia.model.audit.event;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.audit.AuditEventType;

/**
 * This event is recorded when a document is received.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReactivateEvent extends AuditVersionEvent {

    /** By whom the document was reactivated. */
	private User reactivatedBy;

	/** Create  ReceiveEvent. */
	public ReactivateEvent() { super(AuditEventType.REACTIVATE); }

	/**
     * Obtain the received from user.
     *
	 * @return A user.
	 */
	public User getReactivatedBy() { return reactivatedBy; }

	/**
     * Set the reactivated by user.
     *
	 * @param reactivatedBy A user.
	 */
	public void setReactivatedBy(final User reactivatedBy) {
		this.reactivatedBy = reactivatedBy;
	}
}
