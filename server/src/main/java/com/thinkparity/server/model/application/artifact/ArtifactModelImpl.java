/*
 * Created On: Nov 29, 2005
 */
package com.thinkparity.desdemona.model.artifact;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.jivesoftware.wildfire.auth.UnauthorizedException;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.io.sql.ArtifactSql;
import com.thinkparity.desdemona.model.io.sql.ArtifactSubscriptionSql;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.user.UserModel;
import com.thinkparity.desdemona.util.xmpp.IQWriter;

/**
 * @author raykroeker@gmail.com
 * @version 1.1.2.5
 */
class ArtifactModelImpl extends AbstractModelImpl {

    /** Artifact sql io. */
	private final ArtifactSql artifactSql;

    /** Artifact subscription sql io. */
	private final ArtifactSubscriptionSql artifactSubscriptionSql;

	/**
	 * Create a ArtifactModelImpl.
	 */
	ArtifactModelImpl(final Session session) {
		super(session);
		this.artifactSql = new ArtifactSql();
		this.artifactSubscriptionSql = new ArtifactSubscriptionSql();
	}

    /**
     * Add a user to an artifact's team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A user's jabber id.
     */
    void addTeamMember(final UUID uniqueId, final JabberId userId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("userId", userId);
        try {
            final UserModel userModel = getUserModel();
            if (getUserModel().isArchive(userId)) {
                logInfo("Ignoring archive user {0}.", userId);
            } else {
                final User user = userModel.read(userId);
                final Artifact artifact = read(uniqueId);
                artifactSubscriptionSql.insert(artifact.getId(),
                        userId.getUsername(), session.getJabberId());
                notifyTeamMemberAdded(artifact, user);
            }
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

    /**
     * Archive an artifact for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    void archive(final JabberId userId, final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        getArchiveModel().archive(userId, uniqueId);
    }

    /**
     * Confirm an artifact receipt.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @param receivedFrom
     *            The original sender of the document.
     * @throws ParityServerModelException
     */
	void confirmReceipt(final JabberId userId, final UUID uniqueId,
            final Long versionId, final JabberId receivedBy, final Calendar receivedOn) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        logVariable("versionId", versionId);
        logVariable("receivedBy", receivedBy);
        logVariable("receivedOn", receivedOn);
        try {
            assertIsAuthenticatedUser(userId);
            final IQWriter notification = createIQWriter("artifact:received");
            notification.writeUniqueId("uniqueId", uniqueId);
            notification.writeLong("versionId", versionId);
            notification.writeJabberId("receivedBy", receivedBy);
            notification.writeCalendar("receivedOn", receivedOn);
            notifyTeam(uniqueId, notification.getIQ());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * Create an artifact; and add the creator to the team immediately. Note
     * that we are deliberately not using the model api to add a team member
     * because we do not want to fire off notifications.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     * @return The new <code>Artifact</code>.
     */
    Artifact create(final JabberId userId, final UUID uniqueId) {
		logApiId();
		logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
		try {
			artifactSql.insert(uniqueId, userId, ArtifactState.ACTIVE, session
                    .getJabberId());
			final Artifact artifact = read(uniqueId);
            artifactSubscriptionSql.insert(artifact.getId(),
                    userId.getUsername(), session.getJabberId());
			return read(uniqueId);
		} catch (final Throwable t) {
		    throw translateError(t);
        }
	}

    /**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            The artifact unique id.
     */
    void createDraft(final UUID uniqueId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        try {
            assertSystemIsKeyHolder("[SYSTEM IS NOT KEY HOLDER]", uniqueId);
            final Artifact artifact = read(uniqueId);
            artifactSql.updateKeyHolder(
                    artifact.getId(), session.getJabberId().getUsername(),
                    session.getJabberId());
            notifyDraftCreated(artifact, session.getJabberId(), DateUtil
                    .getInstance());
        }
        catch(final ParityServerModelException psmx) {
            throw translateError(psmx);
        }
        catch(final SQLException sqlx) {
            throw translateError(sqlx);
        }
        catch(final UnauthorizedException ux) {
            throw translateError(ux);
        }
    }

	/**
     * Delete a draft from an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    void deleteDraft(final UUID uniqueId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        try {
            assertIsKeyHolder(uniqueId);
            final Artifact artifact = read(uniqueId);
            final JabberId sessionJabberId = session.getJabberId();
            artifactSql.updateKeyHolder(artifact.getId(), User.THINK_PARITY, sessionJabberId);
            notifyDraftDeleted(artifact, session.getJabberId(), DateUtil.getInstance());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    /**
	 * Obtain a handle to an artifact for a given artifact unique id.
	 * 
	 * @param uniqueId
	 *            An artifact unique id.
	 */
	Artifact read(final UUID uniqueId) {
        logApiId();
		logVariable("uniqueId", uniqueId);
		try {
            return artifactSql.select(uniqueId);
		} catch (final Throwable t) {
            throw translateError(t);
		}
	}

    /**
     * Read the key holder for an artifact.
     * 
     * @param userId
     *            The user id <code>JabberId</code>.
     * @param uniqueId
     *            The artifact unique id <code>UUID</code>.
     * @return The artifact key holder <code>JabberId</code>.
     */
    JabberId readKeyHolder(final JabberId userId, final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        assertIsAuthenticatedUser(userId);
        try {
            return artifactSql.readKeyHolder(uniqueId);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

    List<JabberId> readTeamIds(final UUID uniqueId) {
        logApiId();
		logVariable("uniqueId", uniqueId);
		try {
			final Artifact artifact = read(uniqueId);
            if (null == artifact) {
                return Collections.emptyList();
            } else {
                final List<ArtifactSubscription> subscriptions =
                    artifactSubscriptionSql.select(artifact.getId());
                final List<JabberId> teamIds = new ArrayList<JabberId>(subscriptions.size());
                for (final ArtifactSubscription subscription : subscriptions) {
                    teamIds.add(subscription.getJabberId());
                }
                return teamIds;
            }
		} catch (final Throwable t) {
			throw translateError(t);
		}
	}

	/**
     * Remove a user from an artifact's team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A user's jabber id.
     */
    void removeTeamMember(final UUID uniqueId, final JabberId jabberId) {
        logApiId();
        logVariable("uniqueId", uniqueId);
        logVariable("jabberId", jabberId);
        try {
            final Artifact artifact = read(uniqueId);
            final Long artifactId = artifact.getId();
            final ArtifactSubscription subscription =
                artifactSubscriptionSql.read(artifactId, jabberId);
            if(null != subscription) {
                artifactSubscriptionSql.delete(artifactId, jabberId.getUsername());
                // if all subscriptions are deleted; delete the artifact
                if (!artifactSubscriptionSql.existSubscriptions(artifactId)) {
                    artifactSql.delete(artifactId);
                }
            }
            final User user = getUserModel().read(jabberId);
            notifyTeamMemberRemoved(artifact, user);
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * Restore an artifact for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A unique id <code>UUID</code>.
     */
    void restore(final JabberId userId, final UUID uniqueId) {
        logApiId();
        logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        getArchiveModel().restore(userId, uniqueId);
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
    private void notifyDraftCreated(final Artifact artifact,
            final JabberId createdBy, final Calendar createdOn)
            throws ParityServerModelException, UnauthorizedException {
        final IQWriter notification = createIQWriter("artifact:draftcreated");
        notification.writeUniqueId("uniqueId", artifact.getUniqueId());
        notification.writeJabberId("createdBy", createdBy);
        notification.writeCalendar("createdOn", createdOn);
        final List<JabberId> team = getArtifactModel().readTeamIds(artifact.getUniqueId());
        team.remove(createdBy);
        send(team, notification.getIQ());
    }

    /**
     * Notify an artifact's team a draft was deleted.
     * @param artifact An <code>Artifact</code>.
     * @param deletedBy The <code>JabberId</code> of the deleting user.
     * @param deletedOn When the draft was deleted.
     * @throws ParityServerModelException
     * @throws UnauthorizedException
     */
    private void notifyDraftDeleted(final Artifact artifact,
            final JabberId deletedBy, final Calendar deletedOn)
            throws ParityServerModelException, UnauthorizedException {
        final IQWriter notification = createIQWriter("artifact:draftdeleted");
        notification.writeUniqueId("uniqueId", artifact.getUniqueId());
        notification.writeJabberId("deletedBy", deletedBy);
        notification.writeCalendar("deletedOn", deletedOn);
        final List<JabberId> team = getArtifactModel().readTeamIds(artifact.getUniqueId());
        team.remove(deletedBy);
        send(team, notification.getIQ());
    }

    /**
     * Notify an artifact's team; a team member was added.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @param user
     *            A <code>User</code>.
     * @throws ParityServerModelException
     * @throws UnauthorizedException
     */
    private void notifyTeamMemberAdded(final Artifact artifact, final User user)
            throws ParityServerModelException, UnauthorizedException {
        final IQWriter notification = createIQWriter("artifact:teammemberadded");
        notification.writeUniqueId("uniqueId", artifact.getUniqueId());
        notification.writeJabberId("jabberId", user.getId());
        notifyTeam(artifact.getUniqueId(), notification.getIQ());
    }

    /**
     * Notify an artifact's team; a team member was removed.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @param user
     *            A <code>user</code>.
     * @throws ParityServerModelException
     * @throws UnauthorizedException
     */
    private void notifyTeamMemberRemoved(final Artifact artifact,
            final User user) throws ParityServerModelException,
            UnauthorizedException {
        final IQWriter notification = createIQWriter("artifact:teammemberremoved");
        notification.writeUniqueId("uniqueId", artifact.getUniqueId());
        notification.writeJabberId("jabberId", user.getId());
        notifyTeam(artifact.getUniqueId(), notification.getIQ());
    }
}
