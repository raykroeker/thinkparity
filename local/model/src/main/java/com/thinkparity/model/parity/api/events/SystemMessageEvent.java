/*
 * Feb 25, 2006
 */
package com.thinkparity.model.parity.api.events;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SystemMessageEvent {

	/**
	 * Event source.
	 * 
	 */
	private final Object source;

	/**
	 * Create a SystemMessageEvent.
	 */
	public SystemMessageEvent(final Object source) {
		super();
		this.source = source;
	}

	/**
	 * @return Returns the source.
	 */
	public Object getSource() {
		return source;
	}
}
