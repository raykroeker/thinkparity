/*
 * Created On:  4-Jun-07 9:07:12 AM
 */
package com.thinkparity.desdemona.model.queue.notification;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.util.DesdemonaProperties;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class NotificationServer {

    /** All active sessions. */
    private static final Map<String, Long> SESSION_USER_LOOKUP;

    /** All active sessions. */
    private static final Map<String, ServerNotificationSession> SESSIONS;

    /** All active sessions. */
    private static final Map<Long, String> USER_SESSION_LOOKUP;

    static {
        SESSIONS = new HashMap<String, ServerNotificationSession>(50);
        USER_SESSION_LOOKUP = new HashMap<Long, String>(50);
        SESSION_USER_LOOKUP = new HashMap<String, Long>(50);
    }

    /** The charset used when reading/writing. */
    private final Charset charset;

    /** A <code>Log4JWrapper</code>. */
    private final Log4JWrapper logger;

    /** The socket server. */
    private final NotificationSocketServer socketServer;

    /**
     * Create NotificationServer.
     *
     */
    NotificationServer() {
        super();
        final DesdemonaProperties properties = DesdemonaProperties.getInstance();
        this.logger = new Log4JWrapper("NotificationService");
        this.charset = Charset.forName(properties.getProperty("thinkparity.queue.notification-charset"));
        final String bindHost = properties.getProperty("thinkparity.queue.notification.bind-host");
        final Integer bindPort = Integer.valueOf(properties.getProperty("thinkparity.queue.notification.bind-port"));
        this.socketServer = new NotificationSocketServer(this, bindHost, bindPort);
    }

    /**
     * Obtain the character set used by the notification server.
     * 
     * @return A <code>Charset</code>.
     */
    Charset getCharset() {
        return charset;
    }

    /**
     * Obtain a session.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @return A <code>ServerNotificationSession</code> or null if no such
     *         session exists.
     */
    ServerNotificationSession getSession(final String sessionId) {
        synchronized (SESSIONS) {
            return SESSIONS.get(sessionId);
        }
    }

    /**
     * Obtain a session.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>ServerNotificationSession</code> or null if no such
     *         session exists.
     */
    ServerNotificationSession getSession(final User user) {
        synchronized (SESSIONS) {
            final String sessionId = USER_SESSION_LOOKUP.get(user.getLocalId());
            if (null == sessionId) {
                return null;
            } else {
                return SESSIONS.get(sessionId);
            }
        }
    }

    /**
     * Initialize the notification server for a given sesion.
     * 
     * @param session
     *            A <code>ServerNotificationSession</code>.
     */
    void initialize(final User user, final ServerNotificationSession session) {
        Assert.assertNotNull("Session is null.", session);
        Assert.assertNotNull("Session charset is null.", session.getCharset());
        Assert.assertNotNull("Session id is null.", session.getId());
        Assert.assertNotNull("Session server host is null.", session.getServerHost());
        Assert.assertNotNull("Session server port is null.", session.getServerPort());
        final String sessionId;
        synchronized (SESSIONS) {
            sessionId = USER_SESSION_LOOKUP.get(user.getLocalId());
        }
        if (null != sessionId) {
            removeSession(sessionId);
        }
        synchronized (SESSIONS) {
            SESSIONS.put(session.getId(), session);
            SESSION_USER_LOOKUP.put(session.getId(), user.getLocalId());
            USER_SESSION_LOOKUP.put(user.getLocalId(), session.getId());
        }
    }

    /**
     * Log the notification server statistics.
     *
     */
    void logStatistics() {
        logger.logInfo("Notification server charset:{0}", charset);
        logger.logInfo("Notification server sessions:{0}", SESSIONS.size());
        for (final Long userId : SESSION_USER_LOOKUP.values()) {
            logger.logInfo("User {0} -> session {1}.", userId, USER_SESSION_LOOKUP.get(userId));
        }
        for (final ServerNotificationSession session : SESSIONS.values()) {
            logger.logInfo("Notification server session:{0}", session.getId());
        }
        socketServer.logStatistics();
    }

    /**
     * Remove a session.
     * 
     * @param sessionId
     *            A session id <code>String</code>.
     * @return The previous <code>ServerNotificationSession</code> or null if
     *         no such session existed.
     */
    ServerNotificationSession removeSession(final String sessionId) {
        synchronized (SESSIONS) {
            final Long userId = SESSION_USER_LOOKUP.get(sessionId);
            if (null == userId) {
                logger.logWarning(
                        "Notification session {0} has been disconnected.",
                        sessionId);
            }
            SESSION_USER_LOOKUP.remove(sessionId);
            USER_SESSION_LOOKUP.remove(userId);
            return SESSIONS.remove(sessionId);
        }
    }

    /**
     * Send a notification.
     * 
     * @param sessionId
     *            A notification session id <code>String</code>.
     */
    void send(final User user) {
        final ServerNotificationSession session = getSession(user);
        if (null == session) {
            logger.logWarning("Session for user {0} has been disconnected.",
                    user.getUsername());
        } else {
            final NotificationSocketDelegate delegate = session.getDelegate();
            if (null == delegate) {
                logger.logWarning("Session for user {0} has been disconnected.",
                        user.getUsername());
            } else {
                delegate.sendNotify();
            }
        }
    }

    /**
     * Start the notification server.
     *
     */
    void start() throws InterruptedException, IOException {
        logger.logTraceId();
        logger.logInfo("Starting notification server.");
        socketServer.start();
        logger.logInfo("Notification server started.");
    }

    /**
     * Stop the notification server.
     * 
     * @param wait
     *            Whether or not to wait for remote sessions to disconnect.
     */
    void stop(final Boolean wait) throws InterruptedException, IOException {
        logger.logTraceId();
        logger.logInfo("Stopping notification server.");
        socketServer.stop(wait);
        logger.logInfo("Notification server stopped.");
    }
}
