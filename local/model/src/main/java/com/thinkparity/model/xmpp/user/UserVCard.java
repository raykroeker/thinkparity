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

    /** The user's email. */
    private String email;

	/** The user's jabber id. */
	private JabberId jabberId;

	/** The user's name. */
	private String name;

	/** The user's organization. */
	private String organization;

	/** Create UserVCard. */
	public UserVCard() { super(); }

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
	public boolean equals(final Object obj) {
		if(null != obj && obj instanceof UserVCard) {
			return toString().equals(toString());
		}
		return false;
	}

	/**
     * Obtain the user's email.
     *
     * @return The user's email.
     */
    public String getEmail() { return email; }

    /**
     * Obtain the name
     *
     * @return The name.
     */
    public String getFirstName() {
        return createNameTokenizer(name).getGiven();
    }

	/**
	 * Obtain the user's jabber id.
	 * 
	 * @return The user's jabber id.
	 */
	public JabberId getJabberId() { return jabberId; }

	public String getLastName() {
        return createNameTokenizer(name).getFamily();
    }

    public String getMiddleName() {
        return createNameTokenizer(name).getMiddle();
    }

    public String getName() { return name; }

    /**
     * Obtain the user's oragnization.
     * 
     * @return The user's organization.
     */
	public String getOrganization() { return organization; }

	/** @see java.lang.Object#hashCode() */
	public int hashCode() { return toString().hashCode(); }

	public Boolean isSetMiddleName() {
        return createNameTokenizer(name).isSetMiddle();
    }

    /**
     * Set email.
     *
     * @param email The String.
     */
    public void setEmail(final String email) { this.email = email; }

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
        this.name = createNameBuilder(first, null, last).getName();
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
        this.name = createNameBuilder(first, middle, last).getName();
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
		return getClass().getName()
			+ "\\" + name + "\\" + email;
	}

    private UserNameBuilder createNameBuilder(final String first,
            final String middle, final String last) {
        return new UserNameBuilder(first, middle, last);
    }

    private UserNameTokenizer createNameTokenizer(final String name) {
        return new UserNameTokenizer(name);
    }
}
