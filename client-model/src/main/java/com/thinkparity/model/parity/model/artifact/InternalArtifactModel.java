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
import com.thinkparity.model.xmpp.user.User;

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
     * @param user
     *            The user.
     * @throws ParityException
     */
    public void addTeamMember(final Long artifactId, final User user) {
        synchronized(getImplLock()) {
            getImpl().addTeamMember(artifactId, user);
        }
    }

    /**
     * Add the team members. Add the user to the local team data in a pending
     * state; and call the server's add team member api.
     * 
     * @param artifactId
     *            The artifact id.
     * @param users
     *            The users.
     * @see ArtifactModelImpl#addTeamMember(Long, User)
     * @throws ParityException
     */
    public void addTeamMembers(final Long artifactId, final List<User> users) {
        synchronized(getImplLock()) {
            getImpl().addTeamMembers(artifactId, users);
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
        synchronized(getImplLock()) { return getImpl().createTeam(artifactId); }
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
     * Handle the remote event generated when a team member is added. This will
     * download the user's info if required and create the team data locally.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @param jabberId
     *            The user's jabber id.
     */
    public void handleTeamMemberAdded(final UUID uniqueId,
            final JabberId jabberId) throws ParityException {
        synchronized(getImplLock()) {
            getImpl().handleTeamMemberAdded(uniqueId, jabberId);
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
    public void removeTeamMember(final TeamMember teamMember) {
        synchronized(getImplLock()) { getImpl().removeTeamMember(teamMember); }
    }
    
    /**
     * Remove the team members. Removes the users from the local team data.
     * 
     * @param teamMembers
     *            The team members.
     */
    public void removeTeamMembers(final List<TeamMember> teamMembers) {
        synchronized(getImplLock()) { getImpl().removeTeamMembers(teamMembers); }
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
