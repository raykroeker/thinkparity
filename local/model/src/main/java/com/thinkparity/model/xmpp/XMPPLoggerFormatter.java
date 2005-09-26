/*
 * May 15, 2005
 */
package com.thinkparity.model.xmpp;

import com.thinkparity.codebase.StringUtil.Separator;

import com.thinkparity.model.smack.SmackLoggerFormatter;

import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.packet.Packet;

/**
 * XMPPLoggerFormatter
 * Used as a singleton debugger for xmpp objects.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class XMPPLoggerFormatter extends SmackLoggerFormatter {

	/**
	 * Create an XMPPLoggerFormatter
	 */
	public XMPPLoggerFormatter() { super(); }

	public StringBuffer format(final String context, final Packet packet) {
		return new StringBuffer(openContext(context))
			.append(null == packet ? "null" : packet.toXML())
			.append(closeContext(context));
	}

	/**
	 * Given a context and a roster entry, format a debug message.
	 * @param context <code>java.lang.String</code>
	 * @param rosterEntry <code>org.jivesoftware.smack.RosterEntry</code>
	 * @return <code>java.lang.StringBuffer</code>
	 */
	public StringBuffer format(final String context, final RosterEntry rosterEntry) {
		final StringBuffer message = new StringBuffer()
			.append(openContext(context))
			.append("<roster.entry>");
		if(null != rosterEntry) {
			message.append(Separator.SystemNewLine).append(Separator.Tab).append(Separator.Tab)
				.append("<name>").append(rosterEntry.getName()).append("</name>")
				.append(Separator.SystemNewLine).append(Separator.Tab).append(Separator.Tab)
				.append("<type>").append(rosterEntry.getType()).append("</type>")
				.append(Separator.SystemNewLine).append(Separator.Tab).append(Separator.Tab)
				.append("<user>").append(rosterEntry.getUser()).append("</user>")
				.append(Separator.SystemNewLine).append(Separator.Tab);
		}
		else { message.append("null"); }
		return message.append("</roster.entry>")
			.append(closeContext(context));
	}
}
