/*
 * Created On:  4-Jun-07 11:21:57 AM
 */
package com.thinkparity.codebase.model.queue.notification;

import java.io.IOException;
import java.util.Arrays;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NotificationReader extends NotificationClient {
    
    /** A notify flag. */
    private boolean notify;

    /**
     * Create NotificationReader.
     * 
     * @param monitor
     *            A <code>NotificationMonitor</code>.
     * @param session
     *            A <code>NotificationSession</code>.
     */
    public NotificationReader(final NotificationMonitor monitor,
            final NotificationSession session) {
        super(monitor, session);
    }

    /**
     * Close the reader.
     * 
     * @throws IOException
     */
    public void close(final Boolean force) throws IOException {
        if (force.booleanValue()) {
            terminate();
        } else {
            disconnect();
        }
    }

    /**
     * Determine whether or not a notification was received.
     * 
     * @return True if a notification was received.
     */
    public Boolean didNotify() {
        return Boolean.valueOf(notify);
    }

    /**
     * Open the reader.
     * 
     * @throws IOException
     */
    public void open() throws IOException {
        connect();
    }

    /**
     * Read the client's input stream and block until a notify response comes
     * through.
     * 
     */
    public void waitForNotification() {
        LOGGER.logTrace("Entry");
        notify = false;
        final String sessionId = getSessionId();
        final byte[] bytes = new byte[sessionId.getBytes().length];
        LOGGER.logTrace("Before read");
        read(bytes);
        LOGGER.logTrace("After read");
        if (Arrays.equals(sessionId.getBytes(getCharset()), bytes)) {
            notify = true;
        }
        LOGGER.logTrace("Exit");
    }
}
