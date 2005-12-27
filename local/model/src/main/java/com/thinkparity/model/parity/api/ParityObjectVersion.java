/*
 * Apr 16, 2005
 */
package com.thinkparity.model.parity.api;

import java.util.UUID;

/**
 * ParityObjectVersion
 * 
 * @author raykroeker@gmail.com
 * @version 1.2
 */
public abstract class ParityObjectVersion {

	/**
	 * The artifact unique id.
	 */
	private UUID artifactId;

	/**
	 * The artifact type.
	 */
	private ParityObjectType artifactType;

	/**
	 * Contains the version id of the parity version object.
	 */
	private String versionId;

	/**
	 * Create a ParityObjectVersion
	 * 
	 * @param artifactId
	 *            The artifact unique id.
	 * @param artifactType
	 *            The artifact type.
	 * @param versionId
	 *            The version id.
	 */
	protected ParityObjectVersion(final UUID artifactId,
			final ParityObjectType artifactType, final String versionId) {
		super();
		this.artifactId = artifactId;
		this.artifactType = artifactType;
		this.versionId = versionId;
	}

	/**
	 * Obtain the artifact unique id.
	 * 
	 * @return The artifact unique id.
	 */
	public UUID getArtifactId() { return artifactId; }

	/**
	 * Obtain the artifact type.
	 * 
	 * @return The artifactType.
	 */
	public ParityObjectType getArtifactType() { return artifactType; }

	/**
	 * Obtain the version id.
	 * 
	 * @return The version id.
	 */
	public String getVersionId() { return versionId; }
}
