/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.artifact;

import java.util.Calendar;
import java.util.UUID;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Artifact {

	private final Integer artifactId;

	private final String artifactKeyHolder;

	private final UUID artifactUUID;

	private final Calendar createdOn;

	private final Calendar updatedOn;

	/**
	 * Create an Artifact.
	 */
	public Artifact(final Integer artifactId, final UUID artifactUUID,
			final String artifactKeyHolder, final Calendar createdOn,
			final Calendar updatedOn) {
		super();
		this.artifactId = artifactId;
		this.artifactUUID = artifactUUID;
		this.artifactKeyHolder = artifactKeyHolder;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
	}

	/**
	 * Obtain the id.
	 * 
	 * @return The artifact id.
	 */
	public Integer getArtifactId() { return artifactId; }

	/**
	 * Obtain the artifact keyholder.
	 * 
	 * @return The artifact keyholder.
	 */
	public String getArtifactKeyHolder() { return artifactKeyHolder; }

	/**
	 * Obtain the unique id.
	 * 
	 * @return The unique id.
	 */
	public UUID getArtifactUUID() { return artifactUUID; }

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
}
