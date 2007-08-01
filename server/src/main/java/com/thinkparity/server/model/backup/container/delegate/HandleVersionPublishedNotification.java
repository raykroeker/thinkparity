/*
 * Created On:  28-Apr-07 10:15:34 AM
 */
package com.thinkparity.ophelia.model.container.delegate;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedNotificationEvent;

import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.user.InternalUserModel;

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

    /** The event. */
    private VersionPublishedNotificationEvent event;

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
        /* no need to replicate the client logic because the backup is the
         * "hidden" team member */
        /* if the version exists locally */
        final InternalArtifactModel artifactModel = getArtifactModel();
        final Long containerId = artifactModel.readId(event.getVersion().getArtifactUniqueId());
        if (artifactModel.doesVersionExist(containerId, event.getVersion().getVersionId())) {
            final ContainerVersion version = readVersion(containerId, event.getVersion().getVersionId());
            /* create the local published to list for the event */
            final InternalUserModel userModel = getUserModel();
            User localPublishedTo;
            ArtifactReceipt receipt;
            for (final User publishedToUser : event.getPublishedTo()) {
                localPublishedTo = userModel.readLazyCreate(publishedToUser.getId());
                receipt = containerIO.readPublishedToReceipt(
                        containerId, version.getVersionId(),
                        event.getPublishedOn(), localPublishedTo);
                if (null == receipt) {
                    containerIO.createPublishedTo(containerId,
                            version.getVersionId(), localPublishedTo,
                            event.getPublishedOn());
                }
            }
        }
    }

    /**
     * Set the handle published notification event.
     * 
     * @param event
     *            A <code>PublishedNotificationEvent</code>.
     */
    public void setEvent(final VersionPublishedNotificationEvent event) {
        this.event = event;
    }
}
