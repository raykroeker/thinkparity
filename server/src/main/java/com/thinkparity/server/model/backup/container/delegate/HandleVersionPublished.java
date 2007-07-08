/*
 * Created On:  27-Apr-07 2:19:13 PM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedEvent;

import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerDelegate;
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
    private VersionPublishedEvent event;

    /** The published by user. */
    private User publishedBy;

    /** Whether or not the package was restored from archive. */
    private Boolean restore;

    /**
     * Create HandleVersionPublishedDelegate.
     *
     */
    public HandleVersionPublished() {
        super();
    }

    /**
     * Whether or not the package was restored as a result of the operation.
     * 
     * @return True if the package was restored.
     */
    public Boolean didRestore() {
        return restore;
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
        final Container container = handleResolution();
        final ContainerVersion version = handleVersionResolution(
                container.getUniqueId(), event.getVersion().getVersionId(),
                event.getVersion().getName(), event.getVersion().getComment(),
                event.getVersion().getCreatedBy(),
                event.getVersion().getCreatedOn());
        // apply the latest flag
        if (event.isLatestVersion()) {
            applyFlagLatest();
        }
        // update documents
        handleDocumentVersionsResolution(version, event.getDocumentVersions(),
                event.getPublishedBy(), event.getPublishedOn());
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
            /* the backup receives many occurances of this event which can
             * distribute non-received artifact receipts for the same version;
             * therefore we need to check if received on is set before
             * updating */
            if (receipt.isSetReceivedOn()){
                containerIO.updatePublishedTo(container.getId(),
                        version.getVersionId(), receipt.getPublishedOn(),
                        receivedBy.getId(), receipt.getReceivedOn());
            }
        }
        // resolve the published by user
        final List<TeamMember> localTeam = readTeam(container.getId());
        if (contains(localTeam, event.getPublishedBy())) {
            publishedBy = localTeam.get(indexOf(localTeam, event.getPublishedBy()));
        } else {
            publishedBy = getUserModel().readLazyCreate(event.getPublishedBy());
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
        // restore
        final InternalArtifactModel artifactModel = getArtifactModel();
        restore = artifactModel.isFlagApplied(container.getId(),
                ArtifactFlag.ARCHIVED);
        if (restore.booleanValue()) {
            artifactModel.removeFlagArchived(container.getId());
        }
        // index
        getIndexModel().indexContainer(container.getId());
    }

    /**
     * Set event.
     * 
     * @param event
     *            A <code>VersionPublishedEvent</code>.
     */
    public void setEvent(final VersionPublishedEvent event) {
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
     * @param publishedBy
     *            A container published by user id <code>JabberId</code>.
     * @param publishedOn
     *            A conatiner published on <code>Calendar</code>.
     * @return A <code>ContainerVersion</code>.
     */
    private ContainerVersion handleVersionResolution(final UUID uniqueId,
            final Long versionId, final String versionName,
            final String versionComment, final JabberId createdBy,
            final Calendar createdOn) {
        final InternalArtifactModel artifactModel = getArtifactModel();
        final Long containerId = artifactModel.readId(uniqueId);
        final ContainerVersion version;
        if (artifactModel.doesVersionExist(containerId, versionId).booleanValue()) {
            version = readVersion(containerId, versionId);
        } else {
            version = createVersion(containerId, versionId, versionName,
                    versionComment, createdBy, createdOn);
        }
        return version;
    }
}
