/*
 * Mar 21, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.xmpp.user.User;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class KeyRequestDeniedEvent extends AuditEvent {

	private User deniedBy;

	/**
	 * Create a KeyRequestDeniedEvent.
	 * 
	 */
	public KeyRequestDeniedEvent() { super(); }

	/**
	 * @return Returns the deniedBy.
	 */
	public User getDeniedBy() {
		return deniedBy;
	}

	/**
	 * @param deniedBy The deniedBy to set.
	 */
	public void setDeniedBy(final User deniedBy) {
		this.deniedBy = deniedBy;
	}
}
