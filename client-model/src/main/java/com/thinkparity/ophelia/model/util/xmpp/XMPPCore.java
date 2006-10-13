/*
 * Feb 28, 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketFilter;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;

/**
 * Provides core xmpp functionality.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface XMPPCore {
	public void addPacketListener(final PacketListener listener,
            final PacketFilter filter);
    public XMPPMethodResponse execute(final XMPPMethod method);
    public XMPPMethodResponse execute(final XMPPMethod method,
            final Boolean assertResult);
    public JabberId getJabberId();

    /**
     * Translate an error into an unchecked error.
     * 
     * @param t
     *            An error <code>Throwable</code>.
     */
    public RuntimeException translateError(final Throwable t);
}
