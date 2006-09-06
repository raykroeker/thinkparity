/*
 * Nov 29, 2005
 */
package com.thinkparity.model.artifact;

import java.util.Calendar;
import java.util.UUID;

import com.thinkparity.codebase.assertion.Assert;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class Artifact {

	private final Integer artifactId;

	private final String artifactKeyHolder;

	private final State artifactState;

	private final UUID artifactUUID;

	private final Calendar createdOn;

	private final Calendar updatedOn;

	/**
	 * Create an Artifact.
	 */
	public Artifact(final Integer artifactId, final UUID artifactUUID,
			final String artifactKeyHolder, final State artifactState,
			final Calendar createdOn, final Calendar updatedOn) {
		super();
		this.artifactId = artifactId;
		this.artifactUUID = artifactUUID;
		this.artifactKeyHolder = artifactKeyHolder;
		this.artifactState = artifactState;
		this.createdOn = createdOn;
		this.updatedOn = updatedOn;
	}

	/**
	 * Obtain the id.
	 * 
	 * @return The artifact id.
	 */
	public Integer getArtifactId() { return artifactId; }

	/**
	 * Obtain the artifact keyholder.
	 * 
	 * @return The artifact keyholder.
	 */
	public String getArtifactKeyHolder() { return artifactKeyHolder; }

	/**
	 * Obtain the artifact state.
	 * 
	 * @return The artifact state.
	 */
	public State getArtifactState() { return artifactState; }

	/**
	 * Obtain the unique id.
	 * 
	 * @return The unique id.
	 */
	public UUID getArtifactUUID() { return artifactUUID; }

	/**
	 * Obtain the creation date.
	 * 
	 * @return The created on date.
	 */
	public Calendar getCreatedOn() { return createdOn; }

	/**
	 * Obtain the last update date.
	 * 
	 * @return The updated on date.
	 */
	public Calendar getUpdatedOn() { return updatedOn; }

	/**
	 * The artifact state.
	 * 
	 */
	public enum State {
		ACTIVE(0), ARCHIVED(1), CLOSED(2), DELETED(3);

		/**
		 * Find the state with the corresponding id.
		 * 
		 * @param id
		 *            The state id.
		 * @return The state.
		 */
		public static State fromId(final Integer id) {
			switch(id) {
			case 0: return ACTIVE;
			case 1: return ARCHIVED;
			case 2: return CLOSED;
			case 3: return DELETED;
			default: throw Assert.createUnreachable("");
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
		private State(final Integer id) { this.id = id; }

		/**
		 * Obtain the state id.
		 * 
		 * @return The state id.
		 */
		public Integer getId() { return id; }
	}
}
