/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.user;

import org.dom4j.Element;

import com.thinkparity.codebase.jabber.JabberId;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class User {

	private JabberId id;

	private Element vCard;

	/**
	 * Create a User.
	 */
	public User() { super(); }

	/**
	 * @return Returns the id.
	 */
	public JabberId getId() { return id; }

	/**
	 * @return Returns the vCard.
	 */
	public Element getVCard() {
		return vCard;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(final JabberId id) { this.id = id; }

	/**
	 * @param cardInfo The vCard to set.
	 */
	public void setVCard(Element cardInfo) {
		vCard = cardInfo;
	}
}
