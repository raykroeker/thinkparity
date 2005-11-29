/*
 * Nov 28, 2005
 */
package com.thinkparity.server.model;

import org.apache.log4j.Logger;

import com.thinkparity.server.log4j.ServerLoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractModelImpl {

	/**
	 * Handle to a parity server logger.
	 */
	protected final Logger logger = ServerLoggerFactory.getLogger(getClass());

	/**
	 * Create a AbstractModelImpl.
	 */
	protected AbstractModelImpl() { super(); }
}
