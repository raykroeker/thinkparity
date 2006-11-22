/*
 * Apr 26, 2006
 */
package com.thinkparity.ophelia.browser.application.system.notify;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Notification {

    private String message;

    /**
     * Create a TrayNotification.
     */
    public Notification() { super(); }

    /**
     * @return Returns the message.
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message to set.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
