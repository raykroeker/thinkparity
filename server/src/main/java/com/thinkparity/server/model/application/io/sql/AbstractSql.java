/*
 * Nov 29, 2005
 */
package com.thinkparity.desdemona.model.io.sql;

import java.sql.Connection;
import java.sql.SQLException;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicConnectionProvider;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSessionManager;

import org.jivesoftware.database.DbConnectionManager;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.7
 */
public abstract class AbstractSql {

	/** An apache logger. */
	private static final  Log4JWrapper LOGGER;

    /** A database connection provider. */
    private static final HypersonicConnectionProvider CONNECTION_PROVIDER;

    static {
        CONNECTION_PROVIDER = new HypersonicConnectionProvider() {
            public Connection getConnection() {
                try {
                    return DbConnectionManager.getConnection();
                } catch (final SQLException sqlx) {
                    throw new HypersonicException("Could not establish a connection.");
                }
            }
        };
        LOGGER = new Log4JWrapper("DESDEMONA_SQL_DEBUGGER");
    }

    /**
     * Translate an error into an sql error. The session is also rolled back.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param t
     *            An error <code>Throwable</code>.
     * @return A <code>HypersonicException</code>.
     */
    protected static final HypersonicException translateError(
            final HypersonicSession session, final Throwable t) {
        session.rollback();
        if (HypersonicException.class.isAssignableFrom(t.getClass())) {
            return (HypersonicException) t;
        }
        else {
            final String errorId = new ErrorHelper().getErrorId(t);
            LOGGER.logError(t, errorId);
            return new HypersonicException(t);
        }
    }

	/**
     * Create AbstractSql.
     *
     */
	protected AbstractSql() {
        super();
    }

    /**
     * Open a hypersonic session.
     * 
     * @return A <code>HypersonicSession</code>.
     */
    protected HypersonicSession openSession() {
        return HypersonicSessionManager.openSession(CONNECTION_PROVIDER, StackUtil.getCaller());
    }
}
