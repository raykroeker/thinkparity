/**
 * 
 */
package com.thinkparity.desdemona.wildfire.util;

import java.net.InetAddress;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.jabber.JabberIdBuilder;

import com.thinkparity.desdemona.model.Constants.JivePropertyNames;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.wildfire.JIDBuilder;

import org.jivesoftware.util.JiveProperties;
import org.jivesoftware.wildfire.ClientSession;
import org.jivesoftware.wildfire.SessionManager;
import org.jivesoftware.wildfire.XMPPServer;
import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class SessionUtil {

    /** A singleton instance of <code>SessionUtil</code>. */
    private static SessionUtil INSTANCE;

    /**
     * Obtain an instance of session util.
     * 
     * @return A <code>SessionUtil</code>.
     */
    public static SessionUtil getInstance() {
        if (null == INSTANCE) {
            INSTANCE = new SessionUtil();
        }
        return INSTANCE;
    }

    /** A set of <code>JiveProperties</code>. */
    private final JiveProperties jiveProperties;

    /** A Wildfire <code>SessionManager</code>. */
    private final SessionManager sessionManager;

    /**
     * Create SessionUtil.
     * 
     */
    private SessionUtil() {
        super();
        this.jiveProperties = JiveProperties.getInstance();
        this.sessionManager = XMPPServer.getInstance().getSessionManager();
    }

    /**
     * Create a new thinkParity session for an xmpp query.
     * 
     * @param query
     *            An xmpp query <code>IQ</code>.
     * @return A thinkParity <code>Session</code>.
     */
    public Session createSession(final IQ query) {
        return new Session() {
            /** A user id <code>JabberId</code>. */
            private final JabberId userId =
                JabberIdBuilder.parseQualifiedJabberId(query.getFrom().toString());
            /** A user's internet address. */
            private final InetAddress inetAddress =
                getClientSession(userId).getConnection().getInetAddress();
            public InetAddress getInetAddress() {
                return inetAddress;
            }
            public JabberId getJabberId() {
                return userId;
            }
            public String getXmppDomain() {
                return (String) jiveProperties.get(JivePropertyNames.XMPP_DOMAIN);
            }
        };
    }

    /**
     * Determine if the user id is online.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True if the user is online.
     */
    public Boolean isOnline(final JabberId userId) {
        if (0 < sessionManager.getSessionCount(userId.getUsername())) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    /**
     * Obtain a client session for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>ClientSession</code>.
     */
    private ClientSession getClientSession(final JabberId userId) {
        final JID jid = JIDBuilder.buildQualified(userId.getQualifiedJabberId());
        return sessionManager.getSession(jid);
    }
}
