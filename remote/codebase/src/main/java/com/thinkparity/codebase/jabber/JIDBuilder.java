/*
 * Dec 7, 2005
 */
package com.thinkparity.codebase.jabber;

import org.xmpp.packet.JID;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class JIDBuilder {

    /** The singleton instance. */
	private static final JIDBuilder SINGLETON;

	static { SINGLETON = new JIDBuilder(); }

	/**
	 * Build an xmpp JID based upon a fully qualified jid.
	 * 
	 * @param qualifiedJID
	 *            The qualified jid.
	 * @return The xmpp JID.
	 */
	static JID buildQualified(final String qualifiedJID) {
		synchronized(SINGLETON) {
			return SINGLETON.buildQualifiedImpl(qualifiedJID);
		}
	}

	/** Create JIDBuilder. */
	private JIDBuilder() { super(); }
 
	/**
	 * Build an xmpp JID based upon a fully qualified jid.
	 * 
	 * @param qualifiedJID
	 *            The fully qualified jid.
	 * @return The xmpp JID.
	 * @throws IllegalArgumentException
	 *             If qualifiedJID contains an invalid format.
	 * @throws NullPointerException
	 *             If qualifiedJID is null.
	 */
	private JID buildQualifiedImpl(final String qualifiedJID) {
		if(null == qualifiedJID) { throw new NullPointerException(); }
		final Integer indexOfAt = qualifiedJID.indexOf('@');
		// node must exist; and be length > 1
		if(indexOfAt < 1) { throw new IllegalArgumentException(); }
		final Integer indexOfSlash = qualifiedJID.indexOf('/', indexOfAt);
		// resource must exist
		if(indexOfSlash == -1) { throw new IllegalArgumentException(); }
		// resource must be length > 0
		if(indexOfSlash == (qualifiedJID.length() - 1)) { throw new IllegalArgumentException(); }
		// domain must be length > 0
		if(indexOfSlash == (indexOfAt + 1)) { throw new IllegalArgumentException(); }

		return new JID(qualifiedJID);
	}
}
