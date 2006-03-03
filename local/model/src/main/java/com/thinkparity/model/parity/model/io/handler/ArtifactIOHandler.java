/*
 * Mar 2, 2006
 */
package com.thinkparity.model.parity.model.io.handler;

import java.util.List;

import com.thinkparity.model.parity.model.artifact.ArtifactFlag;
import com.thinkparity.model.parity.model.io.db.hsqldb.HypersonicException;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public interface ArtifactIOHandler {

	/**
	 * Obtain a list of all flags for an artifact.
	 * 
	 * @param artifactId
	 *            The artifact id.
	 * @return A list of all flags for the artifact.
	 * @throws HypersonicException
	 */
	public List<ArtifactFlag> getFlags(final Long artifactId)
			throws HypersonicException;

	/**
	 * Set the flags for the artifact.
	 * 
	 * @param session
	 *            The database session.
	 * @param artifactId
	 *            The artifact id.
	 * @param flags
	 *            The flags.
	 * @throws HypersonicException
	 */
	public void updateFlags(final Long artifactId, final List<ArtifactFlag> flags)
			throws HypersonicException;
}
