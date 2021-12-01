/*
 * Created On: Apr 1, 2006
 */
package com.thinkparity.codebase.email;

/**
 * A wrapper for the 2 string fields used to represent an e-mail.
 * 
 * @author raymond@raykroeker.com
 * @version 1.1.2.2
 */
public class EMail {

	/** The domain portion of an e-mail address. */
	private String domain;

	/** The username portion of an e-mail address. */
	private String username;

	/**
     * Create EMail.
     *
	 */
	public EMail() {
        super();
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
    @Override
	public boolean equals(final Object obj) {
        if (null == obj)
            return false;
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        return obj.toString().equals(toString());
	}

	/**
     * Obtain the domain portion of the e-mail address.
     * 
     * @return The domain.
     */
	public String getDomain() {
        return domain;
	}

	/**
     * Obtain the username portion of the e-mail address.
     * 
     * @return The username.
     */
	public String getUsername() {
        return username;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 */
    @Override
	public int hashCode() {
        return toString().hashCode();
    }

	/**
	 * @see java.lang.Object#toString()
	 * 
	 */
    @Override
	public String toString() {
        if (null == domain)
            throw new IllegalArgumentException("Domain not specified.");
        if (null == username)
            throw new IllegalArgumentException("Username not specified.");
		return new StringBuffer(username).append("@").append(domain).toString();
	}

	/**
     * Set the domain portion of the e-mail address.
     * 
     * @param domain
     *            The domain.
     */
	public void setDomain(final String domain) {
        this.domain = domain;
    }

	/**
     * Set the username portion of the e-mail address.
     * 
     * @param username
     *            The username.
     */
    public void setUsername(final String username) {
        this.username = username;
    }
}
