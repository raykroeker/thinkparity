/*
 * Nov 28, 2005
 */
package com.thinkparity.server.model.flag;

import java.util.UUID;

import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityServerModelException;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class FlagModelImpl extends AbstractModelImpl {

	/**
	 * Create a FlagModelImpl.
	 */
	FlagModelImpl() { super(); }

	/**
	 * Flag the artifact.
	 * 
	 * @param artifactId
	 *            The artifact to flag.
	 * @param flag
	 *            The flag to apply.
	 * @throws ParityServerModelException
	 */
	void flag(final UUID artifactId, final Flag flag)
			throws ParityServerModelException {
//		logger.info("flag(UUID,Flag)");
//		logger.debug(artifactId);
//		logger.debug(flag);
		
	}
}
