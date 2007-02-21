/*
 * Apr 26, 2006
 */
package com.thinkparity.ophelia.browser.application.system.notify;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Notification {
    public String getMessage();
    public String getTitle();
    public void invokeAction();
}
