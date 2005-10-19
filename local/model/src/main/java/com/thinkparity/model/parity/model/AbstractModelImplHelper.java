/*
 * Oct 9, 2005
 */
package com.thinkparity.model.parity.model;

import org.apache.log4j.Logger;

import com.thinkparity.model.log4j.ModelLoggerFactory;

/**
 * AbstractModelImplHelper
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public abstract class AbstractModelImplHelper {

	/**
	 * Handle to the apache logger.
	 */
	protected final Logger logger =
		ModelLoggerFactory.getLogger(AbstractModelImplHelper.class);

	/**
	 * Create an AbstractModelImplHelper
	 */
	protected AbstractModelImplHelper() { super(); }
}
