/*
 * May 13, 2005
 */
package com.thinkparity.model.parity.api.events;


public class PresenceEvent {

	private Object source;

	public PresenceEvent(final Object source) {
		super();
		this.source = source;
	}

	public Object getSource() { return source; }
}
