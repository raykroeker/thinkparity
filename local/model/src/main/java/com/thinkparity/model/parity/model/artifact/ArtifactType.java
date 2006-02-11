/*
 * Jul 16, 2005
 */
package com.thinkparity.model.parity.model.artifact;

import com.thinkparity.codebase.assertion.Assert;

/**
 * ArtifactType
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum ArtifactType {

	DOCUMENT(0);

	/**
	 * Obtain an artifact type from its id.
	 * 
	 * @param id
	 *            The artifact type id.
	 * @return The artifact type.
	 */
	public static ArtifactType fromId(final Integer id) {
		switch(id) {
		case 0: return DOCUMENT;
		default:
			throw Assert.createUnreachable("Unknown artifact type id:  " + id);
		}
	}

	/**
	 * The artifact type id.
	 * 
	 */
	private Integer id;

	/**
	 * Create a ArtifactType.
	 * 
	 * @param id
	 *            The artifact type id.
	 */
	private ArtifactType(final Integer id) { this.id = id; }

	/**
	 * Obtain the artifact type id.
	 * 
	 * @return The artifact type id.
	 */
	public Integer getId() { return id; }
}
