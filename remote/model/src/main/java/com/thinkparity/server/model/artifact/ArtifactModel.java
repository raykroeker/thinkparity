/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.artifact;

import org.jivesoftware.messenger.XMPPServer;

import com.thinkparity.server.model.AbstractModel;
import com.thinkparity.server.model.ParityServerModelException;

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
	 * ArtifactFlag the artifact.
	 * 
	 * @param artifactId
	 *            The artifact to flag.
	 * @param flag
	 *            The flag to apply.
	 * @throws ParityServerModelException
	 */
	public void flag(final Artifact artifact, final ArtifactFlag flag)
			throws ParityServerModelException {
		synchronized(implLock) { impl.flag(artifact, flag); }
	}
}
