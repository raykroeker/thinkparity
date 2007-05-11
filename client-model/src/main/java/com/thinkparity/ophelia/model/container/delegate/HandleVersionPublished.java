/*
 * Created On:  27-Apr-07 2:19:13 PM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactReceipt;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedEvent;

import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerDelegate;
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
    private VersionPublishedEvent event;

    /** The published by team member. */
    private TeamMember publishedBy;

    /**
     * Create HandleVersionPublishedDelegate.
     *
     */
    public HandleVersionPublished() {
        super();
    }

    /**
     * Obtain the published by team member.
     * 
     * @return A <code>TeamMember</code>.
     */
    public TeamMember getPublishedBy() {
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
                event.getPublishedBy(), event.getPublishedOn());
        final InternalSessionModel sessionModel = getSessionModel();
        final Calendar receivedOn = sessionModel.readDateTime();
        // apply the latest flag
        if (event.isLatestVersion()) {
            applyFlagLatest();
        }
        // update documents
        handleDocumentVersionsResolution(version, event.getDocumentVersions(),
                event.getPublishedBy(), event.getPublishedOn());
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
        containerIO.updatePublishedTo(container.getId(), version.getVersionId(),
                event.getPublishedOn(), localUserId(), receivedOn);
        // add the published by user to the local team
        final List<TeamMember> localTeam = readTeam(container.getId());
        if (contains(localTeam, event.getPublishedBy())) {
            publishedBy = localTeam.get(indexOf(localTeam, event.getPublishedBy()));
        } else {
            publishedBy = getArtifactModel().addTeamMember(container.getId(),
                    event.getPublishedBy());
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
            containerIO.updatePublishedTo(container.getId(),
                    version.getVersionId(), receipt.getPublishedOn(),
                    receivedBy.getId(), receipt.getReceivedOn());
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
        sessionModel.confirmArtifactReceipt(container.getUniqueId(),
                version.getVersionId(), event.getPublishedBy(),
                event.getPublishedOn(),
                /* NOTE this used to read the local team and pass it as a
                 * parameter; which caused ticket #606
                 * 
                 * now the event's published to list is used
                 * 
                 * this also causes a publish of an existing version not to
                 * propogate the distribution chain #555 therefore it was
                 * changed to use the local published to list
                 */
                getIds(containerIO.readPublishedTo(container.getId(),
                        version.getVersionId()), new ArrayList<JabberId>()),
                localUserId(), receivedOn);
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
            final String versionComment, final JabberId publishedBy,
            final Calendar publishedOn) {
        final InternalArtifactModel artifactModel = getArtifactModel();
        final Long containerId = artifactModel.readId(uniqueId);
        final ContainerVersion version;
        if (artifactModel.doesVersionExist(containerId, versionId).booleanValue()) {
            version = readVersion(containerId, versionId);
        } else {
            version = createVersion(containerId, versionId, versionName,
                    versionComment, publishedBy, publishedOn);
        }
        return version;
    }
}
