/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.model.contact;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Invitation {

	private JabberId from;

	private JabberId to;

	/**
	 * Create a Invitation.
	 */
	public Invitation() {
		super();
	}

	/**
	 * @return Returns the invitationFrom.
	 */
	public JabberId getFrom() {
		return from;
	}

	/**
	 * @return Returns the extendedTo.
	 */
	public JabberId getTo() {
		return to;
	}

	/**
	 * @param invitationFrom The invitationFrom to set.
	 */
	public void setFrom(JabberId invitationFrom) {
		this.from = invitationFrom;
	}

	/**
	 * @param extendedTo The extendedTo to set.
	 */
	public void setTo(JabberId extendedTo) {
		this.to = extendedTo;
	}

}
