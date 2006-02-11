/*
 * Feb 8, 2006
 */
package com.thinkparity.model.parity.model.io.db.hsqldb;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.thinkparity.model.log4j.ModelLoggerFactory;
import com.thinkparity.model.parity.model.io.db.HypersonicException;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class SessionManager {

	private static final String GET_PROPERTY_SQL =
		"select VALUE from META_DATA where KEY = ?";

	private static final String INSERT_PROPERTY_SQL =
		"insert into META_DATA (KEY,VALUE) values (?, ?)";

	private static final List<Session> sessions;

	private static final String UPDATE_PROPERTY_SQL =
		"update META_DATA set (VALUE = ?) where KEY = ?";

	static {
		sessions = new LinkedList<Session>();
		HypersonicUtil.registerDriver();
		HypersonicUtil.setInitialProperties();
		Runtime.getRuntime().addShutdownHook(new Thread("thinkParity - Shutdown Database") {
			public void run() {
				// remove abandoned sessions
				final Logger logger = ModelLoggerFactory.getLogger(SessionManager.class);
				logger.info("Number of abandoned sessions:  " + sessions.size());
				for(final Session session : sessions) {
					logger.info(session.getId());
					session.close();
				}
				HypersonicUtil.shutdown();
			}
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
	public static String getProperty(final Property property)
			throws HypersonicException {
		final Session session = SessionManager.openSession();
		try {
			session.prepareStatement(GET_PROPERTY_SQL);
			session.setString(1, property.toString().toLowerCase());
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
		final Session session = openSession();
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
	public static Session openSession() {
		final Session session = new Session(HypersonicUtil.createConnection());
		sessions.add(session);
		return session;
	}

	/**
	 * Set a named property in the database.
	 * 
	 * @param property
	 *            The property.
	 * @param value
	 *            The property value.
	 * @return The original property value.
	 * @throws HypersonicException
	 */
	public static String setProperty(final Property property, final String value)
			throws HypersonicException {
		final String originalValue = SessionManager.getProperty(property);
		final Session session = SessionManager.openSession();
		try {
			final String propertySql;
			if(null == originalValue) { propertySql = INSERT_PROPERTY_SQL; }
			else { propertySql = UPDATE_PROPERTY_SQL; }
			session.prepareStatement(propertySql);
			session.setString(1, property.toString().toLowerCase());
			session.setString(2, value);
			if(1 != session.executeUpdate())
				throw new HypersonicException("Could not set property:  " + property);

			session.commit();
		}
		catch(final HypersonicException hx) {
			session.rollback();
			throw hx;
		}
		finally { session.close(); }
		return originalValue;
	}

	/**
	 * Close the session.
	 * 
	 * @param session
	 *            The session to close.
	 */
	static void close(final Session session) { sessions.remove(session); }
	
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
	 * Create a SessionManager [Singleton]
	 * 
	 */
	private SessionManager() { super(); }

	public enum Property { VERSION_ID }
}
