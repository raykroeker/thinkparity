/*
 * Mar 2, 2006
 */
package com.thinkparity.ophelia.model.artifact;

import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftCreatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent;

import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.user.TeamMember;
import com.thinkparity.ophelia.model.util.smack.SmackException;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class InternalArtifactModel extends ArtifactModel {

    /**
	 * Create a InternalArtifactModel.
	 * 
	 * @param context
	 *            The parity context.
	 * @param workspace
	 *            The workspace.
	 */
	InternalArtifactModel(final Context context, final Environment environment,
            final Workspace workspace) {
		super(environment, workspace);
	}

	/**
     * Add the team member. Add the user to the local team data in a pending
     * state; and call the server's add team member api.
     * 
     * @param artifactId
     *            The artifact id.
     * @param userId
     *            The user id.
     * @throws ParityException
     */
    public TeamMember addTeamMember(final Long artifactId, final JabberId userId) {
        synchronized(getImplLock()) {
            return getImpl().addTeamMember(artifactId, userId);
        }
    }

    /**
     * Apply the archived flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    public void applyFlagArchived(final Long artifactId) {
        synchronized (getImplLock()) {
            getImpl().applyFlagArchived(artifactId);
        }
    }

    /**
     * Apply the archived flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    public void applyFlagBookmark(final Long artifactId) {
        synchronized (getImplLock()) {
            getImpl().applyFlagBookmark(artifactId);
        }
    }

    /**
     * Apply the key flag.
     * 
     * @param artifactId
     *            The artifact id.
     */
    public void applyFlagKey(final Long artifactId) {
		synchronized (getImplLock()) {
            getImpl().applyFlagKey(artifactId);
		}
	}

	/**
	 * Audit the denial of a key request for an artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param createdBy
	 *            The creator.
	 * @param creatdOn
	 *            The creation date.
	 * @param deniedBy
	 *            The user denying the request.
	 */
	public void auditKeyRequestDenied(final Long artifactId,
			final JabberId createdBy, final Calendar createdOn,
			final JabberId deniedBy) throws ParityException {
		synchronized(getImplLock()) {
			getImpl().auditKeyRequestDenied(artifactId, createdBy, createdOn,
                    deniedBy);
		}
	}

    /**
     * Confirm the reciept of an artifact.
     * 
     * @param receivedFrom
     *            From whom the artifact was received.
     * @param artifactId
     *            The artifact id.
     * @param artifactVersionId
     *            The artifact version id.
     * @throws ParityException
     * @throws SmackException
     */
	public void confirmReceipt(final Long artifactId,
            final Long artifactVersionId) {
	    synchronized(getImplLock()) {
            getImpl().confirmReceipt(artifactId, artifactVersionId);
        }
    }

    /**
     * Create the artifact's remote info.
     * 
     * @param artifactId
     *            The artifact id.
     * @param updatedBy
     *            The remote user to update the artifact.
     * @param updatedOn
     *            The last time the artifact was updated.
     */
	public void createRemoteInfo(final Long artifactId,
			final JabberId updatedBy, final Calendar updatedOn) {
		synchronized(getImplLock()) {
			getImpl().createRemoteInfo(artifactId, updatedBy, updatedOn);
		}
	}

    /**
     * Create the team. This will add the current user to the team.
     * 
     * @param artifactId
     *            An artifact id.
     * @return The new team.
     */
    public List<TeamMember> createTeam(final Long artifactId) {
        synchronized (getImplLock()) {
            return getImpl().createTeam(artifactId);
        }
    }

	/**
     * Delete the artifact's remote info.
     * 
     * @param artifactId
     *            The artifact id.
     */
	public void deleteRemoteInfo(final Long artifactId) {
		synchronized(getImplLock()) {
			getImpl().deleteRemoteInfo(artifactId);
		}
	}

	/**
     * Delete the team in its entirety.
     *
     * @param artifactId
     *      An artifact id.
     * @see InternalArtifactModel#addTeamMember(java.lang.Long,com.thinkparity.codebase.jabber.JabberId)
     * @see InternalArtifactModel#removeTeamMember(java.lang.Long,com.thinkparity.codebase.jabber.JabberId)
     */
    public void deleteTeam(final Long artifactId) {
        synchronized(getImplLock()) { getImpl().deleteTeam(artifactId); }
    }

    /**
     * Determine if an artifact exists.
     * 
     * @param artifactId
     *            The artifact id.
     * @return True if the artifact exists; false otherwise.
     */
    public Boolean doesExist(final Long artifactId) {
        synchronized(getImplLock()) { return getImpl().doesExist(artifactId); }
    }

    /**
     * Determine if an artifact exists.
     * 
     * @param uniqueId
     *            The unique id.
     * @return True if the artifact exists; false otherwise.
     */
    public Boolean doesExist(final UUID uniqueId) {
        synchronized(getImplLock()) { return getImpl().doesExist(uniqueId); }
    }

    /**
     * Determine if the artifact version exists.
     * 
     * @param artifactId
     *            An artifact id.
     * @param versionId
     *            An artifact version id.
     * @return True if the artifact version exists.
     */
    public Boolean doesVersionExist(final Long artifactId, final Long versionId) {
        synchronized(getImplLock()) {
            return getImpl().doesVersionExist(artifactId, versionId);
        }
    }

    /**
     * Handle the remote event generated when a draft is created.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param createdBy
     *            Who created the draft.
     * @param createdOn
     *            When the draft was created.
     */
    public void handleDraftCreated(final ArtifactDraftCreatedEvent event) {
        synchronized (getImplLock()) {
            getImpl().handleDraftCreated(event);
        }
    }

    /**
     * Handle the remote event generated when a draft is deleted.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param createdBy
     *            Who deleted the draft.
     * @param createdOn
     *            When the draft was deleted.
     */
    public void handleDraftDeleted(final ArtifactDraftDeletedEvent event) {
        synchronized (getImplLock()) {
            getImpl().handleDraftDeleted(event);
        }
    }

    public void handlePublished(final ArtifactPublishedEvent event) {
        synchronized (getImplLock()) {
            getImpl().handlePublished(event);
        }
    }

	public void handleReceived(final ArtifactReceivedEvent event) {
        synchronized (getImplLock()) {
            getImpl().handleReceived(event);
        }
    }

    /**
     * Handle the remote event generated when a team member is added. This will
     * download the user's info if required and create the team data locally.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @param jabberId
     *            The user's jabber id.
     */
    public void handleTeamMemberAdded(final ArtifactTeamMemberAddedEvent event) {
        synchronized(getImplLock()) {
            getImpl().handleTeamMemberAdded(event);
        }
    }
    
    /**
     * Handle the team member removed remote event.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
    public void handleTeamMemberRemoved(
            final ArtifactTeamMemberRemovedEvent event) {
        synchronized (getImplLock()) {
            getImpl().handleTeamMemberRemoved(event);
        }
    }

    /**
     * Read the artifact id.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @return The artifact id.
     */
    public Long readId(final UUID uniqueId) {
        synchronized(getImplLock()) { return getImpl().readId(uniqueId); }
    }

    /**
     * Read the latest version id for an artifact.
     * 
     * @param artifactId
     *            An artifact id.
     * @return A version id.
     */
    public Long readLatestVersionId(final Long artifactId) {
        synchronized(getImplLock()) {
            return getImpl().readLatestVersionId(artifactId);
        }
    }

    /**
     * Read the artifact team.
     * 
     * @param artifactId
     *            An artifact id.
     */
    public List<TeamMember> readTeam2(final Long artifactId) {
        // TODO Rename to readTeam.
        synchronized (getImplLock()) {
            return getImpl().readTeam2(artifactId);
        }
    }

    /**
     * Read the team for an artifact.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param comparator
     *            An artifact sort <code>Comparator</code>.
     * @param filter
     *            An artifact <code>Filter</code>.
     * @return A <code>TeamMember</code> <code>List</code>.
     */
    public List<TeamMember> readTeam(final Long artifactId,
            final Comparator<? super User> comparator,
            final Filter<? super User> filter) {
        synchronized (getImplLock()) {
            return getImpl().readTeam(artifactId, comparator, filter);
        }
    }

    /**
     * Read the artifact team.
     * 
     * @param artifactId
     *            An artifact id.
     */
    public List<JabberId> readTeamIds(final Long artifactId) {
        synchronized (getImplLock()) {
            return getImpl().readTeamIds(artifactId);
        }
    }

    /**
     * Read the artifact unique id.
     * 
     * @param artifactId
     *            An artifact id.
     * @return An artifact unique id.
     */
    public UUID readUniqueId(final Long artifactId) {
        synchronized(getImplLock()) { return getImpl().readUniqueId(artifactId); }
    }

    /**
     * Remove the archived flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    public void removeFlagArchived(final Long artifactId) {
        synchronized (getImplLock()) {
            getImpl().removeFlagArchived(artifactId);
        }
    }

    /**
     * Remove the bookmark flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    public void removeFlagBookmark(final Long artifactId) {
        synchronized (getImplLock()) {
            getImpl().removeFlagBookmark(artifactId);
        }
    }

    public void removeFlagKey(final Long artifactId) {
		synchronized(getImplLock()) { getImpl().removeFlagKey(artifactId); }
	}
    
    /**
     * Remove the team member. Removes the user from the local team data.
     * 
     * @param teamMember
     *            The team member.
     */
    public void removeTeamMember(final Long artifactId, final JabberId userId) {
        synchronized(getImplLock()) { getImpl().removeTeamMember(artifactId, userId); }
    }

    /**
     * Update the artifact's remote info.
     * 
     * @param artifactId
     *            The artifact id.
     * @param updatedBy
     *            The last user to update the artifact.
     * @param updatedOn
     *            The last time the artifact was updated.
     */
	public void updateRemoteInfo(final Long artifactId,
			final JabberId updatedBy, final Calendar updatedOn) {
		synchronized(getImplLock()) {
			getImpl().updateRemoteInfo(artifactId, updatedBy, updatedOn);
		}
	}

    /**
     * Update an artifact's state.
     * 
     * @param artifactId
     *            An artifact id.
     * @param state
     *            The artifact state.
     */
	public void updateState(final Long artifactId, final ArtifactState state) {
	    synchronized(getImplLock()) { getImpl().updateState(artifactId, state); }
    }
}
