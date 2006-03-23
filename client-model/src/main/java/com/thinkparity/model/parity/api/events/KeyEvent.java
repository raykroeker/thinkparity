/*
 * Dec 7, 2005
 */
package com.thinkparity.model.parity.api.events;

import com.thinkparity.model.parity.model.session.SessionModel;

/**
 * The key event is used by the key listener and the session model inteface to
 * notify clients of the model library of key requests; key approval requests
 * and key denied requests.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * 
 * @see KeyListener
 * @see SessionModel#addListener(KeyListener)
 * @see SessionModel#removeListener(KeyListener)
 */
public class KeyEvent {

	/**
	 * The artifact id.
	 * 
	 */
	private final Long artifactId;

	/**
	 * Create a KeyEvent.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 */
	public KeyEvent(final Long artifactId) {
		super();
		this.artifactId = artifactId;
	}

	/**
	 * Obtain the artifact id.
	 * 
	 * @return The artifact id.
	 */
	public Long getArtifactId() { return artifactId; }
}
