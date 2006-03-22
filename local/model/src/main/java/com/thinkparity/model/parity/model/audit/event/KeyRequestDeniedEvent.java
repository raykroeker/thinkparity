/*
 * Mar 21, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.xmpp.JabberId;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class KeyRequestDeniedEvent extends AuditEvent {

	private JabberId deniedBy;

	/**
	 * Create a KeyRequestDeniedEvent.
	 * 
	 */
	public KeyRequestDeniedEvent() { super(); }

	/**
	 * @return Returns the deniedBy.
	 */
	public JabberId getDeniedBy() {
		return deniedBy;
	}

	/**
	 * @param deniedBy The deniedBy to set.
	 */
	public void setDeniedBy(JabberId deniedBy) {
		this.deniedBy = deniedBy;
	}

}
