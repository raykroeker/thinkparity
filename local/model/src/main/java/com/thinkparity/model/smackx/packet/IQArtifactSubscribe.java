/*
 * Nov 30, 2005
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

/**
 * A jabber iq extension for parity. This packet is used to subsribe to an
 * artifact once it has been created\received.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 * @see IQArtifact
 * @see IQArtifactUnsubscribe
 */
public class IQArtifactSubscribe extends IQArtifact {

	/**
	 * Create an IQArtifactSubscribe.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 */
	public IQArtifactSubscribe(final UUID artifactUUID) {
		super(Action.SUBSCRIBE, artifactUUID);
	}
}
