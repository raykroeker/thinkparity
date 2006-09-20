/*
 * Created On: Nov 29, 2005
 * $Id$
 */
package com.thinkparity.desdemona.model.artifact;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.artifact.Artifact;

import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.session.Session;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactModel extends AbstractModel<ArtifactModelImpl> {

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
	 * Create a ArtifactModel.
	 */
	private ArtifactModel(final Session session) {
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
	public void addTeamMember(final UUID uniqueId, final JabberId jabberId) {
        synchronized (getImplLock()) {
            getImpl().addTeamMember(uniqueId, jabberId);
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
    public void archive(final JabberId userId, final UUID uniqueId) {
        synchronized (getImplLock()) {
            getImpl().archive(userId, uniqueId);
        }
    }

    /**
     * Confrim receipt of an artifact.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @param versionId
     *            The artifact version id.
     * @param receivedFrom
     *            The original sender of the artifact.
     * @throws ParityServerModelException
     */
    public void confirmReceipt(final UUID uniqueId, final Long versionId,
            final JabberId receivedFrom) throws ParityServerModelException {
        synchronized(getImplLock()) {
            getImpl().confirmReceipt(uniqueId, versionId, receivedFrom);
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
	public Artifact create(final JabberId userId, final UUID uniqueId) {
		synchronized (getImplLock()) {
            return getImpl().create(userId, uniqueId);
		}
	}

	/**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            The artifact unique id.
     */
    public void createDraft(final UUID uniqueId) {
        synchronized(getImplLock()) { getImpl().createDraft(uniqueId); }
    }

	/**
     * Delete a draft from an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    public void deleteDraft(final UUID uniqueId) {
        synchronized (getImplLock()) {
            getImpl().deleteDraft(uniqueId);
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

	public List<JabberId> readTeamIds(final UUID uniqueId) {
		synchronized (getImplLock()) {
            return getImpl().readTeamIds(uniqueId);
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
    public void removeTeamMember(final UUID uniqueId, final JabberId jabberId) {
        synchronized (getImplLock()) {
            getImpl().removeTeamMember(uniqueId, jabberId);
        }
    }
}
