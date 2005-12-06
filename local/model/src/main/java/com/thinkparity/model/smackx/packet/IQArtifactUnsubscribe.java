/*
 * Nov 30, 2005
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

/**
 * A jabber iq extension for parity. This packet is used to unsubsribe from an
 * artifact once it has been deleted.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQArtifactUnsubscribe extends IQArtifact {

	/**
	 * Create a IQArtifactUnsubscribe.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 */
	public IQArtifactUnsubscribe(final UUID artifactUUID) {
		super(Action.UNSUBSCRIBEUSER, artifactUUID);
	}
}
