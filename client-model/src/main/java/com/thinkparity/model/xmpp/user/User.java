/*
 * May 14, 2005
 */
package com.thinkparity.model.xmpp.user;

import org.jivesoftware.smackx.packet.VCard;

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

	static final String NAME_SEP = " ";

	static { SystemUser = new User("thinkparity"); }

	/** The user's email. */
    private String email;

	/** The user's jabber id. */
	private JabberId id;

	/** The local user pk. */
    private Long localId;

	/** The user's name. */
	private String name;

	/** The user's organization. */
	private String organization;

	/** Create User. */
	public User() { super(); }

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
     * Create User.
     * 
     * @param username
     *            A fully qualified username.
     */
	public User(final String username) {
		super();
		try { id = JabberIdBuilder.parseQualifiedJabberId(username); }
		catch(final IllegalArgumentException iax) { id = null; }
		if(null == id) {
			try { id = JabberIdBuilder.parseQualifiedUsername(username); }
			catch(final IllegalArgumentException iax) { id = null; }
			if(null == id) { id = JabberIdBuilder.parseUsername(username); }
		}
	}

    /** @see java.lang.Object#equals(java.lang.Object) */
	public boolean equals(final Object obj) {
		if(null != obj && obj instanceof User) {
			return ((User) obj).id.equals(id);
		}
		return false;
	}

	/**
     * Obtain the email
     *
     * @return The email.
     */
    public String getEmail() { return email; }

	/**
	 * Obtain the user's id.
	 * 
	 * @return The user's jabber id.
	 */
	public JabberId getId() { return id; }

	/**
     * Obtain the user's local id.
     * 
     * @return The local id.
     */
	public Long getLocalId() { return localId; }

	/**
	 * Obtain the user's name.
	 * 
	 * @return The user's name.
	 */
	public String getName() { return name; }

	/**
	 * Obtain the user's organization.
	 * 
	 * @return Returns the organization.
	 */
	public String getOrganization() { return organization; }

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

	/** @see java.lang.Object#hashCode() */
	public int hashCode() { return id.hashCode(); }

    /**
     * Determine if the organiation is set.
     * 
     * @return True if the organization is set.
     */
    public Boolean isSetOrganization() { return null != organization; }

    /**
     * Set email.
     *
     * @param email The email.
     * 
     * @see VCard#getEmailWork()
     */
    public void setEmail(final String email) { this.email = email; }

    /**
	 * Set the user's id.
	 * 
	 * @param id
	 *            The user's jabber id.
	 */
	public void setId(final JabberId id) { this.id = id; }

    /**
     * Set the user's local id.
     * 
     * @param localId
     *            A local id.
     */
    public void setLocalId(final Long localId) { this.localId = localId; }

    /**
	 * Set the user's name.
	 * 
	 * @param name
	 *            The user's name.
	 */
	public void setName(final String name) { this.name = name; }

    /**
     * Set the user's name.
     * 
     * @param first
     *            The user's first name.
     * @param last
     *            The user's last name.
     */
    public void setName(final String first, final String last) {
        this.name = new UserNameBuilder(first, last).getName();
    }

    /**
     * Set the user's name.
     * 
     * @param first
     *            The user's first name.
     * @param middle
     *            The user's middle name.
     * @param last
     *            The user's last name.
     */
    public void setName(final String first, final String middle,
            final String last) {
        this.name = new UserNameBuilder(first, middle, last).getName();
    }

    /**
	 * Set the user's organization.
	 * 
	 * @param organization
	 *            The user's organization.
	 */
	public void setOrganization(final String organization) {
		this.organization = organization;
	}

    /** @see java.lang.Object#toString() */
    public String toString() {
        return new StringBuffer(getClass().getName())
                .append("//").append(id)
                .append("/").append(localId)
                .append("/").append(name)
                .append("/").append(email)
                .append("/").append(organization)
                .toString();
    }
}
