/*
 * Mar 3, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class RequestKeyEvent extends AuditEvent {

	private User requestedBy;

	private User requestedFrom;

	/**
	 * Create a RequestKeyEvent.
	 */
	public RequestKeyEvent() {
		super();
	}

	/**
	 * @return Returns the requestedBy.
	 */
	public User getRequestedBy() {
		return requestedBy;
	}

	/**
	 * @return Returns the requestedFrom.
	 */
	public User getRequestedFrom() {
		return requestedFrom;
	}

	/**
	 * @param requestedBy The requestedBy to set.
	 */
	public void setRequestedBy(final User requestedBy) {
		this.requestedBy = requestedBy;
	}

	/**
	 * @param requestedFrom The requestedFrom to set.
	 */
	public void setRequestedFrom(final User requestedFrom) {
		this.requestedFrom = requestedFrom;
	}
}
