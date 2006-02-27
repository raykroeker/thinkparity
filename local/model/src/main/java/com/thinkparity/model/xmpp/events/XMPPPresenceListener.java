/*
 * May 15, 2005
 */
package com.thinkparity.model.xmpp.events;

import com.thinkparity.model.xmpp.JabberId;

public interface XMPPPresenceListener {

	/**
	 * Event fired when a user invites another user as a contact.
	 * 
	 * @param jabberId
	 *            The user sending the invitation.
	 */
	public void presenceRequested(final JabberId jabberId);
}
