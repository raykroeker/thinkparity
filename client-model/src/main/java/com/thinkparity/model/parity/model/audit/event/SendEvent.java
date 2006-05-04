/*
 * Feb 21, 2006
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.xmpp.user.User;

/**
 * This event is recorded when an artifact version is sent.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendEvent extends AuditVersionEvent {

    /** To whom the artifact was sent. */
	private User sentTo;

	/** Create a SendEvent. */
	public SendEvent() {
        super(AuditEventType.SEND);
    }

	/**
     * Obtain the sent to user.
     *
	 * @return A user.
	 */
	public User getSentTo() { return sentTo; }

    /**
     * Set the sent to user.
     *
     * @param sentTo A user.
     */
    public void setSentTo(final User sentTo) { this.sentTo = sentTo; }
}
