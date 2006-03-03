/*
 * Mar 2, 2006
 */
package com.thinkparity.model.parity.api.events;

import com.thinkparity.model.parity.model.artifact.Artifact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class CloseEvent {

	private final Artifact source;

	/**
	 * Create a CloseEvent.
	 */
	public CloseEvent(final Artifact source) {
		super();
		this.source = source;
	}

	public Object getSource() { return source; }
}
