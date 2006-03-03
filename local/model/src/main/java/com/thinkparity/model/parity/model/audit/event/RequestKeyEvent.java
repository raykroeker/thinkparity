/*
 * Mar 3, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class RequestKeyEvent extends AuditEvent {

	private JabberId requestedBy;

	private JabberId requestedFrom;

	/**
	 * Create a RequestKeyEvent.
	 */
	public RequestKeyEvent() {
		super();
	}

	/**
	 * @return Returns the requestedBy.
	 */
	public JabberId getRequestedBy() {
		return requestedBy;
	}

	/**
	 * @return Returns the requestedFrom.
	 */
	public JabberId getRequestedFrom() {
		return requestedFrom;
	}

	/**
	 * @param requestedBy The requestedBy to set.
	 */
	public void setRequestedBy(JabberId requestedBy) {
		this.requestedBy = requestedBy;
	}

	/**
	 * @param requestedFrom The requestedFrom to set.
	 */
	public void setRequestedFrom(JabberId requestedFrom) {
		this.requestedFrom = requestedFrom;
	}

}
