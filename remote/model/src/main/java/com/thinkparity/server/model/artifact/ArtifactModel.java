/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.artifact;

import java.util.UUID;

import org.jivesoftware.messenger.XMPPServer;

import com.thinkparity.server.model.AbstractModel;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.user.User;

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
	public static ArtifactModel getModel() {
		final XMPPServer xmppServer = XMPPServer.getInstance();
		final ArtifactModel artifactModel = new ArtifactModel(xmppServer);
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
	private ArtifactModel(final XMPPServer xmppServer) {
		super();
		this.impl = new ArtifactModelImpl(xmppServer);
		this.implLock = new Object();
	}

	/**
	 * Create an artifact.
	 * 
	 * @param artifactUUID
	 *            The artifact id.
	 * @return The new artifact.
	 * @throws ParityServerModelException
	 */
	public Artifact create(final UUID artifactUUID)
			throws ParityServerModelException {
		synchronized(implLock) { return impl.create(artifactUUID); }
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
	 * @param artifactUUID
	 *            An artifact unique id.
	 * @throws ParityServerModelException
	 */
	public Artifact get(final UUID artifactUUID) throws ParityServerModelException {
		synchronized(implLock) { return impl.get(artifactUUID); }
	}

	/**
	 * Subscribe a user to an artifact.
	 * 
	 * @param user
	 *            The user to subscribe.
	 * @param artifact
	 *            The artifact to subscribe the user to.
	 * @throws ParityServerModelException
	 */
	public void subscribe(final User user, final Artifact artifact)
			throws ParityServerModelException {
		synchronized(implLock) { impl.subscribe(user, artifact); }
	}

	/**
	 * Unsubscribe a user from an artifact.
	 * 
	 * @param user
	 *            The user to unsubscribe.
	 * @param artifact
	 *            The artifact to unsubscribe the user from.
	 * @throws ParityServerModelException
	 */
	public void unsubscribe(final User user, final Artifact artifact)
			throws ParityServerModelException {
		synchronized(implLock) { impl.unsubscribe(user, artifact); }
	}
}
