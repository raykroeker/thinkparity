/*
 * Jun 6, 2005
 */
package com.thinkparity.model.smackx;


import com.thinkparity.model.parity.util.log4j.BrowserLoggerFactory;
import com.thinkparity.model.xmpp.XMPPLoggerFormatter;

import org.apache.log4j.Logger;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.packet.Packet;

/**
 * XListener
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class XListener implements PacketListener {

	/**
	 * Handle to an internal logger.
	 */
	private static final Logger logger =
		BrowserLoggerFactory.getLogger(XListener.class);

	/**
	 * Handle to a log formatter for xmpp.
	 */
	private static final XMPPLoggerFormatter logger_Formatter =
		new XMPPLoggerFormatter();

	/**
	 * Debug a packet.
	 * @param context <code>java.lang.String</code>
	 * @param packet <code>org.jivesoftware.smack.packet.Packet</code>
	 */
	protected void debug(final String context, final Packet packet) {
		logger.debug(logger_Formatter.format(context, packet));
	}
}
