/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CloseEvent extends AuditEvent {

	/**
	 * Whom the artifact was closed by.
	 * 
	 */
	private JabberId closedBy;

	/**
	 * Create a CloseEvent.
	 * 
	 */
	public CloseEvent() { super(); }

	/**
	 * @return Returns the closedBy.
	 */
	public JabberId getClosedBy() { return closedBy; }

	/**
	 * @param closedBy The closedBy to set.
	 */
	public void setClosedBy(final JabberId closedBy) {
		this.closedBy = closedBy;
	}
}
