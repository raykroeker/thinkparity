/*
 * Created On:  20-Feb-07 11:39:32 AM
 */
package com.thinkparity.ophelia.model.workspace.impl.xapool;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.thinkparity.ophelia.model.Constants.DirectoryNames;
import com.thinkparity.ophelia.model.workspace.Workspace;

import org.enhydra.jdbc.standard.StandardXADataSource;

/**
 * <b>Title:</b>thinkParity Ophelia XA Data Source<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class OpheliaXADataSource extends StandardXADataSource {

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

    /** The base connection url <code>String</code>. */
    private final String baseUrl;

    /**
     * Create OpheliaXADataSource.
     * 
     * @param workspace
     *            A <code>Workspace</code>.
     */
    public OpheliaXADataSource(final Workspace workspace) throws SQLException {
        super();
        setDriverName(DS_DRIVER_NAME);
        setPassword(DS_PASSWORD);
        setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
        final File persistenceDirectory = new File(workspace.getDataDirectory(),
                DirectoryNames.Workspace.Data.DB);
        final StringBuffer url = new StringBuffer("jdbc:derby:")
            .append(persistenceDirectory.getAbsolutePath());
        this.baseUrl = url.toString();
        if (!persistenceDirectory.exists()) {
            url.append(";create=true");
        }
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

    /**
     * Obtain a connection used to shutdown the database.
     * 
     * @return A <code>Connection</code>.
     * @throws SQLException
     */
    public synchronized Connection getShutdownConnection() throws SQLException {
        final String url = new StringBuffer(baseUrl)
            .append(";shutdown=true").toString();
        return DriverManager.getConnection(url);
    }
}