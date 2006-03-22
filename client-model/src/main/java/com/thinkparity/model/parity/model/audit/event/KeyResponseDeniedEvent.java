/*
 * Mar 21, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class KeyResponseDeniedEvent extends AuditEvent {

	private JabberId requestedBy;

	/**
	 * Create a KeyResponseDeniedEvent.
	 */
	public KeyResponseDeniedEvent() { super(); }

	/**
	 * @return Returns the requestedBy.
	 */
	public JabberId getRequestedBy() {
		return requestedBy;
	}

	/**
	 * @param requestedBy The requestedBy to set.
	 */
	public void setRequestedBy(JabberId requestedBy) {
		this.requestedBy = requestedBy;
	}
}
