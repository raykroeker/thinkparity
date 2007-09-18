/*
 * Created On:  18-Sep-07 11:51:21 AM
 */
package com.thinkparity.ophelia.model.io.db.hsqldb;

import java.lang.ref.SoftReference;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

import org.enhydra.jdbc.standard.StandardXAConnectionHandle;

/**
 * <b>Title:</b>thinkParity Ophelia Model Database IO Prepared Statement Cache<br>
 * <b>Description:</b>A cache of soft references to prepared statements for a
 * given connection/query.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class PreparedStatementCache {

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
    PreparedStatementCache(final Connection connection) {
        super();
        this.connection = ((StandardXAConnectionHandle) connection).con;
        init();
    }

    /**
     * Obtain a cached prepared statement. If there exists no such statement in
     * the cache; null is returned.
     * 
     * @param query
     *            A <code>String</code>.
     * @return A <code>PreparedStatement</code>.
     * @throws SQLException
     */
    PreparedStatement get(final String query) throws SQLException {
        final PreparedStatement preparedStatement = getPreparedStatement(query);
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
     * @param query
     *            A <code>String</code>.
     * @return A <code>PreparedStatement</code>.
     * @throws SQLException
     */
    PreparedStatement prepareStatement(final String query) throws SQLException {
        final PreparedStatement preparedStatement = connection.prepareStatement(query);
        putPreparedStatement(query, preparedStatement);
        return preparedStatement;
    }

    /**
     * Obtain a cached prepared statement.
     * 
     * @param query
     *            A <code>String</code>.
     * @return A <code>PreparedStatement</code>.
     */
    private PreparedStatement getPreparedStatement(final String query) {
        if (CACHE.get(connection).containsKey(query)) {
            return CACHE.get(connection).get(query).get();
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
     * @param query
     *            A <code>String</code>.
     * @return A <code>PreparedStatement</code>.
     */
    private void putPreparedStatement(final String query,
            final PreparedStatement preparedStatement) {
        CACHE.get(connection).put(query,
                new SoftReference<PreparedStatement>(preparedStatement));
    }
}
