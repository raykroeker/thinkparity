/*
 * Feb 19, 2006
 */
package com.thinkparity.model.parity.model.artifact;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public enum ArtifactState {

	ACTIVE(0), ARCHIVED(1), CLOSED(2), DELETED(3);

	/**
	 * Find the state with the corresponding id.
	 * 
	 * @param id
	 *            The state id.
	 * @return The state.
	 */
	public static ArtifactState fromId(final Integer id) {
		switch(id) {
		case 0: return ACTIVE;
		case 1: return ARCHIVED;
		case 2: return CLOSED;
		case 3: return DELETED;
		default: throw Assert.createUnreachable("Unknown artifact state id:  " + id);
		}
	}

	/**
	 * The state id.
	 * 
	 */
	private Integer id;

	/**
	 * Create a State.
	 * 
	 * @param id
	 *            The state id.
	 */
	private ArtifactState(final Integer id) { this.id = id; }

	/**
	 * Obtain the state id.
	 * 
	 * @return The state id.
	 */
	public Integer getId() { return id; }
}