/*
 * Created On:  16-Mar-07 8:42:10 PM
 */
package com.thinkparity.ophelia.model.workspace.impl.xapool;

import java.sql.Connection;
import java.sql.SQLException;

import org.enhydra.jdbc.pool.StandardXAPoolDataSource;


/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class OpheliaXADataSourcePool extends StandardXAPoolDataSource {

    private final OpheliaXADataSource xaDataSource;

    /**
     * Create OpheliaXADataSourcePool.
     *
     */
    public OpheliaXADataSourcePool(final OpheliaXADataSource xaDataSource) {
        super(xaDataSource, 3);
        this.xaDataSource = xaDataSource;
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
}
