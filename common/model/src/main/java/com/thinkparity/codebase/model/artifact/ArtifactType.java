/*
 * Created On: Jul 16, 2005
 */
package com.thinkparity.codebase.model.artifact;

import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b>thinkParity Artifact Type<br>
 * <b>Description:</b>Provides an enumerated type for artifacts.
 * 
 * @author raymond@gmail.com
 * @version 1.1.2.4
 */
public enum ArtifactType {

	CONTAINER(1), DOCUMENT(0);

	/**
	 * Obtain an artifact type from its id.
	 * 
	 * @param id
	 *            The artifact type id.
	 * @return The artifact type.
	 */
	public static ArtifactType fromId(final Integer id) {
		switch(id) {
        case 1: return CONTAINER;
		case 0: return DOCUMENT;
		default:
            throw Assert.createUnreachable("[ARTIFACT] [UNKNOWN ARTIFACT TYPE]");
		}
	}

	/** The artifact type id. */
	private Integer id;

	/**
     * Create ArtifactType.
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
