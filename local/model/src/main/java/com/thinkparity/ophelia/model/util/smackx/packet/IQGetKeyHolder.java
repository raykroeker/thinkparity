/*
 * Feb 14, 2006
 */
package com.thinkparity.ophelia.model.util.smackx.packet;

import java.util.UUID;

/**
 * A query to the parity server for the current artifact key holder.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQGetKeyHolder extends IQArtifact {

	/**
	 * Create a IQGetKeyHolder.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 */
	public IQGetKeyHolder(final UUID artifactUniqueId) {
		super(Action.GETKEYHOLDER, artifactUniqueId);
	}
}
