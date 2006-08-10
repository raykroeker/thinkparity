/*
 * Created On: Nov 29, 2005
 * $Id$
 */
package com.thinkparity.server.model.artifact;

import java.util.List;
import java.util.UUID;

import org.xmpp.packet.JID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.model.AbstractModel;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.contact.Contact;
import com.thinkparity.server.model.session.Session;

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
	 * Accept the key request for the artifact from the jid.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @param jid
	 *            The requestor's jive id.
	 * @throws ParityServerModelException
	 */
	public void acceptKeyRequest(final UUID artifactUniqueId, final JID jid)
			throws ParityServerModelException {
		synchronized(implLock) { impl.acceptKeyRequest(artifactUniqueId, jid); }
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
	 * Close an artifact.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @throws ParityServerModelException
	 */
	public void close(final UUID artifactUniqueId)
			throws ParityServerModelException {
		synchronized(implLock) { impl.close(artifactUniqueId); }
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

	public void delete(final UUID artifactUniqueId)
			throws ParityServerModelException {
		synchronized(implLock) { impl.delete(artifactUniqueId); }
	}

	/**
	 * Deny the key request for the artifact from the jid.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @param jid
	 *            The requestor's jive id.
	 * @throws ParityServerModelException
	 */
	public void denyKeyRequest(final UUID artifactUniqueId, final JID jid)
			throws ParityServerModelException {
		synchronized(implLock) { impl.denyKeyRequest(artifactUniqueId, jid); }
	}

	/**
	 * ArtifactFlag the artifact.
	 * 
	 * @param artifactId
	 *            The artifact to flag.
	 * @param flag
	 *            The flag to apply.
	 * @throws ParityServerModelException
	 */
	public void flag(final Artifact artifact,
			final ParityObjectFlag artifactFlag)
			throws ParityServerModelException {
		synchronized(implLock) { impl.flag(artifact, artifactFlag); }
	}

	/**
	 * Obtain a handle to an artifact for a given artifact unique id.
	 * 
	 * @param artifactUniqueId
	 *            An artifact unique id.
	 * @throws ParityServerModelException
	 */
	public Artifact get(final UUID artifactUniqueId) {
		synchronized(implLock) { return impl.get(artifactUniqueId); }
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

	public List<Contact> readContacts(final UUID artifactUniqueId)
			throws ParityServerModelException {
		synchronized(implLock) { return impl.readContacts(artifactUniqueId); }
	}

	/**
	 * Request the key from the artifact's key holder. If the key holder is
	 * currently online; the request will be routed to them; otherwise it will
	 * be queued until the user comes online.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 * @throws ParityServerModelException
	 */
	public void requestKey(final UUID artifactUniqueId)
			throws ParityServerModelException {
		synchronized(implLock) { impl.requestKey(artifactUniqueId); }
	}
}
