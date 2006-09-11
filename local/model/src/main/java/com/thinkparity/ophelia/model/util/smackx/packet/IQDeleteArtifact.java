/*
 * Feb 20, 2006
 */
package com.thinkparity.ophelia.model.util.smackx.packet;

import java.util.UUID;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQDeleteArtifact extends IQArtifact {

	/**
	 * Create a IQDeleteArtifact.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 */
	public IQDeleteArtifact(final UUID artifactUniqueId) {
		super(Action.DELETEARTIFACT, artifactUniqueId);
	}
}
