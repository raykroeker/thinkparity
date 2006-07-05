/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index;

import com.thinkparity.model.parity.model.artifact.ArtifactType;

/**
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class IndexHit {

	/** The artifact id. */
	private Long id;

    /** The artifact type. */
    private ArtifactType type;

	/**
     * Create IndexHit.
     * 
     * @param id
     *            The index id.
     */
	public IndexHit() { super(); }

	/**
	 * Obtain the artifact id.
	 * 
	 * @return The id.
	 */
	public Long getId() { return id; }

    /**
     * Obtain the type
     *
     * @return The ArtifactType.
     */
    public ArtifactType getType() { return type; }

    /**
     * Set artifactId.
     *
     * @param artifactId The Long.
     */
    public void setId(final Long id) { this.id = id; }

    /**
     * Set type.
     *
     * @param type The ArtifactType.
     */
    public void setType(final ArtifactType type) { this.type = type; }
}
