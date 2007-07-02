/*
 * Created On:  4-Jun-07 12:39:13 PM
 */
package com.thinkparity.codebase.model.queue.notification;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class NotificationException extends RuntimeException {

    /**
     * Create NotificationException.
     * 
     * @param message
     *            The exception message.
     */
    NotificationException(final String message) {
        super(message);
    }
    
    /**
     * Create NotificationException.
     * 
     * @param cause
     *            The cause of the notification exception.
     */
    public NotificationException(final Throwable cause) {
        super(cause);
    }
}
