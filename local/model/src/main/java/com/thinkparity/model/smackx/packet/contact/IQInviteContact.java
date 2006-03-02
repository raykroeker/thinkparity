/*
 * Feb 28, 2006
 */
package com.thinkparity.model.smackx.packet.contact;

import com.thinkparity.model.xmpp.JabberId;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQInviteContact extends IQContact {

	/**
	 * Create a IQInviteContact.
	 * 
	 * @param invitedBy
	 *            The invited by jabber id.
	 */
	public IQInviteContact(final JabberId contact) {
		super(Action.INVITECONTACT, contact);
	}

	public JabberId getContact() { return contact; }
}
