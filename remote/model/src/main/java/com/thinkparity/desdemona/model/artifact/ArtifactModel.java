/*
 * Created On: Nov 29, 2005
 * $Id$
 */
package com.thinkparity.desdemona.model.artifact;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.artifact.Artifact;
import com.thinkparity.model.artifact.ArtifactFlag;

import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.contact.Contact;
import com.thinkparity.desdemona.model.session.Session;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactModel extends AbstractModel {

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
	 * Artifact model implementation.
	 */
	private final ArtifactModelImpl impl;

    /**
	 * Synchronization lock for the implementation.
	 */
	private final Object implLock;

	/**
	 * Create a ArtifactModel.
	 */
	private ArtifactModel(final Session session) {
		super();
		this.impl = new ArtifactModelImpl(session);
		this.implLock = new Object();
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
        synchronized(implLock) { impl.addTeamMember(uniqueId, jabberId); }
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
        synchronized(implLock) {
            impl.confirmReceipt(uniqueId, versionId, receivedFrom);
        }
    }

	/**
	 * Create an artifact.
	 * 
	 * @param uniqueId
	 *            The artifact unique id.
	 * @return The new artifact.
	 * @throws ParityServerModelException
	 */
	public Artifact create(final UUID uniqueId)
            throws ParityServerModelException {
		synchronized(implLock) { return impl.create(uniqueId); }
	}

	/**
     * Create a draft for an artifact.
     * 
     * @param uniqueId
     *            The artifact unique id.
     */
    public void createDraft(final UUID uniqueId) {
        synchronized(implLock) { impl.createDraft(uniqueId); }
    }

	/**
     * Delete a draft from an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     */
    public void deleteDraft(final UUID uniqueId) {
        synchronized (implLock) {
            impl.deleteDraft(uniqueId);
        }
    }

    /**
     * Flag an artifact.
     * 
     * @param artifact
     *            An <code>Artifact</code>.
     * @param artifactFlag
     *            A <code>ArtifactFlag</code>.
     * @throws ParityServerModelException
     */
	public void flag(final Artifact artifact,
            final ArtifactFlag artifactFlag) {
		synchronized (implLock) {
            impl.flag(artifact, artifactFlag);
		}
	}

    /**
	 * Obtain a handle to an artifact for a given artifact unique id.
	 * 
	 * @param artifactUniqueId
	 *            An artifact unique id.
	 */
	public Artifact read(final UUID artifactUniqueId) {
		synchronized (implLock) {
            return impl.read(artifactUniqueId);
		}
	}

	public List<ArtifactSubscription> getSubscription(final UUID artifactUniqueId)
			throws ParityServerModelException {
		synchronized(implLock) { return impl.getSubscription(artifactUniqueId); }
	}

	/**
	 * Obtain a list of artifacts the user has keys for.
	 * 
	 * @return A list of artifacts the user has keys for.
	 * @throws ParityServerModelException
	 */
	public List<Artifact> listForKeyHolder() throws ParityServerModelException {
		synchronized(implLock) { return impl.listForKeyHolder(); }
	}

	public void reactivate(final List<JabberId> team, final UUID uniqueId,
            final Long versionId, final String name, final byte[] bytes)
            throws ParityServerModelException {
        synchronized(implLock) {
            impl.reactivate(team, uniqueId, versionId, name, bytes);
        }
    }

	public List<Contact> readContacts(final UUID artifactUniqueId) {
		synchronized(implLock) { return impl.readContacts(artifactUniqueId); }
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
        synchronized (implLock) {
            return impl.readKeyHolder(userId, uniqueId);
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
        synchronized (implLock) {
            impl.removeTeamMember(uniqueId, jabberId);
        }
    }
}
