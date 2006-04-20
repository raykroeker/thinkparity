/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.xmpp.user.User;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReceiveKeyEvent extends AuditEvent {

	private User receivedFrom;

	/**
	 * Create a ReceiveKeyEvent.
	 */
	public ReceiveKeyEvent() {
		super();
	}

	/**
	 * @return Returns the receivedFrom.
	 */
	public User getReceivedFrom() {
		return receivedFrom;
	}

	/**
	 * @param receivedFrom The receivedFrom to set.
	 */
	public void setReceivedFrom(final User receivedFrom) {
		this.receivedFrom = receivedFrom;
	}
}
