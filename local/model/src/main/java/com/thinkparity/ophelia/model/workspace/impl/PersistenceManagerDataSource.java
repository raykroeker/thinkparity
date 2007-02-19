/*
 * Created On:  7-Feb-07 4:17:56 PM
 */
package com.thinkparity.ophelia.model.workspace.impl;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import com.thinkparity.ophelia.model.Constants.DirectoryNames;
import com.thinkparity.ophelia.model.workspace.Workspace;

import org.enhydra.jdbc.standard.StandardXADataSource;

/**
 * <b>Title:</b>thinkParity OpheliaModel Persistence Manager DataSource<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
class PersistenceManagerDataSource extends StandardXADataSource {

    /** The data source driver name. */
    private static final String DS_DRIVER_NAME;

    /** The data source password. */
    private static final String DS_PASSWORD;

    /** The data source user. */
    private static final String DS_USER;

    static {
        DS_DRIVER_NAME = "org.apache.derby.jdbc.EmbeddedDriver";
        DS_USER = "sa";
        DS_PASSWORD = "";
    }

    /**
     * Create PersistenceManagerDataSource.
     *
     */
    public PersistenceManagerDataSource(final Workspace workspace) throws SQLException {
        super();
        setDriverName(DS_DRIVER_NAME);
        setPassword(DS_PASSWORD);
        setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        final File persistenceDirectory = new File(workspace.getDataDirectory(),
                DirectoryNames.Workspace.Data.DB);
        final StringBuffer url = new StringBuffer("jdbc:derby:")
            .append(persistenceDirectory.getAbsolutePath());
        if (!persistenceDirectory.exists())
            url.append(";create=true");
        setUrl(url.toString());
        setUser(DS_USER);
    }

    /**
     * @see org.enhydra.jdbc.standard.StandardDataSource#getConnection()
     *
     */
    @Override
    public synchronized Connection getConnection() throws SQLException {
        final Connection cx = getXAConnection().getConnection();
        cx.setAutoCommit(false);
        return cx;
    }
}
