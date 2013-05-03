/*
 * Created On: Jun 28, 2006 8:49:07 PM
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import java.util.List;

import javax.sql.DataSource;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;

/**
 * <b>Title:</b>thinkParity Migrator IO Handler Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MigratorIOHandler extends AbstractIOHandler implements
        com.thinkparity.ophelia.model.io.handler.MigratorIOHandler {

    /** Sql to create a product. */
    private static final String SQL_CREATE_PRODUCT =
        new StringBuilder("insert into MIGRATOR ")
        .append("(PRODUCT_NAME,RELEASE_NAME)")
        .append("values (?,?)")
        .toString();

    /** Sql to read the downloaded product release. */
    private static final String SQL_READ_DOWNLOADED_PRODUCT_RELEASE =
        new StringBuilder("select DOWNLOADED_RELEASE_NAME ")
        .append("from MIGRATOR M " )
        .append("where M.PRODUCT_NAME=?")
        .toString();

    /** Sql to read installed release. */
    private static final String SQL_READ_PRODUCT_RELEASE =
        new StringBuilder("select M.RELEASE_NAME ")
        .append("from MIGRATOR M ")
        .append("where M.PRODUCT_NAME=?")
        .toString();

    /** Sql to update the downloaded release. */
    private static final String SQL_UPDATE_DOWNLOADED_PRODUCT_RELEASE =
        new StringBuilder("update MIGRATOR ")
        .append("set DOWNLOADED_RELEASE_NAME=? ")
        .append("where PRODUCT_NAME=?")
        .toString();

    /** Sql to update the release. */
    private static final String SQL_UPDATE_PRODUCT_RELEASE =
        new StringBuilder("update MIGRATOR ")
        .append("set RELEASE_NAME=? ")
        .append("where PRODUCT_NAME=?")
        .toString();

    /**
     * Create MigratorIOHandler.
     * 
     * @param dataSource
     *            An sql <code>DataSource</code>.
     */
    public MigratorIOHandler(final DataSource dataSource) {
        super(dataSource);
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#createProduct(java.lang.String,
     *      java.lang.String)
     * 
     */
    public void createProduct(final String name, final String releaseName) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_PRODUCT);
            session.setString(1, name);
            session.setString(2, releaseName);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create product.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#deleteDownloadedProductRelease(java.lang.String)
     *
     */
    public void deleteDownloadedProductRelease(final String name) {
        updateDownloadedProductRelease(name, null);
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#execute(java.util.List)
     *
     */
    public void execute(final List<String> sql) {
        final Session session = openSession();
        try {
            session.execute(sql.toArray(new String[] {}));
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#readDownloadedProductRelease(java.lang.String)
     * 
     */
    public String readDownloadedProductRelease(final String name) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_DOWNLOADED_PRODUCT_RELEASE);
            session.setString(1, name);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getString("DOWNLOADED_RELEASE_NAME");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#readInstalledRelease(com.thinkparity.codebase.model.migrator.Product)
     *
     */
    public String readProductRelease(final String name) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_PRODUCT_RELEASE);
            session.setString(1, name);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getString("RELEASE_NAME");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#updateProductRelease(java.lang.String,
     *      java.lang.String)
     * 
     */
    public void updateDownloadedProductRelease(final String name, final String releaseName) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_DOWNLOADED_PRODUCT_RELEASE);
            session.setString(1, releaseName);
            session.setString(2, name);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update downloaded release.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#updateProductRelease(java.lang.String,
     *      java.lang.String)
     * 
     */
    public void updateProductRelease(final String name, final String releaseName) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_PRODUCT_RELEASE);
            session.setString(1, releaseName);
            session.setString(2, name);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update release.");
        } finally {
            session.close();
        }
    }
}
