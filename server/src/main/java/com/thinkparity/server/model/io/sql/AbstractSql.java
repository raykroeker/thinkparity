/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.io.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;
import org.jivesoftware.database.DbConnectionManager;

import com.thinkparity.server.log4j.ServerLoggerFactory;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractSql {

	/**
	 * Handle to an apache logger.
	 */
	protected Logger logger = ServerLoggerFactory.getLogger(getClass());

	/**
	 * Create a AbstractSql.
	 */
	protected AbstractSql() { super(); }

	protected void close(final Connection cx, final PreparedStatement ps,
			final ResultSet rs) throws SQLException {
		if(null != rs) { rs.close(); }
		if(null != ps) { ps.close(); }
		if(null != cx) {
			if(!cx.isClosed()) { DbConnectionManager.closeConnection(cx); }
		}
	}

	protected Connection getCx() throws SQLException {
		return DbConnectionManager.getConnection();
	}
}
