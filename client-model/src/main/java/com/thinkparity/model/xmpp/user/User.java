/*
 * May 14, 2005
 */
package com.thinkparity.model.xmpp.user;

import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.JabberIdBuilder;

/**
 * Represents a parity user.
 * 
 * @author raykroeker@gmail.com
 * @version 1.2.2.1
 */
public class User {

	public static final User SystemUser;

	static { SystemUser = new User("thinkparity"); }

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
	 * Create a User.
	 * 
	 * @param jabberId
	 *            The user's jabber id.
	 */
	public User(final JabberId jabberId) {
		super();
		this.jabberId = jabberId;
	}

	/**
	 * Create a User.
	 * 
	 * @param username
	 *            The fully qualified username.
	 */
	public User(final String username) {
		super();
		JabberId jabberId = null;
		try { jabberId = JabberIdBuilder.parseQualifiedJabberId(username); }
		catch(final IllegalArgumentException iax) { jabberId = null; }
		if(null == jabberId) {
			try { jabberId = JabberIdBuilder.parseQualifiedUsername(username); }
			catch(final IllegalArgumentException iax) { jabberId = null; }
			if(null == jabberId) {
				jabberId = JabberIdBuilder.parseUsername(username);
			}
		}
		this.jabberId = jabberId;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
	public boolean equals(Object obj) {
		if(null != obj && obj instanceof User) {
			return jabberId.equals(((User) obj).jabberId);
		}
		return false;
	}

	/**
	 * Obtain the user's first name.
	 * 
	 * @return The user's first name.
	 */
	public String getFirstName() { return firstName; }

	/**
	 * Obtain the user's last name.
	 * 
	 * @return The user's last name.
	 */
	public String getLastName() { return lastName; }

	/**
	 * Obtain the user's organization.
	 * 
	 * @return Returns the organization.
	 */
	public String getOrganization() {
		return organization;
	}

	/**
	 * Obtain the simple username of the user.
	 * 
	 * @return The simple username; without the domain\resource suffix.
	 */
	public String getSimpleUsername() { return jabberId.getUsername(); }

	/**
	 * Obtain the username of the user.
	 * 
	 * @return The username of the user.
	 */
	public String getUsername() { return jabberId.getQualifiedUsername(); }

	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 */
	public int hashCode() { return jabberId.hashCode(); }

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
}
