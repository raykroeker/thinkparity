/*
 * Nov 25, 2005
 */
package com.thinkparity.messenger.plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.messenger.container.BasicModule;
import org.jivesoftware.util.Log;

/**
 * Provides the database access for the global data storage plugin. Allows the
 * caller to get\set key\value pairs from the global data plugin table.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class GlobalDataStorage extends BasicModule {

	/**
	 * Insert sql.
	 */
	private static final String SQL_INSERT =
		"insert into pluginGlobalData (value,key) values (?,?)";

	/**
	 * Select sql.
	 */
	private static final String SQL_SELECT =
		"select value from pluginGlobalData where key = ?";

	/**
	 * Update sql.
	 */
	private static final String SQL_UPDATE =
		"update pluginGlobalData set value=? where key = ?";

	/**
	 * Create a GlobalDataStorage.
	 */
	public GlobalDataStorage() { super(GlobalDataPluginConstants.GDS_MODULE_NAME); }

	/**
	 * Obtain the value for a given key.
	 * 
	 * @param key
	 *            The key.
	 * @return The value; or null if no such key has a value.
	 * @throws SQLException
	 */
	public String get(final String key) throws SQLException {
		Connection cx = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			cx = getCx();
			ps = cx.prepareStatement(SQL_SELECT);
			ps.setString(1, key);
			rs = ps.executeQuery();
			if(rs.next()) { return rs.getString(1); }
			else { return null; }
		}
		finally { close(cx, ps, rs); }
	}

	/**
	 * Set the value of the key.
	 * 
	 * @param key
	 *            The key.
	 * @param value
	 *            The value.
	 * @return The previous value; or null if no previous value existed.
	 * @throws SQLException
	 */
	public String set(final String key, final String value) throws SQLException {
		final String previousValue = get(key);
		final String sql;
		if(null == previousValue) { sql = SQL_INSERT; }
		else { sql = SQL_UPDATE; }

		Connection cx = null;
		PreparedStatement ps = null;
		try {
			cx = getCx();
			ps = cx.prepareStatement(sql);
			ps.setString(1, value);
			ps.setString(2, key);
			ps.executeUpdate();

			return previousValue;
		}
		finally { close(cx, ps, null); }
	}

	/**
	 * Close the sql connection, prepared statement and result set in the
	 * correct order.
	 * 
	 * @param cx
	 *            Sql connection.
	 * @param ps
	 *            Sql prepared statement.
	 * @param rs
	 *            Sql result set.
	 */
	private void close(final Connection cx, final PreparedStatement ps,
			final ResultSet rs) {
		try { if(null != rs) { rs.close(); } }
		catch(SQLException sqlx) { Log.error(sqlx); }
		try { if(null != ps) { ps.close(); } }
		catch(SQLException sqlx) { Log.error(sqlx); }
		try { if(null != cx) { cx.close(); } }
		catch(SQLException sqlx) { Log.error(sqlx); }
	}

	/**
	 * Create an instance of an sql connection.
	 * 
	 * @return An sql connection.
	 * @throws SQLException
	 */
	private Connection getCx() throws SQLException {
		return DbConnectionManager.getConnection();
	}
}
