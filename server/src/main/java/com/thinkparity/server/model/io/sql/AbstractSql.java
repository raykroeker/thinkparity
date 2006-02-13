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
import org.jivesoftware.database.SequenceManager;

import com.thinkparity.server.org.apache.log4j.ServerLoggerFactory;

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

	protected void close(final Connection cx, final PreparedStatement ps)
			throws SQLException {
		close(cx, ps, null);
	}

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

	protected Integer nextId(final AbstractSql abstractSql) {
		final Long nextId = SequenceManager.nextID(abstractSql);
		return nextId.intValue();
	}

	protected void debugSql(final String sql) {
		final StringBuffer message = new StringBuffer("[SQL] ")
			.append(sql);
		logger.debug(message);
	}

	protected void debugSql(final Integer index, final Object value) {
		debugSql(new StringBuffer("[")
				.append(index)
				.append("] ")
				.append(value).toString());
	}
}
