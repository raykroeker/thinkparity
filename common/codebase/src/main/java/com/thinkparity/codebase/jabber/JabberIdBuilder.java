/*
 * Feb 15, 2006
 */
package com.thinkparity.codebase.jabber;

import java.text.MessageFormat;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class JabberIdBuilder {

    /** The singleton instance. */
	private static final JabberIdBuilder SINGLETON;

	static { SINGLETON = new JabberIdBuilder(); }

	public static JabberId build(final String username, final String service) {
        return SINGLETON.doBuild(username, service);
    }
    public static JabberId parse(final String jabberId) {
        return SINGLETON.doParse(jabberId);
    }

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
     * Build a jabber id.
     * 
     * @param username
     *            A username <code>String</code>.
     * @param service
     *            A service <code>String</code>.
     * @return A well-formed <code>JabberId</code>.
     */
	private JabberId doBuild(final String username, final String service) {
        return new JabberId(username, service);
    }

    /**
     * Parse a jabber id.
     * 
     * @param jabberId
     *            A jabber id <code>String</code>.
     * @return A well-formed <code>JabberId</code>.
     */
	private JabberId doParse(final String jabberId) {
        final int indexOfAt = jabberId.indexOf('@');
        if (-1 == indexOfAt)
            panic("Jabber id {0} contains no username service separator.", jabberId);
        final String username = jabberId.substring(0, indexOfAt);
        if (null == username)
            panic("Jabber id {0} contains no username.", jabberId);
        if (1 > username.length())
            panic("Jabber id {0} contains 0 length username.", jabberId);

        final String service, resource;
        final int indexOfSlash = jabberId.indexOf('/');
        if (-1 == indexOfSlash) {
            service = jabberId.substring(indexOfAt + 1);
            resource = null;
        } else {
            service = jabberId.substring(indexOfAt + 1, indexOfSlash);
            resource = jabberId.substring(indexOfSlash + 1);
        }
        if (null == service)
            panic("Jabber id {0} conains no service.", jabberId);
        if (1 > service.length())
            panic("Jabber id {0} contains 0 length service.", jabberId);

        if (null == resource || 1 > resource.length()) {
            return new JabberId(username, service);
        } else {
            return new JabberId(username, service, resource);
        }
    }

    /**
     * Panic. This throws an illegal argument exception using a pattern and
     * arguments to form the exception message.
     * 
     * @param panicPattern
     *            The message pattern.
     * @param panicArguments
     *            The message arguments.
     */
    private void panic(final String panicPattern,
            final Object... panicArguments) {
        throw new IllegalArgumentException(
                MessageFormat.format(panicPattern, panicArguments));
    }

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
		return build(username, Constants.SERVICE);
	}

    private static final class Constants {
        private static final String SERVICE = "thinkparity.dyndns.org";
    }
}
