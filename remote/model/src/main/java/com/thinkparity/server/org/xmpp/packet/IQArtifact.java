/*
 * Nov 30, 2005
 */
package com.thinkparity.server.org.xmpp.packet;

import java.util.UUID;


/**
 * An abstraction of the artifact jabber iq extensions for parity. This includes
 * the create,falg,subscribe and unsubscribe extensions.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.2
 */
public abstract class IQArtifact extends IQParity {

	/**
	 * Artifact unique id.
	 */
	private final UUID artifactUUID;

	/**
	 * Create a IQArtifact.
	 */
	protected IQArtifact(final Action action, final UUID artifactUUID) {
		super(action);
		this.artifactUUID = artifactUUID;
	}

	/**
	 * Obtain the artifact unique id.
	 * 
	 * @return The artifact unique id.
	 */
	public UUID getArtifactUUID() { return artifactUUID; }
}
