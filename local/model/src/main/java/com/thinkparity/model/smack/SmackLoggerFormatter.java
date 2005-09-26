/*
 * May 15, 2005
 */
package com.thinkparity.model.smack;

import com.thinkparity.codebase.log4j.LoggerFormatter;

import org.jivesoftware.smack.XMPPConnection;

public class SmackLoggerFormatter extends LoggerFormatter {

	public SmackLoggerFormatter() { super(); }

	public StringBuffer format(final String context,
			final XMPPConnection xmppConnection) {
		return new StringBuffer(openContextInline(context))
			.append(null == xmppConnection ? "null" : xmppConnection.toString())
			.append(closeContextInline(context));
	}
}
