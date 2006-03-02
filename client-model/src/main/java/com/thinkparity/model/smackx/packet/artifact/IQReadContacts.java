/*
 * Mar 1, 2006
 */
package com.thinkparity.model.smackx.packet.artifact;

import java.util.UUID;

import com.thinkparity.model.smackx.packet.IQArtifact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQReadContacts extends IQArtifact {

	/**
	 * Create a IQReadContacts.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 */
	public IQReadContacts(final UUID artifactUniqueId) {
		super(Action.ARTIFACTREADCONTACTS, artifactUniqueId);
	}
}
