/*
 * Created On: Feb 19, 2006
 * $Id$
 */
package com.thinkparity.codebase.model.artifact;

import com.thinkparity.codebase.assertion.Assert;

/**
 * @author raymond@raykroeker.com
 * @version $Revision$
 */
public enum ArtifactState {

	ACTIVE(0), CLOSED(2);

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
		case 2: return CLOSED;
		default:
            throw Assert.createUnreachable(
                    "[ARTIFACT MODEL] [UNKNOWN ARTIFACT STATE]");
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