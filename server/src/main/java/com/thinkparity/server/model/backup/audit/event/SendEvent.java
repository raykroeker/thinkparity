/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.audit.event;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.audit.AuditEventType;

/**
 * This event is recorded when an artifact version is sent.
 *
 * @author raymond@raykroeker.com
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
