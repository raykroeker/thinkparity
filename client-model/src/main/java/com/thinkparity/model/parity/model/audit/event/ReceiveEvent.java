/*
 * Created On: Feb 21, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.xmpp.user.User;

/**
 * This event is recorded when a document is received.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReceiveEvent extends AuditVersionEvent {

    /** From whom the document was received. */
	private User receivedFrom;

	/** Create  ReceiveEvent. */
	public ReceiveEvent() { super(AuditEventType.RECEIVE); }

	/**
     * Obtain the received from user.
     *
	 * @return A user.
	 */
	public User getReceivedFrom() { return receivedFrom; }

	/**
     * Set the received from user.
     *
	 * @param receivedFrom A user.
	 */
	public void setReceivedFrom(final User receivedFrom) {
		this.receivedFrom = receivedFrom;
	}
}
