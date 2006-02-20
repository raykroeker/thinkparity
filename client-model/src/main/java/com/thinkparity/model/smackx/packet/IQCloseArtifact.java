/*
 * Feb 18, 2006
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQCloseArtifact extends IQArtifact {

	/**
	 * Create a IQCloseArtifact.
	 * 
	 */
	public IQCloseArtifact(final UUID artifactUniqueId) {
		super(Action.CLOSEARTIFACT, artifactUniqueId);
	}
}
