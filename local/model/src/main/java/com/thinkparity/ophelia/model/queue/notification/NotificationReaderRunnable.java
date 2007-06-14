/*
 * Created On:  4-Jun-07 11:38:49 AM
 */
package com.thinkparity.ophelia.model.queue.notification;

import java.io.IOException;
import java.util.Observable;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.queue.notification.NotificationMonitor;
import com.thinkparity.codebase.model.queue.notification.NotificationReader;
import com.thinkparity.codebase.model.queue.notification.NotificationSession;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NotificationReaderRunnable extends Observable implements
        Runnable {

    /** A log4j wrapper. */
    private final Log4JWrapper logger;

    /** A notification reader. */
    private final NotificationReader reader;

    /** A run indicator. */
    private boolean running;

    /**
     * Create NotificationReaderRunnable.
     * 
     * @param session
     *            A <code>NotificationSession</code>.
     * @throws IOException
     */
    public NotificationReaderRunnable(final NotificationMonitor monitor,
            final NotificationSession session) throws IOException {
        super();
        this.logger = new Log4JWrapper(getClass());
        this.reader = new NotificationReader(monitor, session);
        this.reader.open();
        this.running = true;
    }

    /**
     * Close the reader.
     * 
     * @throws IOException
     */
    public void closeReader() throws IOException {
        closeReader(Boolean.FALSE);
    }

    /**
     * Close the reader.
     * 
     * @throws IOException
     */
    public void closeReader(final Boolean force) throws IOException {
        running = false;
        reader.close(force);
    }

    /**
     * @see java.lang.Runnable#run()
     *
     */
    public void run() {
        while (running) {
            reader.waitForNotification();
            if (reader.didNotify()) {
                setChanged();
                try {
                    notifyObservers(Boolean.TRUE);
                } catch (final Throwable t) {
                    logger.logError(t, "An error occured firing notification.");
                }
            }
        }
    }
}
