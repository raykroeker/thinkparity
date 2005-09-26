/*
 * Mar 3, 2005
 */
package com.thinkparity.model.parity.api.events;

import com.thinkparity.model.parity.api.ParityObject;

/**
 * CreationEvent
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CreationEvent {

	/**
	 * Cause of the creation event.
	 */
	private ParityObject source;

	/**
	 * Create a new CreationEvent
	 * @param source <code>java.lang.Object</code>
	 */
	public CreationEvent(final ParityObject source) {
		super();
		this.source = source;
	}

	/**
	 * Obtain the value of the object causing the creation event.
	 * @return <code>java.lang.Object</code>
	 */
	public ParityObject getSource() { return source; }
}
