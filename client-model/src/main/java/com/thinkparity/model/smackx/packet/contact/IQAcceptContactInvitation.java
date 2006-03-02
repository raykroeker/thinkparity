/*
 * Feb 28, 2006
 */
package com.thinkparity.model.smackx.packet.contact;

import com.thinkparity.model.xmpp.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQAcceptContactInvitation extends IQContact {

	/**
	 * Create a IQAcceptContactInvitation.
	 * 
	 * @param contact
	 *            The contact to accept.
	 */
	public IQAcceptContactInvitation(final JabberId contact) {
		super(Action.ACCEPTCONTACTINVITATION, contact);
	}

	public JabberId getContact() { return contact; }
}
