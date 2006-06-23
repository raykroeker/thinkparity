/*
 * Created On: Jun 22, 2006 1:18:24 PM
 * $Id$
 */
package com.thinkparity.codebase.jabber;

import org.xmpp.packet.JID;

import com.thinkparity.codebase.Constants.Jabber;

/**
 * <b>Title:</b>thinkParity Jabber Id Builder <br>
 * <b>Description:</b>Creates jive server jabber id objects.
 * 
 * @author raymond@thinkparity.com
 * @version $Revision$
 * @see JabberId
 */
public class JabberIdBuilder {

    /** The singleton instance. */
	private static final JabberIdBuilder SINGLETON;

	static { SINGLETON = new JabberIdBuilder(); }

    /**
     * Parse a jive JID.
     * 
     * @param jid
     *            A jid.
     * @return A jabber id.
     */
    public static JabberId parseJID(final JID jid) {
        synchronized(SINGLETON) { return SINGLETON.doParseJID(jid); }
    }

	/**
	 * Parse the qualified jabber id.
	 * 
	 * @param qualifiedJabberId
	 *            The qualified jabber id: user@host/resource
	 * @return The qualified jabber id.
	 */
	public static JabberId parseQualifiedJabberId(final String qualifiedJabberId) {
		synchronized(SINGLETON) {
			return SINGLETON.doParseQualifiedJabberId(qualifiedJabberId);
		}
	}

	/**
	 * Parse the qualified username and build a jabber id.
	 * 
	 * @param qualifiedUsername
	 *            The qualified username:  user@host
	 * @return The jabber id.
	 */
	public static JabberId parseQualifiedUsername(final String qualifiedUsername) {
		synchronized(SINGLETON) {
			return SINGLETON.doParseQualifiedUsername(qualifiedUsername);
		}
	}

    /** The default resource component of the id. */
	private final String defaultResource;

	/** The qualified jabber id. */
	private String qualifiedJID;

	/** The qualified username. */
	private String qualifiedUsername;

	/**
	 * Create a JabberIdBuilder.
	 * 
	 * <strong>For singleton use only.</strong>
	 */
	private JabberIdBuilder() {
		super();
		this.defaultResource = Jabber.RESOURCE;
	}

	/**
	 * Obtain the qualified jabber id:  user@host/resource
	 * 
	 * @return The qualified jabber id.
	 */
	public String getQualifiedJID() { return qualifiedJID; }

	/**
	 * Obtain the qualified username:  user@host
	 * 
	 * @return The qualified username.
	 */
	public String getQualifiedUsername() {
		return qualifiedUsername;
	}

	/**
     * Parse a jive JID.
     * 
     * @param jid
     *            A jid.
     * @return A jabber id.
     */
    private JabberId doParseJID(final JID jid) {
        if(null == jid.getResource()) {
            final String qualifiedUsername = new StringBuffer(jid.getNode())
                .append('@')
                .append(jid.getDomain())
                .toString();
            return doParseQualifiedUsername(qualifiedUsername);
        }
        else {
            final String qualifiedJabberId = new StringBuffer(jid.getNode())
                .append('@')
                .append(jid.getDomain())
                .append('/')
                .append(jid.getResource()).toString();
            return doParseQualifiedJabberId(qualifiedJabberId);
        }
    }

	/**
	 * Parse the qualified jabber id.
	 * 
	 * <strong>For singleton use only.</strong>
	 * 
	 * @param qualifiedJabberId
	 *            The qualified jabber id: user@host/resource
	 * @return The qualified jabber id.
	 */
	private JabberId doParseQualifiedJabberId(final String qualifiedJabberId) {
		final int indexOfAt = qualifiedJabberId.indexOf('@');
		if(-1 == indexOfAt) throw new IllegalArgumentException("Qualified jabber id contains no user\\host separation.");
		final String username = qualifiedJabberId.substring(0, indexOfAt);
		final int indexOfSlash = qualifiedJabberId.indexOf('/');
		if(-1 == indexOfSlash) throw new IllegalArgumentException("Qualified jabber id contains no host\\resource separation.");
		final String host = qualifiedJabberId.substring(indexOfAt + 1, indexOfSlash);
		final String resource = qualifiedJabberId.substring(indexOfSlash + 1);
		if(null == username) throw new IllegalArgumentException("Username cannot be null.");
		if(1 > username.length()) throw new IllegalArgumentException("Username cannot be empty.");
		if(null == host) throw new IllegalArgumentException("Host cannot be null.");
		if(1 > host.length()) throw new IllegalArgumentException("Host cannot be empty.");
		if(null == resource) throw new IllegalArgumentException("Resource cannot be null.");
		if(1 > resource.length()) throw new IllegalArgumentException("Resource cannot be empty.");
		return new JabberId(username, host, resource);
	}

    /**
	 * Parse the qualified username and build a jabber id.
	 * 
	 * <strong>For singleton use only.</strong>
	 * 
	 * @param qualifiedUsername
	 *            The qualified username: user@host
	 * @return The jabber id.
	 */
	private JabberId doParseQualifiedUsername(final String qualifiedUsername) {
		final int indexOfAt = qualifiedUsername.indexOf('@');
		if(-1 == indexOfAt) throw new IllegalArgumentException("Qualified username contains no user\\host separation.");
		final String username = qualifiedUsername.substring(0, indexOfAt);
		final String host = qualifiedUsername.substring(indexOfAt + 1);
		if(null == username) throw new IllegalArgumentException("Username cannot be null.");
		if(1 > username.length()) throw new IllegalArgumentException("Username cannot be empty.");
		if(null == host) throw new IllegalArgumentException("Host cannot be null.");
		if(1 > host.length()) throw new IllegalArgumentException("Host cannot be empty.");
		if(-1 != host.indexOf('/')) throw new IllegalArgumentException("Host cannot contain '/'");
		return new JabberId(username, host, defaultResource);
	}

}
