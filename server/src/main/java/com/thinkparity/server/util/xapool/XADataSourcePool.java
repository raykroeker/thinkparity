/*
 * Created On:  16-Mar-07 8:42:10 PM
 */
package com.thinkparity.codebase.model.util.xapool;

import java.sql.Connection;
import java.sql.SQLException;

import javax.transaction.TransactionManager;

import org.enhydra.jdbc.pool.StandardXAPoolDataSource;

/**
 * <b>Title:</b>thinkParity CommonModel Transaction Data Source Pool<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class XADataSourcePool extends StandardXAPoolDataSource {

    /** The underlying <code>XADataSource</code>. */
    private final XADataSource xaDataSource;

    /**
     * Create XADataSourcePool.
     * 
     * @param xaDataSource
     *            An <code>XADataSource</code>.
     */
    public XADataSourcePool(final XADataSource xaDataSource) {
        super(xaDataSource, 5);
        /* TIMEOUT - XADataSourcePool#<init>
         * SYNC - XADataSourcePool#<init> - when a "multi-threaded"
         * model is required; the timeout will need to drop in order to release
         * resources from the transaction if it ceases to respond */
        setLifeTime(Long.valueOf(Integer.MAX_VALUE));
        this.xaDataSource = xaDataSource;
        setUser(xaDataSource.getUser());
        setPassword(xaDataSource.getPassword());
    }

    /**
     * Obtain a connection used to shutdown the database.
     * 
     * @return A <code>Connection</code>.
     * @throws SQLException
     */
    public synchronized Connection getShutdownConnection() throws SQLException {
        return xaDataSource.getShutdownConnection();
    }

    /**
     * @see org.enhydra.jdbc.pool.StandardXAPoolDataSource#setTransactionManager(javax.transaction.TransactionManager)
     *
     */
    @Override
    public void setTransactionManager(final TransactionManager tm) {
        xaDataSource.setTransactionManager(tm);
        super.setTransactionManager(tm);
    }
}
