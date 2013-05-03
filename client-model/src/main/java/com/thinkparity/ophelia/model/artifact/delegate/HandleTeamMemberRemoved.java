/*
 * Created On:  27-Nov-07 5:09:01 PM
 */
package com.thinkparity.ophelia.model.artifact.delegate;

import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent;

import com.thinkparity.ophelia.model.artifact.ArtifactDelegate;

/**
 * <b>Title:</b>thinkParity Ophelia Model Handle Team Member Removed Artifact
 * Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class HandleTeamMemberRemoved extends ArtifactDelegate {

    /** An event. */
    private ArtifactTeamMemberRemovedEvent event;

    /**
     * Create HandleTeamMemberRemoved.
     *
     */
    public HandleTeamMemberRemoved() {
        super();
    }

    /**
     * Handle a team member removed event.
     * 
     */
    public void handleTeamMemberRemoved() {
        final Long artifactId = artifactIO.readId(event.getUniqueId());
        if (null == artifactId) {
            logger.logWarning("Artifact for event {0} does no longer exists.",
                    event);
        } else {
            final User user = getUserModel().read(event.getJabberId());
            if (artifactIO.doesTeamMemberExist(artifactId, user)) {
                final TeamMember teamMember = artifactIO.readTeamRel(artifactId, user.getLocalId());
                artifactIO.deleteTeamRel(artifactId, user.getLocalId());
                // HACK assuming container
                getContainerModel().notifyTeamMemberRemoved(teamMember);
            } else {
                logger.logInfo("User {0} is no longer on the team for artifact {1}.",
                        event.getJabberId(), event.getUniqueId());
            }
        }
    }

    /**
     * Set the event.
     *
     * @param event
     *		A <code>ArtifactTeamMemberRemovedEvent</code>.
     */
    public void setEvent(final ArtifactTeamMemberRemovedEvent event) {
        this.event = event;
    }

}
