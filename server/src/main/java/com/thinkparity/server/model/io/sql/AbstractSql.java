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

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.LoggerFactory;
import com.thinkparity.server.model.artifact.Artifact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractSql {

	/**
	 * Handle to an apache logger.
	 */
	protected Logger logger;

	/**
	 * Create a AbstractSql.
	 */
	protected AbstractSql() {
        super();
        this.logger = LoggerFactory.getLogger(getClass());
    }

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

	protected void debugSql(final Integer index, final Object value) {
		debugSql(new StringBuffer("[")
				.append(index)
				.append("] ")
				.append(value).toString());
	}

	protected void debugSql(final String sql) {
		final StringBuffer message = new StringBuffer("[SQL] ")
			.append(sql);
		logger.debug(message);
	}

	protected Connection getCx() throws SQLException {
		return DbConnectionManager.getConnection();
	}

	protected Integer nextId(final AbstractSql abstractSql) {
		final Long nextId = SequenceManager.nextID(abstractSql);
		return nextId.intValue();
	}

	/**
	 * Set a variable in a prepared statement to an integer value.
	 * 
	 * @param ps
	 *            The prepared statement.
	 * @param index
	 *            The index of the variable in the prepared statement.
	 * @param integer
	 *            The integer value.
	 * @throws SQLException
	 */
	protected void set(final PreparedStatement ps, final Integer index,
			final Integer integer) throws SQLException {
		debugSql(index, integer);
		ps.setInt(index, integer);
	}

	/**
	 * Set a variable in a prepared statement to a jabber id value.
	 * 
	 * @param ps
	 *            The prepared statement.
	 * @param index
	 *            The index of the variable in the prepared statement.
	 * @param jabberId
	 *            The jabber id.
	 * @throws SQLException
	 */
	protected void set(final PreparedStatement ps, final Integer index,
			final JabberId jabberId) throws SQLException {
		debugSql(index, jabberId.getUsername());
		ps.setString(index, jabberId.getUsername());
	}

	/**
	 * Set a variable in a prepared statement to a string value.
	 * 
	 * @param ps
	 *            The prepared statement.
	 * @param index
	 *            The index of the variable in the prepared statement.
	 * @param string
	 *            The string value.
	 * @throws SQLException
	 */
	protected void set(final PreparedStatement ps, final Integer index,
			final String string) throws SQLException {
		debugSql(index, string);
		ps.setString(index, string);
	}

	/**
	 * Set a variable in a prepared statement to an artifact state value.
	 * 
	 * @param ps
	 *            The prepared statement.
	 * @param index
	 *            The index of the variable in the prepared statement.
	 * @param state
	 *            The artifact state.
	 * @throws SQLException
	 */
	protected void set(final PreparedStatement ps, final Integer index,
			final Artifact.State state) throws SQLException {
		debugSql(index, state.getId());
		ps.setInt(index, state.getId());
	}

	/**
	 * Prepare a statement for execution for a connection.
	 * 
	 * @param cx
	 *            The connection.
	 * @param sql
	 *            The sql.
	 * @return The prepared statement.
	 */
	protected PreparedStatement prepare(final Connection cx, final String sql)
			throws SQLException {
		debugSql(sql);
		return cx.prepareStatement(sql);
	}
}
