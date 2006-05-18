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

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HypersonicUtil {

	private static final Properties connectionInfo;

	private static final String connectionURL;

	private static final HypersonicUtil singleton;

	private static final Object singletonLock;

	static {
		singleton = new HypersonicUtil();
		singletonLock = new Object();

        connectionURL = new StringBuffer("jdbc:hsqldb:file:")
            .append(System.getProperty("hsqldb.file"))
            .toString();
		connectionInfo = new Properties();
		connectionInfo.setProperty("user", "sa");
		connectionInfo.setProperty("password", "");
		connectionInfo.setProperty("hsqldb.default_table_type", "cached");
	}

	/**
	 * Issue a checkpoint command to the hypersonic database.
	 * 
	 * @throws HypersonicException
	 */
	static void checkpoint() throws HypersonicException {
		synchronized(singletonLock) { singleton.doCheckpoint(); }
	}

	/**
	 * Issue a compact command to the hypersonic database.
	 *
	 * @throws HypersonicException
	 */
	static void compact() throws HypersonicException {
		synchronized(singletonLock) { singleton.doCompact(); }
	}

	/**
	 * Create a jdbc connection to the database.
	 * 
	 * @return A jdbc connection to the database.
	 */
	static Connection createConnection() throws HypersonicException {
		return singleton.doCreateConnection();
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
	static void setInitialProperties() throws HypersonicException {
		synchronized(singletonLock) { singleton.doSetInitialProperties(); }
	}

	/**
	 * Issue a shutdown command to the hypersonic database.
	 * 
	 * @throws HypersonicException
	 */
	static void shutdown() throws HypersonicException {
		synchronized(singletonLock) { singleton.doCompact(); }
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
		try {
			final Connection connection =
				DriverManager.getConnection(connectionURL, connectionInfo);
			connection.setAutoCommit(false);
			return connection;
		}
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
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
				Class.forName("org.hsqldb.jdbcDriver");
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
			final HypersonicSession session = HypersonicSessionManager.openSession();
			try {
				final String[] sql = {
						"SET PROPERTY \"sql.tx_no_multi_rewrite\" TRUE"
				};
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
