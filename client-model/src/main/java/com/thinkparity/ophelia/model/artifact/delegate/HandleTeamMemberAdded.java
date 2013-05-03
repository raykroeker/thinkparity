/**
 * Created On: 14-Dec-07 1:19:19 PM
 * $Id$
 */
package com.thinkparity.ophelia.model.artifact.delegate;

import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent;

import com.thinkparity.ophelia.model.artifact.ArtifactDelegate;

/**
 * <b>Title:</b>thinkParity Ophelia Model Handle Team Member Added Artifact
 * Delegate<br>
 * <b>Description:</b><br>
 * 
 * @author robert@thinkparity.com
 * @version $Revision$
 */
public class HandleTeamMemberAdded extends ArtifactDelegate {

    /** An event. */
    private ArtifactTeamMemberAddedEvent event;

    /**
     * Create HandleTeamMemberAdded.
     *
     */
    public HandleTeamMemberAdded() {
        super();
    }

    /**
     * Handle a team member removed event.
     * 
     */
    public void handleTeamMemberAdded() {
        if (localUserId().equals(event.getJabberId())) {
            logger.logInfo("Ignoring local user.");
        } else {
            final Long artifactId = artifactIO.readId(event.getUniqueId());
            if (null == artifactId) {
                logger.logWarning("Artifact for event {0} does not exist locally.",
                        event);
            } else {
                final User user = getUserModel().readLazyCreate(event.getJabberId());
                if (artifactIO.doesTeamMemberExist(artifactId, user)) {
                    logger.logInfo("User {0} is already on the team for artifact {1}.",
                            event.getJabberId(), event.getUniqueId());
                } else {
                    final TeamMember teamMember = addTeamMember(artifactId, user);
                    // HACK assuming container
                    getContainerModel().notifyTeamMemberAdded(teamMember);
                }
            }
        }
    }

    /**
     * Set the event.
     *
     * @param event
     *      A <code>ArtifactTeamMemberAddedEvent</code>.
     */
    public void setEvent(final ArtifactTeamMemberAddedEvent event) {
        this.event = event;
    }
}
