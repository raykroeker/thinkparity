/*
 * Created On:  2007-01-24 18:32:00 PST
 */
package com.thinkparity.desdemona.wildfire.util;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Hashtable;
import java.util.Map;

import com.thinkparity.codebase.Constants;
import com.thinkparity.codebase.FileSystem;
import com.thinkparity.codebase.FileUtil;
import com.thinkparity.codebase.assertion.Assert;
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
 * <b>Title:</b>thinkParity Session Util<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.3
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

    /** A map of user id <code>JabberId</code>s to their <code>Session</code>s. */
    private final Map<JabberId, SessionImpl> sessions;

    /**
     * Create SessionUtil.
     * 
     */
    private SessionUtil() {
        super();
        this.jiveProperties = JiveProperties.getInstance();
        this.sessionManager = XMPPServer.getInstance().getSessionManager();
        this.sessions = new Hashtable<JabberId, SessionImpl>(100, 0.75F);
    }

    /**
     * Delete a session for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    public void destroySession(final org.jivesoftware.wildfire.Session session) {
        final JabberId userId = toJabberId(session.getAddress());
        Assert.assertTrue(sessions.containsKey(userId),
                "Session for user {0} no longer exists.", userId);
        SessionImpl impl = sessions.remove(userId);
        impl.destroy();
        impl = null;
    }

    /**
     * Create a new thinkParity session for an anonymous session.
     * 
     * @param session
     *            A wildfire <code>Session</code>.
     */
    public void initializeAnonymousSession(
            final org.jivesoftware.wildfire.Session session) {
        final JabberId userId = toJabberId(session.getAddress());
        Assert.assertNotTrue(sessions.containsKey(userId),
                "Session for anonymous user {0} has already been initialized.",
                userId.getUsername());
        sessions.put(userId, new AnonymousSessionImpl(getClientSession(userId),
                userId, getXMPPDomain()));
    }

    /**
     * Create a new thinkParity session for a session.
     * 
     * @param session
     *            A wildfire <code>Session</code>.
     */
    public void initializeSession(
            final org.jivesoftware.wildfire.Session session) {
        final JabberId userId = toJabberId(session.getAddress());
        Assert.assertNotTrue(sessions.containsKey(userId),
                "Session for user {0} has already been initialized.",
                userId.getUsername());
        sessions.put(userId, new SessionImpl(getClientSession(userId),
                        getTempDirectory(userId), userId, getXMPPDomain()));
    }

    /**
     * Obtain a session for a query's from user.
     * 
     * @param query
     *            An internet query <code>IQ</code>
     * @return A <code>Session</code>.
     */
    public Session lookupSession(final IQ query) {
        final JabberId userId = JabberIdBuilder.parse(query.getFrom().toString());
        Assert.assertTrue(sessions.containsKey(userId),
                "Session for user {0} no longer exists.", userId);
        return sessions.get(userId);
    }

    /**
     * Obtain a client session for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>ClientSession</code>.
     */
    private ClientSession getClientSession(final JabberId userId) {
        return sessionManager.getSession(toJID(userId));
    }

    /**
     * Obtain a temp directory root for a user. The thinkParity temp root is
     * obtained as a jive property and a user specific root is created below
     * that.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A temp directory <code>File</code>.
     */
    private final File getTempDirectory(final JabberId userId) {
        final File tempRoot = new File((String) jiveProperties.get(JivePropertyNames.THINKPARITY_TEMP_ROOT));
        final File tempDirectory = new File(tempRoot, userId.getUsername());
        if (tempDirectory.exists())
            FileUtil.deleteTree(tempDirectory);
        Assert.assertTrue(tempDirectory.mkdirs(),
                "Cannot create temporary directory {0} for user {1}.",
                tempDirectory.getAbsolutePath(), userId.getUsername());
        return tempDirectory;
    }

    /**
     * Obtain the xmpp domain jive property.
     *      * @return An xmpp domain <code>String</code>.
     */
    private final String getXMPPDomain() {
        return (String) jiveProperties.get(JivePropertyNames.XMPP_DOMAIN);
    }

    /**
     * Convert a jive id to a jabber id.
     * 
     * @param jid
     *            A <code>JID</code>.
     * @return A <code>JabberId</code>.
     */
    private JabberId toJabberId(final JID jid) {
        return JabberIdBuilder.build(jid.getNode(), jid.getDomain(), jid.getResource());
    }

    /**
     * Convert a jabber id to a jive id .
     * 
     * @param jabberId
     *            A <code>JabberId</code>.
     * @return A <code>JID</code>.
     */
    private JID toJID(final JabberId jabberId) {
        return JIDBuilder.buildQualified(jabberId.getQualifiedJabberId());
    }

    /**
     * <b>Title:</b>thinkParity Offline Session Implementation<br>
     * <b>Description:</b>A session representing an offline user.<br>
     */
    private static class AnonymousSessionImpl extends SessionImpl {

        /**
         * Create AnonymousSessionImpl.
         * 
         * @param clientSession
         *            A <code>ClientSession</code>.
         * @param userId
         *            A user id <code>JabberId</code>.
         * @param xmppDomain
         *            The xmpp domain <code>String</code>.
         */
        private AnonymousSessionImpl(final ClientSession clientSession,
                final JabberId userId, final String xmppDomain) {
            super(clientSession, null, userId, xmppDomain);
        }
    }

    /**
     * <b>Title:</b>thinkParity Session Implementation<br>
     * <b>Description:</b>The thinkParity server platform session
     * implementation.<br>
     */
    private static class SessionImpl implements Session {

        /** The user's <code>InetAddress</code>. */
        private final InetAddress inetAddress;

        /** The user's temporary directory root. */
        private final FileSystem tempFileSystem;

        /** The user id <code>JabberId</code>. */
        private final JabberId userId;

        /** The xmpp domain <code>String</code>. */
        private final String xmppDomain;

        /**
         * Create SessionImpl.
         *
         */
        private SessionImpl() {
            super();
            this.userId = null;
            this.inetAddress = null;
            this.tempFileSystem = null;
            this.xmppDomain = null;
        }

        /**
         * Create SessionImpl.
         * 
         * @param clientSession
         *            A user's <code>ClientSession</code>.
         * @param tempRoot
         *            A user's temporary root directory <code>File</code>.
         * @param userId
         *            A user id <code>JabberId</code>.
         * @param xmppDomain
         *            The xmpp domain <code>String</code>.
         */
        private SessionImpl(final ClientSession clientSession,
                final File tempRoot, final JabberId userId,
                final String xmppDomain) {
            this.inetAddress = clientSession.getConnection().getInetAddress();
            this.tempFileSystem = null == tempRoot ? null : new FileSystem(tempRoot);
            this.userId = userId;
            this.xmppDomain = xmppDomain;
        }

        /**
         * @see com.thinkparity.desdemona.model.session.Session#createTempDirectory()
         *
         */
        public File createTempDirectory() throws IOException {
            final String tempFileSuffix = new StringBuffer(".")
                    .append(System.currentTimeMillis())
                    .toString();
            return createTempDirectory(tempFileSuffix);
        }

        /**
         * @see com.thinkparity.desdemona.model.session.Session#createTempDirectory(java.lang.String)
         *
         */
        public File createTempDirectory(final String suffix) throws IOException {
            final File tempDirectory = new File(tempFileSystem.getRoot(), suffix);
            Assert.assertTrue(tempDirectory.mkdir(),
                    "Could not create temp directory {0}.",
                    tempDirectory.getAbsolutePath());
            return tempDirectory;
        }

        /**
         * @see com.thinkparity.desdemona.model.session.Session#createTempFile()
         *
         */
        public File createTempFile() throws IOException {
            final String tempFileSuffix = new StringBuffer(".")
                    .append(System.currentTimeMillis())
                    .toString();
            return createTempFile(tempFileSuffix);
        }

        /**
         * @see com.thinkparity.desdemona.model.session.Session#createTempFile(java.lang.String)
         *
         */
        public File createTempFile(final String suffix) throws IOException {
            return File.createTempFile(
                    Constants.File.TEMP_FILE_PREFIX, suffix, tempFileSystem.getRoot());
        }

        /**
         * @see com.thinkparity.desdemona.model.session.Session#getInetAddress()
         *
         */
        public InetAddress getInetAddress() {
            return inetAddress;
        }

        /**
         * @see com.thinkparity.desdemona.model.session.Session#getJabberId()
         *
         */
        public JabberId getJabberId() {
            return userId;
        }

        /**
         * @see com.thinkparity.desdemona.model.session.Session#getXmppDomain()
         *
         */
        public String getXmppDomain() {
            return xmppDomain;
        }

        /**
         * Destroy the session.
         *
         */
        private void destroy() {
            if (null != tempFileSystem) {
                tempFileSystem.deleteTree();
            }
        }
    }
}    
