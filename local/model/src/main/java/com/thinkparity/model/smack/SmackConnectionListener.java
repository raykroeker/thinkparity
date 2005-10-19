/*
 * May 14, 2005
 */
package com.thinkparity.model.smack;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.ConnectionEstablishedListener;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.XMPPConnection;

import com.thinkparity.model.log4j.ModelLoggerFactory;

/**
 * SmackConnectionListener
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class SmackConnectionListener implements
		ConnectionEstablishedListener, ConnectionListener {

	/**
	 * Handle to an internal logger.
	 */
	private static final Logger logger =
		ModelLoggerFactory.getLogger(SmackConnectionListener.class);

	/**
	 * Handle to a log statement formatter.
	 */
	private static final SmackLoggerFormatter loggerFormatter =
		new SmackLoggerFormatter();

	/**
	 * Create an SmackConnectionListener
	 */
	protected SmackConnectionListener() { super(); }

	/**
	 * Debug an XMPPConnection.
	 * @param context <code>java.lang.String</code>
	 * @param xmppConnection <code>org.jivesoftware.smack.XMPPConnection</code>
	 */
	protected void debug(final String context, final XMPPConnection xmppConnection) {
		logger.debug(loggerFormatter.format(context, xmppConnection));
	}
}
