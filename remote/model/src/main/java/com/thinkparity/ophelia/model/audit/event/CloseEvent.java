/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.audit.event;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.audit.AuditEventType;

/**
 * This event is recorded when an artifact is closed.
 *
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class CloseEvent extends AuditEvent {

	/** By whom the artifact was closed. */
	private User closedBy;

	/** Create CloseEvent. */
	public CloseEvent() { super(AuditEventType.CLOSE); }

	/**
     * Obtain the closed by user.
     *
	 * @return A user.
	 */
	public User getClosedBy() { return closedBy; }

	/**
     * Set the closed by user.
     *
	 * @param closedBy
     *      A user.
	 */
	public void setClosedBy(final User closedBy) { this.closedBy = closedBy; }
}
