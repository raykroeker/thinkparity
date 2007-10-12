/**
 * Created On: 9-Oct-07 8:00:42 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system;

import com.thinkparity.codebase.JVMUniqueId;

import com.thinkparity.ophelia.browser.application.system.dialog.Notification;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author raymond@thinkparity.com, robert@thinkparity.com
 * @version $Revision$
 */
public abstract class DefaultNotification implements Notification {

    /** The default id <code>String</code>. */
    private String id;

    /**
     * Create DefaultNotification.
     */
    public DefaultNotification() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#getData()
     */
    public Data getData() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#getGroupId()
     */
    public String getGroupId() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#getId()
     */
    public String getId() {
        if (null == id) {
            id = JVMUniqueId.nextId().toString();
        }
        return id;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#getMessage()
     */
    public String getMessage() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#getPriority()
     */
    public NotificationPriority getPriority() {
        return NotificationPriority.LOW;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#invokeAction()
     */
    public void invokeAction() {}

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.Notification#invokeDeclineAction()
     */
    public void invokeDeclineAction() {}
}
