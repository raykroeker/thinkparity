/*
 * Nov 29, 2005
 */
package com.thinkparity.desdemona.model.io.sql;

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
import com.thinkparity.codebase.log4j.Log4JHelper;

import com.thinkparity.codebase.model.artifact.ArtifactState;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicConnectionProvider;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSessionManager;


/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractSql implements HypersonicConnectionProvider {

	/** An apache logger. */
	protected static final  Logger logger;

    static {
        logger = Logger.getLogger(AbstractSql.class);
    }

    /**
     * Obtain an error id.
     * 
     * @return An error id.
     */
    protected static final Object getErrorId(final Throwable t) {
        return MessageFormat.format("[{0}] [{1}] - [{2}]",
                    StackUtil.getFrameClassName(2),
                    StackUtil.getFrameMethodName(2),
                    t.getMessage());
    }

    protected static final HypersonicException translateError(final Throwable t) {
        if (HypersonicException.class.isAssignableFrom(t.getClass())) {
            return (HypersonicException) t;
        }
        else {
            final Object errorId = getErrorId(t);
            logger.error(errorId, t);
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
    protected final void debugVariable(final String name, final Object value) {
        if(logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format("[{0}:{1}]",
                    name, Log4JHelper.render(logger, value)));
        }
    }

    protected Connection getCx() throws SQLException {
		return DbConnectionManager.getConnection();
	}

    /** Log an api id. */
    protected final void logApiId() {
        if(logger.isInfoEnabled()) {
            logger.info(MessageFormat.format("[{0}] [{1}]",
                    StackUtil.getCallerClassName(),
                    StackUtil.getCallerMethodName()));
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
            logger.debug(MessageFormat.format("[Sql:{0}]", statement));
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
            logger.debug(MessageFormat.format("[Sql variable index:{0} value:{1}]",
                    index, value));
        }
    }

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
