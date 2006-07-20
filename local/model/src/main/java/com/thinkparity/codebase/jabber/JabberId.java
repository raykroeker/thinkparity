/*
 * Created On: Jul 20, 2006 11:44:54 AM
 */
package com.thinkparity.codebase.jabber;

import com.thinkparity.model.parity.model.io.xmpp.XMPPMethod;

/**
 * A proxy for the original jabber id such that the XMPPMethod class can use the
 * java type to construct it.
 * 
 * @author raymond@thinkparity.com
 * @version
 * @see XMPPMethod
 */
public class JabberId extends com.thinkparity.model.xmpp.JabberId {

    /**
     * Create a JabberId.
     * 
     * @param username
     *            The jabber username.
     * @param host
     *            The jabber host.
     * @param resource
     *            The jabber resource.
     */
    protected JabberId(final String username, final String host,
            final String resource) {
        super(username, host, resource);
    }
}
