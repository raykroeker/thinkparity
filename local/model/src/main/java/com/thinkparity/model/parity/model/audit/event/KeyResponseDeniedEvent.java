/*
 * Mar 21, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class KeyResponseDeniedEvent extends AuditEvent {

	private User requestedBy;

	/**
	 * Create a KeyResponseDeniedEvent.
	 */
	public KeyResponseDeniedEvent() { super(); }

	/**
	 * @return Returns the requestedBy.
	 */
	public User getRequestedBy() {
		return requestedBy;
	}

	/**
	 * @param requestedBy The requestedBy to set.
	 */
	public void setRequestedBy(final User requestedBy) {
		this.requestedBy = requestedBy;
	}
}
