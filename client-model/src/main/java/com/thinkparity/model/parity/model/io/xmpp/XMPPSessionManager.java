/*
 * Created On: Fri May 12 2006 11:00 PDT
 * $Id$
 */
package com.thinkparity.model.parity.model.io.xmpp;

import java.util.Vector;

import com.thinkparity.model.parity.model.session.Credentials;

/**
 * An xmpp session manager.
 *
 * @author raymond@thinkparity.com
 * @version $Revision$
 */
public class XMPPSessionManager {

    /** All open sessions. */
    private static final Vector<XMPPSession> sessions;

    static {
        sessions = new Vector<XMPPSession>();
        Runtime.getRuntime().addShutdownHook(new Thread("") {
            public void run() {}
        });
    }

    public static XMPPSession openAuthenticated(final String serverHost,
            final Integer serverPort, final Credentials credentials) {
        synchronized(sessions) {
            sessions.add(new XMPPSession(serverHost, serverPort, credentials));
            return sessions.get(sessions.size() - 1);
        }
    }

    /**
     * Open a new anonymous xmpp session.
     *
     * @return An xmpp session.
     */
    public static XMPPSession openAnonymous(final String serverHost,
            final Integer serverPort) {
        synchronized(sessions) {
            sessions.add(new XMPPSession(serverHost, serverPort));
            return sessions.get(sessions.size() - 1);
        }
    }

    /**
     * Close an xmpp session.
     *
     * @param An xmpp session.
     */
    static void close(XMPPSession session) {
        synchronized(sessions) {
            sessions.remove(session);
            session = null;
        }
    }
}
