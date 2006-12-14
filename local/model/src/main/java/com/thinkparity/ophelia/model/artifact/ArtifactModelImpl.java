/*
 * Mar 2, 2006
 */
package com.thinkparity.ophelia.model.artifact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.TrueAssertion;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftCreatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactPublishedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent;

import com.thinkparity.ophelia.model.AbstractModelImpl;
import com.thinkparity.ophelia.model.ParityException;
import com.thinkparity.ophelia.model.container.InternalContainerModel;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler;
import com.thinkparity.ophelia.model.user.InternalUserModel;
import com.thinkparity.ophelia.model.util.sort.ModelSorter;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
final class ArtifactModelImpl extends AbstractModelImpl {

    /** Artifact persistance io. */
	private final ArtifactIOHandler artifactIO;

	/** The artifact model's auditor. */
    private final ArtifactModelAuditor auditor;

	/**
	 * Create a ArtifactModelImpl.
	 * 
	 * @param workspace
	 *            The parity workspace
	 */
	ArtifactModelImpl(final Environment environment, final Workspace workspace) {
		super(environment, workspace);
		this.artifactIO = IOFactory.getDefault(workspace).createArtifactHandler();
		this.auditor = new ArtifactModelAuditor(internalModelFactory);
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
	    logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        logger.logVariable("artifactId", userId);
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
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        logger.logVariable("userId", userId);
        artifactIO.createTeamRel(artifactId, userId);
        return artifactIO.readTeamRel(artifactId, userId);
    }

