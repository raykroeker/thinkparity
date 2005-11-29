/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.artifact;

import com.thinkparity.server.model.user.User;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactSubscription {

	private final Artifact artifact;

	private final User user;

	/**
	 * Create an ArtifactSubscription.
	 */
	public ArtifactSubscription() { this(null, null); }

	/**
	 * Create an ArtifactSubscription.
	 */
	public ArtifactSubscription(final Artifact artifact, final User user) {
		super();
		this.artifact = artifact;
		this.user = user;
	}

	/**
	 * Obtain the artifact.
	 * 
	 * @return The artifact.
	 */
	public Artifact getArtifact() { return artifact; }

	/**
	 * Obtain the user.
	 * 
	 * @return The user.
	 */
	public User getUser() { return user; }
}
