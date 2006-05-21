/*
 * Feb 8, 2006
 */
package com.thinkparity.migrator.io.hsqldb;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.thinkparity.migrator.LoggerFactory;
import com.thinkparity.migrator.io.hsqldb.util.HypersonicValidator;
import com.thinkparity.migrator.io.md.MetaData;
import com.thinkparity.migrator.io.md.MetaDataType;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class HypersonicSessionManager {

	/**
	 * Contains all open sessions to the hsqldb database.
	 * 
	 */
	private static final Vector<HypersonicSession> sessions;

	private static final String SQL_GET_META_DATA =
		"select VALUE from META_DATA where META_DATA_TYPE_ID=? and KEY=?";

	static {
		sessions = new Vector<HypersonicSession>();
		HypersonicUtil.registerDriver();
		HypersonicUtil.setInitialProperties();
        HypersonicValidator.validate();
		Runtime.getRuntime().addShutdownHook(new Thread("[RMIGRATOR] [IO] [HYPERSONIC SESSION MANAGER] [SHUTDOWN]") {
			public void run() { shutdown(); }
		});
	}

	/**
	 * Obtain a named property from the database.
	 * 
	 * @param property
	 *            The property.
	 * @return The property value; or null if no such property exists.
	 * @throws HypersonicException
	 */
	public static String getMetaDataString(final MetaData metaData)
			throws HypersonicException {
		final HypersonicSession session = HypersonicSessionManager.openSession();
		try {
			session.prepareStatement(SQL_GET_META_DATA);
			session.setTypeAsInteger(1, MetaDataType.STRING);
			session.setMetaDataAsString(2, metaData);
			session.executeQuery();
			if(session.nextResult()) { return session.getString("VALUE"); }
			else { return null; }
		}
		finally { session.close(); }
	}

	/**
	 * Obtain a list of all of the parity tables in the hypersonic database.
	 * 
	 * @return A list of tables.
	 */
	public static List<Table> listTables() {
		final HypersonicSession session = openSession();
		ResultSet resultSet = null;
		try {
			final DatabaseMetaData md = session.getMetaData();
			resultSet = md.getTables(null, "PUBLIC", null, new String[] {"TABLE"});
			final List<Table> tables = new LinkedList<Table>();
			while(resultSet.next()) {
				tables.add(extractTable(resultSet));
			}
			return tables;
		}
		catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
		finally {
			try { close(resultSet); }
			finally { session.close(); }
		}
	}

	/**
	 * Open a new database session.
	 * 
	 * @return The new database session.
	 */
	public static HypersonicSession openSession() {
		final HypersonicSession session = new HypersonicSession(HypersonicUtil.createConnection());
		sessions.add(session);
		return session;
	}

    /** Shutdown the database. */
    public static void shutdown() {
        // remove abandoned sessions
        final Logger logger = LoggerFactory.getLogger(HypersonicSessionManager.class);
        synchronized(sessions) {
            logger.warn("[RMIGRATOR] [IO] [HYPERSONIC SESSION MANAGER] [ABANDONED SESSIONS] [" + sessions.size() + "]");
            for(final HypersonicSession session : sessions) {
                logger.warn(session.getId());
                session.close();
            }
        }
		HypersonicUtil.shutdown();
    }

	/**
	 * Close the session.
	 * 
	 * @param session
	 *            The session to close.
	 */
	static void close(final HypersonicSession session) { sessions.remove(session); }
	
	/**
	 * Close the result set.
	 * 
	 * @param resultSet
	 *            The result set.
	 */
	private static void close(final ResultSet resultSet) {
		if(null != resultSet) {
			try { resultSet.close(); }
			catch(final SQLException sqlx) { throw new HypersonicException(sqlx); }
		}
	}

	/**
	 * Extract table metadata information from the result set.
	 * 
	 * @param rs
	 *            The result set.
	 * @return The table information.
	 * @throws SQLException
	 */
	private static Table extractTable(final ResultSet rs) throws SQLException {
		final Table t = new Table();
		t.setCatalog(rs.getString("TABLE_CAT"));
		t.setComments(rs.getString("REMARKS"));
		t.setName(rs.getString("TABLE_NAME"));
		t.setSchema(rs.getString("TABLE_SCHEM"));
		t.setType(rs.getString("TABLE_TYPE"));
		return t;
	}

	/**
	 * Create a HypersonicSessionManager [Singleton]
	 * 
	 */
	private HypersonicSessionManager() { super(); }
}
