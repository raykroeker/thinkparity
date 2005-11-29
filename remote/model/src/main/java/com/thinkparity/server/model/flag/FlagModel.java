/*
 * Nov 28, 2005
 */
package com.thinkparity.server.model.flag;

import java.util.UUID;

import com.thinkparity.server.model.AbstractModel;
import com.thinkparity.server.model.ParityServerModelException;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class FlagModel extends AbstractModel {

	/**
	 * Obtain a handle to the flag model.
	 * 
	 * @return A handle to the flag model.
	 */
	public static FlagModel getModel() {
		final FlagModel flagModel = new FlagModel();
		return flagModel;
	}

	/**
	 * Implementation.
	 */
	private final FlagModelImpl impl;

	/**
	 * Synchronization lock for the implemenation.
	 */
	private final Object implLock;

	/**
	 * Create a FlagModel.
	 */
	private FlagModel() {
		super();
		this.impl = new FlagModelImpl();
		this.implLock = new Object();
	}

	/**
	 * Flag the artifact.
	 * 
	 * @param artifactId
	 *            The artifact to flag.
	 * @param flag
	 *            The flag to apply.
	 * @throws ParityServerModelException
	 */
	public void flag(final UUID artifactId, final Flag flag)
			throws ParityServerModelException {
		synchronized(implLock) { impl.flag(artifactId, flag); }
	}
}
