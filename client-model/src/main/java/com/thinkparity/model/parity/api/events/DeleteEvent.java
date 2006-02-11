/*
 * 21-Oct-2005
 */
package com.thinkparity.model.parity.api.events;

import com.thinkparity.model.parity.model.artifact.Artifact;

/**
 * The deletion event is fired whenever a parity object is deleted.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DeleteEvent {

	/**
	 * Source of the deletion event.
	 */
	private final Artifact source;

	/**
	 * Create a DeleteEvent.
	 */
	public DeleteEvent(final Artifact source) {
		super();
		this.source = source;
	}

	/**
	 * Obtain the event source.
	 * 
	 * @return The event source.
	 */
	public Artifact getSource() { return source; }
}
