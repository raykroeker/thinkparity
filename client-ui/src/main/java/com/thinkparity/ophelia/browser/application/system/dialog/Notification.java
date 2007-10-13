/**
 * Created On: 9-Oct-07 3:57:24 PM
 * $Id$
 */
package com.thinkparity.ophelia.browser.application.system.dialog;

import com.thinkparity.ophelia.browser.platform.action.Data;

/**
 * @author raymond@thinkparity.com, robert@thinkparity.com
 * @version $Revision$
 */
public interface Notification {

    /**
     * Obtain the data.
     * 
     * @return A <code>Data</code>.
     */
    Data getData();

    /**
     * Obtain the notification group id.
     * 
     * @return A notification group id <code>String</code>.
     */
    String getGroupId();

    /**
     * Obtain the notification id.
     * 
     * @return A notification id <code>String</code>.
     */
    String getId();

    /**
     * Obtain the message.
     * 
     * @return A message <code>String</code>.
     */
    String getMessage();

    /**
     * Obtain the notification priority.
     * 
     * @return A <code>NotificationPriority</code>.
     */
    NotificationPriority getPriority();

    /**
     * Obtain the notification type.
     * 
     * @return A <code>NotificationType</code>.
     */
    NotificationType getType();

    /**
     * Invoke the notification's primary (or accept) action.
     */
    void invokeAction();

    /**
     * Invoke the notification's decline action.
     */
    void invokeDeclineAction();

    /**
     * NotificationPriority enum.
     */
    public enum NotificationPriority {

        HIGHEST(4), // PRODUCT_INSTALLED
        HIGH(3),    // EMAIL_UPDATED
        MEDIUM(2),  // PROFILE_PASSIVATED
        LOW(1),     // INVITATION
        LOWEST(0);  // CONTAINER_PUBLISHED

        /** An <code>Integer</code> priority. */
        private Integer priority;

        /**
         * Create NotificationPriority.
         * 
         * @param priority
         *            An <code>Integer</code> priority.
         */
        private NotificationPriority(final Integer priority) {
            this.priority = priority;
        }

        /**
         * Obtain the priority integer value.
         * 
         * @return The <code>Integer</code> priority.
         */
        public Integer toInteger() {
            return priority;
        }
    }

    public enum NotificationType { CONTAINER_PUBLISHED, EMAIL_UPDATED, INVITATION, PRODUCT_INSTALLED, PROFILE_PASSIVATED }
}
