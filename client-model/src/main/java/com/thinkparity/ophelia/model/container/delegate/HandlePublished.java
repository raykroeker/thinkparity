/*
 * Created On:  27-Apr-07 2:19:13 PM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.container.ContainerVersion;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedEvent;

import com.thinkparity.ophelia.model.container.ContainerDelegate;
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

    /** A published event. */
    private PublishedEvent event;

    /** The published by user. */
    private User publishedBy;

    /**
     * Create HandlePublishedDelegate.
     *
     */
    public HandlePublished() {
        super();
    }

    /**
     * Obtain the published by team member.
     * 
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
        final InternalSessionModel sessionModel = getSessionModel();
        final Container container = handleResolution();
        final ContainerVersion version = createVersion(container.getId(),
                event.getVersion().getVersionId(), event.getVersion().getName(),
                event.getVersion().getComment(), event.getPublishedBy(),
                event.getVersion().getCreatedOn());
        handleTeamResolution(container, event.getTeam());
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
