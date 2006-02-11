/*
 * Jul 16, 2005
 */
package com.thinkparity.model.parity.api.events;

import com.thinkparity.model.parity.model.artifact.ArtifactVersion;

/**
 * VersionCreationEvent
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class VersionCreationEvent {

	/**
	 * Source parity object version.
	 */
	private ArtifactVersion source;

	/**
	 * Create a VersionCreationEvent
	 */
	public VersionCreationEvent(final ArtifactVersion source) {
		super();
		this.source = source;
	}

	/**
	 * Obtain source.
	 * @return <code>ArtifactVersion</code>
	 */
	public ArtifactVersion getSource() { return source; }	
}
