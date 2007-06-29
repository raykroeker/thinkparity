/*
 * Created On:  28-Apr-07 10:15:34 AM
 */
package com.thinkparity.ophelia.model.container.delegate;

import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedNotificationEvent;

import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.document.CannotLockException;

/**
 * <b>Title:</b>thinkParity OpheliaModel Container Handle Published
 * Notification Delegate<br>
 * <b>Description:</b>Handles the event fired to everyone indicating a version
 * has been published. Is used to keep the team definition up to date.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HandleVersionPublishedNotification extends
        ContainerDelegate {

    /**
     * Create HandlePublishedNotificationDelegate.
     *
     */
    public HandleVersionPublishedNotification() {
        super();
    }

    /**
     * Handle the version published notification remote event.
     *
     */
    public void handleVersionPublishedNotification() throws CannotLockException {
    }

    /**
     * Set the handle published notification event.
     * 
     * @param event
     *            A <code>PublishedNotificationEvent</code>.
     */
    public void setEvent(final VersionPublishedNotificationEvent event) {
    }
}
