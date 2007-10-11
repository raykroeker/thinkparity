/**
 * Created On: 9-Oct-07 8:00:42 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system;

import com.thinkparity.codebase.JVMUniqueId;

import com.thinkparity.ophelia.browser.application.system.dialog.PriorityNotification;
import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public abstract class DefaultPriorityNotification implements PriorityNotification {

    /** The default id <code>String</code>. */
    private String id;

    /**
     * Create DefaultPriorityNotification.
     */
    public DefaultPriorityNotification() {
        super();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.PriorityNotification#getData()
     */
    public Data getData() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.PriorityNotification#getId()
     */
    public String getId() {
        if (null == id) {
            id = JVMUniqueId.nextId().toString();
        }
        return id;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.PriorityNotification#getMessage()
     */
    public String getMessage() {
        return null;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.PriorityNotification#getPriority()
     */
    public NotificationPriority getPriority() {
        return NotificationPriority.LOW;
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.PriorityNotification#invokeAction()
     */
    public void invokeAction() {}

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.PriorityNotification#invokeDeclineAction()
     */
    public void invokeDeclineAction() {}
}
