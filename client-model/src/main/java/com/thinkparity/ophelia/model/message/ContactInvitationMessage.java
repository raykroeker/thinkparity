/*
 * Feb 28, 2006
 */
package com.thinkparity.ophelia.model.message;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ContactInvitationMessage extends SystemMessage {

	private JabberId invitedBy;

	/**
	 * Create a ContactInvitationMessage.
	 * 
	 */
	public ContactInvitationMessage() {
		super();
	}

	/**
	 * @return Returns the invitedBy.
	 */
	public JabberId getInvitedBy() {
		return invitedBy;
	}

	/**
	 * @param invitedBy The invitedBy to set.
	 */
	public void setInvitedBy(JabberId invitedBy) {
		this.invitedBy = invitedBy;
	}
}
