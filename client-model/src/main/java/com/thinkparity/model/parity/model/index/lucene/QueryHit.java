/*
 * Mar 7, 2006
 */
package com.thinkparity.model.parity.model.index.lucene;

import com.thinkparity.model.artifact.ArtifactType;

/**
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class QueryHit {

    /** The artifact id. */
	private Long id;

    /** The artifact type. */
    private ArtifactType type;
	
	/** Create QueryHit. */
	QueryHit() { super(); }

	/**
     * Obtain the artifact id.
     * 
     * @return An artifact id.
     */
	public Long getId() { return id; }

    /**
     * Obtain the artifact type.
     * 
     * @return An artifact type.
     */
    public ArtifactType getType() { return type; }

    /**
     * Set the id.
     * 
     * @param id
     *            An artifact id.
     */
	void setId(final Long id) { this.id = id; }

    /**
     * Set the type.
     * 
     * @param type
     *            The ArtifactType.
     */
    void setType(final ArtifactType type) { this.type = type; }
}
