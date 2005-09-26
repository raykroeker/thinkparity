/*
 * Jul 16, 2005
 */
package com.thinkparity.model.parity.api.events;

import com.thinkparity.model.parity.api.ParityObjectVersion;

/**
 * VersionCreationEvent
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class VersionCreationEvent {

	/**
	 * Source parity object version.
	 */
	private ParityObjectVersion source;

	/**
	 * Create a VersionCreationEvent
	 */
	public VersionCreationEvent(final ParityObjectVersion source) {
		super();
		this.source = source;
	}

	/**
	 * Obtain source.
	 * @return <code>ParityObjectVersion</code>
	 */
	public ParityObjectVersion getSource() { return source; }	
}
