/*
 * Nov 29, 2005
 */
package com.thinkparity.desdemona.model.io.sql;

import javax.sql.DataSource;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSessionManager;

import com.thinkparity.desdemona.service.persistence.PersistenceService;

import com.thinkparity.desdemona.util.crypto.CryptoProvider;

/**
 * <b>Title:</b>thinkParity Desdemona Model SQL Abstraction<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.8
 */
public abstract class AbstractSql {

    /** An apache logger. */
	private static final  Log4JWrapper LOGGER;

    static {
        LOGGER = new Log4JWrapper("DESDEMONA_SQL_DEBUGGER");
    }

    protected static final HypersonicException panic(final String message,
            final Object... arguments) {
        return new HypersonicException(message, arguments);
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

	/** A <code>HypersonicSessionManager</code>. */
    private final HypersonicSessionManager sessionManager;

	/**
     * Create AbstractSql.
     *
     */
	protected AbstractSql() {
        this(PersistenceService.getInstance().getDataSource2(), Boolean.TRUE);
    }

    /**
     * Create AbstractSql.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     */
    protected AbstractSql(final DataSource dataSource) {
        this(dataSource, Boolean.FALSE);
    }

    /**
     * Create AbstractSql.
     * 
     * @param dataSource
     *            A <code>DataSource</code>.
     * @param commit
     *            A commit <code>Boolean</code>.
     */
    protected AbstractSql(final DataSource dataSource, final Boolean commit) {
        super();
        this.sessionManager = new HypersonicSessionManager(dataSource, commit);
    }

    /**
     * Set the crypto provider.
     * 
     * @param cryptoProvider
     *            A <code>CryptoProvider</code>.
     */
    public void setCryptoProvider(final CryptoProvider cryptoProvider) {
        this.sessionManager.setCryptoProvider(cryptoProvider);
    }

    /**
     * Open a hypersonic session.
     * 
     * @return A <code>HypersonicSession</code>.
     */
    protected HypersonicSession openSession() {
        return sessionManager.openSession(StackUtil.getCaller());
    }
}
