/*
 * Nov 29, 2005
 */
package com.thinkparity.server.model.io.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;

import org.apache.log4j.Logger;

import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.database.SequenceManager;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.ParityServerConstants.Logging;
import com.thinkparity.server.model.artifact.Artifact;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractSql {

	/** An apache logger. */
	protected Logger logger;

	/** Create AbstractSql. */
	protected AbstractSql() {
        super();
        this.logger = Logger.getLogger(getClass());
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

    protected Connection getCx() throws SQLException {
		return DbConnectionManager.getConnection();
	}

    /** Log an api id. */
    protected final void logApiId() {
        if(logger.isInfoEnabled()) {
            logger.info(MessageFormat.format("[{0}] [{1}] [{2}]",
                    Logging.SQL_LOG_ID,
                    StackUtil.getCallerClassName().toUpperCase(),
                    StackUtil.getCallerMethodName().toUpperCase()));
        }
    }

	/**
     * Log an sql statement.
     * 
     * @param statement
     *            An sql statement.
     */
    protected final void logStatement(final String statement) {
        if(logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format("[{0}] [{1}] [{2}] [STATEMENT] [{3}]",
                    Logging.SQL_LOG_ID,
                    StackUtil.getCallerClassName().toUpperCase(),
                    StackUtil.getCallerMethodName().toUpperCase(),
                    statement));
        }
    }

	/**
     * Log an sql statement.
     * 
     * @param statement
     *            An sql statement.
     */
    protected final void logStatementParameter(final Integer index,
            final Object value) {
        if(logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format("[{0}] [STATEMENT PARAMETERS] [{1}:{2}]",
                    Logging.SQL_LOG_ID,
                    index, value));
        }
    }

	protected Integer nextId(final AbstractSql abstractSql) {
		final Long nextId = SequenceManager.nextID(abstractSql);
		return nextId.intValue();
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
		logStatement(sql);
		return cx.prepareStatement(sql);
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
        logStatementParameter(index, state.getId());
		ps.setInt(index, state.getId());
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
        logStatementParameter(index, integer);
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
        logStatementParameter(index, jabberId.getUsername());
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
        logStatementParameter(index, string);
		ps.setString(index, string);
	}
}