    /**
     * Apply the archived flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    void applyFlagArchived(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        try {
            applyFlag(artifactId, ArtifactFlag.ARCHIVED);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Apply the bookmark flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    void applyFlagBookmark(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        try {
            applyFlag(artifactId, ArtifactFlag.BOOKMARK);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
	 * Apply the key flag.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	void applyFlagKey(final Long artifactId) {
		logger.logApiId();
		applyFlag(artifactId, ArtifactFlag.KEY);
	}

    /**
	 * Apply the seen flag.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	void applyFlagSeen(final Long artifactId) {
		logger.logApiId();
		applyFlag(artifactId, ArtifactFlag.SEEN);
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
		logger.logApiId();
		logger.logVariable("variable", artifactId);
		logger.logVariable("variable", updatedBy);
		logger.logVariable("variable", updatedOn);
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
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
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
		logger.logApiId();
		logger.logVariable("variable", artifactId);
		artifactIO.deleteRemoteInfo(artifactId);
	}

    void deleteTeam(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("variable", artifactId);
        artifactIO.deleteTeamRel(artifactId);
    }

    Boolean doesExist(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        try {
            return artifactIO.doesExist(artifactId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	Boolean doesExist(final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
        try {
            return artifactIO.doesExist(uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    Boolean doesVersionExist(final Long artifactId, final Long versionId) {
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        logger.logVariable("versionId", versionId);
        return artifactIO.doesVersionExist(artifactId, versionId);
    }

    Boolean doesVersionExist(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        return artifactIO.doesVersionExist(artifactId);
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
    void handleDraftCreated(final ArtifactDraftCreatedEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            final Long artifactId = artifactIO.readId(event.getUniqueId());
            switch (artifactIO.readType(artifactId)) {
            case CONTAINER:
                getContainerModel().handleDraftCreated(artifactId,
                        event.getCreatedBy(), event.getCreatedOn());
                break;
            default:
                Assert.assertUnreachable("UNSUPPORTED ARTIFACT TYPE");
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
    void handleDraftDeleted(final ArtifactDraftDeletedEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            final Long artifactId = artifactIO.readId(event.getUniqueId());
            switch (artifactIO.readType(artifactId)) {
            case CONTAINER:
                getContainerModel().handleDraftDeleted(event);
                break;
            default:
                Assert.assertUnreachable("Unsupported artifact type.");
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    void handlePublished(final ArtifactPublishedEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            final Long artifactId = readId(event.getUniqueId());
            if (doesVersionExist(artifactId, event.getVersionId())) {
                applyFlagLatest(artifactId);
            } else {
                removeFlagLatest(artifactId);
            }
            /* delete a draft - this will happen when an existing team member is
             * not published to */
            switch (readType(artifactId)) {
            case CONTAINER:
                final InternalContainerModel containerModel = getContainerModel();
                if (containerModel.doesExistDraft(artifactId)) {
                    containerModel.deleteDraft(artifactId);
                }
                containerModel.handlePublished(event);
                break;
            case DOCUMENT:  // deliberate fall through
            default:
                Assert.assertUnreachable("Unexpected artifact type.");
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    void handleReceived(final ArtifactReceivedEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            final Long artifactId = artifactIO.readId(event.getUniqueId());
            if (doesVersionExist(artifactId, event.getVersionId())) {
                switch (artifactIO.readType(artifactId)) {
                case CONTAINER:
                    getContainerModel().handleReceived(event);
                    break;
                default:
                    Assert.assertUnreachable("UNSUPPORTED ARTIFACT TYPE");
                }
            } else {
                logger.logWarning("Artifact version {0}:{1} does not exist locally.",
                        event.getUniqueId(), event.getVersionId());
            }
        } catch (final Throwable t) {
            throw translateError(t);
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
    void handleTeamMemberAdded(final ArtifactTeamMemberAddedEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            final Long artifactId = readId(event.getUniqueId());
            if (null == artifactId) {
                logger.logWarning("Artifact {0} no longer exists.", event
                        .getUniqueId());
            } else {
                addTeamMember(artifactId, event.getJabberId());
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
    void handleTeamMemberRemoved(final ArtifactTeamMemberRemovedEvent event) {
        logger.logApiId();
        logger.logVariable("event", event);
        try {
            final Long artifactId = readId(event.getUniqueId());
            final User user = getInternalUserModel().read(event.getJabberId());
            artifactIO.deleteTeamRel(artifactId, user.getLocalId());
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
		logger.logApiId();
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
		logger.logApiId();
		logger.logVariable("variable", artifactId);
		logger.logVariable("variable", flag);
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
        logger.logApiId();
        logger.logVariable("uniqueId", uniqueId);
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
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        assertOnline();
        return getSessionModel().readKeyHolder(
                localUserId(), readUniqueId(artifactId));
    }

    /**
     * Read the latest version id for an artifact.
     * 
     * @param artifactId
     *            An artifact id.
     * @return A version id.
     */
    Long readLatestVersionId(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
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
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        return artifactIO.readTeamRel(artifactId);
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
    List<TeamMember> readTeam(final Long artifactId,
            final Comparator<? super User> comparator,
            final Filter<? super User> filter) {
        logger.logVariable("artifactId", artifactId);
        logger.logVariable("comparator", comparator);
        logger.logVariable("filter", filter);
        try {
            final List<TeamMember> team = artifactIO.readTeamRel2(artifactId);
            FilterManager.filter(team, filter);
            ModelSorter.sortTeamMembers(team, comparator);
            return team;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the artifact team.
     * 
     * @param artifactId
     *            An artifact id.
     */
    List<TeamMember> readTeam2(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        return artifactIO.readTeamRel2(artifactId);
    }

    /**
     * Read the artifact team.
     * 
     * @param artifactId
     *            An artifact id.
     */
    List<JabberId> readTeamIds(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        try {
            final List<TeamMember> teamMembers = artifactIO.readTeamRel2(artifactId);
            final List<JabberId> teamIds = new ArrayList<JabberId>(teamMembers.size());
            for (final TeamMember teamMember : teamMembers) {
                teamIds.add(teamMember.getId());
            }
            return teamIds;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the artifact's type.
     * 
     * @param artifactId
     *            An artifact id.
     * @return An <code>ArtifactType</code>.
     */
    ArtifactType readType(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        try {
            return artifactIO.readType(artifactId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Read the artifact unique id.
     * 
     * @param artifactId
     *            An artifact id.
     * @return An artifact unique id.
     */
    UUID readUniqueId(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("variable", artifactId);
        return artifactIO.readUniqueId(artifactId);
    }

    /**
     * Remove the archived flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    void removeFlagArchived(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        try {
            removeFlag(artifactId, ArtifactFlag.ARCHIVED);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Remove the bookmark flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    void removeFlagBookmark(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        try {
            removeFlag(artifactId, ArtifactFlag.BOOKMARK);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
	 * Remove the key flag.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	void removeFlagKey(final Long artifactId) {
		logger.logApiId();
		removeFlag(artifactId, ArtifactFlag.KEY);
	}

    /**
	 * Remove the seen flag.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	void removeFlagSeen(final Long artifactId) {
		logger.logApiId();
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
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        logger.logVariable("userId", userId);
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
		logger.logApiId();
		logger.logVariable("variable", artifactId);
		logger.logVariable("variable", updatedBy);
		logger.logVariable("variable", updatedOn);
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
        logger.logApiId();
        logger.logVariable("variable", artifactId);
        logger.logVariable("variable", state);
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
		logger.logApiId();
		logger.logVariable("variable", artifactId);
		logger.logVariable("variable", flag);
		final List<ArtifactFlag> flags = artifactIO.getFlags(artifactId);
		if(flags.contains(flag)) {
			logger.logWarning("Artifact {0} is already flagged as {1}.",
                    artifactId, flag);
		}
		else {
			flags.add(flag);
			artifactIO.updateFlags(artifactId, flags);
		}
	}
    
    /**
     * Apply the archived flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    private void applyFlagLatest(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        try {
            applyFlag(artifactId, ArtifactFlag.LATEST);
        } catch (final Throwable t) {
            throw translateError(t);
        }
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
		logger.logApiId();
		final List<ArtifactFlag> flags = artifactIO.getFlags(artifactId);
		if(flags.contains(flag)) {
			flags.remove(flag);
			artifactIO.updateFlags(artifactId, flags);
		}
		else {
			logger.logWarning("Artifact {0} is not flagged as {1}.",
                    artifactId, flag);
		}
	}

    /**
     * Remove the latest flag.
     * 
     * @param artifactId
     *            An artifact id.
     */
    private void removeFlagLatest(final Long artifactId) {
        logger.logApiId();
        logger.logVariable("artifactId", artifactId);
        try {
            removeFlag(artifactId, ArtifactFlag.LATEST);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }


}
