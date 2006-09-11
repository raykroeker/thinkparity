/*
 * May 14, 2005
 */
package com.thinkparity.ophelia.model.util.smack;

import org.jivesoftware.smack.ConnectionEstablishedListener;
import org.jivesoftware.smack.ConnectionListener;

/**
 * SmackConnectionListener
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class SmackConnectionListener implements
		ConnectionEstablishedListener, ConnectionListener {

	/**
	 * Create an SmackConnectionListener
	 */
	protected SmackConnectionListener() { super(); }
}
