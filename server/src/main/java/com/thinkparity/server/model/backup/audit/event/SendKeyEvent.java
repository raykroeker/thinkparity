/*
 * Created On: Feb 21, 2006
 * $Id$
 */
package com.thinkparity.ophelia.model.audit.event;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.audit.AuditEventType;

/**
 * This event is recorded when an artifact key is sent.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SendKeyEvent extends AuditVersionEvent {

    /** To whom the key was sent. */
	private User sentTo;

	/** Create SendKeyEvent. */
	public SendKeyEvent() { super(AuditEventType.SEND_KEY); }

	/**
     * Obtain the sent to user.
     *
	 * @return A user.
	 */
	public User getSentTo() { return sentTo; }

	/**
     * Set the sent to user.
     *
	 * @param sentTo
     *      A user.
	 */
	public void setSentTo(final User sentTo) { this.sentTo = sentTo; }
}
