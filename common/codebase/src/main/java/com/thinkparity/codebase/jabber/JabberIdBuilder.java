/*
 * Feb 15, 2006
 */
package com.thinkparity.codebase.jabber;

import java.text.MessageFormat;

import com.thinkparity.common.StringUtil.Separator;


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

    public static JabberId build(final String username, final String service,
            final String resource) {
        return SINGLETON.doBuild(username, service, resource);
    }

    public static JabberId parse(final String jabberId) {
        return SINGLETON.doParse(jabberId);
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
     * Build a jabber id.
     * 
     * @param username
     *            A username <code>String</code>.
     * @param service
     *            A service <code>String</code>.
     * @return A well-formed <code>JabberId</code>.
     */
    private JabberId doBuild(final String username, final String service,
            final String resource) {
        return new JabberId(username, service, resource);
    }

    /**
     * Parse a jabber id.
     * 
     * @param jabberId
     *            A jabber id <code>String</code>.
     * @return A well-formed <code>JabberId</code>.
     */
	private JabberId doParse(final String jabberId) {
        final int indexOfAt = jabberId.indexOf(Separator.At.toString());
        if (-1 == indexOfAt)
            panic("Jabber id {0} contains no username service separator.", jabberId);
        final String username = jabberId.substring(0, indexOfAt);
        if (null == username)
            panic("Jabber id {0} contains no username.", jabberId);
        if (1 > username.length())
            panic("Jabber id {0} contains 0 length username.", jabberId);

        final String service, resource;
        final int indexOfSlash = jabberId.indexOf(Separator.ForwardSlash.toString());
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
     * Parse a username and build a jabber id.
     * 
     * @param username
     *            The username <code>String>.
     * @return A well-formed <code>JabberId</code>.
     */
	private JabberId doParseUsername(final String username) {
        final String jabberId = new StringBuffer(username).append(Separator.At)
                .append(Constants.SERVICE).toString();
        return doParse(jabberId);
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

    private static final class Constants {
        private static final String SERVICE = "thinkparity.net";
    }
}
