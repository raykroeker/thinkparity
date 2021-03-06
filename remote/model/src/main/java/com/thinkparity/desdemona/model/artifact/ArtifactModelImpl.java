/*
 * Created On: Nov 29, 2005
 */
package com.thinkparity.desdemona.model.artifact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactVersion;
import com.thinkparity.codebase.model.artifact.DraftExistsException;
import com.thinkparity.codebase.model.artifact.IllegalVersionException;
import com.thinkparity.codebase.model.crypto.Secret;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftCreatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.sql.ArtifactSql;
import com.thinkparity.desdemona.util.DateTimeProvider;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Artifact Model Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ArtifactModelImpl extends AbstractModelImpl implements
        ArtifactModel, InternalArtifactModel {

    /** Artifact sql io. */
	private ArtifactSql artifactSql;

    /**
     * Create ArtifactModelImpl.
     *
	 */
    public ArtifactModelImpl() {
        super();
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#addTeamMember(com.thinkparity.codebase.model.artifact.Artifact)
     *
     */
    public void addTeamMember(final Artifact artifact) {
        try {
            final List<User> users = new ArrayList<User>(1);
            users.add(user);
            addTeamMembers(artifact, users);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#addTeamMember(com.thinkparity.codebase.model.artifact.Artifact)
     * 
     */
    public void addTeamMembers(final Artifact artifact, final List<User> users) {
        try {
            final List<Long> userIds = new ArrayList<Long>(users.size());
            for (final User user : users) {
                userIds.add(user.getLocalId());
            }
            artifactSql.createTeamRel(artifact.getId(), userIds);
            // fire notifications
            final List<TeamMember> team = artifactSql.readTeamRel(artifact.getId());
            ArtifactTeamMemberAddedEvent event;
            for (final User user : users) {
                event = new ArtifactTeamMemberAddedEvent();
                event.setJabberId(user.getId());
                event.setUniqueId(artifact.getUniqueId());
                enqueueEvents(getIds(team, new ArrayList<JabberId>(team.size())), event);    
            }
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#create(com.thinkparity.codebase.model.artifact.Artifact,
     *      com.thinkparity.codebase.model.artifact.ArtifactVersion)
     * 
     */
    public Artifact create(final Artifact artifact, final ArtifactVersion latestVersion) {
		try {
            artifactSql.create(artifact, latestVersion, user, user);
			return read(artifact.getUniqueId());
		} catch (final Throwable t) {
		    throw translateError(t);
        }
	}

    /**
     * @see com.thinkparity.desdemona.model.artifact.ArtifactModel#createDraft(java.util.List, java.util.UUID, java.lang.Long, java.util.Calendar)
     *
     */
    public void createDraft(final List<JabberId> team, final UUID uniqueId,
            final Long latestVersionId, final Calendar createdOn)
            throws DraftExistsException, IllegalVersionException {
        try {
            final JabberId keyHolderId = readKeyHolder(uniqueId);
            if (keyHolderId.equals(User.THINKPARITY.getId())) {
                final Artifact artifact = read(uniqueId);
                if (isLatestVersionId(artifact.getId(), latestVersionId)) {
                    final User currentOwner = getUserModel().read(User.THINKPARITY.getId());
                    try {
                        artifactSql.updateDraftOwner(artifact, latestVersionId,
                                currentOwner, user, createdOn);
                    } catch (final HypersonicException hx) {
                        // TODO use a specific error code for this scenario
                        if ("Could not update draft owner.".equals(hx.getMessage())) {
                            if (isLatestVersionId(artifact.getId(), latestVersionId)) {
                                throw new DraftExistsException();
                            } else {
                                throw new IllegalVersionException();
                            }
                        } else {
                            throw hx;
                        }
                    }
                    notifyDraftCreated(team, artifact, user.getId(),
                            DateTimeProvider.getCurrentDateTime());
                } else {
                    throw new IllegalVersionException();
                }
            } else {
                throw new DraftExistsException();
            }
        } catch (final DraftExistsException dex) {
            throw dex;
        } catch (final IllegalVersionException ivx) {
            throw ivx;
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#createSecret(com.thinkparity.codebase.model.artifact.ArtifactVersion,
     *      com.thinkparity.codebase.model.crypto.Secret)
     * 
     */
    public void createSecret(final ArtifactVersion version, final Secret secret) {
        try {
            artifactSql.createSecret(version, secret);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#createVersion(com.thinkparity.codebase.model.artifact.ArtifactVersion)
     *
     */
    public void createVersion(final ArtifactVersion version) {
        try {
            artifactSql.createVersion(version);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#deleteDraft(com.thinkparity.codebase.model.artifact.Artifact,
     *      java.util.Calendar, java.lang.Boolean)
     * 
     */
    @Override
    public void deleteDraft(final Artifact artifact, final Calendar deletedOn,
            final Boolean enqueueEvents) {
        try {
            Assert.assertTrue(isDraftOwner(artifact),
                    "User {0} is not the draft owner.", user.getId());

            // update draft owner to system user
            final User newOwner = getUserModel().read(User.THINKPARITY.getId());
            final Long latestVersionId = artifactSql.readLatestVersionId(artifact.getId());
            artifactSql.updateDraftOwner(artifact, latestVersionId, user,
                    newOwner, deletedOn);

            if (enqueueEvents) {
                /* enqueue events */
                enqueueDeleteDraftEvents(artifact, deletedOn);
            }
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.ArtifactModel#deleteDraft(java.util.List,
     *      java.util.UUID, java.util.Calendar)
     * 
     */
    public void deleteDraft(final List<JabberId> team, final UUID uniqueId,
            final Calendar deletedOn) {
        try {
            deleteDraft(user.getId(), team, uniqueId, deletedOn);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#deleteDrafts(java.util.Calendar)
     * 
     */
    public void deleteDrafts(final Calendar deletedOn) {
        try {
            final List<UUID> uniqueIds = artifactSql.readDraftUniqueIds(user.getId());
            final List<TeamMember> teamMembers = new ArrayList<TeamMember>();
            final List<JabberId> teamIds = new ArrayList<JabberId>();
            Long artifactId;
            for (final UUID uniqueId : uniqueIds) {
                artifactId = artifactSql.readArtifactId(uniqueId);
                teamMembers.clear();
                teamMembers.addAll(artifactSql.readTeamRel(artifactId));
                for (final TeamMember teamMember : teamMembers) {
                    teamIds.add(teamMember.getId());
                }
                deleteDraft(user.getId(), teamIds, uniqueId, deletedOn);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#doesExist(java.util.UUID)
     *
     */
    public Boolean doesExist(final UUID uniqueId) {
        try {
            return artifactSql.doesExist(uniqueId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#doesExistSecret(com.thinkparity.codebase.model.artifact.ArtifactVersion)
     *
     */
    public Boolean doesExistSecret(final ArtifactVersion version) {
        try {
            return artifactSql.doesExistSecret(version);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#doesExistVersion(com.thinkparity.codebase.model.artifact.Artifact,
     *      java.lang.Long)
     * 
     */
    public Boolean doesExistVersion(final Artifact artifact, final Long versionId) {
        try {
            return artifactSql.doesExistVersion(artifact, versionId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#enqueueDeleteDraftEvents(com.thinkparity.codebase.model.artifact.Artifact, java.util.Calendar)
     *
     */
    @Override
    public void enqueueDeleteDraftEvents(final Artifact artifact,
            final Calendar deletedOn) {
        try {
            final ArtifactDraftDeletedEvent draftDeleted = new ArtifactDraftDeletedEvent();
            draftDeleted.setUniqueId(artifact.getUniqueId());
            draftDeleted.setDeletedBy(user.getId());
            draftDeleted.setDeletedOn(deletedOn);

            final List<TeamMember> team = artifactSql.readTeamRel(artifact.getId());
            final List<JabberId> teamIds = new ArrayList<JabberId>(team.size());
            for (final TeamMember teamMember : team) {
                teamIds.add(teamMember.getId());
            }
            enqueueEvents(teamIds, draftDeleted);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#isDraftOwner(com.thinkparity.codebase.model.artifact.Artifact)
     *
     */
    public Boolean isDraftOwner(final Artifact artifact) {
        try {
            final JabberId draftOwner = artifactSql.readDraftOwner(
                    artifact.getUniqueId());
            return draftOwner.equals(user.getId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#isTeamMember(com.thinkparity.codebase.model.artifact.Artifact)
     *
     */
    public Boolean isTeamMember(final Artifact artifact) {
        try {
            return artifactSql.doesExistTeamMember(
                    artifact.getId(), user.getLocalId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.ArtifactModel#read(java.util.UUID)
     * 
     */
    public Artifact read(final UUID uniqueId) {
        logApiId();
		logVariable("uniqueId", uniqueId);
		try {
            return artifactSql.read(uniqueId);
		} catch (final Throwable t) {
            throw translateError(t);
		}
	}

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#readDraftOwner(java.util.UUID)
     * 
     */
    public JabberId readDraftOwner(final UUID uniqueId) {
        logVariable("uniqueId", uniqueId);
        try {
            return artifactSql.readDraftOwner(uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.ArtifactModel#readKeyHolder(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.UUID)
     * 
     */
    public JabberId readKeyHolder(final UUID uniqueId) {
        try {
            return artifactSql.readDraftOwner(uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#readLatestVersionId(com.thinkparity.codebase.model.artifact.Artifact)
     *
     */
    public Long readLatestVersionId(Artifact artifact) {
        try {
            return artifactSql.readLatestVersionId(artifact.getId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#readSecretKey(com.thinkparity.codebase.model.artifact.ArtifactVersion)
     *
     */
    public Secret readSecret(final ArtifactVersion version) {
        try {
            return artifactSql.readSecret(version);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#readTeam(com.thinkparity.codebase.jabber.JabberId,
     *      java.lang.Long)
     * 
     */
    public List<TeamMember> readTeam(final Long artifactId) {
        try {
            return artifactSql.readTeamRel(artifactId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#readTeamArtifactIds(com.thinkparity.codebase.model.user.User)
     * 
     */
    public List<UUID> readTeamArtifactIds(final User user) {
        try {
            return artifactSql.readTeamArtifactUniqueIds(user.getLocalId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#readVersion(com.thinkparity.codebase.model.artifact.Artifact, java.lang.Long)
     *
     */
    public ArtifactVersion readVersion(final Artifact artifact, final Long versionId) {
        try {
            return artifactSql.readVersion(artifact, versionId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#removeTeamMember(com.thinkparity.codebase.model.artifact.Artifact)
     *
     */
    public void removeTeamMember(final Artifact artifact) {
        try {
            final List<TeamMember> team = artifactSql.readTeamRel(artifact.getId());
            artifactSql.deleteTeamRel(artifact.getId(), user.getLocalId());
            // fire notifications
            ArtifactTeamMemberRemovedEvent event = new ArtifactTeamMemberRemovedEvent();
            event.setJabberId(user.getId());
            event.setUniqueId(artifact.getUniqueId());
            enqueueEvents(getIds(team, new ArrayList<JabberId>(team.size())), event);    
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#updateLatestVersion(com.thinkparity.codebase.model.artifact.ArtifactVersion)
     *
     */
    public void updateLatestVersionId(final Artifact artifact,
            final Long versionId, final Calendar createdOn) {
        try {
            final User draftOwner = getUserModel().read(User.THINKPARITY.getId());
            artifactSql.updateLatestVersionId(artifact.getId(),
                    versionId, draftOwner.getLocalId(),
                    user.getLocalId(), createdOn);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.AbstractModelImpl#initializeModel(com.thinkparity.desdemona.model.session.Session)
     *
     */
    @Override
    protected void initialize() {
        artifactSql = new ArtifactSql();
    }

    /**
     * Delete a draft for a draft owner.
     * 
     * @param userId
     *            A user id <code>JabberId</code> for the current draft owner.
     * @param team
     *            A team <code>List<JabberId></code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @param deletedOn
     *            A deleted on <code>Calendar</code>.
     */
    private void deleteDraft(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final Calendar deletedOn) {
        final JabberId keyHolderId = readKeyHolder(uniqueId);
        Assert.assertTrue(keyHolderId.equals(userId),
                "User {0} is not the active key holder {1}.", userId,
                keyHolderId);

        // update key data
        final Artifact artifact = read(uniqueId);
        final User newOwner = getUserModel().read(User.THINKPARITY.getId());
        final Long latestVersionId = artifactSql.readLatestVersionId(artifact.getId());
        artifactSql.updateDraftOwner(artifact, latestVersionId, user, newOwner,
                deletedOn);

        // fire notification
        final ArtifactDraftDeletedEvent draftDeleted = new ArtifactDraftDeletedEvent();
        draftDeleted.setUniqueId(artifact.getUniqueId());
        draftDeleted.setDeletedBy(user.getId());
        draftDeleted.setDeletedOn(currentDateTime());
        enqueueEvents(team, draftDeleted);
    }

    /**
     * Determine whether or not the version id is the latest version id.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param versionId
     *            A version id <code>Long</code>.
     * @return True if the version id is equal to the latest version id.
     */
    private boolean isLatestVersionId(final Long artifactId, final Long versionId) {
        return artifactSql.readLatestVersionId(artifactId).equals(versionId);
    }

    /**
     * Notify the team a a draft was created.
     * 
     * @param artifact
     *            The <code>Artifact</code>.
     * @param createdBy
     *            The draft creator <code>JabberId</code>.
     * @param createdOn
     *            The draft creation <code>Calendar</code>.
     * @throws ParityServerModelException
     * @throws UnauthorizedException
     */
    private void notifyDraftCreated(final List<JabberId> team,
            final Artifact artifact, final JabberId createdBy,
            final Calendar createdOn) {
        final ArtifactDraftCreatedEvent draftCreated = new ArtifactDraftCreatedEvent();
        draftCreated.setUniqueId(artifact.getUniqueId());
        draftCreated.setCreatedBy(createdBy);
        draftCreated.setCreatedOn(createdOn);
        enqueueEvents(team, draftCreated);
    }
}
