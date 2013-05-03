/*
 * Created On:  Sep 25, 2007 9:21:16 AM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.lang.ref.SoftReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

/**
 * <b>Title:</b>thinkParity Ophelia Model Workspace Statement Cache<br>
 * <b>Description:</b>A cache of soft references to prepared statements for a
 * given connection/sql query.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class StatementCache {

    /** The cache. */
    private static final Map<Connection, Map<String, SoftReference<PreparedStatement>>> CACHE;

    /** The initialization size of the cache. */
    private static final int CACHE_INIT_SIZE;

    static {
        CACHE = new Hashtable<Connection, Map<String, SoftReference<PreparedStatement>>>(7, 1.0F);
        CACHE_INIT_SIZE = 7;
    }

    /** A connection. */
    private final Connection connection;

    /**
     * Create PreparedStatementCache.
     * 
     * @param connection
     *            A <code>Connection</code>.
     */
    StatementCache(final ConnectionImpl connection) {
        super();
        this.connection = connection.getConnectionDelegate();
        init();
    }

    /**
     * Obtain a cached prepared statement. If there exists no such statement in
     * the cache; null is returned.
     * 
     * @param sql
     *            A <code>String</code>.
     * @return A <code>PreparedStatement</code>.
     * @throws SQLException
     */
    PreparedStatement get(final String sql) throws SQLException {
        final PreparedStatement preparedStatement = getPreparedStatement(sql);
        if (null == preparedStatement) {
            return preparedStatement;
        } else {
            preparedStatement.clearParameters();
            return preparedStatement;
        }
    }

    /**
     * Prepare a statement and cache it.
     * 
     * @param sql
     *            A <code>String</code>.
     * @return A <code>PreparedStatement</code>.
     * @throws SQLException
     */
    PreparedStatement prepareStatement(final String sql) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement(sql);
        putPreparedStatement(sql, preparedStatement);
        return preparedStatement;
    }

    /**
     * Obtain a cached prepared statement.
     * 
     * @param sql
     *            A <code>String</code>.
     * @return A <code>PreparedStatement</code>.
     */
    private PreparedStatement getPreparedStatement(final String sql) {
        if (CACHE.get(connection).containsKey(sql)) {
            return CACHE.get(connection).get(sql).get();
        } else {
            return null;
        }
    }

    /**
     * Initialize the cache.
     * 
     */
    private void init() {
        if (CACHE.containsKey(connection)) {
            return;
        } else {
            CACHE.put(connection,
                    new Hashtable<String, SoftReference<PreparedStatement>>(
                            CACHE_INIT_SIZE, 0.75F));
        }
    }

    /**
     * Obtain a cached prepared statement.
     * 
     * @param sql
     *            A <code>String</code>.
     * @return A <code>PreparedStatement</code>.
     */
    private void putPreparedStatement(final String sql,
            final PreparedStatement preparedStatement) {
        CACHE.get(connection).put(sql,
                new SoftReference<PreparedStatement>(preparedStatement));
    }
}
