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
import com.thinkparity.codebase.model.artifact.DraftExistsException;
import com.thinkparity.codebase.model.user.TeamMember;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftCreatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.Constants.Versioning;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.sql.ArtifactSql;
import com.thinkparity.desdemona.model.user.InternalUserModel;
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
     * @see com.thinkparity.desdemona.model.artifact.ArtifactModel#addTeamMember(java.util.List,
     *      java.util.UUID, com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public void addTeamMember(final List<JabberId> team, final UUID uniqueId,
            final JabberId teamMemberId) {
        try {
            final InternalUserModel userModel = getUserModel();

            final Artifact artifact = read(uniqueId);
            final User teamMember = userModel.read(teamMemberId);
            final List<TeamMember> teamMembers = readTeam(artifact.getId());
            if (!contains(teamMembers, teamMember)) {
                addTeamMember(artifact.getId(), teamMember.getLocalId());
            }

            final ArtifactTeamMemberAddedEvent teamMemberAdded = new ArtifactTeamMemberAddedEvent();
            teamMemberAdded.setJabberId(teamMemberId);
            teamMemberAdded.setUniqueId(uniqueId);
            enqueueEvents(team, teamMemberAdded);
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#addTeamMember(java.lang.Long)
     *
     */
    public void addTeamMember(final Long artifactId) {
        try {
            addTeamMember(artifactId, user.getLocalId());
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.ArtifactModel#confirmReceipt(java.util.UUID,
     *      java.lang.Long, com.thinkparity.codebase.jabber.JabberId,
     *      java.util.Calendar, java.util.List, java.util.Calendar)
     * 
     */
    public void confirmReceipt(final UUID uniqueId, final Long versionId,
            final JabberId publishedBy, final Calendar publishedOn,
            final List<JabberId> publishedTo, final Calendar receivedOn) {
        try {
            final ArtifactReceivedEvent received = new ArtifactReceivedEvent();
            received.setUniqueId(uniqueId);
            received.setVersionId(versionId);
            received.setPublishedOn(publishedOn);
            received.setReceivedBy(user.getId());
            received.setReceivedOn(receivedOn);
            final List<JabberId> eventUserIds =
                new ArrayList<JabberId>(publishedTo.size() + 1);
            eventUserIds.add(publishedBy);
            eventUserIds.addAll(publishedTo);
            enqueueEvents(eventUserIds, received);

            // add user to the team
            final InternalArtifactModel artifactModel = getArtifactModel();
            final Artifact artifact = getArtifactModel().read(uniqueId);
            final List<TeamMember> localTeam = artifactModel.readTeam(
                    artifact.getId());
            final User receivedByUser = getUserModel().read(user.getId());
            if (!contains(localTeam, receivedByUser))
                addTeamMember(artifact.getId(), receivedByUser.getLocalId());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.ArtifactModel#create(java.util.UUID,
     *      java.util.Calendar)
     * 
     */
    public Artifact create(final UUID uniqueId, final Calendar createdOn) {
		try {
			artifactSql.create(uniqueId, user.getId(), Versioning.START,
                    user.getId(), createdOn);
			return read(uniqueId);
		} catch (final Throwable t) {
		    throw translateError(t);
        }
	}

    /**
     * @see com.thinkparity.desdemona.model.artifact.ArtifactModel#createDraft(com.thinkparity.codebase.jabber.JabberId,
     *      java.util.List, java.util.UUID, java.util.Calendar)
     * 
     */
    public void createDraft(final List<JabberId> team, final UUID uniqueId,
            final Calendar createdOn) throws DraftExistsException {
        try {
            final JabberId keyHolderId = readKeyHolder(uniqueId);
            if (keyHolderId.equals(User.THINKPARITY.getId())) {
                final Artifact artifact = read(uniqueId);
                final User currentOwner = getUserModel().read(User.THINKPARITY.getId());
                try {
                    artifactSql.updateDraftOwner(artifact, currentOwner, user,
                            createdOn);
                } catch (final HypersonicException hx) {
                    // TODO use a specific error code for this scenario
                    if ("Could not update draft owner.".equals(hx.getMessage())) {
                        throw new DraftExistsException();
                    } else {
                        throw hx;
                    }
                }
                notifyDraftCreated(team, artifact, user.getId(),
                        DateTimeProvider.getCurrentDateTime());
            } else {
                throw new DraftExistsException();
            }
        } catch (final DraftExistsException dex) {
            throw dex;
        } catch (final Throwable t) {
            throw translateError(t);
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
     * @see com.thinkparity.desdemona.model.artifact.InternalArtifactModel#removeTeamMember(com.thinkparity.codebase.jabber.JabberId,
     *      java.lang.Long, java.lang.Long)
     * 
     */
    public void removeTeamMember(final JabberId userId, final Long artifactId,
            final Long teamMemberId) {
        try {
            artifactSql.deleteTeamRel(artifactId, teamMemberId);
        } catch (final Throwable t) {
            throw panic(t);
        }
    }

    /**
     * @see com.thinkparity.desdemona.model.artifact.ArtifactModel#removeTeamMember(java.util.List,
     *      java.util.UUID, com.thinkparity.codebase.jabber.JabberId)
     * 
     */
    public void removeTeamMember(final List<JabberId> team,
            final UUID uniqueId, final JabberId teamMemberId) {
        try {
            final InternalUserModel userModel = getUserModel();

            final Artifact artifact = read(uniqueId);
            final User teamMember = userModel.read(teamMemberId);
            artifactSql.deleteTeamRel(artifact.getId(), teamMember.getLocalId());

            final ArtifactTeamMemberRemovedEvent teamMemberRemoved = 
                new ArtifactTeamMemberRemovedEvent();
            teamMemberRemoved.setUniqueId(uniqueId);
            teamMemberRemoved.setJabberId(teamMemberId);
            enqueueEvents(team, teamMemberRemoved);
        } catch (final Throwable t) {
            throw translateError(t);
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
     * Add a user to an artifact team.
     * 
     * @param artifactId
     *            An artifact id <code>Long</code>.
     * @param userId
     *            A user id <code>Long</code>.
     */
    private void addTeamMember(final Long artifactId, final Long userId) {
        artifactSql.createTeamRel(artifactId, userId);
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
        artifactSql.updateDraftOwner(artifact, user, newOwner, deletedOn);

        // fire notification
        final ArtifactDraftDeletedEvent draftDeleted = new ArtifactDraftDeletedEvent();
        draftDeleted.setUniqueId(artifact.getUniqueId());
        draftDeleted.setDeletedBy(user.getId());
        draftDeleted.setDeletedOn(currentDateTime());
        enqueueEvents(team, draftDeleted);
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
