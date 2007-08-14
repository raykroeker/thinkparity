/*
 * Created On:  4-Jun-07 11:38:49 AM
 */
package com.thinkparity.ophelia.model.queue.notification;

import java.io.IOException;
import java.util.Observable;

import com.thinkparity.codebase.BytesFormat;
import com.thinkparity.codebase.StringUtil.Separator;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.queue.notification.NotificationException;
import com.thinkparity.codebase.model.queue.notification.NotificationMonitor;
import com.thinkparity.codebase.model.queue.notification.NotificationReader;
import com.thinkparity.codebase.model.queue.notification.NotificationSession;

/**
 * <b>Title:</b>thinkParity Ophelia Model Queue Notification Client<br>
 * <b>Description:</b>A runnable designed to establish a tcp connection to a
 * server an monitor for notification events. When notification events are
 * received by the client; a queue processor is invoked.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NotificationClient extends Observable implements Runnable {

    /**
     * Maximum number of attempts to open the notification reader before giving
     * up.
     */
    private static final int MAX_OPEN_ATTEMPTS;

    static {
        MAX_OPEN_ATTEMPTS = 3;
    }

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /** A notify override. */
    private Boolean notify;

    /** A notification reader. */
    private NotificationReader reader;

    /** A run indicator. */
    private boolean run;

    /** A notification session. */
    private NotificationSession session;

    /**
     * Create NotificationReaderRunnable.
     * 
     * @param session
     *            A <code>NotificationSession</code>.
     * @throws IOException
     */
    public NotificationClient() {
        super();
        this.logger = new Log4JWrapper(getClass());
        this.run = true;
    }

    /**
     * Disconnect the notification client.
     * 
     */
    public void disconnect() {
        run = false;
        try {
            reader.close(Boolean.FALSE);
        } catch (final IOException iox) {
            logger.logError(iox, "An error occured closing notification reader.");
        }
        if (reader.isOpen()) {
            try {
                reader.close(Boolean.TRUE);
            } catch (final IOException iox) {
                logger.logWarning(iox, "An error occured closing notification reader.");
            }
        }
    }

    /**
     * Obtain the notification session.
     * 
     * @return A <code>NotificationSession</code>.
     */
    public NotificationSession getSession() {
        return session;
    }

    /**
     * Connect the notification client.
     * 
     */
    public void connect() {
        this.reader = new NotificationReader(newMonitor(), session);
        int openAttempt = 0;
        while (true) {
            try {
                reader.open();
                break;
            } catch (final Throwable t) {
                openAttempt++;
                logger.logWarning(t,
                        "Could not connect notification reader {0}/{1}.",
                        openAttempt, MAX_OPEN_ATTEMPTS);
                if (openAttempt > MAX_OPEN_ATTEMPTS) {
                    throw new NotificationException("Could not connect notification reader.");
                } else {
                    try {
                        Thread.sleep(750);
                    } catch (final InterruptedException ix) {}
                }
            }
        }
    }

    /**
     * @see java.lang.Runnable#run()
     *
     */
    public void run() {
        while (run) {
            if (isConnected()) {
                /* allow for a single use notification override */
                if (notify) {
                    notify = Boolean.FALSE;
                    setChanged();
                    notifyPendingEvents();
                } else {
                    reader.waitForNotification();
                    if (reader.didNotify()) {
                        setChanged();
                        notifyPendingEvents();
                    }
                }
            } else {
                try {
                    connect();
                } catch (final NotificationException nx) {
                    run = false;
                    setChanged();
                    notifyClientOffline();
                }
            }
        }
    }

    /**
     * Determine if the client is connected.
     * 
     * @return True if the reader is not null and is open.
     */
    private boolean isConnected() {
        return null != reader && reader.isOpen().booleanValue(); 
    }

    /**
     * Set the notify override.
     * 
     * @param notify
     *            A <code>Boolean</code>.
     */
    public void setNotify(final Boolean notify) {
        this.notify = notify;
    }

    /**
     * Set the notification session.
     * 
     * @param session
     *            A <code>NotificationSession</code>.
     */
    public void setSession(final NotificationSession session) {
        this.session = session;
    }

    /**
     * Create a new notification monitor.
     * 
     * @return A <code>NotificationMonitor</code>.
     */
    private NotificationMonitor newMonitor() {
        final BytesFormat bytesFormat = new BytesFormat();
        return new NotificationMonitor() {

            /**
             * @see com.thinkparity.codebase.model.queue.notification.NotificationMonitor#chunkReceived(int)
             *
             */
            public void chunkReceived(final int chunkSize) {
                logger.logDebug("A notification client chunk ({0}) has been received.{1}    {2}",
                        bytesFormat.format(Integer.valueOf(chunkSize)),
                        Separator.SystemNewLine, session);
            }

            /**
             * @see com.thinkparity.codebase.model.queue.notification.NotificationMonitor#chunkSent(int)
             *
             */
            public void chunkSent(final int chunkSize) {
                logger.logDebug("A notification client chunk ({0}) has been sent.{1}    {2}",
                        bytesFormat.format(Integer.valueOf(chunkSize)),
                        Separator.SystemNewLine, session);
            }

            /**
             * @see com.thinkparity.codebase.model.queue.notification.NotificationMonitor#headerReceived(java.lang.String)
             *
             */
            public void headerReceived(final String header) {
                logger.logDebug("A notification client stream header {0} has been received.{1}    {2}",
                        header, Separator.SystemNewLine, session);
            }

            /**
             * @see com.thinkparity.codebase.model.queue.notification.NotificationMonitor#headerSent(java.lang.String)
             *
             */
            public void headerSent(final String header) {
                logger.logDebug("A notification client stream header {0} has been sent.{1}    {2}",
                        header, Separator.SystemNewLine, session);
            }

            /**
             * @see com.thinkparity.codebase.model.queue.notification.NotificationMonitor#streamError(com.thinkparity.codebase.model.queue.notification.NotificationException)
             *
             */
            public void streamError(final NotificationException error) {
                logger.logWarning(error, "A notification client stream error has occured.{0}    {1}",
                        Separator.SystemNewLine, session);
                try {
                    reader.close(Boolean.TRUE);
                } catch (final IOException iox) {
                    logger.logWarning(iox, "An error occured closing notification client reader.");
                }
            }
        };
    }

    /**
     * Notify all observers that the client is offline.
     *
     */
    private void notifyClientOffline() {
        notifyObservableEvent(ObservableEvent.CLIENT_OFFLINE);
    }

    /**
     * Notify all observers about the event.
     * 
     * @param observableEvent
     *            An <code>ObservableEvent</code>.
     */
    private void notifyObservableEvent(final ObservableEvent observableEvent) {
        try {
            notifyObservers(observableEvent);
        } catch (final Throwable t) {
            logger.logError(t, "An error occured posting event {0}.",
                    observableEvent);
        }        
    }

    /**
     * Notify all observers that there are pending events.
     *
     */
    private void notifyPendingEvents() {
        notifyObservableEvent(ObservableEvent.PENDING_EVENTS);
    }

    /** <b>Title:</b>Observable Event<br> */
    public enum ObservableEvent { CLIENT_OFFLINE, PENDING_EVENTS }
}
