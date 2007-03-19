/*
 * Nov 14, 2005
 */
package com.thinkparity.codebase.model.artifact;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;

/**
 * <b>Title:</b>thinkParity Artifact Flag<br>
 * <b>Description:</b>The artifact flag is an enumerated flag that can be
 * applied to an artifact. Each flag can only be applied to an artifact a single
 * time. Each flag must be numerically represented by an id that is a power of
 * two.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.5
 */
public enum ArtifactFlag {

	ARCHIVED(1), BOOKMARK(2), KEY(4), LATEST(8), SEEN(16);

    /**
     * Extract the flags from an id sum.
     * 
     * @param idSum
     *            An integer representing the sum of the ids of artifact flags.
     * @return A <code>List</code> of <code>ArtifactFlag</code>s.
     */
    public static List<ArtifactFlag> fromIdSum(final Integer idSum) {
        final List<ArtifactFlag> values = new ArrayList<ArtifactFlag>(values().length);
        for (final ArtifactFlag value : values())
            if (value.id == (idSum & value.id))
                values.add(value);
        return values;
    }

    /**
     * Extract the flags from an id sum.
     * 
     * @param idSum
     *            An integer representing the sum of the ids of artifact flags.
     * @return A <code>List</code> of <code>ArtifactFlag</code>s.
     */
    public static Integer toIdSum(final List<ArtifactFlag> flags) {
        int idSum = 0;
        for (final ArtifactFlag flag : flags)
            idSum = idSum | flag.id;
        return Integer.valueOf(idSum);
    }

	/**
	 * Obtain an artifact type from its id.
	 * 
	 * @param id
	 *            The artifact type id.
	 * @return The artifact type.
	 */
	public static ArtifactFlag fromId(final Integer id) {
		switch(id) {
		case 1:
            return ARCHIVED;
        case 2:
            return BOOKMARK;
        case 4:
            return KEY;
        case 8:
            return LATEST;
		case 16:
            return SEEN;
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
