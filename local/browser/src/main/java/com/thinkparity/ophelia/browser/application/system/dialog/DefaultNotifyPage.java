/**
 * Created On: 9-Oct-07 4:24:58 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

import com.thinkparity.codebase.assertion.Assert;

import com.thinkparity.ophelia.browser.application.system.SystemApplication;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public abstract class DefaultNotifyPage extends javax.swing.JPanel
        implements NotifyPage {

    /** The <code>Notification</code> currently displayed. */
    protected Notification notification;

    /** The <code>SystemApplication</code>. */
    protected final SystemApplication systemApplication;

    /** The online flag. */
    private Boolean online;

    /**
     * Create a new DefaultNotifyPage.
     * 
     * @param systemApplication
     *            The <code>SystemApplication</code>.
     */
    public DefaultNotifyPage(final SystemApplication systemApplication) {
        super();
        this.systemApplication = systemApplication;
        this.online = isOnline();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.NotifyPage#getPageName()
     */
    public final String getPageName() {
        return getType().toString();
    }

    /**
     * @see com.thinkparity.ophelia.browser.application.system.dialog.NotifyPage#reload(com.thinkparity.ophelia.browser.application.system.dialog.Notification)
     */
    public void reload(final Notification notification) {
        this.notification = notification;
        reloadOnline();
    }

    /**
     * Close the notification.
     */
    protected void closeNotification() {
        if (isNotificationLoaded()) {
            systemApplication.clearNotification(notification.getId());
        }
    }

    /**
     * Determine if the system is online.
     * 
     * @return true if the system is online.
     */
    protected Boolean isOnline() {
        return online;
    }

    /**
     * Determine if a notification has been loaded.
     * 
     * @return true if a notification has been loaded.
     */
    private boolean isNotificationLoaded() {
        return null != notification;
    }

    /**
     * Reload the online status.
     */
    private void reloadOnline() {
        this.online = Boolean.FALSE;
        switch (systemApplication.getConnection()) {
        case OFFLINE:
            this.online = Boolean.FALSE;
            break;
        case ONLINE:
            this.online = Boolean.TRUE;
            break;
        default:
            throw Assert.createUnreachable("Unknown connection.");
        }
    }
}
