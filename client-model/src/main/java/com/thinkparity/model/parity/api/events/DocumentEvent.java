/*
 * Jun 5, 2005
 */
package com.thinkparity.model.parity.api.events;

/**
 * DocumentEvent
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class DocumentEvent {

	private Object source;

	public DocumentEvent(final Object source) {
		this.source = source;
	}

	/**
	 * Obtain source.
	 * @return <code>Object</code>
	 */
	public Object getSource() { return source; }
}
