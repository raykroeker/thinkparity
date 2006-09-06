/*
 * Created On: Feb 10, 2006
 * $Id$
 */
package com.thinkparity.model.io.hsqldb;

import org.apache.log4j.Logger;

/**
 * @author raykroeker@gmail.com
 * @version $Revision$
 */
public class HypersonicUtil {

	private static final HypersonicUtil singleton;

	private static final Object singletonLock;

	static {
		singleton = new HypersonicUtil();
		singletonLock = new Object();
	}

	/**
	 * Issue a checkpoint command to the hypersonic database.
	 * 
	 * @throws HypersonicException
	 */
	static void checkpoint(final HypersonicSession session) throws HypersonicException {
		synchronized (singletonLock) {
            singleton.doCheckpoint(session);
		}
	}

	/**
	 * Register the jdbc driver.
	 * 
	 * @throws HypersonicException
	 */
	static void registerDriver() {
		synchronized(singletonLock) { singleton.doRegisterDriver(); }
	}

	/**
	 * Set the initial database properties.
	 * 
	 * @throws HypersonicException
	 */
	static void setInitialProperties(final HypersonicSession session) {
		synchronized (singletonLock) {
            singleton.doSetInitialProperties(session);
		}
	}

	/**
	 * Flag indicating whether or not the driver has already been registered.
	 * 
	 */
	private Boolean isDriverRegistered;

	/**
	 * Flag indicating whether or not the initial properties have been set.
	 * 
	 */
	private Boolean isInitalPropertiesSet;

	/**
	 * An apache logger.
	 * 
	 */
	private final Logger logger;

	/**
	 * Create a HypersonicUtil [Singleton]
	 * 
	 */
	private HypersonicUtil() { 
		super();
		this.isDriverRegistered = Boolean.FALSE;
		this.isInitalPropertiesSet = Boolean.FALSE;
		this.logger = Logger.getLogger(getClass());
	}

	/**
	 * Issue a checkpoint command to the hypersonic database.
	 * 
	 * @throws HypersonicException
	 */
	private void doCheckpoint(final HypersonicSession session) throws HypersonicException {
		logger.info("Issuing checkpoint.");
		try {
            session.execute("CHECKPOINT DEFRAG");
            session.commit();
		} finally {
            session.close(); 
        }
	}

	/**
	 * Register the jdbc driver.
	 * 
	 * @throws HypersonicException
	 */
	private void doRegisterDriver() throws HypersonicException {
		logger.info("Registering the hypersonic driver.");
		if(!isDriverRegistered) {
			try {
				Class.forName("org.hsqldb.jdbcDriver");
				isDriverRegistered = Boolean.TRUE;
			}
			catch(final ClassNotFoundException cnfx) {
				throw new HypersonicException(cnfx);
			}
		}
		else { logger.warn("Hypersonic driver already registered."); }
	}

	/**
	 * Set the initial database properties.
	 * 
	 * @throws HypersonicException
	 */
	private void doSetInitialProperties(final HypersonicSession session) throws HypersonicException {
		logger.info("Setting initial hypersonic properties.");
		if(!isInitalPropertiesSet) {
			// set custom hypersonic settings
			try {
				final String[] sql = {
						"SET PROPERTY \"sql.tx_no_multi_rewrite\" TRUE"
				};
				session.execute(sql);
                session.commit();
			} finally {
                session.close();
			}
			isInitalPropertiesSet = Boolean.TRUE;
		} else {
			logger.warn("Initial hypersonic properties have already been set.");
		}
	}
}
