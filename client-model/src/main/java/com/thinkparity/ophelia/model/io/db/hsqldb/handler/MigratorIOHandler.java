/*
 * Created On: Jun 28, 2006 8:49:07 PM
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.thinkparity.codebase.OS;

import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;

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
        new StringBuilder("insert into PRODUCT ")
        .append("(PRODUCT_NAME,INSTALLED_RELEASE_ID,LATEST_RELEASE_ID,")
        .append("PREVIOUS_RELEASE_ID) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to create a release. */
    private static final String SQL_CREATE_RELEASE =
        new StringBuilder("insert into PRODUCT_RELEASE ")
        .append("(RELEASE_NAME,RELEASE_OS,RELEASE_DATE,RELEASE_INITIALIZED) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to create a release resource. */
    private static final String SQL_CREATE_RELEASE_RESOURCE =
        new StringBuilder("insert into PRODUCT_RELEASE_RESOURCE ")
        .append("(RELEASE_ID,RESOURCE_CHECKSUM,RESOURCE_CHECKSUM_ALGORITHM,")
        .append("RESOURCE_PATH,RESOURCE_SIZE) ")
        .append("values (?,?,?,?,?)")
        .toString();

    /** Sql to delete the release resources. */
    private static final String SQL_DELETE_RELEASE =
        new StringBuilder("delete from PRODUCT_RELEASE ")
        .append("where RELEASE_ID=?")
        .toString();

    /** Sql to delete the release resources. */
    private static final String SQL_DELETE_RELEASE_RESOURCES =
        new StringBuilder("delete from PRODUCT_RELEASE_RESOURCES ")
        .append("where RELEASE_ID=?")
        .toString();

    /** Sql to determine product existence by its unique key. */
    private static final String SQL_DOES_EXIST_PRODUCT_UK =
        new StringBuilder("select count(P.PRODUCT_ID) \"PRODUCT_COUNT\" ")
        .append("from PRODUCT P ")
        .append("where P.PRODUCT_NAME=?")
        .toString();

    /** Sql to determine if the release is initialized by its primary key. */
    private static final String SQL_IS_RELEASE_INITIALIZED_PK =
        new StringBuilder("select PR.RELEASE_INITIALIZED ")
        .append("from PRODUCT_RELEASE PR ")
        .append("where PR.RELEASE_ID=?")
        .toString();

    /** Sql to read installed release. */
    private static final String SQL_READ_INSTALLED_RELEASE =
        new StringBuilder("select RELEASE_DATE,RELEASE_ID,RELEASE_NAME,")
        .append("RELEASE_OS ")
        .append("from PRODUCT_RELEASE PR ")
        .append("inner join PRODUCT P on P.INSTALLED_RELEASE_ID=PR.RELEASE_ID ")
        .append("where P.PRODUCT_ID=?")
        .toString();

    /** Sql to read installed resources. */
    private static final String SQL_READ_INSTALLED_RESOURCES =
        new StringBuilder("select PRR.RESOURCE_CHECKSUM,")
        .append("PRR.RESOURCE_CHECKSUM_ALGORITHM,PRR.RESOURCE_ID,")
        .append("PRR.RESOURCE_PATH,PRR.RESOURCE_SIZE ")
        .append("from PRODUCT_RELEASE_RESOURCE PRR ")
        .append("inner join PRODUCT_RELEASE PR on PR.RELEASE_ID=PRR.RELEASE_ID ")
        .append("inner join PRODUCT P on P.INSTALLED_RELEASE_ID=PR.RELEASE_ID ")
        .append("where P.PRODUCT_ID=?")
        .toString();

    /** Sql to read latest release. */
    private static final String SQL_READ_LATEST_RELEASE =
        new StringBuilder("select RELEASE_DATE,RELEASE_ID,RELEASE_NAME,")
        .append("RELEASE_OS ")
        .append("from PRODUCT_RELEASE PR ")
        .append("inner join PRODUCT P on P.LATEST_RELEASE_ID=PR.RELEASE_ID ")
        .append("where P.PRODUCT_ID=?")
        .toString();

    /** Sql to read latest resources. */
    private static final String SQL_READ_LATEST_RESOURCES =
        new StringBuilder("select PRR.RESOURCE_CHECKSUM,")
        .append("PRR.RESOURCE_CHECKSUM_ALGORITHM,PRR.RESOURCE_ID,")
        .append("PRR.RESOURCE_PATH,PRR.RESOURCE_SIZE ")
        .append("from PRODUCT_RELEASE_RESOURCE PRR ")
        .append("inner join PRODUCT_RELEASE PR on PR.RELEASE_ID=PRR.RELEASE_ID ")
        .append("inner join PRODUCT P on P.LATEST_RELEASE_ID=PR.RELEASE_ID ")
        .append("where P.PRODUCT_ID=?")
        .toString();

    /** Sql to read previous release. */
    private static final String SQL_READ_PREVIOUS_RELEASE =
        new StringBuilder("select RELEASE_DATE,RELEASE_ID,RELEASE_NAME,")
        .append("RELEASE_OS ")
        .append("from PRODUCT_RELEASE PR ")
        .append("inner join PRODUCT P on P.PREVIOUS_RELEASE_ID=PR.RELEASE_ID ")
        .append("where P.PRODUCT_ID=?")
        .toString();

    /** Sql to read a product by its unique key. */
    private static final String SQL_READ_PRODUCT_UK =
        new StringBuilder("select P.PRODUCT_ID,P.PRODUCT_NAME ")
        .append("from PRODUCT P ")
        .append("where P.PRODUCT_NAME=?")
        .toString();

    /** Sql to read all releases. */
    private static final String SQL_READ_RELEASES =
        new StringBuilder("select RELEASE_DATE,RELEASE_ID,RELEASE_NAME,")
        .append("RELEASE_OS ")
        .append("from PRODUCT_RELEASE PR")
        .toString();

    /** Sql to update the latest release. */
    private static final String SQL_UPDATE_INSTALLED_RELEASE =
        new StringBuilder("update PRODUCT ")
        .append("set INSTALLED_RELEASE_ID=? ")
        .append("where PRODUCT_ID=?")
        .toString();

    /** Sql to update the latest release. */
    private static final String SQL_UPDATE_LATEST_RELEASE =
        new StringBuilder("update PRODUCT ")
        .append("set LATEST_RELEASE_ID=? ")
        .append("where PRODUCT_ID=?")
        .toString();

    /** Sql to update the previous release. */
    private static final String SQL_UPDATE_PREVIOUS_RELEASE =
        new StringBuilder("update PRODUCT ")
        .append("set PREVIOUS_RELEASE_ID=? ")
        .append("where PRODUCT_ID=?")
        .toString();

    /** Sql to update the release initialization flag by its primary key. */
    private static final String SQL_UPDATE_RELEASE_INITIALIZATION_PK =
        new StringBuilder("update PRODUCT_RELEASE ")
        .append("set RELEASE_INITIALIZED=? ")
        .append("where RELEASE_ID=?")
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
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#createProduct(com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release, java.util.List)
     * 
     */
    public void createProduct(final Product product, final Release release,
            final List<Resource> resources) {
        final Session session = openSession();
        try {
            createRelease(session, release, resources);

            session.prepareStatement(SQL_CREATE_PRODUCT);
            session.setString(1, product.getName());
            session.setLong(2, release.getId());
            session.setLong(3, release.getId());
            session.setLong(4, null);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create product.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#createRelease(com.thinkparity.codebase.model.migrator.Release,
     *      java.util.List)
     * 
     */
    public void createRelease(final Release release,
            final List<Resource> resources) {
        final Session session = openSession();
        try {
            createRelease(session, release, resources);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#delete(com.thinkparity.codebase.model.migrator.Release)
     *
     */
    public void delete(final Release release) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_RELEASE_RESOURCES);
            session.setLong(1, release.getId());
            if (1 < session.executeUpdate())
                throw new HypersonicException("Could not delete release resources.");

            session.prepareStatement(SQL_DELETE_RELEASE);
            session.setLong(1, release.getId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not delete release.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#doesExistProduct(java.lang.String)
     * 
     */
    public Boolean doesExistProduct(final String name) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_PRODUCT_UK);
            session.setString(1, name);
            session.executeQuery();
            if (session.nextResult()) {
                final int productCount = session.getInteger("PRODUCT_COUNT");
                if (0 == productCount) {
                    return Boolean.FALSE;
                } else if (1 == productCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine product existence.");
                }
            } else {
                throw new HypersonicException("Could not determine product existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#isReleaseInitialized(com.thinkparity.codebase.model.migrator.Release)
     *
     */
    public Boolean isReleaseInitialized(final Release release) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_IS_RELEASE_INITIALIZED_PK);
            session.setLong(1, release.getId());
            session.executeQuery();
            if (session.nextResult()) {
                return session.getBoolean("RELEASE_INITIALIZED");
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
    public Release readInstalledRelease(final Product product) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_INSTALLED_RELEASE);
            session.setLong(1, product.getId());
            session.executeQuery();
            if (session.nextResult()) {
                return extractRelease(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#readInstalledResources(com.thinkparity.codebase.model.migrator.Product)
     * 
     */
    public List<Resource> readInstalledResources(final Product product) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_INSTALLED_RESOURCES);
            session.setLong(1, product.getId());
            session.executeQuery();
            final List<Resource> resources = new ArrayList<Resource>();
            while (session.nextResult()) {
                resources.add(extractResource(session));
            }
            return resources;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#readLatestRelease(com.thinkparity.codebase.model.migrator.Product)
     *
     */
    public Release readLatestRelease(final Product product) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_LATEST_RELEASE);
            session.setLong(1, product.getId());
            session.executeQuery();
            if (session.nextResult()) {
                return extractRelease(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#readLatestResources(com.thinkparity.codebase.model.migrator.Product)
     *
     */
    public List<Resource> readLatestResources(final Product product) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_LATEST_RESOURCES);
            session.setLong(1, product.getId());
            session.executeQuery();
            final List<Resource> resources = new ArrayList<Resource>();
            while (session.nextResult()) {
                resources.add(extractResource(session));
            }
            return resources;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#readPreviousRelease(com.thinkparity.codebase.model.migrator.Product)
     *
     */
    public Release readPreviousRelease(final Product product) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_PREVIOUS_RELEASE);
            session.setLong(1, product.getId());
            session.executeQuery();
            if (session.nextResult()) {
                return extractRelease(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#readProduct()
     *
     */
    public Product readProduct(final String name) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_PRODUCT_UK);
            session.setString(1, name);
            session.executeQuery();
            if (session.nextResult()) {
                return extractProduct(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#readReleases(com.thinkparity.codebase.model.migrator.Product)
     *
     */
    public List<Release> readReleases() {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_RELEASES);
            session.executeQuery();
            final List<Release> releases = new ArrayList<Release>();
            while (session.nextResult()) {
                releases.add(extractRelease(session));
            }
            return releases;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#updateInstalledRelease(com.thinkparity.codebase.model.migrator.Product, com.thinkparity.codebase.model.migrator.Release)
     *
     */
    public void updateInstalledRelease(final Product product,
            final Release release) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_INSTALLED_RELEASE);
            session.setLong(1, release.getId());
            session.setLong(2, product.getId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update latest release.");
        } finally {
            session.close();
        }
    }

    
    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#updateLatestRelease(com.thinkparity.codebase.model.migrator.Product,
     *      com.thinkparity.codebase.model.migrator.Release)
     * 
     */
    public void updateLatestRelease(final Product product, final Release release) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_LATEST_RELEASE);
            session.setLong(1, release.getId());
            session.setLong(2, product.getId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update latest release.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#updatePreviousRelease(com.thinkparity.codebase.model.migrator.Product, com.thinkparity.codebase.model.migrator.Release)
     *
     */
    public void updatePreviousRelease(final Product product,
            final Release release) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_PREVIOUS_RELEASE);
            session.setLong(1, release.getId());
            session.setLong(2, product.getId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update previous release.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#updateReleaseInitialization(com.thinkparity.codebase.model.migrator.Release)
     *
     */
    public void updateReleaseInitialization(final Release release) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_UPDATE_RELEASE_INITIALIZATION_PK);
            session.setBoolean(1, Boolean.TRUE);
            session.setLong(2, release.getId());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not update release initialization.");
        } finally {
            session.close();
        }
    }

    /**
     * Create a release using the session.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param release
     *            A <code>Release</code>.
     * @param releaseResources
     *            A <code>List</code> of <code>Resource</code>s.
     */
    private void createRelease(final Session session, final Release release,
            final List<Resource> resources) {
        session.prepareStatement(SQL_CREATE_RELEASE);
        session.setString(1, release.getName());
        session.setString(2, release.getOs().name());
        session.setCalendar(3, release.getDate());
        session.setBoolean(4, Boolean.FALSE);
        if (1 != session.executeUpdate())
            throw new HypersonicException("Could not create release.");
        release.setId(session.getIdentity("PRODUCT_RELEASE"));

        for (final Resource resource : resources) {
            session.prepareStatement(SQL_CREATE_RELEASE_RESOURCE);
            session.setLong(1, release.getId());
            session.setString(2, resource.getChecksum());
            session.setString(3, resource.getChecksumAlgorithm());
            session.setString(4, resource.getPath());
            session.setLong(5, resource.getSize());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create release resource.");
            resource.setId(session.getIdentity("PRODUCT_RELEASE_RESOURCE"));
        }
    }

    /**
     * Extract a product from the session.
     * 
     * @param session
     *            A <code>Session</code>.
     * @return A <code>Product</code>.
     */
    private Product extractProduct(final Session session) {
        final Product product = new Product();
        product.setId(session.getLong("PRODUCT_ID"));
        product.setName(session.getString("PRODUCT_NAME"));
        return product;
    }

    /**
     * Extract a release from a session for a product.
     * 
     * @param session
     *            A <code>Session</code>.
     * @return A <code>Release</code>.
     */
    private Release extractRelease(final Session session) {
        final Release release = new Release();
        release.setDate(session.getCalendar("RELEASE_DATE"));
        release.setId(session.getLong("RELEASE_ID"));
        release.setName(session.getString("RELEASE_NAME"));
        release.setOs(OS.valueOf(session.getString("RELEASE_OS")));
        return release;
    }

    private Resource extractResource(final Session session) {
        final Resource resource = new Resource();
        resource.setChecksum(session.getString("RESOURCE_CHECKSUM"));
        resource.setChecksumAlgorithm(session.getString("RESOURCE_CHECKSUM_ALGORITHM"));
        resource.setId(session.getLong("RESOURCE_ID"));
        resource.setPath(session.getString("RESOURCE_PATH"));
        resource.setSize(session.getLong("RESOURCE_SIZE"));
        return resource;
    }
}
