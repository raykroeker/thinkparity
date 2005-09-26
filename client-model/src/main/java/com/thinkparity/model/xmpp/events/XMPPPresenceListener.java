/*
 * May 15, 2005
 */
package com.thinkparity.model.xmpp.events;

import com.thinkparity.model.xmpp.user.User;

public interface XMPPPresenceListener {

	/**
	 * This event is fired when another parity user requests visibility into
	 * this user's presence state.
	 * 
	 * @param xmppUser
	 *            <code>org.kcs.projectmanager.xmpp.user.XMPPUser</code>
	 */
	public void presenceRequested(final User xmppUser);
}
