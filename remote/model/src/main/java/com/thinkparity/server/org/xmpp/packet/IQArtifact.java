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
	 * Artifact actions that are possible to perform.
	 * 
	 */
	public enum Action { CLOSE, CREATE, FLAG, GETKEYHOLDER, GETKEYS, GETSUBSCRIPTION,
		REQUESTKEY, SUBSCRIBE, UNSUBSCRIBE }

	/**
	 * IQAction to perform.
	 */
	private final Action action;

	/**
	 * Artifact unique id.
	 */
	private final UUID artifactUUID;

	/**
	 * Create a IQArtifact.
	 */
	protected IQArtifact(final Action action, final UUID artifactUUID) {
		super();
		this.action = action;
		this.artifactUUID = artifactUUID;
	}

	/**
	 * Obtain the action.
	 * 
	 * @return The action.
	 */
	public Action getAction() { return action; }

	/**
	 * Obtain the artifact unique id.
	 * 
	 * @return The artifact unique id.
	 */
	public UUID getArtifactUUID() { return artifactUUID; }
}
