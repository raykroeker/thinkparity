/*
 * Created On:  27-Apr-07 2:19:13 PM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedEvent;

import com.thinkparity.ophelia.model.artifact.InternalArtifactModel;
import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.container.ContainerDraft;
import com.thinkparity.ophelia.model.document.CannotLockException;
import com.thinkparity.ophelia.model.session.InternalSessionModel;
import com.thinkparity.ophelia.model.user.InternalUserModel;

/**
 * <b>Title:</b>thinkParity OpheliaModel Container Handle Published Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HandlePublished extends ContainerDelegate {

    /** The existing container draft. */
    private ContainerDraft draft;

    /** A published event. */
    private PublishedEvent event;

    /** The published by user. */
    private User publishedBy;

    /** Whether or not the package was restored from archive. */
    private Boolean restore;

    /**
     * Create HandlePublishedDelegate.
     *
     */
    public HandlePublished() {
        super();
    }

    /**
     * Determine whether or not the package was restored from the archive as
     * part of the publish.
     * 
     * @return True if the package was restored.
     */
    public Boolean didRestore() {
        return restore;
    }

    /**
     * Obtain the container draft.
     * 
     * @return A <code>ContainerDraft</code>.
     */
    public ContainerDraft getDraft() {
        return draft;
    }

    /**
     * Obtain the published by team member.
     * @return A the published by <code>TeamMember</code>.
     */
    public User getPublishedBy() {
        return publishedBy;
    }

    /**
     * Handle the publish event.
     *
     */
    public void handlePublished() throws CannotLockException {
        final InternalArtifactModel artifactModel = getArtifactModel();
        final InternalSessionModel sessionModel = getSessionModel();
        final Container container = handleResolution();
        final ContainerVersion version = createVersion(container.getId(),
                event.getVersion().getVersionId(), event.getVersion().getName(),
                event.getVersion().getComment(), event.getPublishedBy(),
                event.getVersion().getCreatedOn());
        final Calendar receivedOn = sessionModel.readDateTime();
        // update documents
        handleDocumentVersionsResolution(version, event.getDocumentVersions(),
                event.getPublishedBy(), event.getPublishedOn());
        // build published to list
        final InternalUserModel userModel = getUserModel();
        final List<JabberId> publishedToIds = new ArrayList<JabberId>(event.getPublishedTo().size());
        final List<User> publishedToUsers = new ArrayList<User>(event.getPublishedTo().size());
        for (final User publishedToUser : event.getPublishedTo()) {
            publishedToIds.add(publishedToUser.getId());
            publishedToUsers.add(userModel.readLazyCreate(publishedToUser.getId()));
        }
        // resolve published by user
        final List<TeamMember> localTeam = readTeam(container.getId());
        if (contains(localTeam, event.getPublishedBy())) {
            publishedBy = localTeam.get(indexOf(localTeam, event.getPublishedBy()));
        } else {
            publishedBy = getUserModel().readLazyCreate(event.getPublishedBy());
        }
        // delete draft
        draft = readDraft(container.getId());
        if (null == draft) {
            logger.logInfo("Draft did not previously exist for {0}.",
                    container.getName());
        } else {
            deleteDraft(container.getId());
        }
        // create published to list
        containerIO.createPublishedTo(container.getId(),
                version.getVersionId(), publishedToUsers,
                event.getPublishedOn());
        // update published to for local user
        containerIO.updatePublishedTo(container.getId(),
                version.getVersionId(), event.getPublishedOn(),
                localUserId(), receivedOn);
        // calculate differences
        final ContainerVersion previous = readPreviousVersion(
                container.getId(), version.getVersionId());
        if (null == previous) {
            logger.logInfo("First version of {0}.", container.getName());
        } else {
            containerIO.createDelta(calculateDelta(container, version, previous));
        }
        // restore
        restore = artifactModel.isFlagApplied(container.getId(),
                ArtifactFlag.ARCHIVED);
        if (restore.booleanValue()) {
            artifactModel.removeFlagArchived(container.getId());
        }
        // index
        getIndexModel().indexContainer(container.getId());
        // send confirmation
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
     *		A ContainerPublishedEvent.
     */
    public void setEvent(final PublishedEvent event) {
        this.event = event;
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
}
