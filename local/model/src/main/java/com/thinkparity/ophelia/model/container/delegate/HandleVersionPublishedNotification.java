/*
 * Created On:  28-Apr-07 10:15:34 AM
 */
package com.thinkparity.ophelia.model.container.delegate;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.container.Container;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.container.VersionPublishedNotificationEvent;

import com.thinkparity.ophelia.model.container.ContainerDelegate;
import com.thinkparity.ophelia.model.container.ContainerDraft;
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

    /** The notification event. */
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
        final Long localId = getArtifactModel().readId(event.getVersion().getArtifactUniqueId());
        final Container container = read(localId);
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
    }

    private JabberId readKeyHolder(final Container container) {
        return getSessionModel().readKeyHolder(container.getUniqueId());
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
}
