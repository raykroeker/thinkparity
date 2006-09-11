/*
 * May 13, 2005
 */
package com.thinkparity.ophelia.model.events;


public class PresenceEvent {

	private Object source;

	public PresenceEvent(final Object source) {
		super();
		this.source = source;
	}

	public Object getSource() { return source; }
}
