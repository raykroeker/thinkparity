/*
 * Nov 30, 2005
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

/**
 * A jabber iq extension for parity. This extension is used to register an
 * artifact once it has been created. Once it is created; subsequent flag;
 * subscribe and unsubscribe extensions can be used.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQArtifactCreate extends IQArtifact {

	/**
	 * Create a IQArtifactCreate.
	 * 
	 * @param artifactUUID
	 *            The artifact unique id.
	 */
	public IQArtifactCreate(final UUID artifactUUID) {
		super(Action.CREATE, artifactUUID);
	}
}
