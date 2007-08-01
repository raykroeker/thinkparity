/*
 * Created On:  28-Apr-07 10:15:34 AM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedNotificationEvent;

import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.container.ContainerDraft;
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

    /** The container. */
    private Container container;

    /** The notification event. */
    private VersionPublishedNotificationEvent event;

    /** Whether or not to fire a notification post invocation. */
    private Boolean notify;

    /** The artifact receipts for the version. */
    private final List<ArtifactReceipt> receipts;

    /** The version. */
    private ContainerVersion version;

    /**
     * Create HandlePublishedNotificationDelegate.
     *
     */
    public HandleVersionPublishedNotification() {
        super();
        this.notify = Boolean.FALSE;
        this.receipts = new ArrayList<ArtifactReceipt>();
    }

    /**
     * Determine whether or not to fire a notification post invocation.
     * 
     * @return True if a notification should be fired.
     */
    public Boolean doNotify() {
        return notify;
    }

    /**
     * Obtain the container.
     * 
     * @return A <code>Container</code>.
     */
    public Container getContainer() {
        return container;
    }

    /**
     * Obtain the version receipts.
     * 
     * @return A <code>List<ArtifactReceipt></code>.
     */
    public List<ArtifactReceipt> getReceipts() {
        return receipts;
    }

    /**
     * Obtain the version.
     * 
     * @return A <code>ContainerVersion</code>.
     */
    public ContainerVersion getVersion() {
        return version;
    }

    /**
     * Handle the version published notification remote event.
     *
     */
    public void handleVersionPublishedNotification() throws CannotLockException {
        final InternalArtifactModel artifactModel = getArtifactModel();
        final Long containerId = artifactModel.readId(event.getVersion().getArtifactUniqueId());
        container = read(containerId);
        // create draft if one exists remotely
        final JabberId draftOwnerId = readKeyHolder(container);
        if (draftOwnerId.equals(User.THINKPARITY.getId())) {
            logger.logInfo("No remote draft exists for {0}.", container.getName());
        } else {
            final ContainerDraft localDraft = readDraft(container.getId());
            if (null == localDraft) {
                logger.logInfo("Creating local draft for {0}.", container.getName());
                final List<TeamMember> team = artifactIO.readTeamRel2(container.getId());
                final ContainerDraft draft = new ContainerDraft();
                draft.setLocal(Boolean.FALSE);
                draft.setContainerId(container.getId());
                draft.setOwner(team.get(indexOf(team, draftOwnerId)));
                containerIO.createDraft(draft);
            } else {
                if (draftOwnerId.equals(localDraft.getOwner().getId())) {
                    logger.logInfo("Draft owner {0} does matches {1} for {2}.",
                            localDraft.getOwner().getId(), draftOwnerId,
                            container.getName());
                } else {
                    logger.logWarning("Draft owner {0} does not match {1} for {2}.",
                            localDraft.getOwner().getId(), draftOwnerId,
                            container.getName());
                }
            }
        }
        /* if the version exists locally */
        if (artifactModel.doesVersionExist(containerId, event.getVersion().getVersionId())) {
            version = readVersion(containerId, event.getVersion().getVersionId());
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
                    notify = Boolean.TRUE;
                    containerIO.createPublishedTo(containerId,
                            version.getVersionId(), localPublishedTo,
                            event.getPublishedOn());
                }
            }
            if (notify.booleanValue()) {
                receipts.addAll(readPublishedTo(version));
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

    private JabberId readKeyHolder(final Container container) {
        return getSessionModel().readKeyHolder(container.getUniqueId());
    }
}
