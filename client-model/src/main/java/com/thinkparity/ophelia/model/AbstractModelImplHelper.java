/*
 * Oct 9, 2005
 */
package com.thinkparity.ophelia.model;

import org.apache.log4j.Logger;

/**
 * AbstractModelImplHelper
 * @author raykroeker@gmail.com
 * @version 1.0
 */
public abstract class AbstractModelImplHelper {

	/** An apache logger. */
	protected final Logger logger;

	/**
	 * Create an AbstractModelImplHelper
	 */
	protected AbstractModelImplHelper() {
        super();
        this.logger = Logger.getLogger(getClass());
    }
}
