/*
 * Nov 14, 2005
 */
package com.thinkparity.codebase.model.artifact;

import com.thinkparity.codebase.assertion.Assert;

/**
 * Represents a list of flags which can be attached to any parity artifact.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum ArtifactFlag {

	ARCHIVED(0), KEY(1), SEEN(2);

	/**
	 * Obtain an artifact type from its id.
	 * 
	 * @param id
	 *            The artifact type id.
	 * @return The artifact type.
	 */
	public static ArtifactFlag fromId(final Integer id) {
		switch(id) {
		case 0: return ARCHIVED;
        case 1: return KEY;
		case 2: return SEEN;
		default:
			throw Assert.createUnreachable("Unknown artifact flag id:  " + id);
		}
	}

	/**
	 * The artifact type id.
	 * 
	 */
	private Integer id;

	/**
	 * Create a ArtifactFlag.
	 * 
	 * @param id
	 *            The artifact flag id.
	 */
	private ArtifactFlag(final Integer id) { this.id = id; }

	/**
	 * Obtain the artifact flag id.
	 * 
	 * @return The artifact flag id.
	 */
	public Integer getId() { return id; }
}
