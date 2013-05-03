/**
 * Created On: 9-Oct-07 4:21:09 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

import com.thinkparity.ophelia.browser.application.system.dialog.Notification.NotificationType;

/**
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public interface NotifyPage {

    /**
     * Obtain the page name.
     * 
     * @return A page name <code>String</code>.
     */
    String getPageName();

    /**
     * Obtain the notification type supported by this page.
     * 
     * @return A <code>NotificationType</code>.
     */
    NotificationType getType();

    /**
     * Reload the page with a notification.
     * 
     * @param notification
     *            A <code>Notification</code>.
     */
    void reload(final Notification notification);
}
