/*
 * Feb 28, 2006
 */
package com.thinkparity.model.smackx.packet.contact;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQDeclineContactInvitation extends IQContact {

	/**
	 * Create a IQDeclineContactInvitation.
	 * 
	 * @param contact
	 *            The contact.
	 */
	public IQDeclineContactInvitation(final JabberId contact) {
		super(Action.DECLINECONTACTINVITATION, contact);
	}

	public JabberId getContact() { return contact; }
}
