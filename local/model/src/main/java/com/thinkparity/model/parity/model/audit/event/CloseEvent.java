/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.xmpp.user.User;

/**
 * This event is recorded when an artifact is closed.
 *
 * @author raykroeker@gmail.com
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
