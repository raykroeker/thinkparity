/*
 * Created On:  20-Feb-07 11:39:32 AM
 */
package com.thinkparity.codebase.model.util.xapool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.thinkparity.codebase.model.util.xapool.XADataSourceConfiguration.Key;


import org.enhydra.jdbc.standard.StandardXADataSource;

/**
 * <b>Title:</b>thinkParity CommonModel XA Data Source<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class XADataSource extends StandardXADataSource {

    /**
     * Extract a configuration value.
     * 
     * @param configuration
     *            The <code>XADataSourceConfiguration</code>.
     * @param key
     *            The <code>Key</code>.
     * @return The configuration value <code>String</code>.
     */
    private static String extractValue(
            final XADataSourceConfiguration configuration, final Key key) {
        return configuration.getProperty(key.name());
    }

    /**
     * Create XADataSource.
     * 
     * @param configuration
     *            The <code>XADataSourceConfiguration</code>.
     * @throws SQLException
     */
    public XADataSource(final XADataSourceConfiguration configuration)
            throws SQLException {
        super();
        setDriverName(extractValue(configuration, Key.DRIVER));
        setPassword(extractValue(configuration, Key.PASSWORD));
        setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        setUrl(extractValue(configuration, Key.URL));
        setUser(extractValue(configuration, Key.USER));
    }

    /**
     * Obtain a connection used to shutdown the database.
     * 
     * @return A <code>Connection</code>.
     * @throws SQLException
     */
    synchronized Connection getShutdownConnection() throws SQLException {
        final String url = new StringBuilder(getUrl())
            .append(";shutdown=true").toString();
        return DriverManager.getConnection(url);
    }
}