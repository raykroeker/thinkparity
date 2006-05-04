/*
 * Created On: Feb 21, 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.xmpp.user.User;

/**
 * This event is recorded when an artifact key is received.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReceiveKeyEvent extends AuditEvent {

    /** From whom the key was received. */
	private User receivedFrom;

	/** Create ReceiveKeyEvent. */
	public ReceiveKeyEvent() { super(AuditEventType.RECEIVE_KEY); }

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
