/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.artifact;

import java.util.UUID;

import org.xmpp.packet.JID;

import com.thinkparity.server.model.AbstractModel;
import com.thinkparity.server.model.ParityServerModelException;
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
	 * Create an artifact.
	 * 
	 * @param artifactUniqueId
	 *            The artifact id.
	 * @return The new artifact.
	 * @throws ParityServerModelException
	 */
	public Artifact create(final UUID artifactUniqueId)
			throws ParityServerModelException {
		synchronized(implLock) { return impl.create(artifactUniqueId); }
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
	public Artifact get(final UUID artifactUniqueId) throws ParityServerModelException {
		synchronized(implLock) { return impl.get(artifactUniqueId); }
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

	/**
	 * SubscribeUser a user to an artifact.
	 * 
	 * @param artifact
	 *            The artifact to subscribe the user to.
	 * @throws ParityServerModelException
	 */
	public void subscribe(final Artifact artifact)
			throws ParityServerModelException {
		synchronized(implLock) { impl.subscribe(artifact); }
	}

	/**
	 * Unsubscribe a user from an artifact.
	 * 
	 * @param artifact
	 *            The artifact to unsubscribe the user from.
	 * @throws ParityServerModelException
	 */
	public void unsubscribe(final Artifact artifact)
			throws ParityServerModelException {
		synchronized(implLock) { impl.unsubscribe(artifact); }
	}
}
