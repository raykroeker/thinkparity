/*
 * Created On:  29-Mar-07 2:55:46 PM
 */
package com.thinkparity.ophelia.browser.application.system;

import com.thinkparity.codebase.JVMUniqueId;

import com.thinkparity.ophelia.browser.application.system.dialog.Notification;

/**
 * <b>Title:</b>thinkParity OpheliaUI System Application Default Notification<br>
 * <b>Description:</b>Represents null title headings and content.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class DefaultNotification implements Notification {

    /**
     * Create DefaultNotification.
     *
     */
    public DefaultNotification() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#getContentLine1()
     *
     */
    public String getContentLine1() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#getContentLine2()
     *
     */
    public String getContentLine2() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#getGroupId()
     */
    public String getGroupId() {
        return JVMUniqueId.nextId().toString();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#getHeadingLine1()
     *
     */
    public String getHeadingLine1() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#getHeadingLine2()
     *
     */
    public String getHeadingLine2() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#getId()
     *
     */
    public String getId() {
        return JVMUniqueId.nextId().toString();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#getLinkTitle()
     *
     */
    public String getLinkTitle() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#getNumberLines()
     *
     */
    public int getNumberLines() {
        return 0;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#getTextTitle()
     *
     */
    public String getTextTitle() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#invokeAction()
     *
     */
    public void invokeAction() {}
}
