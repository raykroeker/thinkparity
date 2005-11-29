/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.artifact;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Artifact {

	private final ArtifactId id;

	/**
	 * Create an Artifact.
	 */
	public Artifact(final ArtifactId id) {
		super();
		this.id = id;
	}

	/**
	 * Obtain the id.
	 * 
	 * @return The id.
	 */
	public ArtifactId getId() { return id; }
}
