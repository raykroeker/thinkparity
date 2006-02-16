/*
 * Feb 16, 2006
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQGetSubscription extends IQArtifact {

	/**
	 * Create a IQGetSubscription.
	 * 
	 * @param artifactUniqueId
	 *            The artifact unique id.
	 */
	public IQGetSubscription(final UUID artifactUniqueId) {
		super(Action.GETSUBSCRIPTION, artifactUniqueId);
	}
}
