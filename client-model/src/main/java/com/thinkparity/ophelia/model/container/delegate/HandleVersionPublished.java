/*
 * Created On:  27-Apr-07 2:19:13 PM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.container.event.LocalVersionPublishedEvent;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;

/**
 * <b>Title:</b>thinkParity OpheliaModel Container Handle Published Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HandleVersionPublished extends ContainerDelegate {

    /** The local container id for the event. */
    private Long containerId;

    /** A version published event. */
    private LocalVersionPublishedEvent event;

    /** Whether or not to notify. */
    private Boolean notify;

    /** The published by user. */
    private User publishedBy;

    /**
     * Create HandleVersionPublishedDelegate.
     *
     */
    public HandleVersionPublished() {
        super();
    }

    /**
     * Obtain the notification indication.
     * 
     * @return A <code>Boolean</code>.
     */
    public Boolean doNotify() {
        return notify;
    }

    /**
     * Obtain the published by team member.
     * 
     * @return A <code>User</code>.
     */
    public User getPublishedBy() {
        return publishedBy;
    }

    /**
     * Handle the publish version event.
     *
     */
    public void handleVersionPublished() {
        /* determine whether or not we need to fire an event */
        notify = !(doesExist(event.getVersion().getArtifactUniqueId())
            && doesVersionExist(event.getVersion().getArtifactUniqueId(),
                    event.getVersion().getVersionId()));
        /* order here is imporant; the team data is linked to the container; and
         * must be in place before the version is created because in the case
         * of a version published event; the version creator/publisher need not
         * be the same user, and resolving the team before resolving the version
         * will ensure the "created by" user is downloaded before the link to
         * the version is made */
        final Container container = handleResolution();
        handleTeamResolution(container, event.getTeam());
        final ContainerVersion version = handleVersionResolution(
                container.getUniqueId(), event.getVersion().getVersionId(),
                event.getVersion().getName(), event.getVersion().getComment(),
                event.getVersion().getCreatedBy(),
                event.getVersion().getCreatedOn());
        final InternalSessionModel sessionModel = getSessionModel();
        final Calendar receivedOn = sessionModel.readDateTime();
        // apply the latest flag
        if (event.isLatestVersion()) {
            applyFlagLatest();
        }
        // update documents
        handleDocumentVersionsResolution(version, event.getDocumentVersions(),
                event.getDocumentVersionFiles(), event.getPublishedBy(),
                event.getPublishedOn());
        // update the local user published to
        final ArtifactReceipt localUserReceipt =
            containerIO.readPublishedToReceipt(container.getId(),
                    version.getVersionId(), event.getPublishedOn(),
                    localUser());
        if (null == localUserReceipt) {
            containerIO.createPublishedTo(container.getId(),
                    version.getVersionId(), localUser(),
                    event.getPublishedOn());
        }
        // update published to for the local user
        containerIO.updatePublishedTo(container.getId(), version.getVersionId(),
                event.getPublishedOn(), localUserId(), receivedOn);
        // resolve the published by user
        final List<TeamMember> localTeam = readTeam(container.getId());
        if (contains(localTeam, event.getPublishedBy())) {
            publishedBy = localTeam.get(indexOf(localTeam, event.getPublishedBy()));
        } else {
            publishedBy = getUserModel().readLazyCreate(event.getPublishedBy());
        }
        // update the local published to list for the received by list
        ArtifactReceipt localReceipt;
        User receivedBy;
        final InternalUserModel userModel = getUserModel();
        for (final ArtifactReceipt receipt : event.getReceivedBy()) {
            receivedBy = userModel.readLazyCreate(receipt.getUser().getId());
            localReceipt = containerIO.readPublishedToReceipt(
                container.getId(), version.getVersionId(),
                receipt.getPublishedOn(), receivedBy);
            if (null == localReceipt) {
                containerIO.createPublishedTo(container.getId(),
                        version.getVersionId(), receivedBy,
                        receipt.getPublishedOn());
            }
            /* the receipt many not include actual received on information; 
             * therefore care must be taken not to update */
            if (receipt.isSetReceivedOn()) {
                containerIO.updatePublishedTo(container.getId(),
                        version.getVersionId(), receipt.getPublishedOn(),
                        receivedBy.getId(), receipt.getReceivedOn());
            }
        }
        // calculate delta information
        final ContainerVersion previous = readPreviousVersion(
                container.getId(), version.getVersionId());
        final ContainerVersion next = readNextVersion(
                container.getId(), version.getVersionId());
        if (null == previous) {
            logger.logInfo("First version of {0}.", container.getName());
        } else {
            containerIO.deleteDelta(container.getId(), version.getVersionId(),
                    previous.getVersionId());
            containerIO.createDelta(calculateDelta(container,
                    version, previous));
        }
        if (null == next) {
            logger.logInfo("Latest version of {0}.", container.getName());
        } else {
            containerIO.deleteDelta(container.getId(), next.getVersionId(),
                    version.getVersionId());
            containerIO.createDelta(calculateDelta(container, next,
                    version));
        }
        // index
        getIndexModel().indexContainer(container.getId());
        // confirm receipt
        containerService.confirmReceipt(getAuthToken(), version,
                event.getPublishedOn(), receivedOn);
    }

    /**
     * Set event.
     * 
     * @param event
     *            A <code>LocalVersionPublishedEvent</code>.
     */
    public void setEvent(final LocalVersionPublishedEvent event) {
        this.event = event;
    }

    /**
     * Apply the latest flag to the container.
     * 
     */
    private void applyFlagLatest() {
        getArtifactModel().applyFlagLatest(getContainerId());
    }

    /**
     * Determine whether or not the artifact exists.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return True if the artifact exists.
     */
    private boolean doesExist(final UUID uniqueId) {
        return getArtifactModel().doesExist(uniqueId).booleanValue();
    }

    /**
     * Determine whether or not the artifact version exists.
     * 
     * @param containerId
     *            An container id <code>Long</code>.
     * @param versionId
     *            An artifact version id <code>Long</code>.
     * @return True if the artifact version exists.
     */
    private boolean doesVersionExist(final Long artifactId, final Long versionId) {
        return getArtifactModel().doesVersionExist(artifactId, versionId).booleanValue();
    }

    /**
     * Determine whether or not the artifact version exists.
     * 
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @param versionId
     *            An artifact version id <code>Long</code>.
     * @return True if the artifact version exists.
     */
    private boolean doesVersionExist(final UUID uniqueId, final Long versionId) {
        return getArtifactModel().doesVersionExist(uniqueId, versionId).booleanValue();
    }

    /**
     * Obtain the local container id for the event version.
     * 
     * @return A container id <code>Long</code>.
     */
    private Long getContainerId() {
        if (null == containerId) {
            containerId = getArtifactModel().readId(
                    event.getVersion().getArtifactUniqueId());
        }
        return containerId;
    }

    /**
     * Handle the resolution of a container.
     * 
     * @return A <code>Container</code>
     * 
     * @see PublishHandlerDelegate#handleResolution(java.util.UUID, JabberId,
     *      Calendar, String)
     */
    private Container handleResolution() {
        return handleResolution(event.getVersion().getArtifactUniqueId(),
                event.getPublishedBy(), event.getPublishedOn(),
                event.getVersion().getArtifactName());
    }

    /**
     * Handle the resolution of a container version. If the version does not
     * exist it will be created.
     * 
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @param versionId
     *            A container version id <code>Long</code>.
     * @param versionName
     *            An optional version name <code>String</code>.
     * @param versionComment
     *            An optional version comment <code>String</code>.
     * @param createdBy
     *            A container version created by user id <code>JabberId</code>.
     * @param createdOn
     *            A container version created on <code>Calendar</code>.
     * @return A <code>ContainerVersion</code>.
     */
    private ContainerVersion handleVersionResolution(final UUID uniqueId,
            final Long versionId, final String versionName,
            final String versionComment, final JabberId createdBy, 
            final Calendar createdOn) {
        final InternalArtifactModel artifactModel = getArtifactModel();
        final ContainerVersion version;
        final Long containerId = artifactModel.readId(uniqueId);
        if (doesVersionExist(containerId, versionId)) {
            version = readVersion(containerId, versionId);
        } else {
            version = createVersion(containerId, versionId, versionName,
                    versionComment, createdBy, createdOn);
        }
        return version;
    }
}
