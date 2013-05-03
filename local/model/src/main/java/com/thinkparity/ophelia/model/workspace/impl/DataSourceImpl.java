/*
 * Created On:  24-Sep-07 11:27:31 AM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.io.PrintWriter;
import java.lang.ref.SoftReference;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * <b>Title:</b>thinkParity Ophelia Model Workspace Data Source Implementation<br>
 * <b>Description:</b>A wrapper around a derby embedded data source.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class DataSourceImpl implements DataSource {

    /** A thread-local connection. */
    private static final ThreadLocal<SoftReference<Connection>> connection;

    static {
        connection = new ThreadLocal<SoftReference<Connection>>();
    }

    /** A wrapped data source. */
    private final DataSource dataSource;

    /** A data source password. */
    private final transient String password;

    /** A data source username. */
    private final transient String username;

    /**
     * Create DataSourceImpl.
     *
     */
    public DataSourceImpl(final DataSource dataSource, final String username,
            final String password) {
        super();
        this.dataSource = dataSource;
        this.username = username;
        this.password = password;
    }

    /**
     * @see javax.sql.DataSource#getConnection()
     *
     */
    @Override
    public Connection getConnection() throws SQLException {
        return getConnection(username, password);
    }

    /**
     * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
     *
     */
    @Override
    public Connection getConnection(final String username, final String password)
            throws SQLException {
        final SoftReference<Connection> localSoftConnection = connection.get();
        if (null == localSoftConnection) {
            return createConnection();
        } else {
            final Connection localConnection = localSoftConnection.get();
            if (null == localConnection) {
                return createConnection();
            } else {
                if (localConnection.isClosed()) {
                    return createConnection();
                } else {
                    return localConnection;
                }
            }
        }
    }

    /**
     * @see javax.sql.CommonDataSource#getLoginTimeout()
     *
     */
    @Override
    public int getLoginTimeout() throws SQLException {
        return dataSource.getLoginTimeout();
    }

    /**
     * @see javax.sql.CommonDataSource#getLogWriter()
     *
     */
    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return dataSource.getLogWriter();
    }

    /**
     * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     *
     */
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return false;
    }

    /**
     * @see javax.sql.CommonDataSource#setLoginTimeout(int)
     *
     */
    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        dataSource.setLoginTimeout(seconds);
    }

    /**
     * @see javax.sql.CommonDataSource#setLogWriter(java.io.PrintWriter)
     *
     */
    @Override
    public void setLogWriter(final PrintWriter out) throws SQLException {
        dataSource.setLogWriter(out);
    }

    /**
     * @see java.sql.Wrapper#unwrap(java.lang.Class)
     *
     */
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    /**
     * Create a connection for a username/password.
     * 
     * @param username
     *            A <code>String</code>.
     * @param password
     *            A <code>String</code>.
     * @return A <code>Connection</code>.
     * @throws SQLException
     */
    private Connection createConnection() throws SQLException {
        final Connection localConnection = new ConnectionImpl(dataSource.getConnection());
        connection.set(new SoftReference<Connection>(localConnection));
        return localConnection;
    }
}
