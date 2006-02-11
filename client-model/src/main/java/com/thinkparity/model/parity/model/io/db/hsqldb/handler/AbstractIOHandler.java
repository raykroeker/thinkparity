/*
 * Feb 8, 2006
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.handler;

import org.apache.log4j.Logger;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.model.io.db.hsqldb.Session;
import com.thinkparity.model.parity.model.io.db.hsqldb.SessionManager;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
abstract class AbstractIOHandler {

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

	/**
	 * Create a AbstractIOHandler.
	 * 
	 */
	protected AbstractIOHandler() {
		super();
		this.logger = ModelLoggerFactory.getLogger(getClass());
	}

	/**
	 * Open a database session.
	 * 
	 * @return The database session.
	 */
	protected Session openSession() { return SessionManager.openSession(); }
}
