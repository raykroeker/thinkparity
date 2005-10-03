/*
 * May 14, 2005
 */
package com.thinkparity.model.smack.packet;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;


import com.thinkparity.model.parity.util.log4j.ModelLoggerFactory;
import com.thinkparity.model.xmpp.XMPPLoggerFormatter;

/**
 * XMPPPresenceListener This listener is used to translate presence packets into
 * events required by the parity client. The currently required events are
 * subscription requests by other parity users.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class SmackPresenceListener implements PacketListener {

	/**
	 * Handle to an internal logger.
	 */
	private static final Logger logger =
		ModelLoggerFactory.getLogger(SmackPresenceListener.class);

	/**
	 * Handle to a log formatter for xmpp.
	 */
	private static final XMPPLoggerFormatter logger_Formatter =
		new XMPPLoggerFormatter();

	/**
	 * Create an XMPPPresenceListener
	 */
	protected SmackPresenceListener() { super(); }

	/**
	 * Debug a packet.
	 * @param context <code>java.lang.String</code>
	 * @param packet <code>org.jivesoftware.smack.packet.Packet</code>
	 */
	private void debug(final String context, final Packet packet) {
		logger.debug(logger_Formatter.format(context, packet));
	}
}
