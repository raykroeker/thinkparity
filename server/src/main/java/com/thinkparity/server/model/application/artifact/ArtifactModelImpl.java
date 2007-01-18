/*
 * Created On: Nov 29, 2005
 */
package com.thinkparity.desdemona.model.artifact;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.DateUtil;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.artifact.Artifact;
import com.thinkparity.codebase.model.artifact.ArtifactState;
import com.thinkparity.codebase.model.user.User;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftCreatedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactDraftDeletedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactReceivedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberAddedEvent;
import com.thinkparity.codebase.model.util.xmpp.event.ArtifactTeamMemberRemovedEvent;

import com.thinkparity.desdemona.model.AbstractModelImpl;
import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.io.sql.ArtifactSql;
import com.thinkparity.desdemona.model.io.sql.ArtifactSubscriptionSql;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.model.user.UserModel;

import org.jivesoftware.wildfire.auth.UnauthorizedException;

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
    void addTeamMember(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final JabberId teamMemberId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("team", team);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("teamMemberId", teamMemberId);
        try {
            final UserModel userModel = getUserModel();
            if (userModel.isArchive(userId)) {
                logInfo("Ignoring archive user {0}.", userId);
            } else {
                final ArtifactTeamMemberAddedEvent teamMemberAdded = new ArtifactTeamMemberAddedEvent();
                teamMemberAdded.setJabberId(teamMemberId);
                teamMemberAdded.setUniqueId(uniqueId);
                enqueueEvent(userId, team, teamMemberAdded);
            }
        } catch(final Throwable t) {
            throw translateError(t);
        }
    }

	void confirmReceipt(final JabberId userId, final UUID uniqueId,
            final Long versionId, final JabberId publishedBy,
            final Calendar publishedOn, final List<JabberId> publishedTo,
            final JabberId receivedBy, final Calendar receivedOn) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("versionId", versionId);
        logger.logVariable("publishedBy", publishedBy);
        logger.logVariable("publishedOn", publishedOn);
        logger.logVariable("publishedTo", publishedTo);
        logger.logVariable("receivedBy", receivedBy);
        logger.logVariable("receivedOn", receivedOn);
        try {
            assertIsAuthenticatedUser(userId);
            if (getUserModel().isArchive(userId)) {
                logInfo("Ignoring archive user {0}.", userId);
            } else {
                final ArtifactReceivedEvent received = new ArtifactReceivedEvent();
                received.setUniqueId(uniqueId);
                received.setVersionId(versionId);
                received.setPublishedOn(publishedOn);
                received.setReceivedBy(receivedBy);
                received.setReceivedOn(receivedOn);
                final List<JabberId> enqueueFor =
                    new ArrayList<JabberId>(publishedTo.size() + 1);
                enqueueFor.add(publishedBy);
                enqueueFor.addAll(publishedTo);
                enqueueEvent(userId, enqueueFor, received);
            }
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
    Artifact create(final JabberId userId, final UUID uniqueId,
            final Calendar createdOn) {
		logApiId();
		logVariable("userId", userId);
        logVariable("uniqueId", uniqueId);
        logVariable("createdOn", createdOn);
		try {
			artifactSql.insert(uniqueId, userId, ArtifactState.ACTIVE, session
                    .getJabberId(), createdOn);
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
    void createDraft(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("team", team);
        logger.logVariable("uniqueId", uniqueId);
        try {
            assertIsAuthenticatedUser(userId);
            assertSystemIsKeyHolder(uniqueId);
            final Artifact artifact = read(uniqueId);
            artifactSql.updateKeyHolder(
                    artifact.getId(), session.getJabberId().getUsername(),
                    session.getJabberId());
            notifyDraftCreated(team, artifact, session.getJabberId(), DateUtil
                    .getInstance());
        } catch (final Throwable t) {
            throw translateError(t);
        }
    }

	/**
     * Delete a draft from an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    void deleteDraft(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("team", team);
        logger.logVariable("uniqueId", uniqueId);
        try {
            assertIsKeyHolder(uniqueId);
            assertIsAuthenticatedUser(userId);
            // update key data
            final Artifact artifact = read(uniqueId);
            artifactSql.updateKeyHolder(artifact.getId(), User.THINK_PARITY, userId);
            // fire notification
            final ArtifactDraftDeletedEvent draftDeleted = new ArtifactDraftDeletedEvent();
            draftDeleted.setUniqueId(artifact.getUniqueId());
            draftDeleted.setDeletedBy(userId);
            draftDeleted.setDeletedOn(currentDateTime());
            enqueueEvent(userId, team, draftDeleted);
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
    void removeTeamMember(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final JabberId teamMemberId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("team", team);
        logger.logVariable("uniqueId", uniqueId);
        logger.logVariable("teamMemberId", teamMemberId);
        try {
            final UserModel userModel = getUserModel();
            if (userModel.isArchive(userId)) {
                logInfo("Ignoring archive user {0}.", userId);
            } else {
                final ArtifactTeamMemberRemovedEvent teamMemberRemoved = 
                    new ArtifactTeamMemberRemovedEvent();
                teamMemberRemoved.setUniqueId(uniqueId);
                teamMemberRemoved.setJabberId(teamMemberId);
                enqueueEvent(userId, team, teamMemberRemoved);
            }
        } catch (final Throwable t) {
            throw translateError(t);
        }
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
        enqueueEvent(session.getJabberId(), team, draftCreated);
    }
}
