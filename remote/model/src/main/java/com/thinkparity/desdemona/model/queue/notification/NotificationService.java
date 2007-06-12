/*
 * Created On:  4-Jun-07 9:00:02 AM
 */
package com.thinkparity.desdemona.model.queue.notification;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParit Desdemona Model Queue Notification Service<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NotificationService {

    /** A singleton instance of the notification service. */
    private static final NotificationService SINGLETON;

    static {
        SINGLETON = new NotificationService();
    }

    /**
     * Obtain an instance of the notification service.
     * 
     * @return An instance of <code>NotificationService</code>.
     */
    public static NotificationService getInstance() {
        return SINGLETON;
    }

    /** A <code>Log4JWrapper</code>. */
    private final Log4JWrapper logger;

    /** A notification socket server. */
    private NotificationServer server;

    /**
     * Create NotificationService.
     *
     */
    private NotificationService() {
        super();
        this.logger = new Log4JWrapper("NotificationService");
    }

    /**
     * Initialize a session.
     * 
     * @param session
     *            A <code>NotificationSession</code>.
     */
    public void initialize(final User user,
            final ServerNotificationSession session) {
        server.initialize(user, session);
    }

    /**
     * Log the notification service statistics.
     *
     */
    public void logStatistics() {
        if (null == server) {
            logger.logWarning("Notification service has not been started.");
        } else {
            server.logStatistics();
        }
    }

    /**
     * Send a notification.
     * 
     * @param sessionId
     *            A notification session id <code>String</code>.
     */
    public void send(final User user) {
        logger.logTraceId();
        logger.logInfo("Sending notification.");
        server.send(user);
        logger.logInfo("Notification sent.");
    }

    /**
     * Start the notification service.
     *
     */
    public void start() {
        logger.logTraceId();
        logger.logInfo("Starting notification service.");
        synchronized (this) {
            startImpl();
        }
        logger.logInfo("Notification service started.");
    }

    /**
     * Stop the notification service.
     *
     */
    public void stop() {
        logger.logTraceId();
        logger.logInfo("Stopping notification service.");
        synchronized (this) {
            stopImpl();
        }
        logger.logInfo("Notification service stopped.");
    }

    /**
     * Start implementation.
     *
     */
    private void startImpl() {
        try {
            server = new NotificationServer();
            server.start();
        } catch (final Throwable t) {
            server = null;
            throw new RuntimeException(t);
        }
    }

    /**
     * Stop implementation.
     *
     */
    private void stopImpl() {
        try {
            server.stop(Boolean.TRUE);
        } catch (final Throwable t) {
            throw new RuntimeException(t);
        } finally {
            server = null;
        }
    }
}
