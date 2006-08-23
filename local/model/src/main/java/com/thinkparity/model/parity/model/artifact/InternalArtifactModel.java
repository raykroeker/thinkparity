/*
 * Mar 2, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.Context;
import com.thinkparity.model.parity.model.user.TeamMember;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.JabberId;

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
	InternalArtifactModel(final Context context, final Workspace workspace) {
		super(workspace);
		context.assertContextIsValid();
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

    public void applyFlagKey(final Long artifactId) {
		synchronized(getImplLock()) { getImpl().applyFlagKey(artifactId); }
	}

	/**
     * Audit the confirmation receipt of the artifact.
     * 
     * @param artifactId
     *            The artifact id.
     * @param receivedBy
     *            By whom the artifact was received.
     * @throws ParityException
     */
    public void auditConfirmationReceipt(final Long artifactId,
            final Long versionId, final JabberId createdBy,
            final Calendar createdOn, final JabberId receivedFrom)
            throws ParityException {
        synchronized(getImplLock()) {
            getImpl().auditConfirmationReceipt(
                    artifactId, versionId, createdBy, createdOn, receivedFrom);
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
	public void confirmReceipt(final JabberId receivedFrom,
            final Long artifactId, final Long artifactVersionId)
            throws ParityException, SmackException {
	    synchronized(getImplLock()) {
            getImpl().confirmReceipt(receivedFrom, artifactId, artifactVersionId);
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
     * @see InternalArtifactModel#addTeamMember(java.lang.Long,com.thinkparity.model.xmpp.JabberId)
     * @see InternalArtifactModel#removeTeamMember(java.lang.Long,com.thinkparity.model.xmpp.JabberId)
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
     * Handle the remote event generated when a team member is added. This will
     * download the user's info if required and create the team data locally.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @param jabberId
     *            The user's jabber id.
     */
    public void handleTeamMemberAdded(final UUID uniqueId,
            final JabberId jabberId) {
        synchronized(getImplLock()) {
            getImpl().handleTeamMemberAdded(uniqueId, jabberId);
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
    public void handleDraftCreated(final UUID uniqueId,
            final JabberId createdBy, final Calendar createdOn) {
        synchronized (getImplLock()) {
            getImpl().handleDraftCreated(uniqueId, createdBy, createdOn);
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
    public void handleDraftDeleted(final UUID uniqueId,
            final JabberId deletedBy, final Calendar deletedOn) {
        synchronized (getImplLock()) {
            getImpl().handleDraftDeleted(uniqueId, deletedBy, deletedOn);
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
    public void handleTeamMemberRemoved(final UUID uniqueId,
            final JabberId jabberId) {
        synchronized (getImplLock()) {
            getImpl().handleTeamMemberRemoved(uniqueId, jabberId);
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
     * Read a key request.
     * 
     * @param keyRequestId
     *            A key request id.
     * @return A key request.
     */
    public KeyRequest readKeyRequest(final Long keyRequestId) {
        synchronized(getImplLock()) {
            return getImpl().readKeyRequest(keyRequestId);
        }
    }

    /**
	 * Obtain all pending key requests for the artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return A list of key requests.
	 */
	public List<KeyRequest> readKeyRequests(final Long artifactId) {
		synchronized(getImplLock()) {
			return getImpl().readKeyRequests(artifactId);
		}
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
	    synchronized(getImplLock()) { return getImpl().readTeam2(artifactId); }
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
