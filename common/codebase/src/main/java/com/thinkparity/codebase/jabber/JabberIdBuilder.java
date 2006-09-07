/*
 * Feb 15, 2006
 */
package com.thinkparity.codebase.jabber;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class JabberIdBuilder {

    /** The singleton instance. */
	private static final JabberIdBuilder SINGLETON;

	static { SINGLETON = new JabberIdBuilder(); }

	/**
	 * Parse the qualified jabber id.
	 * 
	 * @param qualifiedJabberId
	 *            The qualified jabber id: user@domain/resource
	 * @return The qualified jabber id.
	 */
	public static JabberId parseQualifiedJabberId(final String qualifiedJabberId) {
	    return SINGLETON.doParseQualifiedJabberId(qualifiedJabberId);
	}

	/**
	 * Parse the qualified username and build a jabber id.
	 * 
	 * @param qualifiedUsername
	 *            The qualified username:  user@domain
	 * @return The jabber id.
	 */
	public static JabberId parseQualifiedUsername(final String qualifiedUsername) {
	    return SINGLETON.doParseQualifiedUsername(qualifiedUsername);
	}

	/**
	 * Parse the username and build a jabber id.
	 * 
	 * @param username
	 *            The username.
	 * @return The jabber id.
	 */
	public static JabberId parseUsername(final String username) {
	    return SINGLETON.doParseUsername(username);
	}

	/** Create JabberIdBuilder. */
	private JabberIdBuilder() { super(); }

	/**
	 * Parse the qualified jabber id.
	 * 
	 * <strong>For singleton use only.</strong>
	 * 
	 * @param qualifiedJabberId
	 *            The qualified jabber id: user@domain/resource
	 * @return The qualified jabber id.
	 */
	private JabberId doParseQualifiedJabberId(final String qualifiedJabberId) {
		final int indexOfAt = qualifiedJabberId.indexOf('@');
		if(-1 == indexOfAt) throw new IllegalArgumentException("Qualified jabber id contains no user/domain separation:  " + qualifiedJabberId);
		final String username = qualifiedJabberId.substring(0, indexOfAt);
		final int indexOfSlash = qualifiedJabberId.indexOf('/');
		if(-1 == indexOfSlash) throw new IllegalArgumentException("Qualified jabber id contains no domain/resource separation:  " + qualifiedJabberId);
		final String domain = qualifiedJabberId.substring(indexOfAt + 1, indexOfSlash);
		final String resource = qualifiedJabberId.substring(indexOfSlash + 1);
		if(null == username) throw new IllegalArgumentException("Username cannot be null:  " + qualifiedJabberId);
		if(1 > username.length()) throw new IllegalArgumentException("Username cannot be empty:  " + qualifiedJabberId);
		if(null == domain) throw new IllegalArgumentException("Domain cannot be null:  " + qualifiedJabberId);
		if(1 > domain.length()) throw new IllegalArgumentException("Domain cannot be empty:  " + qualifiedJabberId);
		if(null == resource) throw new IllegalArgumentException("Resource cannot be null:  " + qualifiedJabberId);
		if(1 > resource.length()) throw new IllegalArgumentException("Resource cannot be empty:  " + qualifiedJabberId);
		return new JabberId(username, domain, resource);
	}

	/**
	 * Parse the qualified username and build a jabber id.
	 * 
	 * <strong>For singleton use only.</strong>
	 * 
	 * @param qualifiedUsername
	 *            The qualified username: user@domain
	 * @return The jabber id.
	 */
	private JabberId doParseQualifiedUsername(final String qualifiedUsername) {
		final int indexOfAt = qualifiedUsername.indexOf('@');
		if(-1 == indexOfAt) throw new IllegalArgumentException("Qualified username contains no user/domain separation:  " + qualifiedUsername);
		final String username = qualifiedUsername.substring(0, indexOfAt);
		final String domain = qualifiedUsername.substring(indexOfAt + 1);
		if(null == username) throw new IllegalArgumentException("Username cannot be null:  " + qualifiedUsername);
		if(1 > username.length()) throw new IllegalArgumentException("Username cannot be empty:  " + qualifiedUsername);
		if(null == domain) throw new IllegalArgumentException("Domain cannot be null:  " + qualifiedUsername);
		if(1 > domain.length()) throw new IllegalArgumentException("Domain cannot be empty:  " + qualifiedUsername);
		if(-1 != domain.indexOf('/')) throw new IllegalArgumentException("Domain cannot contain '/':  " + qualifiedUsername);
		return new JabberId(username, domain, Constants.RESOURCE);
	}

	/**
	 * Parse the username and build a jabber id.
	 * 
	 * <strong>For singleton use only.</strong>
	 * 
	 * @param username
	 *            The username.
	 * @return A fully qualified jabber id.
	 */
	private JabberId doParseUsername(final String username) {
		if(null == username) throw new IllegalArgumentException("Username cannot be null:  " + username);
		if(1 > username.length()) throw new IllegalArgumentException("Username cannot be empty:  " + username);
		if(-1 != username.indexOf('@')) throw new IllegalArgumentException("Username cannot contain '@':  " + username);
		if(-1 != username.indexOf('/')) throw new IllegalArgumentException("Username cannot contain '/':  " + username);
		return new JabberId(username, Constants.DOMAIN, Constants.RESOURCE);
	}

    private static final class Constants {
        private static final String DOMAIN = "thinkparity.dyndns.org";
        private static final String RESOURCE = "parity";
    }
}
