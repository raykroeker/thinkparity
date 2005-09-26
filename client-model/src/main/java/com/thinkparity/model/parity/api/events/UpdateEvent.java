/*
 * Apr 20, 2005
 */
package com.thinkparity.model.parity.api.events;

import com.thinkparity.model.parity.api.ParityObject;

/**
 * UpdateEvent
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class UpdateEvent {

	/**
	 * Source of the update event.
	 */
	private ParityObject source;

	/**
	 * Create a new UpdateEvent
	 */
	public UpdateEvent(final ParityObject source) {
		super();
		this.source = source;
	}

	/**
	 * Obtain the value of source.
	 * @return <code>Object</code>
	 */
	public ParityObject getSource() { return source; }
}
