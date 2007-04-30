/*
 * Created On:  28-Apr-07 10:15:34 AM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.util.List;

import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
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

    /** The event container version's local container id. */
    private Long containerId;

    /** A version published notification event. */
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
        // update the team definition
        final List<TeamMember> localTeam = readTeam();
        final List<User> eventTeam = getTeam();
        for (final User eventTeamUser : eventTeam) {
            if (!contains(localTeam, eventTeamUser)) {
                addTeamMember(eventTeamUser);
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
     * Read the team for the container.
     * 
     * @return A <code>List</code> of <code>TeamMember</code>s.
     */
    private List<TeamMember> readTeam() {
        return getArtifactModel().readTeam2(getContainerId());
    }
}
