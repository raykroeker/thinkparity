/*
 * Created On:  24-Sep-07 11:24:24 AM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.thinkparity.ophelia.model.workspace.Transaction;
import com.thinkparity.ophelia.model.workspace.WorkspaceException;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class TransactionImpl implements Transaction {

    /** An active indicator. */
    private boolean active;

    /** A connection. */
    private Connection connection;

    /** A data source. */
    private final DataSource dataSource;

    /** A rollback only indicator. */
    private boolean rollbackOnly;

    /**
     * Create TransactionImpl.
     *
     */
    public TransactionImpl(final DataSource dataSource) {
        super();
        this.active = false;
        this.dataSource = dataSource;
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Transaction#begin()
     *
     */
    @Override
    public void begin() {
        if (isActive()) {
            throw new IllegalStateException("Nested transaction not supported.");
        }

        try {
            active = false;
            /* a connection can be established as part of a supported but not
             * required transaction; therefore if at a later time begin is
             * called the connection will be supported */
            if (null == connection || connection.isClosed()) {
                connection = dataSource.getConnection();
            }
            active = true;
        } catch (final SQLException sqlx) {
            throw new WorkspaceException("Cannot obtain transctional connection.", sqlx);
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Transaction#commit()
     *
     */
    @Override
    public void commit() {
        if (isNotActive()) {
            throw new IllegalStateException("Cannot commit inactive transaction.");
        }
        if (isRollbackOnly()) {
            throw new IllegalStateException("Cannot commit rollback only transaction.");
        }

        SQLException commitException = null;
        SQLException releaseException = null;
        try {
            connection.commit();
        } catch (final SQLException sqlx) {
            commitException = sqlx;
        } finally {
            try {
                release();
            } catch (final SQLException sqlx) {
                releaseException = sqlx;
            }
        }
        if (null != commitException || null != releaseException) {
            if (null == commitException) {
                throw new WorkspaceException("Cannot release transaction.", releaseException);
            } else {
                commitException.setNextException(releaseException);
                throw new WorkspaceException("Cannot commit transaction.", commitException);
            }
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Transaction#isActive()
     *
     */
    @Override
    public Boolean isActive() {
        return Boolean.valueOf(active);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Transaction#isRollbackOnly()
     *
     */
    @Override
    public Boolean isRollbackOnly() {
        return Boolean.valueOf(rollbackOnly);
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Transaction#rollback()
     *
     */
    @Override
    public void rollback() {
        if (isNotActive()) {
            throw new IllegalStateException("Cannot rollback inactive transaction.");
        }

        SQLException rollbackException = null;
        SQLException releaseException = null;
        try {
            connection.rollback();
        } catch (final SQLException sqlx) {
            throw new WorkspaceException("Cannnot rollback transaction.", sqlx);
        } finally {
            try {
                release();
            } catch (final SQLException sqlx) {
                releaseException = sqlx;
            }
        }
        if (null != rollbackException || null != releaseException) {
            if (null == rollbackException) {
                throw new WorkspaceException("Cannot release transaction.", releaseException);
            } else {
                rollbackException.setNextException(releaseException);
                throw new WorkspaceException("Cannot rollback transaction.", rollbackException);
            }
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.workspace.Transaction#setRollbackOnly()
     *
     */
    @Override
    public void setRollbackOnly() {
        rollbackOnly = true;
    }

    /**
     * Obtain the transaction's data source.
     * 
     * @return A <code>DataSource</code>.
     */
    DataSource getDataSource() {
        return new DataSource() {

            /**
             * @see javax.sql.DataSource#getConnection()
             *
             */
            @Override
            public Connection getConnection() throws SQLException {
                /* there are cases when a connection is required however will
                 * not participate in a transaction; therefore we create the
                 * connection here; and it can potentially later become
                 * involved in a transaction */
                if (null == connection || connection.isClosed()) {
                    connection = dataSource.getConnection();
                }
                return connection;
            }

            /**
             * @see javax.sql.DataSource#getConnection(java.lang.String, java.lang.String)
             *
             */
            @Override
            public Connection getConnection(final String username,
                    final String password) throws SQLException {
                throw new UnsupportedOperationException();
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
                return dataSource.isWrapperFor(iface);
            }

            /**
             * @see javax.sql.CommonDataSource#setLoginTimeout(int)
             *
             */
            @Override
            public void setLoginTimeout(final int seconds) throws SQLException {
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
                return dataSource.unwrap(iface);
            }
        };
    }

    /**
     * Determine if we are not active.
     * 
     * @return True if we are not active.
     */
    private Boolean isNotActive() {
        return Boolean.valueOf(!isActive().booleanValue());
    }

    /**
     * Release the connection.
     * 
     * @throws SQLException
     */
    private void release() throws SQLException {
        try {
            ((ConnectionImpl) connection).release();
        } finally {
            active = false;
        }
    }
}
