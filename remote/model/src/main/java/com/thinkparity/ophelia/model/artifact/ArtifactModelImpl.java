/*
 * Mar 2, 2006
 */
package com.thinkparity.ophelia.model.artifact;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.TrueAssertion;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftCreatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent;

import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.io.IOFactory;
import com.thinkparity.ophelia.model.io.handler.ArtifactIOHandler;
import com.thinkparity.ophelia.model.util.sort.ModelSorter;
import com.thinkparity.ophelia.model.workspace.Workspace;

/**
 * <b>Title:</b>thinkParity Artifact Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.26
 */
public final class ArtifactModelImpl extends Model implements
        ArtifactModel, InternalArtifactModel {

    /** Artifact persistance io. */
	private ArtifactIOHandler artifactIO;

	/**
	 * Create a ArtifactModelImpl.
	 * 
	 * @param workspace
	 *            The parity workspace
	 */
	public ArtifactModelImpl() {
        super();
	}

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#addTeamMember(java.lang.Long,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
	public TeamMember addTeamMember(final Long artifactId, final JabberId userId) {
        try {
            assertNotTeamMember("The team member has already been added.",
                    artifactId, userId);
            // create local user data
            final User user = getUserModel().readLazyCreate(userId);
            // create local team data
            return addTeamMember(artifactId, user.getLocalId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#applyFlagLatest(java.lang.Long)
     * 
     */
    public void applyFlagLatest(final Long artifactId) {
        try {
            applyFlag(artifactId, ArtifactFlag.LATEST);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.ArtifactModel#applyFlagSeen(java.lang.Long)
     * 
     */
    public void applyFlagSeen(final Long artifactId) {
        try {
            applyFlag(artifactId, ArtifactFlag.SEEN);
        } catch (final Throwable t) {
            throw panic(t);
        }
	}

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#deleteTeam(java.lang.Long)
     * 
     */
    public void deleteTeam(final Long artifactId) {
        try {
            artifactIO.deleteTeamRel(artifactId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#doesExist(java.util.UUID)
     * 
     */
    public Boolean doesExist(final UUID uniqueId) {
        try {
            return artifactIO.doesExist(uniqueId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#doesVersionExist(java.lang.Long)
     *
     */
    public Boolean doesVersionExist(final Long artifactId) {
        try {
            return artifactIO.doesVersionExist(artifactId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#doesVersionExist(java.lang.Long,
     *      java.lang.Long)
     * 
     */
    public Boolean doesVersionExist(final Long artifactId, final Long versionId) {
        try {
            return artifactIO.doesVersionExist(artifactId, versionId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#doesVersionExist(java.util.UUID, java.lang.Long)
     *
     */
    public Boolean doesVersionExist(final UUID uniqueId, final Long versionId) {
        try {
            return doesExist(uniqueId)
                    && doesVersionExist(readId(uniqueId), versionId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#handleDraftCreated(com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftCreatedEvent)
     * 
     */
    public void handleDraftCreated(final ArtifactDraftCreatedEvent event) {
        try {
            final Long artifactId = artifactIO.readId(event.getUniqueId());
            if (null == artifactId) {
                logger.logWarning("Artifact for event {0} does not exist locally.",
                        event);
            } else {
                switch (artifactIO.readType(artifactId)) {
                case CONTAINER:
                    getContainerModel().handleDraftCreated(artifactId,
                            event.getCreatedBy(), event.getCreatedOn());
                    break;
                default:
                    Assert.assertUnreachable("UNSUPPORTED ARTIFACT TYPE");
                }
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#handleDraftDeleted(com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent)
     * 
     */
    public void handleDraftDeleted(final ArtifactDraftDeletedEvent event) {
        try {
            final Long artifactId = artifactIO.readId(event.getUniqueId());
            if (null == artifactId) {
                logger.logWarning("Artifact for event {0} does not exist locally.",
                        event);
            } else {
                switch (artifactIO.readType(artifactId)) {
                case CONTAINER:
                    getContainerModel().handleDraftDeleted(event);
                    break;
                default:
                    Assert.assertUnreachable("Unsupported artifact type.");
                }
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#handleReceived(com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent)
     * 
     */
    public void handleReceived(final ArtifactReceivedEvent event) {
        try {
            final Long artifactId = artifactIO.readId(event.getUniqueId());
            if (null == artifactId) {
                logger.logWarning("Artifact for event {0} does not exist locally.",
                        event);
            } else {
                if (doesVersionExist(artifactId, event.getVersionId())) {
                    switch (artifactIO.readType(artifactId)) {
                    case CONTAINER:
                        getContainerModel().handleReceived(event);
                        break;
                    default:
                        Assert.assertUnreachable("UNSUPPORTED ARTIFACT TYPE");
                    }
                } else {
                    logger.logWarning("Artifact version for event {0} does not exist locally.",
                            event);
                }
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#handleTeamMemberAdded(com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent)
     * 
     */
    public void handleTeamMemberAdded(final ArtifactTeamMemberAddedEvent event) {
        try {
            final Long artifactId = readId(event.getUniqueId());
            if (null == artifactId) {
                logger.logWarning("Artifact for event {0} does not exist locally.",
                        event);
            } else {
                addTeamMember(artifactId, event.getJabberId());
            }
        } catch(final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#handleTeamMemberRemoved(com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent)
     * 
     */
    public void handleTeamMemberRemoved(final ArtifactTeamMemberRemovedEvent event) {
        try {
            final Long artifactId = readId(event.getUniqueId());
            if (null == artifactId) {
                logger.logWarning("Artifact for event {0} does not exist locally.",
                        event);
            } else {
                final User user = getUserModel().read(event.getJabberId());
                artifactIO.deleteTeamRel(artifactId, user.getLocalId());
            }
        } catch(final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.ArtifactModel#isFlagApplied(java.lang.Long,
     *      com.thinkparity.codebase.model.artifact.ArtifactFlag)
     * 
     */
    public Boolean isFlagApplied(final Long artifactId, final ArtifactFlag flag) {
        try {
            final List<ArtifactFlag> flags = artifactIO.readFlags(artifactId);
            return flags.contains(flag);
        } catch (final Throwable t) {
            throw panic(t);
        }
	}

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#readId(java.util.UUID)
     * 
     */
    public Long readId(final UUID uniqueId) {
        try {
            return artifactIO.readId(uniqueId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }
    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#readTeam(java.lang.Long,
     *      java.util.Comparator, com.thinkparity.codebase.filter.Filter)
     * 
     */
    public List<TeamMember> readTeam(final Long artifactId,
            final Comparator<? super User> comparator,
            final Filter<? super User> filter) {
        try {
            final List<TeamMember> team = artifactIO.readTeamRel2(artifactId);
            FilterManager.filter(team, filter);
            ModelSorter.sortTeamMembers(team, comparator);
            return team;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#readTeam2(java.lang.Long)
     *
     */
    public List<TeamMember> readTeam2(final Long artifactId) {
        try {
            return artifactIO.readTeamRel2(artifactId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#readTeamIds(java.lang.Long)
     * 
     */
    public List<JabberId> readTeamIds(final Long artifactId) {
        try {
            final List<TeamMember> teamMembers = artifactIO.readTeamRel2(artifactId);
            final List<JabberId> teamIds = new ArrayList<JabberId>(teamMembers.size());
            for (final TeamMember teamMember : teamMembers) {
                teamIds.add(teamMember.getId());
            }
            return teamIds;
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.ArtifactModel#readType(java.lang.Long)
     *
     */
    public ArtifactType readType(final Long artifactId) {
        try {
            return artifactIO.readType(artifactId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#readUniqueId(java.lang.Long)
     * 
     */
    public UUID readUniqueId(final Long artifactId) {
        try {
            return artifactIO.readUniqueId(artifactId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#removeFlagLatest(java.lang.Long)
     * 
     */
    public void removeFlagLatest(final Long artifactId) {
        try {
            removeFlag(artifactId, ArtifactFlag.LATEST);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#removeTeamMember(java.lang.Long,
     *      com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public void removeTeamMember(final Long artifactId, final JabberId userId) {
        try {
            assertTeamMember("USER IS NOT A TEAM MEMBER", artifactId, userId);
            final User user = getUserModel().read(userId);
            artifactIO.deleteTeamRel(artifactId, user.getLocalId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.Model#initializeModel(com.thinkparity.codebase.model.session.Environment,
     *      com.thinkparity.ophelia.model.workspace.Workspace)
     * 
     */
    @Override
    protected void initializeModel(final Environment environment,
            final Workspace workspace) {
        this.artifactIO = IOFactory.getDefault(workspace).createArtifactHandler();
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
    private TeamMember addTeamMember(final Long artifactId, final Long userId) {
        artifactIO.createTeamRel(artifactId, userId);
        // reindex NOTE no practical assurance that this is a container id
        getIndexModel().indexContainer(artifactId);
        return artifactIO.readTeamRel(artifactId, userId);
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
		final List<ArtifactFlag> flags = artifactIO.readFlags(artifactId);
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
		final List<ArtifactFlag> flags = artifactIO.readFlags(artifactId);
		if(flags.contains(flag)) {
			flags.remove(flag);
			artifactIO.updateFlags(artifactId, flags);
		}
		else {
			logger.logWarning("Artifact {0} is not flagged as {1}.",
                    artifactId, flag);
		}
	}
}
