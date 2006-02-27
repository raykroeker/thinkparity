/*
 * Feb 25, 2006
 */
package com.thinkparity.model.xmpp.user;

import com.thinkparity.model.xmpp.JabberId;

/**
 * The user vCard contains user information as outlined in the vCard rfc.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UserVCard {

	/**
	 * The user's first name.
	 * 
	 */
	private String firstName;

	/**
	 * The user's jabber id.
	 * 
	 */
	private JabberId jabberId;

	/**
	 * The user's last name.
	 * 
	 */
	private String lastName;

	/**
	 * The user's organization.
	 * 
	 */
	private String organization;

	/**
	 * Create a UserVCard.
	 * 
	 */
	public UserVCard() { super(); }

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
	public boolean equals(Object obj) {
		if(null != obj && obj instanceof UserVCard) {
			return toString().equals(obj.toString());
		}
		return false;
	}

	/**
	 * Obtain the user's first name.
	 * 
	 * @return The user's firstName.
	 */
	public String getFirstName() { return firstName; }

	/**
	 * Obtain the user's jabber id.
	 * 
	 * @return The user's jabber id.
	 */
	public JabberId getJabberId() { return jabberId; }

	/**
	 * Obtain the user's last name.
	 * 
	 * @return The user's last name.
	 */
	public String getLastName() { return lastName; }

	/**
	 * Obtain the user's oragnization.
	 * @return Set the user's organization.
	 */
	public String getOrganization() { return organization; }

	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 */
	public int hashCode() { return toString().hashCode(); }

	/**
	 * Set the user's first name.
	 * 
	 * @param firstName
	 *            The user's first name.
	 */
	public void setFirstName(final String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Set the user's jabber id.
	 * 
	 * @param jabberId
	 *            The users's jabber id.
	 */
	public void setJabberId(final JabberId jabberId) {
		this.jabberId = jabberId;
	}

	/**
	 * Set the user's last name.
	 * 
	 * @param lastName
	 *            The user's last name.
	 */
	public void setLastName(final String lastName) { this.lastName = lastName; }

	/**
	 * Set the user's organization.
	 * 
	 * @param organization
	 *            The user's organization.
	 */
	public void setOrganization(final String organization) {
		this.organization = organization;
	}

	/**
	 * @see java.lang.Object#toString()
	 * 
	 */
	public String toString() {
		return getClass().getName()
			+ "\\" + getOrganization() + "\\" + getLastName() + "\\" +
			getFirstName();
	}
}
