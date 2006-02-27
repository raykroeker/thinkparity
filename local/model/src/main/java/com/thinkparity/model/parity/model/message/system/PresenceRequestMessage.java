/*
 * Feb 24, 2006
 */
package com.thinkparity.model.parity.model.message.system;

import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class PresenceRequestMessage extends SystemMessage {

	private JabberId requestedBy;

	private User requestedByUser;

	/**
	 * Create a PresenceRequestMessage.
	 */
	public PresenceRequestMessage() {
		super();
	}

	/**
	 * @return Returns the requestedBy.
	 */
	public JabberId getRequestedBy() { return requestedBy; }

	/**
	 * @return Returns the requestedByUser.
	 */
	public User getRequestedByUser() {
		return requestedByUser;
	}

	/**
	 * @param requestedBy The requestedBy to set.
	 */
	public void setRequestedBy(final JabberId requestedBy) {
		this.requestedBy = requestedBy;
	}

	/**
	 * @param requestedByUser The requestedByUser to set.
	 */
	public void setRequestedByUser(User requestedByUser) {
		this.requestedByUser = requestedByUser;
	}
}
