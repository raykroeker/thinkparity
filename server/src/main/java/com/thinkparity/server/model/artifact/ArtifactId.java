/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.artifact;

import java.util.UUID;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactId {

	private final UUID id;

	/**
	 * Create a ArtifactId.
	 */
	public ArtifactId(final UUID id) {
		super();
		this.id = id;
	}

	/**
	 * Obtain the id.
	 * 
	 * @return The id.
	 */
	public UUID getId() { return id; }


}
