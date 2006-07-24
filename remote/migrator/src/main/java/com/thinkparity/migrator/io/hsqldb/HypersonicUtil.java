/*
 * Feb 10, 2006
 */
package com.thinkparity.migrator.io.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.thinkparity.migrator.LoggerFactory;
import com.thinkparity.migrator.Constants.CalpurniaPropertyNames;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HypersonicUtil {

	private static final Properties INFO;

    private static final String DRIVER;

	private static final HypersonicUtil SINGLETON;

	private static final String URL;

	static {
		SINGLETON = new HypersonicUtil();

        URL = System.getProperty(CalpurniaPropertyNames.DB_URL);

        INFO = new Properties();
        INFO.setProperty("user", System.getProperty(CalpurniaPropertyNames.DB_USERNAME));
        INFO.setProperty("password", System.getProperty(CalpurniaPropertyNames.DB_PASSWORD));
        INFO.setProperty("hsqldb.default_table_type", "cached");

        DRIVER = System.getProperty(CalpurniaPropertyNames.DB_DRIVER);
	}

	/**
	 * Issue a checkpoint command to the hypersonic database.
	 * 
	 * @throws HypersonicException
	 */
	static void checkpoint() throws HypersonicException {
		synchronized(SINGLETON) { SINGLETON.doCheckpoint(); }
	}

	/**
	 * Issue a compact command to the hypersonic database.
	 *
	 * @throws HypersonicException
	 */
	static void compact() throws HypersonicException {
		synchronized(SINGLETON) { SINGLETON.doCompact(); }
	}

	/**
	 * Create a jdbc connection to the database.
	 * 
	 * @return A jdbc connection to the database.
	 */
	static Connection createConnection() throws HypersonicException {
		return SINGLETON.doCreateConnection();
	}

	/**
	 * Register the jdbc driver.
	 * 
	 * @throws HypersonicException
	 */
	static void registerDriver() {
		synchronized(SINGLETON) { SINGLETON.doRegisterDriver(); }
	}

	/**
	 * Set the initial database properties.
	 * 
	 * @throws HypersonicException
	 */
	static void setInitialProperties() throws HypersonicException {
		synchronized(SINGLETON) { SINGLETON.doSetInitialProperties(); }
	}

	/**
	 * Issue a shutdown command to the hypersonic database.
	 * 
	 * @throws HypersonicException
	 */
	static void shutdown() throws HypersonicException {
		synchronized(SINGLETON) { SINGLETON.doCompact(); }
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
		this.logger = LoggerFactory.getLogger(getClass());
	}

	/**
	 * Issue a checkpoint command to the hypersonic database.
	 * 
	 * @throws HypersonicException
	 */
	private void doCheckpoint() throws HypersonicException {
		logger.info("[RMIGRATOR] [IO] [HYPERSONIC UTIL] [CHECKPOINT]");
		final HypersonicSession session = HypersonicSessionManager.openSession();
		try { session.execute("CHECKPOINT DEFRAG"); }
		finally { session.close(); }
	}

	/**
	 * Issue a compact command to the hypersonic database.
	 *
	 * @throws HypersonicException
	 */
	private void doCompact() throws HypersonicException {
		logger.info("[RMIGRATOR] [IO] [HYPERSONIC UTIL] [COMPACT]");
		final HypersonicSession session = HypersonicSessionManager.openSession();
		try { session.execute("SHUTDOWN COMPACT"); }
		finally { session.close(); }
	}

	/**
	 * Create a jdbc connection to the database.
	 * 
	 * @return A jdbc connection to the database.
	 */
	private Connection doCreateConnection() throws HypersonicException {
        logger.info("[RMIGRATOR] [IO] [HYPERSONIC UTIL] [CREATE CONNECTION]");
		try {
            logger.info("[RMIGRATOR] [IO] [HYPERSONIC UTIL] [CREATE CONNECTION] [B4 DRIVER MANAGER GET CONNECTION]");
			final Connection connection =
				DriverManager.getConnection(URL, INFO);
            logger.info("[RMIGRATOR] [IO] [HYPERSONIC UTIL] [CREATE CONNECTION] [AFTER DRIVER MANAGER GET CONNECTION]");
			connection.setAutoCommit(false);
			return connection;
		}
		catch(final SQLException sqlx) {
		    logger.fatal(URL);
            logger.fatal(INFO);
            logger.fatal("[RMIGRATOR] [IO] [HYPERSONIC UTIL] [CREATE CONNECTION] [COULD NOT CREATE CONNECTION]", sqlx);
            throw new HypersonicException(sqlx);
        }
	}

	/**
	 * Register the jdbc driver.
	 * 
	 * @throws HypersonicException
	 */
	private void doRegisterDriver() throws HypersonicException {
		logger.info("[RMIGRATOR] [IO] [HYPERSONIC UTIL] [REGISTER DRIVER]");
		if(!isDriverRegistered) {
			try {
				Class.forName(DRIVER);
				isDriverRegistered = Boolean.TRUE;
			}
			catch(final ClassNotFoundException cnfx) {
				throw new HypersonicException(cnfx);
			}
		}
		else {
            logger.warn("[RMIGRATOR] [IO] [HYPERSONIC UTIL] [REGISTER DRIVER] [DRIVER ALREADY REGISTERED]");
        }
	}

	/**
	 * Set the initial database properties.
	 * 
	 * @throws HypersonicException
	 */
	private void doSetInitialProperties() throws HypersonicException {
		logger.info("[RMIGRATOR] [IO] [HYPERSONIC UTIL] [SET INITIAL PROPERTIES]");
		if(!isInitalPropertiesSet) {
			// set custom hypersonic settings
            logger.info("[RMIGRATOR] [IO] [HYPERSONIC UTIL] [SET INITIAL PROPERTIES] [B4 OPEN SESSION]");
			final HypersonicSession session = HypersonicSessionManager.openSession();
            logger.info("[RMIGRATOR] [IO] [HYPERSONIC UTIL] [SET INITIAL PROPERTIES] [B4 OPEN SESSION]");
			try {
				final String[] sql = {"SET PROPERTY \"sql.tx_no_multi_rewrite\" TRUE"};
				session.execute(sql);
			}
			finally { session.close(); }
			isInitalPropertiesSet = Boolean.TRUE;
		}
		else {
			logger.warn("[RMIGRATOR] [IO] [HYPERSONIC UTIL] [SET INITIAL PROPERTIES] [INITIAL PROPERTIES ALREADY SET]");
		}
	}
}
