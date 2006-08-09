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

    /** thinkParity user. */
    public static final User THINK_PARITY;

	static final String NAME_SEP = " ";

	static {
        THINK_PARITY = new User();
        THINK_PARITY.setId(JabberIdBuilder.parseUsername("thinkparity"));
    }

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

	/** @see java.lang.Object#equals(java.lang.Object) */
	public boolean equals(final Object obj) {
		if(null != obj && obj instanceof User) {
			return ((User) obj).id.equals(id);
		}
		return false;
	}

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

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(id)
                .append("/").append(localId)
                .append("/").append(name)
                .append("/").append(organization)
                .toString();
    }
}
