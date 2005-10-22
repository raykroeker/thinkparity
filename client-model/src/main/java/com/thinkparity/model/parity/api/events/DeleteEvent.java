/*
 * 21-Oct-2005
 */
package com.thinkparity.model.parity.api.events;

import com.thinkparity.model.parity.api.ParityObject;

/**
 * The deletion event is fired whenever a parity object is deleted.
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DeleteEvent {

	/**
	 * Source of the deletion event.
	 */
	private final ParityObject source;

	/**
	 * Create a DeleteEvent.
	 */
	public DeleteEvent(final ParityObject source) {
		super();
		this.source = source;
	}

	/**
	 * Obtain the event source.
	 * 
	 * @return The event source.
	 */
	public ParityObject getSource() { return source; }
}
