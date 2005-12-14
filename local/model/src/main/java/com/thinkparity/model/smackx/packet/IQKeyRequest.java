/*
 * Dec 7, 2005
 */
package com.thinkparity.model.smackx.packet;

import java.util.UUID;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQKeyRequest extends IQArtifact {

	/**
	 * Create a IQKeyRequest.
	 * 
	 * @param artifactUUID
	 *            The parity artifact unique id.
	 */
	public IQKeyRequest(final UUID artifactUUID) {
		super(Action.REQUESTKEY, artifactUUID);
	}
}
