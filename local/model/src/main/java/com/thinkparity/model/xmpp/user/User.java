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

    /** The local user pk. */
    private Long localId;

	/**
	 * The user's first name.
	 * 
	 */
	private String firstName;

	/**
	 * The user's jabber id.
	 * 
	 */
	private JabberId id;

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
	 * @param id
	 *            The user's jabber id.
	 */
	public User(final JabberId id) {
		super();
		this.id = id;
	}

	/**
	 * Create a User.
	 * 
	 * @param username
	 *            The fully qualified username.
	 */
	public User(final String username) {
		super();
		JabberId id = null;
		try { id = JabberIdBuilder.parseQualifiedJabberId(username); }
		catch(final IllegalArgumentException iax) { id = null; }
		if(null == id) {
			try { id = JabberIdBuilder.parseQualifiedUsername(username); }
			catch(final IllegalArgumentException iax) { id = null; }
			if(null == id) {
				id = JabberIdBuilder.parseUsername(username);
			}
		}
		this.id = id;
	}

	/**
	 * Create a User.
	 * 
	 */
	public User() { super(); }

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
	public boolean equals(Object obj) {
		if(null != obj && obj instanceof User) {
			return id.equals(((User) obj).id);
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
	 * Obtain the user's id.
	 * 
	 * @return The user's jabber id.
	 */
	public JabberId getId() { return id; }

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
	public String getSimpleUsername() { return id.getUsername(); }

	/**
	 * Obtain the username of the user.
	 * 
	 * @return The username of the user.
	 */
	public String getUsername() { return id.getQualifiedUsername(); }

	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 */
	public int hashCode() { return id.hashCode(); }

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
	 * Set the user's id.
	 * 
	 * @param id
	 *            The user's jabber id.
	 */
	public void setId(final JabberId id) { this.id = id; }

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

    public Long getLocalId() { return localId; }

    public void setLocalId(final Long localId) { this.localId = localId; }
}
