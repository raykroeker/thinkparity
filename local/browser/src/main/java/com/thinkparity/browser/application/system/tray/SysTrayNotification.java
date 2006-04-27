/*
 * Apr 26, 2006
 */
package com.thinkparity.browser.application.system.tray;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SysTrayNotification {

    private String message;

    /**
     * Create a SysTrayNotification.
     */
    public SysTrayNotification() { super(); }

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
