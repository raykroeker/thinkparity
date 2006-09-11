/*
 * May 29, 2005
 */
package com.thinkparity.ophelia.model.util.smack;

import org.jivesoftware.smack.RosterListener;

/**
 * SmackRosterListener
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class SmackRosterListener implements RosterListener {

	/**
	 * Create a SmackRosterListener
	 */
	protected SmackRosterListener() { super(); }

	/**
	 * @see org.jivesoftware.smack.RosterListener#rosterModified()
	 */
	public abstract void rosterModified();

	/**
	 * @see org.jivesoftware.smack.RosterListener#presenceChanged(java.lang.String)
	 */
	public abstract void presenceChanged(String XMPPAddress);
}
