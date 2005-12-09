/*
 * Dec 7, 2005
 */
package com.thinkparity.model.parity.api.events;

import java.util.UUID;

import com.thinkparity.model.parity.model.session.SessionModel;
import com.thinkparity.model.xmpp.user.User;

/**
 * The key event is used by the key listener and the session model inteface to
 * notify clients of the model library of key requests; key approval requests
 * and key denied requests.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * @see KeyListener
 * @see SessionModel#addListener(KeyListener)
 * @see SessionModel#removeListener(KeyListener)
 */
public class KeyEvent {

	/**
	 * If the event fired is the keyRequested event; the user is the requesting
	 * user; otherwise it is null.
	 */
	private final User user;

	/**
	 * The artifact unique id.
	 */
	private final UUID artifactUUID;

	/**
	 * Create a KeyEvent.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 * @param user
	 *            The user.
	 */
	public KeyEvent(final UUID artifactUUID, final User user) {
		super();
		this.artifactUUID = artifactUUID;
		this.user = user;
	}

	/**
	 * Obtain the artifact unique id.
	 * 
	 * @return The artifact unique id.
	 */
	public UUID getArtifactUUID() { return artifactUUID; }

	/**
	 * Obtain the user.
	 * 
	 * @return The user.
	 */
	public User getUser() { return user; }
}
