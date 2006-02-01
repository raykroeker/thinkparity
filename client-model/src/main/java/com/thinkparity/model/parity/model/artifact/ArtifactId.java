/*
 * Jan 31, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import java.io.Serializable;
import java.util.UUID;

/**
 * An artifact id is guaranteed unique across the parity system. This class is a
 * simple wrapper around a UUID.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ArtifactId implements Serializable, Comparable<ArtifactId> {

	/**
	 * @see java.io.Serializable
	 * 
	 */
	private static final long serialVersionUID = 1;

	/**
	 * The underlying unique identifier.
	 * 
	 */
	private final UUID uniqueId;

	/**
	 * Create a ArtifactId.
	 */
	public ArtifactId(final UUID uniqueId) {
		super();
		this.uniqueId = uniqueId;
	}

	/**
	 * @see java.lang.Comparable#compareTo(T)
	 * 
	 */
	public int compareTo(final ArtifactId o) {
		return uniqueId.compareTo(o.uniqueId);
	}
}
