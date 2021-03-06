/*
 * Mar 2, 2006
 */
package com.thinkparity.ophelia.model.artifact;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.TrueAssertion;
import com.thinkparity.codebase.filter.Filter;
import com.thinkparity.codebase.filter.FilterManager;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.ThinkParityException;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactFlag;
import com.thinkparity.codebase.model.artifact.ArtifactType;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.artifact.ArtifactVersionFlag;
import com.thinkparity.codebase.model.session.Environment;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftCreatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent;

import com.thinkparity.ophelia.model.Delegate;
import com.thinkparity.ophelia.model.Model;
import com.thinkparity.ophelia.model.artifact.delegate.HandleTeamMemberAdded;
import com.thinkparity.ophelia.model.artifact.delegate.HandleTeamMemberRemoved;
import com.thinkparity.ophelia.model.events.ArtifactListener;
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
public final class ArtifactModelImpl extends Model<ArtifactListener> implements
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
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#addTeamMember(com.thinkparity.codebase.model.artifact.Artifact,
     *      com.thinkparity.codebase.model.user.User)
     * 
     */
    public TeamMember addTeamMember(final Artifact artifact, final User user) {
        try {
            return addTeamMember(artifact.getId(), user);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#applyFlagBookmark(java.lang.Long)
     * 
     */
    public void applyFlagBookmark(final Long artifactId) {
        try {
            applyFlag(artifactId, ArtifactFlag.BOOKMARK);
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
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#applyFlagSeen(com.thinkparity.codebase.model.artifact.ArtifactVersion)
     *
     */
    public void applyFlagSeen(final ArtifactVersion version) {
        try {
            applyFlag(version, ArtifactVersionFlag.SEEN);
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
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#createTeam(java.lang.Long)
     * 
     */
    public List<TeamMember> createTeam(final Artifact artifact) {
        try {
            addTeamMember(artifact, localUser());
            return readTeam2(artifact.getId());
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
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#doesExist(java.lang.Long)
     * 
     */
    public Boolean doesExist(final Long artifactId) {
        try {
            return artifactIO.doesExist(artifactId);
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
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#handleEvent(com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent)
     *
     */
    @Override
    public void handleEvent(final ArtifactTeamMemberAddedEvent event) {
        try {
            final HandleTeamMemberAdded delegate = createDelegate(HandleTeamMemberAdded.class);
            delegate.setEvent(event);
            delegate.handleTeamMemberAdded();
        } catch(final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#handleEvent(com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent)
     *
     */
    @Override
    public void handleEvent(final ArtifactTeamMemberRemovedEvent event) {
        try {
            final HandleTeamMemberRemoved delegate = createDelegate(HandleTeamMemberRemoved.class);
            delegate.setEvent(event);
            delegate.handleTeamMemberRemoved();
        } catch(final Throwable t) {
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
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#isVersionSeenFlagApplied(com.thinkparity.codebase.model.artifact.Artifact)
     *
     */
    public Boolean isVersionFlagSeenApplied(final Artifact artifact) {
        try {
            return artifactIO.isVersionSeenFlagApplied(artifact);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#isVersionFlagSeenAppliedAll(com.thinkparity.codebase.model.artifact.Artifact)
     *
     */
    public Boolean isVersionFlagSeenAppliedAll(final Artifact artifact) {
        try {
            return artifactIO.isVersionSeenFlagAppliedAll(artifact);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#readEarliestVersionId(java.lang.Long)
     * 
     */
    public Long readEarliestVersionId(final Long artifactId) {
        try {
            return artifactIO.readEarliestVersionId(artifactId);
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
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#readLatestVersionId(java.lang.Long)
     * 
     */
    public Long readLatestVersionId(final Long artifactId) {
        try {
            return artifactIO.readLatestVersionId(artifactId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#readTeam(com.thinkparity.codebase.model.artifact.Artifact)
     *
     */
    public List<TeamMember> readTeam(final Artifact artifact) {
        try {
            /* TODO ArtifactModelImpl#readTeam - not quite right; the interface
             * artifact model should have default team member
             * comparators/filters */
            return readTeam(artifact.getId(), new Comparator<User>() {
                public int compare(final User o1, final User o2) {
                    return 0;
                }
            }, FilterManager.createDefault());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.ArtifactModel#readTeam(java.lang.Long)
     * 
     */
    public Set<User> readTeam(final Long artifactId) {
        try {
            return artifactIO.readTeamRel(artifactId);
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
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#removeFlagBookmark(java.lang.Long)
     * 
     */
    public void removeFlagBookmark(final Long artifactId) {
        try {
            removeFlag(artifactId, ArtifactFlag.BOOKMARK);
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
     * @see com.thinkparity.ophelia.model.artifact.InternalArtifactModel#removeFlagSeen(com.thinkparity.codebase.model.artifact.ArtifactVersion)
     *
     */
    public void removeFlagSeen(final ArtifactVersion version) {
        try {
            removeFlag(version, ArtifactVersionFlag.SEEN);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.artifact.ArtifactModel#removeFlagSeen(java.lang.Long)
     * 
     */
    public void removeFlagSeen(final Long artifactId) {
		try {
		    removeFlag(artifactId, ArtifactFlag.SEEN);   
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
     * Add a team member to an artifact.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param user
     *            A <code>User</code>.
     * @return A <code>TeamMember</code>.
     */
    TeamMember addTeamMember(final Long artifactId, final User user) {
        // create local team data
        artifactIO.createTeamRel(artifactId, user.getLocalId());
        // reindex NOTE no practical assurance that this is a container id
        getIndexModel().indexContainer(artifactId);
        return artifactIO.readTeamRel(artifactId, user.getLocalId());
    }

    /**
     * Obtain an artifact persistence interface.
     * 
     * @return An <code>ArtifactIOHandler</code>.
     */
    ArtifactIOHandler getArtifactIO() {
        return artifactIO;
    }

    /**
     * Apply a flag to an artifact version.
     * 
     * @param version
     *            The <code>ArtifactVersion</code>.
     * @param flag
     *            The flag.
     */
    private void applyFlag(final ArtifactVersion version, final ArtifactVersionFlag flag) {
        final List<ArtifactVersionFlag> flags = artifactIO.readFlags(version);
        if (flags.contains(flag)) {
            logger.logWarning("Artifact version {0} is already flagged as {1}.",
                    version, flag);
        }
        else {
            flags.add(flag);
            version.setFlags(flags);
            artifactIO.updateFlags(version, flags);
        }
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
     * Create an instance of a delegate.
     * 
     * @param <D>
     *            A delegate type.
     * @param type
     *            The delegate type <code>Class</code>.
     * @return An instance of <code>D</code>.
     */
    private <D extends Delegate<ArtifactModelImpl>> D createDelegate(
            final Class<D> type) {
        try {
            final D instance = type.newInstance();
            instance.initialize(this);
            return instance;
        } catch (final IllegalAccessException iax) {
            throw new ThinkParityException("Could not create delegate.", iax);
        } catch (final InstantiationException ix) {
            throw new ThinkParityException("Could not create delegate.", ix);
        }
    }

    /**
     * Remove a flag from an artifact version.
     * 
     * @param version
     *            The <code>ArtifactVersion</code>.
     * @param flag
     *            The flag.
     */
    private void removeFlag(final ArtifactVersion version, final ArtifactVersionFlag flag) {
        final List<ArtifactVersionFlag> flags = artifactIO.readFlags(version);
        if (flags.contains(flag)) {
            flags.remove(flag);
            version.setFlags(flags);
            artifactIO.updateFlags(version, flags);
        }
        else {
            logger.logWarning("Artifact version {0} is not flagged as {1}.",
                    version, flag);
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
