/*
 * Feb 28, 2006
 */
package com.thinkparity.model.parity.model.message.system;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ContactInvitationResponseMessage extends SystemMessage {

	private Boolean didAcceptInvitation;

	private JabberId responseFrom;

	/**
	 * Create a ContactInvitationMessage.
	 * 
	 */
	public ContactInvitationResponseMessage() {
		super();
	}

	public Boolean didAcceptInvitation() { return didAcceptInvitation; }

	/**
	 * @return Returns the responseFrom.
	 */
	public JabberId getResponseFrom() {
		return responseFrom;
	}

	/**
	 * @param didAcceptResponse The didAcceptResponse to set.
	 */
	public void setDidAcceptInvitation(Boolean didAcceptResponse) {
		this.didAcceptInvitation = didAcceptResponse;
	}

	/**
	 * @param responseFrom The responseFrom to set.
	 */
	public void setResponseFrom(JabberId invitedBy) {
		this.responseFrom = invitedBy;
	}
}
