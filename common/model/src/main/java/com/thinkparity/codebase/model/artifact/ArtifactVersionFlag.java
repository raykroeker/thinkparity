/*
 * Nov 14, 2005
 */
package com.thinkparity.codebase.model.artifact;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b>thinkParity Artifact Version Flag<br>
 * <b>Description:</b>The artifact version flag is an enumerated flag that can be
 * applied to an artifact version. Each flag can only be applied to an artifact
 * a single time. Each flag must be numerically represented by an id that is a
 * power of two.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.5
 */
public enum ArtifactVersionFlag {

	SEEN(1);

    /**
	 * Obtain an artifact version flag from its id.
	 * 
	 * @param id
	 *            The artifact version flag id.
	 * @return The artifact version flag.
	 */
	public static ArtifactVersionFlag fromId(final Integer id) {
		switch (id) {
		case 1:
            return SEEN;
		default:
			throw Assert.createUnreachable("Unknown artifact version flag id:  " + id);
		}
	}

    /**
     * Extract the flags from an id sum.
     * 
     * @param idSum
     *            An integer representing the sum of the ids of artifact version
     *            flags.
     * @return A <code>List</code> of <code>ArtifactVersionFlag</code>s.
     */
    public static List<ArtifactVersionFlag> fromIdSum(final Integer idSum) {
        final List<ArtifactVersionFlag> values = new ArrayList<ArtifactVersionFlag>(values().length);
        for (final ArtifactVersionFlag value : values())
            if (value.id == (idSum & value.id))
                values.add(value);
        return values;
    }

	/**
     * Extract the flags from an id sum.
     * 
     * @param idSum
     *            An integer representing the sum of the ids of artifact version
     *            flags.
     * @return A <code>List</code> of <code>ArtifactVersionFlag</code>s.
     */
    public static Integer toIdSum(final List<ArtifactVersionFlag> flags) {
        int idSum = 0;
        for (final ArtifactVersionFlag flag : flags)
            idSum = idSum | flag.id;
        return Integer.valueOf(idSum);
    }

	/**
	 * The artifact type id.
	 * 
	 */
	private Integer id;

	/**
	 * Create a ArtifactVersionFlag.
	 * 
	 * @param id
	 *            The artifact version flag id.
	 */
	private ArtifactVersionFlag(final Integer id) {
        this.id = id;
	}

	/**
	 * Obtain the artifact version flag id.
	 * 
	 * @return The artifact version flag id.
	 */
	public Integer getId() {
        return id;
	}
}
