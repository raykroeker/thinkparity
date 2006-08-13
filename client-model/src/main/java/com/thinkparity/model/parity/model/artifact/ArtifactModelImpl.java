/*
 * Mar 2, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.TrueAssertion;

import com.thinkparity.model.parity.ParityException;
import com.thinkparity.model.parity.model.AbstractModelImpl;
import com.thinkparity.model.parity.model.io.IOFactory;
import com.thinkparity.model.parity.model.io.handler.ArtifactIOHandler;
import com.thinkparity.model.parity.model.message.system.KeyRequestMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessage;
import com.thinkparity.model.parity.model.message.system.SystemMessageType;
import com.thinkparity.model.parity.model.user.InternalUserModel;
import com.thinkparity.model.parity.model.user.TeamMember;
import com.thinkparity.model.parity.model.user.TeamMemberState;
import com.thinkparity.model.parity.model.workspace.Workspace;
import com.thinkparity.model.smack.SmackException;
import com.thinkparity.model.xmpp.JabberId;
import com.thinkparity.model.xmpp.user.User;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class ArtifactModelImpl extends AbstractModelImpl {

    /**
     * Obtain a logging api id.
     * 
     * @param api
     *            An api.
     * @return An api id.
     */
    private static StringBuffer getApiId(final String api) {
        return getModelId("ARTIFACT").append(" ").append(api);
    }

	/**
	 * The artifact model's auditor.
	 * 
	 */
	protected final ArtifactModelAuditor auditor;

	/**
	 * Artifact persistance io.
	 * 
	 */
	private final ArtifactIOHandler artifactIO;

	/**
	 * Create a ArtifactModelImpl.
	 * 
	 * @param workspace
	 *            The parity workspace
	 */
	ArtifactModelImpl(final Workspace workspace) {
		super(workspace);
		this.artifactIO = IOFactory.getDefault().createArtifactHandler();
		this.auditor = new ArtifactModelAuditor(getContext());
	}

	/**
     * Add the team member. Add the user to the local team data in a local
     * state. If the user has been locally removed (ie no publish has yet
     * occured) that row will be replaced.
     * 
     * @param artifactId
     *            The artifact id.
     * @param user
     *            The user.
     * @throws ParityException
     */
	void addTeamMember(final Long artifactId, final User user) {
        logger.info("[LMODEL] [ARTIFACT] [ADD TEAM MEMBER]");
        logger.debug(artifactId);
        logger.debug(user);
        final List<TeamMember> team = artifactIO.readTeamRel2(artifactId);
        if(contains(team, user)) {
            final TeamMember teamMember = get(team, user);
            Assert.assertTrue(
                    getApiId("[ADD TEAM MEMBER] [CANNOT ADD USER MORE THAN ONCE]"),
                    TeamMemberState.LOCAL_REMOVED == teamMember.getState());
            artifactIO.deleteTeamRel(artifactId, user.getLocalId());
        }
        artifactIO.createTeamRel(
                artifactId, user.getLocalId(), TeamMemberState.LOCAL_ADDED);
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
    void addTeamMembers(final Long artifactId, final List<User> users) {
        logger.info(getApiId("[ADD TEAM MEMBERS]"));
        logger.debug(artifactId);
        logger.debug(users);
        for(final User user : users)
            addTeamMember(artifactId, user);
    }

    /**
	 * Apply the key flag.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	void applyFlagKey(final Long artifactId) {
		logger.info("[LMODEL] [ARTIFACT] [APPLY KEY]");
		applyFlag(artifactId, ArtifactFlag.KEY);
	}

    /**
	 * Apply the seen flag.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	void applyFlagSeen(final Long artifactId) {
		logger.info("[LMODEL] [ARTIFACT] [APPLY SEEN]");
		applyFlag(artifactId, ArtifactFlag.SEEN);
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
    void auditConfirmationReceipt(final Long artifactId, final Long versionId,
            final JabberId createdBy, final Calendar createdOn,
            final JabberId receivedFrom) throws ParityException {
        auditor.confirmationReceipt(artifactId, versionId, createdBy,
                createdOn, receivedFrom);
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
	void auditKeyRequestDenied(final Long artifactId,
			final JabberId createdBy, final Calendar createdOn,
			final JabberId deniedBy) throws ParityException {
		auditor.keyRequestDenied(artifactId, createdBy, createdOn, deniedBy);
	}

    /**
     * Confirm the reciept of an artifact.
     * 
     * @param receivedFrom
     *            From whom the artifact was received.
     * @param artifactId
     *            The artifact id.
     * @param versionId
     *            The artifact version id.
     */
    void confirmReceipt(final JabberId receivedFrom, final Long artifactId,
            final Long versionId) throws ParityException,
            SmackException {
        final UUID uniqueId = readArtifactUniqueId(artifactId);
        getInternalSessionModel().confirmArtifactReceipt(receivedFrom, uniqueId, versionId);
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
	void createRemoteInfo(final Long artifactId, final JabberId updatedBy,
			final Calendar updatedOn) {
		logger.info("[LMODEL] [ARTIFACT] [CREATE REMOTE INFO]");
		logger.debug(artifactId);
		logger.debug(updatedBy);
		logger.debug(updatedOn);
		artifactIO.createRemoteInfo(artifactId, updatedBy, updatedOn);
	}

	/**
     * Create the team for an artifact.
     * 
     * @param artifactId
     *            An artifact id.
     * @return The single team member.
     */
    List<TeamMember> createTeam(final Long artifactId) {
        logger.info(getApiId("[CREATE TEAM]"));
        logger.debug(artifactId);
        addTeamMember(artifactId, localUser());
        return readTeam2(artifactId);
    }

	/**
     * Delete the artifact's remote info.
     * 
     * @param artifactId
     *            The artifact id.
     */
	void deleteRemoteInfo(final Long artifactId) {
		logger.info("[LMODEL] [ARTIFACT] [DELETE REMOTE INFO]");
		logger.debug(artifactId);
		artifactIO.deleteRemoteInfo(artifactId);
	}

	void deleteTeam(final Long artifactId) {
        logger.info("[LMODEL] [ARTIFACT] [DELETE TEAM]");
        logger.debug(artifactId);
        artifactIO.deleteTeamRel(artifactId);
    }

    Boolean doesExist(final Long artifactId) {
        logger.info("[LMODEL] [ARTIFACT] [DOES EXIST]");
        logger.debug(artifactId);
        return null != artifactIO.readUniqueId(artifactId);
    }

    Boolean doesExist(final UUID uniqueId) {
        logger.info("[LMODEL] [ARTIFACT] [DOES EXIST]");
        logger.debug(uniqueId);
        return null != artifactIO.readId(uniqueId);
    }

    Boolean doesVersionExist(final Long artifactId, final Long versionId) {
        logApiId();
        debugVariable("artifactId", artifactId);
        debugVariable("versionId", versionId);
        return null != artifactIO.doesVersionExist(artifactId, versionId);
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
    void handleTeamMemberAdded(final UUID uniqueId, final JabberId jabberId) {
        logApiId();
        debugVariable("uniqueId", uniqueId);
        debugVariable("jabberId", jabberId);
        try {
            final Long artifactId = readId(uniqueId);
            final InternalUserModel userModel = getInternalUserModel();
            User user = userModel.read(jabberId);
            if(null == user) { user = userModel.create(jabberId); }
    
            // if receiving your own team member added event you have just been
            // added to the team; so download the entire team.
            if(user.equals(localUser())) {
                final List<User> remoteTeam =
                    getInternalSessionModel().readArtifactTeamList(artifactId);
                JabberId remoteUserId;
                for(User remoteUser : remoteTeam) {
                    remoteUserId = remoteUser.getId();
                    remoteUser = userModel.read(remoteUserId);
                    if(null == remoteUser) { remoteUser = userModel.create(remoteUserId); }
                    artifactIO.createTeamRel(artifactId, remoteUser
                            .getLocalId(), TeamMemberState.DISTRIBUTED);
                }
            }
            else {
                artifactIO.createTeamRel(artifactId, user.getLocalId(),
                        TeamMemberState.DISTRIBUTED);
            }
        } catch(final Throwable t) {
            throw translateError("[HANDLE TEAM MEMBER ADDED]", t);
        }
    }

    /**
     * Handle the remote event generated when a team member is removed.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @param jabberId
     *            The user's jabber id.
     */
    void handleTeamMemberRemoved(final UUID uniqueId, final JabberId jabberId) {
        logApiId();
        debugVariable("uniqueId", uniqueId);
        debugVariable("jabberId", jabberId);
        try {
            final Long artifactId = readId(uniqueId);
            artifactIO.deleteTeamRel(artifactId);
        } catch(final Throwable t) {
            throw translateError("[HANDLE TEAM MEMBER REMOVED]", t);
        }
    }

    /**
	 * Determine whether or not the artifact has been seen.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return True if the artifact has been seen.
	 */
	Boolean hasBeenSeen(final Long artifactId) {
		logger.info("[LMODEL] [ARTIFACT] [HAS BEEN SEEN]");
		return isFlagApplied(artifactId, ArtifactFlag.SEEN);
	}

	/**
	 * Determine whether or not an artifact has a flag applied.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param flag
	 *            The artifact flag.
	 * @return True if the flag is applied; false otherwise.
	 */
	Boolean isFlagApplied(final Long artifactId, final ArtifactFlag flag) {
		logger.info("[LMODEL] [ARTIFACT] [IS FLAG APPLIED]");
		logger.debug(artifactId);
		logger.debug(flag);
		final List<ArtifactFlag> flags = artifactIO.getFlags(artifactId);
		return flags.contains(flag);
	}

	/**
     * Read the artifact id.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @return The artifact id.
     */
    Long readId(final UUID uniqueId) {
        logger.info("[LMODEL] [ARTIFACT] [READ ID]");
        logger.debug(uniqueId);
        return artifactIO.readId(uniqueId);
    }

    /**
     * Read the artifact key holder.
     * 
     * @param artifactId
     *            The artifact id.
     * @return The artifact key holder.
     */
    JabberId readKeyHolder(final Long artifactId) throws ParityException {
        logger.info(getApiId("[READ KEY HOLDER]"));
        logger.debug(artifactId);
        assertOnline(getApiId("[READ KEY HOLDER]"));
        return getInternalSessionModel().readKeyHolder(readUniqueId(artifactId));
    }

    /**
     * Read a key request.
     * 
     * @param keyRequestId
     *            A key request id.
     * @return A key request.
     */
    KeyRequest readKeyRequest(final Long keyRequestId) {
        logger.info(getApiId("[READ KEY REQUEST]"));
        logger.debug(keyRequestId);
        return createKeyRequest((KeyRequestMessage) getInternalMessageModel().read(keyRequestId));
    }

    /**
	 * Read all key requests for the given artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return A list of requests.
	 */
	List<KeyRequest> readKeyRequests(final Long artifactId) {
		logger.info(getApiId("[READ KEY REQUESTS]"));
		logger.debug(artifactId);
		final List<SystemMessage> messages =
			getInternalSystemMessageModel().readForArtifact(artifactId, SystemMessageType.KEY_REQUEST);
		final List<KeyRequest> requests = new ArrayList<KeyRequest>();
		for(final SystemMessage message : messages) {
			requests.add(createKeyRequest((KeyRequestMessage) message));
		}
		return requests;
	}

	/**
     * Read the latest version id for an artifact.
     * 
     * @param artifactId
     *            An artifact id.
     * @return A version id.
     */
    Long readLatestVersionId(final Long artifactId) {
        logApiId();
        debugVariable("artifactId", artifactId);
        return artifactIO.readLatestVersionId(artifactId);
    }

	/**
     * Read the artifact team.
     * 
     * @param artifactId
     *            An artifact id.
     * @return The artifact team.
     */
    Set<User> readTeam(final Long artifactId) {
        logger.info("[LMODEL] [ARTIFACT] [READ TEAM]");
        return artifactIO.readTeamRel(artifactId);
    }

	/**
     * Read the artifact team.
     * 
     * @param artifactId
     *            An artifact id.
     */
    List<TeamMember> readTeam2(final Long artifactId) {
        logger.info(getApiId("[READ TEAM2]"));
        logger.debug(artifactId);
        return artifactIO.readTeamRel2(artifactId);
    }

    /**
     * Read the artifact unique id.
     * 
     * @param artifactId
     *            An artifact id.
     * @return An artifact unique id.
     */
    UUID readUniqueId(final Long artifactId) {
        logger.info("[LMODEL] [ARTIFACT] [READ UNIQUE ID]");
        logger.debug(artifactId);
        return artifactIO.readUniqueId(artifactId);
    }

    /**
	 * Remove the key flag.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	void removeFlagKey(final Long artifactId) {
		logger.info("[LMODEL] [ARTIFACT] [REMOVE KEY]");
		removeFlag(artifactId, ArtifactFlag.KEY);
	}

    /**
	 * Remove the seen flag.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	void removeFlagSeen(final Long artifactId) {
		logger.info("[LMODEL] [ARTIFACT] [REMOVE SEEN]");
		removeFlag(artifactId, ArtifactFlag.SEEN);
	}

    /**
     * Remove a team member. Update the local team data in a locally removed
     * state. If the user has been locally added (ie no publish has yet occured)
     * that row will be deleted.
     * 
     * @param artifactId
     *            The artifact id.
     * @param user
     *            The user.
     */
	void removeTeamMember(final TeamMember teamMember) {
        logger.info(getApiId("[REMOVE TEAM MEMBER]"));
        logger.debug(teamMember);
        artifactIO.deleteTeamRel(teamMember.getArtifactId(), teamMember.getLocalId());
        if(TeamMemberState.DISTRIBUTED == teamMember.getState()) {
            artifactIO.createTeamRel(
                    teamMember.getArtifactId(), teamMember.getLocalId(), TeamMemberState.LOCAL_REMOVED);
        }
    }

    /**
     * Remove the team members. Removes the users from the local team data.
     * 
     * @param teamMembers
     *            The team members.
     */
    void removeTeamMembers(final List<TeamMember> teamMembers) {
        logger.info(getApiId("[REMOVE TEAM MEMBERS]"));
        logger.debug(teamMembers);
        for(final TeamMember teamMember : teamMembers)
            removeTeamMember(teamMember);
    }

    /**
     * Update the artifact's remote info.
     * 
     * @param artifactId
     *            The artifact id.
     * @param updatedBy
     *            The last remote user to update the artifact.
     * @param updatedOn
     *            The last time the artifact was remotely updated.
     */
	void updateRemoteInfo(final Long artifactId, final JabberId updatedBy,
			final Calendar updatedOn) {
		logger.info("[LMODEL] [ARTIFACT] [UPDATE REMOTE INFO]");
		logger.debug(artifactId);
		logger.debug(updatedBy);
		logger.debug(updatedOn);
		artifactIO.updateRemoteInfo(artifactId, updatedBy, updatedOn);
	}

    /**
     * Update an artifact's state.
     * 
     * @param artifactId
     *            An artifact id.
     * @param state
     *            The artifact state.
     */
    void updateState(final Long artifactId, final ArtifactState state) {
        logger.info("[LMODEL] [ARTIFACT] [UPDATE STATE]");
        logger.debug(artifactId);
        logger.debug(state);
        final ArtifactState currentState = artifactIO.readState(artifactId);
        assertStateTransition(currentState, state);
        artifactIO.updateState(artifactId, state);
    }


    /**
	 * Apply a flag to an artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param flag
	 *            The flag.
	 */
	private void applyFlag(final Long artifactId, final ArtifactFlag flag) {
		logger.info("[LMODEL] [ARTIFACT] [APPLY FLAG]");
		logger.debug(artifactId);
		logger.debug(flag);
		final List<ArtifactFlag> flags = artifactIO.getFlags(artifactId);
		if(flags.contains(flag)) {
			logger.warn("Artifact [" + artifactId
					+ "] has already been flagged as [" + flag + "].");
		}
		else {
			flags.add(flag);
			artifactIO.updateFlags(artifactId, flags);
		}
	}

    /**
	 * Create a key request based upon a key request system message.
	 * 
	 * @param message
	 *            The key request system message.
	 * @return The key request.
	 */
	private KeyRequest createKeyRequest(final KeyRequestMessage message) {
		final KeyRequest request = new KeyRequest();
		request.setArtifactId(message.getArtifactId());
		request.setRequestedBy(message.getRequestedBy());
		request.setRequestedByName(message.getRequestedByName());
		request.setId(message.getId());
		return request;
	}

    /**
	 * Remove a flag from an artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @param flag
	 *            The flag.
	 * @throws TrueAssertion
	 *             <ul>
	 *             <li>If the flag has not been applied
	 *             </ul>
	 */
	private void removeFlag(final Long artifactId, final ArtifactFlag flag) {
		logger.info("[] [] []");
		final List<ArtifactFlag> flags = artifactIO.getFlags(artifactId);
		if(flags.contains(flag)) {
			flags.remove(flag);
			artifactIO.updateFlags(artifactId, flags);
		}
		else {
			logger.warn("Artifact [" + artifactId
					+ "] has no flag [" + flag + "].");
		}
	}

}
