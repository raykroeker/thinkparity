/*
 * Apr 1, 2006
 */
package com.thinkparity.codebase.email;

/**
 * A wrapper for the 2 string fields used to represent an e-mail.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class EMail {

	/**
	 * The domain portion of an e-mail address.
	 * 
	 */
	private String domain;

	/**
	 * The username portion of an e-mail address.
	 * 
	 */
	private String username;

	/**
	 * Create a EMail.
	 * 
	 */
	public EMail() { super(); }

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 * 
	 */
	public boolean equals(final Object obj) {
		if(null != obj && obj instanceof EMail) {
			return obj.toString().equals(toString());
		}
		return false;
	}

	/**
     * Obtain the domain portion of the e-mail address.
     * 
     * @return The domain.
     */
	public String getDomain() { return domain; }

	/**
     * Obtain the username portion of the e-mail address.
     * 
     * @return The username.
     */
	public String getUsername() { return username; }

	/**
	 * @see java.lang.Object#hashCode()
	 * 
	 */
	public int hashCode() { return toString().hashCode(); }

	/**
     * Set the domain portion of the e-mail address.
     * 
     * @param domain
     *            The domain.
     */
	public void setDomain(final String domain) { this.domain = domain; }

	/**
     * Set the username portion of the e-mail address.
     * 
     * @param username
     *            The username.
     */
	public void setUsername(final String username) { this.username = username; }

	/**
	 * @see java.lang.Object#toString()
	 * 
	 */
	public String toString() {
		return new StringBuffer(username).append("@").append(domain).toString();
	}
}
