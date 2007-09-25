/*
 * Created On:  24-Sep-07 12:00:55 PM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.sql.*;
import java.util.Map;
import java.util.Properties;

/**
 * <b>Title:</b>thinkParity Ophelia Model Workspace Connection Impl<br>
 * <b>Description:</b>A wrapper around a connection that does not close the
 * underlying conneciton.<br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class ConnectionImpl implements Connection {

    /** A connection. */
    private final Connection connection;

    /**
     * Create ConnectionImpl.
     *
     */
    ConnectionImpl(final Connection connection) {
        super();
        this.connection = connection;
    }

    /**
     * @see java.sql.Connection#clearWarnings()
     *
     */
    @Override
    public void clearWarnings() throws SQLException {
        connection.clearWarnings();
    }

    /**
     * @see java.sql.Connection#close()
     *
     */
    @Override
    public void close() throws SQLException {}

    /**
     * @see java.sql.Connection#commit()
     *
     */
    @Override
    public void commit() throws SQLException {
        connection.commit();
    }

    /**
     * @see java.sql.Connection#createArrayOf(java.lang.String, java.lang.Object[])
     *
     */
    @Override
    public Array createArrayOf(final String typeName, final Object[] elements)
            throws SQLException {
        return connection.createArrayOf(typeName, elements);
    }

    /**
     * @see java.sql.Connection#createBlob()
     *
     */
    @Override
    public Blob createBlob() throws SQLException {
        return connection.createBlob();
    }

    /**
     * @see java.sql.Connection#createClob()
     *
     */
    @Override
    public Clob createClob() throws SQLException {
        return connection.createClob();
    }

    /**
     * @see java.sql.Connection#createNClob()
     *
     */
    @Override
    public NClob createNClob() throws SQLException {
        return connection.createNClob();
    }

    /**
     * @see java.sql.Connection#createSQLXML()
     *
     */
    @Override
    public SQLXML createSQLXML() throws SQLException {
        return connection.createSQLXML();
    }

    /**
     * @see java.sql.Connection#createStatement()
     *
     */
    @Override
    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

    /**
     * @see java.sql.Connection#createStatement(int, int)
     *
     */
    @Override
    public Statement createStatement(final int resultSetType,
            final int resultSetConcurrency) throws SQLException {
        return connection.createStatement(resultSetType, resultSetConcurrency);
    }

    /**
     * @see java.sql.Connection#createStatement(int, int, int)
     *
     */
    @Override
    public Statement createStatement(final int resultSetType,
            final int resultSetConcurrency, final int resultSetHoldability)
            throws SQLException {
        return connection.createStatement(resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    /**
     * @see java.sql.Connection#createStruct(java.lang.String, java.lang.Object[])
     *
     */
    @Override
    public Struct createStruct(final String typeName, final Object[] attributes)
            throws SQLException {
        return connection.createStruct(typeName, attributes);
    }

    /**
     * @see java.sql.Connection#getAutoCommit()
     *
     */
    @Override
    public boolean getAutoCommit() throws SQLException {
        return connection.getAutoCommit();
    }

    /**
     * @see java.sql.Connection#getCatalog()
     *
     */
    @Override
    public String getCatalog() throws SQLException {
        return connection.getCatalog();
    }

    /**
     * @see java.sql.Connection#getClientInfo()
     *
     */
    @Override
    public Properties getClientInfo() throws SQLException {
        return connection.getClientInfo();
    }

    /**
     * @see java.sql.Connection#getClientInfo(java.lang.String)
     *
     */
    @Override
    public String getClientInfo(final String name) throws SQLException {
        return connection.getClientInfo(name);
    }

    /**
     * @see java.sql.Connection#getHoldability()
     *
     */
    @Override
    public int getHoldability() throws SQLException {
        return connection.getHoldability();
    }

    /**
     * @see java.sql.Connection#getMetaData()
     *
     */
    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        return connection.getMetaData();
    }

    /**
     * @see java.sql.Connection#getTransactionIsolation()
     *
     */
    @Override
    public int getTransactionIsolation() throws SQLException {
        return connection.getTransactionIsolation();
    }

    /**
     * @see java.sql.Connection#getTypeMap()
     *
     */
    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return connection.getTypeMap();
    }

    /**
     * @see java.sql.Connection#getWarnings()
     *
     */
    @Override
    public SQLWarning getWarnings() throws SQLException {
        return connection.getWarnings();
    }

    /**
     * @see java.sql.Connection#isClosed()
     *
     */
    @Override
    public boolean isClosed() throws SQLException {
        return connection.isClosed();
    }

    /**
     * @see java.sql.Connection#isReadOnly()
     *
     */
    @Override
    public boolean isReadOnly() throws SQLException {
        return connection.isReadOnly();
    }

    /**
     * @see java.sql.Connection#isValid(int)
     *
     */
    @Override
    public boolean isValid(final int timeout) throws SQLException {
        return connection.isValid(timeout);
    }

    /**
     * @see java.sql.Wrapper#isWrapperFor(java.lang.Class)
     *
     */
    @Override
    public boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return connection.isWrapperFor(iface);
    }

    /**
     * @see java.sql.Connection#nativeSQL(java.lang.String)
     *
     */
    @Override
    public String nativeSQL(final String sql) throws SQLException {
        return connection.nativeSQL(sql);
    }

    /**
     * @see java.sql.Connection#prepareCall(java.lang.String)
     *
     */
    @Override
    public CallableStatement prepareCall(final String sql) throws SQLException {
        return connection.prepareCall(sql);
    }

    /**
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int)
     *
     */
    @Override
    public CallableStatement prepareCall(final String sql,
            final int resultSetType, final int resultSetConcurrency)
            throws SQLException {
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency);
    }

    /**
     * @see java.sql.Connection#prepareCall(java.lang.String, int, int, int)
     *
     */
    @Override
    public CallableStatement prepareCall(final String sql,
            final int resultSetType, final int resultSetConcurrency,
            final int resultSetHoldability) throws SQLException {
        return connection.prepareCall(sql, resultSetType, resultSetConcurrency,
                resultSetHoldability);
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String)
     *
     */
    @Override
    public PreparedStatement prepareStatement(final String sql)
            throws SQLException {
        return connection.prepareStatement(sql);
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int)
     *
     */
    @Override
    public PreparedStatement prepareStatement(final String sql,
            final int autoGeneratedKeys) throws SQLException {
        return connection.prepareStatement(sql, autoGeneratedKeys);
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int)
     *
     */
    @Override
    public PreparedStatement prepareStatement(final String sql,
            final int resultSetType, final int resultSetConcurrency)
            throws SQLException {
        return connection.prepareStatement(sql, resultSetType,
                resultSetConcurrency);
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int, int, int)
     *
     */
    @Override
    public PreparedStatement prepareStatement(final String sql,
            final int resultSetType, final int resultSetConcurrency,
            final int resultSetHoldability) throws SQLException {
        return connection.prepareStatement(sql, resultSetType,
                resultSetConcurrency, resultSetHoldability);
    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, int[])
     *
     */
    @Override
    public PreparedStatement prepareStatement(final String sql,
            final int[] columnIndexes) throws SQLException {
        return connection.prepareStatement(sql, columnIndexes);

    }

    /**
     * @see java.sql.Connection#prepareStatement(java.lang.String, java.lang.String[])
     *
     */
    @Override
    public PreparedStatement prepareStatement(final String sql,
            final String[] columnNames) throws SQLException {
        return connection.prepareStatement(sql, columnNames);
    }

    /**
     * @see java.sql.Connection#releaseSavepoint(java.sql.Savepoint)
     *
     */
    @Override
    public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
        connection.releaseSavepoint(savepoint);
    }

    /**
     * @see java.sql.Connection#rollback()
     *
     */
    @Override
    public void rollback() throws SQLException {
        connection.rollback();
    }

    /**
     * @see java.sql.Connection#rollback(java.sql.Savepoint)
     *
     */
    @Override
    public void rollback(final Savepoint savepoint) throws SQLException {
        connection.rollback(savepoint);
    }

    /**
     * @see java.sql.Connection#setAutoCommit(boolean)
     *
     */
    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        connection.setAutoCommit(autoCommit);
    }

    /**
     * @see java.sql.Connection#setCatalog(java.lang.String)
     *
     */
    @Override
    public void setCatalog(final String catalog) throws SQLException {
        connection.setCatalog(catalog);
    }

    /**
     * @see java.sql.Connection#setClientInfo(java.util.Properties)
     *
     */
    @Override
    public void setClientInfo(final Properties properties)
            throws SQLClientInfoException {
        connection.setClientInfo(properties);
    }

    /**
     * @see java.sql.Connection#setClientInfo(java.lang.String, java.lang.String)
     *
     */
    @Override
    public void setClientInfo(final String name, final String value)
            throws SQLClientInfoException {
        connection.setClientInfo(name, value);
    }

    /**
     * @see java.sql.Connection#setHoldability(int)
     *
     */
    @Override
    public void setHoldability(final int holdability) throws SQLException {
        connection.setHoldability(holdability);
    }

    /**
     * @see java.sql.Connection#setReadOnly(boolean)
     *
     */
    @Override
    public void setReadOnly(final boolean readOnly) throws SQLException {
        connection.setReadOnly(readOnly);
    }

    /**
     * @see java.sql.Connection#setSavepoint()
     *
     */
    @Override
    public Savepoint setSavepoint() throws SQLException {
        return connection.setSavepoint();
    }

    /**
     * @see java.sql.Connection#setSavepoint(java.lang.String)
     *
     */
    @Override
    public Savepoint setSavepoint(final String name) throws SQLException {
        return connection.setSavepoint(name);
    }

    /**
     * @see java.sql.Connection#setTransactionIsolation(int)
     *
     */
    @Override
    public void setTransactionIsolation(final int level) throws SQLException {
        connection.setTransactionIsolation(level);
    }

    /**
     * @see java.sql.Connection#setTypeMap(java.util.Map)
     *
     */
    @Override
    public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
        connection.setTypeMap(map);
    }

    /**
     * @see java.sql.Wrapper#unwrap(java.lang.Class)
     *
     */
    @Override
    public <T> T unwrap(final Class<T> iface) throws SQLException {
        return connection.unwrap(iface);
    }

    /**
     * Release the connection.
     * 
     * @throws SQLException
     */
    void release() throws SQLException {
        connection.close();
    }
}
