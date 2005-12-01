/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.artifact;

import java.util.Calendar;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactSubscription {

	private final Integer artifactId;

	private final Integer artifactSubscriptionId;

	private final Calendar createdOn;

	private final Calendar updatedOn;

	private final String username;

	/**
	 * Create an ArtifactSubscription.
	 */
	public ArtifactSubscription(final Integer artifactId,
			final Integer artifactSubscriptionId, final Calendar createdOn,
			final Calendar updatedOn, final String username) {
		super();
		this.artifactId = artifactId;
		this.artifactSubscriptionId = artifactSubscriptionId;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
		this.username = username;
	}

	/**
	 * Obtain the artifact id.
	 * 
	 * @return The artifact id.
	 */
	public Integer getArtifactId() { return artifactId; }

	/**
	 * Obtain the artifact subscription id.
	 * 
	 * @return The artifact subscription id.
	 */
	public Integer getArtifactSubscriptionId() { return artifactSubscriptionId; }

	/**
	 * Obtain the creation date.
	 * 
	 * @return The created on date.
	 */
	public Calendar getCreatedOn() { return createdOn; }

	/**
	 * Obtain the last update date.
	 * 
	 * @return The updated on date.
	 */
	public Calendar getUpdatedOn() { return updatedOn; }

	/**
	 * Obtain the username.
	 * 
	 * @return The username.
	 */
	public String getUsername() { return username; }
}
