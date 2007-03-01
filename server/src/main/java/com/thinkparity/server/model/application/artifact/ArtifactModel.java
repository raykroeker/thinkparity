/*
 * Created On: Nov 29, 2005
 * $Id$
 */
package com.thinkparity.desdemona.model.artifact;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.artifact.Artifact;

import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.session.Session;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactModel extends AbstractModel<ArtifactModelImpl> {

    public static InternalArtifactModel getInternalModel(final Context context,
            final Session session) {
        return new InternalArtifactModel(context, session);
    }

    /**
	 * Obtain a handle to the artifact model.
	 * 
	 * @return A handle to the artifact model.
	 */
	public static ArtifactModel getModel(final Session session) {
		final ArtifactModel artifactModel = new ArtifactModel(session);
		return artifactModel;
	}

    /**
     * Create ArtifactModel.
     * 
     * @param session
     *            A <code>Session</code>.
     */
	protected ArtifactModel(final Session session) {
		super(new ArtifactModelImpl(session));
	}

	/**
     * Add a user to an artifact's team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A user's jabber id.
     */
	public void addTeamMember(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final JabberId teamMemberId) {
        synchronized (getImplLock()) {
            getImpl().addTeamMember(userId, team, uniqueId, teamMemberId);
        }
    }

    /**
     * Confrim receipt of an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param receivedBy
     *            The received by user id <code>JabberId</code>.
     * @param uniqueId
     *            The unique id <code>UUID</code>.
     * @param versionId
     *            The version id <code>Long</code>.
     */
    public void confirmReceipt(final JabberId userId, final UUID uniqueId,
            final Long versionId, final JabberId publishedBy,
            final Calendar publishedOn, final List<JabberId> publishedTo,
            final JabberId receivedBy, final Calendar receivedOn) {
        synchronized (getImplLock()) {
            getImpl().confirmReceipt(userId, uniqueId, versionId, publishedBy,
                    publishedOn, publishedTo, receivedBy, receivedOn);
        }
    }

    /**
     * Create an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
	public Artifact create(final JabberId userId, final UUID uniqueId,
            final Calendar createdOn) {
		synchronized (getImplLock()) {
            return getImpl().create(userId, uniqueId, createdOn);
		}
	}

    /**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            The artifact unique id.
     */
    public void createDraft(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final Calendar createdOn) {
        synchronized (getImplLock()) {
            getImpl().createDraft(userId, team, uniqueId, createdOn);
        }
    }

	/**
     * Delete a draft from an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    public void deleteDraft(final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final Calendar deletedOn) {
        synchronized (getImplLock()) {
            getImpl().deleteDraft(userId, team, uniqueId, deletedOn);
        }
    }

	/**
	 * Obtain a handle to an artifact for a given artifact unique id.
	 * 
	 * @param artifactUniqueId
	 *            An artifact unique id.
	 */
	public Artifact read(final UUID artifactUniqueId) {
		synchronized (getImplLock()) {
            return getImpl().read(artifactUniqueId);
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
    public JabberId readKeyHolder(final JabberId userId, final UUID uniqueId) {
        synchronized (getImplLock()) {
            return getImpl().readKeyHolder(userId, uniqueId);
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
    public void removeTeamMember(final JabberId userId,
            final List<JabberId> team, final UUID uniqueId,
            final JabberId teamMemberId) {
        synchronized (getImplLock()) {
            getImpl().removeTeamMember(userId, team, uniqueId, teamMemberId);
        }
    }
}
