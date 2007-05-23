/*
 * Created On:  28-Apr-07 10:15:34 AM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.util.List;

import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.container.PublishedNotificationEvent;

import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.document.CannotLockException;

/**
 * <b>Title:</b>thinkParity OpheliaModel Container Handle Published
 * Notification Delegate<br>
 * <b>Description:</b>Handles the event fired to everyone indicating a new
 * version has been published. Is used to keep the is latest flag up to date, as
 * well the team definition and draft information.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HandlePublishedNotification extends
        ContainerDelegate {

    /** The event container version's local container id. */
    private Long containerId;

    /** A published notification event. */
    private PublishedNotificationEvent event;

    /**
     * Create HandlePublishedNotificationDelegate.
     *
     */
    public HandlePublishedNotification() {
        super();
    }

    /**
     * Handle the publish notification remote event.
     *
     */
    public void handlePublishedNotification() throws CannotLockException {
        // apply/remove the latest flag
        if (doesVersionExist()) {
            applyFlagLatest();
        } else {
            removeFlagLatest();
        }
        // update the team definition
        final List<TeamMember> localTeam = readTeam();
        final List<User> eventTeam = getTeam();
        for (final User eventTeamUser : eventTeam) {
            if (!contains(localTeam, eventTeamUser)) {
                addTeamMember(eventTeamUser);
            }
        }
        // if a draft exists locally delete it
        if (doesExistDraft()) {
            logger.logWarning("Invalid state.  Local draft for event {0} should not exist.",
                    event);
            deleteDraft();
        }
    }

    /**
     * Set the handle published notification event.
     * 
     * @param event
     *            A <code>PublishedNotificationEvent</code>.
     */
    public void setEvent(final PublishedNotificationEvent event) {
        this.event = event;
    }

    /**
     * Add a user to the team.
     * 
     * @param user
     *            A <code>User</code>.
     * @return A <code>TeamMember</code>.
     */
    private TeamMember addTeamMember(final User user) {
        return getArtifactModel().addTeamMember(getContainerId(), user.getId());
    }

    /**
     * Apply the latest flag to the container.
     * 
     */
    private void applyFlagLatest() {
        getArtifactModel().applyFlagLatest(getContainerId());
    }

    /**
     * Delete the draft.
     * 
     */
    private void deleteDraft() throws CannotLockException {
        deleteDraft(getContainerId());
    }

    /**
     * Determine whether or not a draft exists.
     * 
     * @return True if a draft eixsts.
     */
    private boolean doesExistDraft() {
        return doesExistDraft(getContainerId()).booleanValue();
    }

    /**
     * Determine if the container version for the event exists.
     * 
     * @return True if the container version exists.
     */
    private boolean doesVersionExist() {
        return getArtifactModel().doesVersionExist(getContainerId(),
                getVersionId());
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
     * Obtain the event's team users.
     * 
     * @return A <code>List</code> of <code>User</code>s.
     */
    private List<User> getTeam() {
        return event.getTeam();
    }

    /**
     * Obtain the event version's version id.
     * 
     * @return A version id <code>Long</code>.
     */
    private Long getVersionId() {
        return event.getVersion().getVersionId();
    }

    /**
     * Read the team for the container.
     * 
     * @return A <code>List</code> of <code>TeamMember</code>s.
     */
    private List<TeamMember> readTeam() {
        return getArtifactModel().readTeam2(getContainerId());
    }

    /**
     * Remove the latest flag from the container.
     * 
     */
    private void removeFlagLatest() {
        getArtifactModel().removeFlagLatest(getContainerId());
    }
}
