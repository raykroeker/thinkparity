/*
 * Nov 29, 2005
 */
package com.thinkparity.desdemona.model.io.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.artifact.ArtifactState;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicConnectionProvider;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSessionManager;

import org.jivesoftware.database.DbConnectionManager;
import org.jivesoftware.database.SequenceManager;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractSql implements HypersonicConnectionProvider {

	/** An apache logger. */
	protected static final  Log4JWrapper logger;

    static {
        logger = new Log4JWrapper("DESDEMONA_SQL_DEBUGGER");
    }

    protected static final HypersonicException translateError(
            final HypersonicSession session, final Throwable t) {
        session.rollback();
        return translateError(t);
    }

    protected static final HypersonicException translateError(final Throwable t) {
        if (HypersonicException.class.isAssignableFrom(t.getClass())) {
            return (HypersonicException) t;
        }
        else {
            final String errorId = new ErrorHelper().getErrorId(t);
            logger.logError(t, errorId);
            return new HypersonicException(t);
        }
    }

    /** Create AbstractSql. */
	protected AbstractSql() {
        super();
    }

	/**
     * @see com.thinkparity.desdemona.model.io.hsqldb.HypersonicConnectionProvider#getConnection()
     */
    public Connection getConnection() {
        try {
            return getCx();
        } catch (final Throwable t) {
            throw translateError(t);
        }
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

    /**
     * Debug a variable.
     * 
     * @param name
     *            A variable name.
     * @param value
     *            A variable value.
     */
    protected final <V> V debugVariable(final String name, final V value) {
        return logVariable(name, value);
    }

    protected Connection getCx() throws SQLException {
		return DbConnectionManager.getConnection();
	}

    /** Log an api id. */
    protected final void logApiId() {
        logger.logApiId();
    }

    /**
     * Log an sql statement.
     * 
     * @param statement
     *            An sql statement.
     */
    protected final void logStatement(final String statement) {
        logger.logVariable("sql", statement);
    }

	/**
     * Log an sql statement.
     * 
     * @param statement
     *            An sql statement.
     */
    protected final <V> V logStatementParameter(final Integer index,
            final V value) {
        logger.logDebug("sql:{0}={1}", index, value);
        return value;
    }

	protected final <V> V logVariable(final String name, final V value)  {
        return logger.logVariable(name, value);    }

	protected Integer nextId(final AbstractSql abstractSql) {
		final Long nextId = SequenceManager.nextID(abstractSql);
		return nextId.intValue();
	}

	/**
     * Open a hypersonic session.
     * 
     * @return A <code>HypersonicSession</code>.
     */
    protected HypersonicSession openSession() {
        return HypersonicSessionManager.openSession(this, StackUtil.getCaller());
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
            final ArtifactState state) throws SQLException {
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
            final Long longInteger) throws SQLException {
        logStatementParameter(index, longInteger);
        ps.setLong(index, longInteger);
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
