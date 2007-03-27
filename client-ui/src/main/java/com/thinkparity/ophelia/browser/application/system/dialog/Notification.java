/*
 * Apr 26, 2006
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface Notification {
    public String getContentLine1();
    public String getContentLine2();
    public String getHeadingLine1();
    public String getHeadingLine2();
    public int getNumberLines();
    public String getTitle();
    public void invokeAction();
}
