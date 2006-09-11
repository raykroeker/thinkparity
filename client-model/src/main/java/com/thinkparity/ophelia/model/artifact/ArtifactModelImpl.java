/*
 * Mar 2, 2006
 */
package com.thinkparity.ophelia.model.artifact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.TrueAssertion;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.user.User;


import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler;
import com.thinkparity.ophelia.model.message.KeyRequestMessage;
import com.thinkparity.ophelia.model.message.SystemMessage;
import com.thinkparity.ophelia.model.message.SystemMessageType;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.user.TeamMember;
import com.thinkparity.ophelia.model.util.smack.SmackException;
import com.thinkparity.ophelia.model.workspace.Workspace;

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
	 * Artifact persistance io.
	 * 
	 */
	public final ArtifactIOHandler artifactIO;

	/**
	 * The artifact model's auditor.
	 * 
	 */
	protected final ArtifactModelAuditor auditor;

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
     *            The artifact id <code>Long</code>.
     * @param userId
     *            The user id <code>JabberId</code>.
     */
	TeamMember addTeamMember(final Long artifactId, final JabberId userId) {
	    logApiId();
        logVariable("artifactId", artifactId);
        logVariable("artifactId", userId);
        assertNotTeamMember("TEAM MEMBER ALREADY ADDED", artifactId, userId);
        assertOnline("USER NOT ONLINE");
        // create local user data
        final InternalUserModel userModel = getInternalUserModel();
        User user = userModel.read(userId);
        if (null == user) {
            user = userModel.create(userId);
        }
        // create local team data
        return addTeamMember(artifactId, user.getLocalId());
    }

    /**
     * Add a team member. Since we are using a local user id instead of a remote
     * one we know the user info has already been downloaded and the api does
     * not require the user to be online.
     * 
     * @param artifactId
     *            An artifact id.
     * @param userId
     *            A local user id.
     * @return A team member.
     */
    TeamMember addTeamMember(final Long artifactId, final Long userId) {
        logApiId();
        logVariable("artifactId", artifactId);
        logVariable("userId", userId);
        artifactIO.createTeamRel(artifactId, userId);
        return artifactIO.readTeamRel(artifactId, userId);
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
        logApiId();
        logVariable("artifactId", artifactId);
        // note that we are calling the "offline" version of the api
        addTeamMember(artifactId, localUser().getLocalId());
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
        logVariable("artifactId", artifactId);
        logVariable("versionId", versionId);
        return artifactIO.doesVersionExist(artifactId, versionId);
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
    void handleDraftCreated(final UUID uniqueId,
            final JabberId createdBy, final Calendar createdOn) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("createdBy", createdBy);
        logVariable("createdOn", createdOn);
        final Long artifactId = artifactIO.readId(uniqueId);
        switch (artifactIO.readType(artifactId)) {
        case CONTAINER:
            getInternalContainerModel().handleDraftCreated(artifactId, createdBy, createdOn);
            break;
        default:
            Assert.assertUnreachable("UNSUPPORTED ARTIFACT TYPE");
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
    void handleDraftDeleted(final UUID uniqueId,
            final JabberId deletedBy, final Calendar deletedOn) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("deletedBy", deletedBy);
        logVariable("deletedOn", deletedOn);
        final Long artifactId = artifactIO.readId(uniqueId);
        switch (artifactIO.readType(artifactId)) {
        case CONTAINER:
            getInternalContainerModel().handleDraftDeleted(artifactId, deletedBy, deletedOn);
            break;
        default:
            Assert.assertUnreachable("UNSUPPORTED ARTIFACT TYPE");
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
    void handleTeamMemberAdded(final UUID uniqueId, final JabberId jabberId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("jabberId", jabberId);
        try {
            final Long artifactId = readId(uniqueId);
            // if receiving your own team member added event you have just been
            // added to the team; so download the entire team.
            if (jabberId.equals(localUserId())) {
                final List<JabberId> remoteTeam =
                    getInternalSessionModel().readArtifactTeamIds(uniqueId);
                for (final JabberId remoteUser : remoteTeam) {
                    try {
                        addTeamMember(artifactId, remoteUser);
                    } catch (final TrueAssertion ta) {
                        if ("TEAM MEMBER ALREADY ADDED".equals(ta.getMessage())) {
                            logWarning(ta);
                        } else {
                            throw ta;
                        }
                    }
                }
            } else {
                addTeamMember(artifactId, jabberId);
            }
        } catch(final Throwable t) {
            throw translateError(t);
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
        logVariable("uniqueId", uniqueId);
        logVariable("jabberId", jabberId);
        try {
            final Long artifactId = readId(uniqueId);
            artifactIO.deleteTeamRel(artifactId);
        } catch(final Throwable t) {
            throw translateError(t);
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
    JabberId readKeyHolder(final Long artifactId) {
        logApiId();
        logVariable("artifactId", artifactId);
        assertOnline();
        return getInternalSessionModel().readKeyHolder(
                localUserId(), readUniqueId(artifactId));
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
        logVariable("artifactId", artifactId);
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
	void removeTeamMember(final Long artifactId, final JabberId userId) {
        logApiId();
        logVariable("artifactId", artifactId);
        logVariable("userId", userId);
        assertTeamMember("USER IS NOT A TEAM MEMBER", artifactId, userId);
        final User user = getInternalUserModel().read(userId);
        artifactIO.deleteTeamRel(artifactId, user.getLocalId());
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
