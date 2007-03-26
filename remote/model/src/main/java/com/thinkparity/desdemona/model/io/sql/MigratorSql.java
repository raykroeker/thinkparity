/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.model.io.sql;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.OS;

import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicException;
import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;

/**
 * <b>Title:</b>thinkParity Migrator SQL<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MigratorSql extends AbstractSql {

    /** Sql to create an error. */
    private static final String SQL_CREATE_ERROR =
        new StringBuilder("insert into TPSD_PRODUCT_ERROR ")
        .append("(PRODUCT_ID,USER_ID,ERROR_DATE,ERROR_XML) ")
        .append("values (?,?,?,?) ")
        .toString();

    /** Sql to create a product. */
    private static final String SQL_CREATE_PRODUCT =
        new StringBuilder("insert into TPSD_PRODUCT ")
        .append("(PRODUCT_ID,PRODUCT_NAME) ")
        .append("values (?,?)")
        .toString();

    /** Sql to create a release. */
    private static final String SQL_CREATE_RELEASE =
        new StringBuilder("insert into TPSD_PRODUCT_RELEASE ")
        .append("(PRODUCT_ID,RELEASE_NAME,RELEASE_OS,RELEASE_DATE) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to add a resource. */
    private static final String SQL_CREATE_RELEASE_RESOURCE_REL =
        new StringBuilder("insert into TPSD_PRODUCT_RELEASE_RESOURCE_REL ")
        .append("(RELEASE_ID,RESOURCE_ID,RESOURCE_PATH) ")
        .append("values (?,?,?)")
        .toString();

    /** Sql to add a resource. */
    private static final String SQL_CREATE_RESOURCE =
        new StringBuilder("insert into TPSD_PRODUCT_RELEASE_RESOURCE ")
        .append("(RESOURCE_NAME,RESOURCE_VERSION,RESOURCE_CHECKSUM,RESOURCE_SIZE,RESOURCE) ")
        .append("values (?,?,?,?,?)")
        .toString();

    /** Sql to create a resource os relationship. */
    private static final String SQL_CREATE_RESOURCE_OS =
        new StringBuilder("insert into TPSD_PRODUCT_RELEASE_RESOURCE_OS ")
        .append("(RESOURCE_ID,RESOURCE_OS) ")
        .append("values (?,?)")
        .toString();

    /** Sql to determine product existence by primary key. */
    private static final String SQL_DOES_EXIST_PRODUCT_PK =
        new StringBuilder("select count(PRODUCT_ID) \"PRODUCT_COUNT\" ")
        .append("from TPSD_PRODUCT P ")
        .append("where P.PRODUCT_NAME=? ")
        .toString();

    /** Sql to determine release existence. */
    private static final String SQL_DOES_EXIST_RELEASE_BY_NAME =
        new StringBuilder("select count(R.RELEASE_ID) \"RELEASE_COUNT\" ")
        .append("from TPSD_PRODUCT_RELEASE R ")
        .append("where R.PRODUCT_ID=? and R.RELEASE_NAME=? and R.RELEASE_OS=?")
        .toString();

    /** Sql to determine resource existence. */
    private static final String SQL_DOES_EXIST_RESOURCE_BY_ID_OS =
        new StringBuilder("select count(R.RESOURCE_ID) \"RESOURCE_COUNT\" ")
        .append("from TPSD_PRODUCT_RELEASE_RESOURCE R ")
        .append("inner join TPSD_PRODUCT_RELEASE_RESOURCE_OS RO ")
        .append("on R.RESOURCE_ID=RO.RESOURCE_ID ")
        .append("where R.RESOURCE_ID=? and RO.RESOURCE_OS=? ")
        .toString();

    /** Sql to determine resource existence. */
    private static final String SQL_DOES_EXIST_RESOURCE_BY_NAME_VERSION_CHECKSUM =
        new StringBuilder("select count(RESOURCE_ID) \"RESOURCE_COUNT\" ")
        .append("from TPSD_PRODUCT_RELEASE_RESOURCE R ")
        .append("where R.RESOURCE_NAME=? and R.RESOURCE_VERSION=? ")
        .append("and R.RESOURCE_CHECKSUM=?")
        .toString();

    /** Sql to read a release. */
    private static final String SQL_READ_LATEST_RELEASE_NAME =
        new StringBuilder("select RELEASE_NAME ")
        .append("from TPSD_PRODUCT_RELEASE R ")
        .append("inner join TPSD_PRODUCT P on R.PRODUCT_ID=P.PRODUCT_ID ")
        .append("where R.RELEASE_OS=? ")
        .append("order by R.RELEASE_DATE desc")
        .toString();

    /** Sql to read a product. */
    private static final String SQL_READ_PRODUCT =
        new StringBuilder("select P.PRODUCT_ID,P.PRODUCT_NAME ")
        .append("from TPSD_PRODUCT P ")
        .append("where P.PRODUCT_NAME=? ")
        .toString();

    /** Sql to read a release. */
    private static final String SQL_READ_RELEASE =
        new StringBuilder("select R.RELEASE_ID,R.RELEASE_NAME,R.RELEASE_OS,")
        .append("R.RELEASE_DATE,P.PRODUCT_NAME ")
        .append("from TPSD_PRODUCT_RELEASE R ")
        .append("inner join TPSD_PRODUCT P on R.PRODUCT_ID=P.PRODUCT_ID ")
        .append("where R.RELEASE_NAME=? and R.RELEASE_OS=?")
        .toString();

    /** Sql to read a resource id. */
    private static final String SQL_READ_RESOURCE_ID =
        new StringBuilder("select R.RESOURCE_ID ")
        .append("from TPSD_PRODUCT_RELEASE_RESOURCE R ")
        .append("where R.RESOURCE_NAME=? and R.RESOURCE_VERSION=? ")
        .append("and R.RESOURCE_CHECKSUM=?")
        .toString();

    /** Sql to read resources. */
    private static final String SQL_READ_RESOURCES =
        new StringBuilder("select RESOURCE_ID,RESOURCE_NAME,RESOURCE_VERSION,")
        .append("RESOURCE_CHECKSUM,RESOURCE_SIZE,RESOURCE_OS.RESOURCE_OS,")
        .append("RESOURCE_RELEASE_REL.RESOURCE_PATH ")
        .append("from TPSD_PRODUCT_RELEASE_RESOURCE RESOURCE ")
        .append("inner join TPSD_PRODUCT_RELEASE_RESOURCE_REL RESOURCE_RELEASE_REL on ")
        .append("RESOURCE.RESOURCE_ID=RESOURCE_RELEASE_REL.RESOURCE_ID ")
        .append("inner join TPSD_PRODUCT_RELEASE RELEASE on  ")
        .append("RESOURCE_RELEASE_REL.RELEASE_ID=RELEASE.RELEASE_ID ")
        .append("inner join TPSD_PRODUCT_RELEASE_RESOURCE_OS RESOURCE_OS on ")
        .append("RESOURCE.RESOURCE_ID=RESOURCE_OS.RESOURCE_ID and ")
        .append("RELEASE.RELEASE_OS=RESOURCE_OS.RESOURCE_OS ")
        .append("inner join TPSD_PRODUCT PRODUCT on ")
        .append("RELEASE.PRODUCT_ID=PRODUCT.PRODUCT_ID ")
        .append("where P.PRODUCT_NAME=? and RELEASE.RELEASE_NAME=? ")
        .append("and RELEASE.RELEASE_OS=?")
        .toString();

    /**
     * Create MigratorSql.
     *
	 */
	public MigratorSql() {
        super();
	}

    /**
     * Add a resource to a release.
     * 
     * @param release
     *            A <code>Release</code>.
     * @param resource
     *            A <code>Resource</code>.
     * @param streamSize
     *            The stream size <code>Long</code>.
     */
    public void addResource(final Release release, final Resource resource) {
        final HypersonicSession session = openSession();
        try {
            addResource(session, release, resource);
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Add a resource to a release.
     * 
     * @param release
     *            A <code>Release</code>.
     * @param resource
     *            A <code>Resource</code>.
     * @param stream
     *            A <code>InputStream</code>.
     * @param streamSize
     *            The stream size <code>Long</code>.
     */
    public void addResource(final Release release, final Resource resource,
            final InputStream stream, final Integer bufferSize) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_RESOURCE);
            session.setString(1, resource.getName());
            session.setString(2, resource.getVersion());
            session.setString(3, resource.getChecksum());
            session.setLong(4, resource.getSize());
            session.setBinaryStream(5, stream, resource.getSize(), bufferSize);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create release.");
            resource.setId(session.getIdentity("TPSD_PRODUCT_RELEASE_RESOURCE"));

            addResource(session, resource, resource.getOs());
            addResource(session, release, resource);

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Add a resource os relationship.
     * 
     * @param resource
     *            A <code>Resource</code>.
     * @param os
     *            An <code>OS</code>.
     */
    public void addResource(final Resource resource, final OS os) {
        final HypersonicSession session = openSession();
        try {
            addResource(session, resource, os);
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void createError(final User user, final Product product,
            final InputStream errorStream, final Long errorLength,
            final Integer bufferSize, final Error error) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_ERROR);
            session.setLong(1, product.getId());
            session.setLong(2, user.getLocalId());
            session.setCalendar(3, error.getOccuredOn());
            session.setBinaryStream(4, errorStream, errorLength, bufferSize);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create error.");
            error.setId(session.getIdentity("TPSD_PRODUCT_ERROR"));
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Create a product.
     * 
     * @param product
     *            A <code>Product</code>.
     */
    public void createProduct(final Product product) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_PRODUCT);
            session.setLong(1, product.getId());
            session.setString(2, product.getName());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create product.");

            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Create a release.
     * 
     * @param release
     *            A <code>Release</code>.
     * @param resourceStreams
     *            A <code>Map</code> of <code>Resource</code>s and their
     *            <code>InputStream</code>s.
     */
    public void createRelease(final Product product, final Release release) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_RELEASE);
            session.setLong(1, product.getId());
            session.setString(2, release.getName());
            session.setString(3, release.getOs().name());
            session.setCalendar(4, release.getDate());
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not create release.");

            release.setId(session.getIdentity("TPSD_PRODUCT_RELEASE"));
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Determine product existence by name.
     * 
     * @param name
     *            A product name <code>String</code>.
     * @return True if the product exists; false otherwise.
     */
    public Boolean doesExistProduct(final String name) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_PRODUCT_PK);
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
     * Determine if a release exists.
     * 
     * @param productId
     *            A product id <code>Long</code..
     * @param name
     *            A relese name.
     * @return True if the release exists.
     */
    public Boolean doesExistRelease(final Long productId, final String name,
            final OS os) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_RELEASE_BY_NAME);
            session.setLong(1, productId);
            session.setString(2, name);
            session.setString(3, os.name());
            session.executeQuery();
            session.nextResult();
            if (0 == session.getInteger("RELEASE_COUNT")) {
                return Boolean.FALSE;
            } else if (1 == session.getInteger("RELEASE_COUNT")) {
                return Boolean.TRUE;
            } else {
                throw new HypersonicException("Could not determine release existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Determine if a resource exists.
     * 
     * @param productId
     *            A product id <code>Long</code..
     * @param name
     *            A relese name.
     * @return True if the release exists.
     */
    public Boolean doesExistResource(final Long id, final OS os) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_RESOURCE_BY_ID_OS);
            session.setLong(1, id);
            session.setString(2, os.name());
            session.executeQuery();
            if (session.nextResult()) {
                final int resourceCount = session.getInteger("RESOURCE_COUNT");
                if (0 == resourceCount) {
                    return Boolean.FALSE;
                } else if (1 == resourceCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine resource existence.");
                }
            } else {
                throw new HypersonicException("Could not determine resource existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Determine if a resource exists.
     * 
     * @param productId
     *            A product id <code>Long</code..
     * @param name
     *            A relese name.
     * @return True if the release exists.
     */
    public Boolean doesExistResource(final String name, final String version,
            final String checksum) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_RESOURCE_BY_NAME_VERSION_CHECKSUM);
            session.setString(1, name);
            session.setString(2, version);
            session.setString(3, checksum);

            session.executeQuery();
            session.nextResult();
            if (0 == session.getInteger("RESOURCE_COUNT")) {
                return Boolean.FALSE;
            } else if (1 == session.getInteger("RESOURCE_COUNT")) {
                return Boolean.TRUE;
            } else {
                throw new HypersonicException("Could not determine resource existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Read a release.
     * 
     * @param productUniqueId
     *            A product unique id <code>UUID</code>.
     * @param name
     *            A release name <code>String</code>.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    public String readLatestReleaseName(final UUID productUniqueId, final OS os) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_LATEST_RELEASE_NAME);
            session.setUniqueId(1, productUniqueId);
            session.setString(2, os.name());
            session.executeQuery();
            if (session.nextResult()) {
                return session.getString("RELEASE_NAME");
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read a product.
     * 
     * @param name
     *            A product name.
     * @return A <code>Product</code>.
     */
    public Product readProduct(final String name) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PRODUCT);
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
     * Read a release.
     * 
     * @param productUniqueId
     *            A product unique id <code>UUID</code>.
     * @param name
     *            A release name <code>String</code>.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    public Release readRelease(final UUID productUniqueId, final String name,
            final OS os) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_RELEASE);
            session.setUniqueId(1, productUniqueId);
            session.setString(2, name);
            session.setString(3, os.name());
            session.executeQuery();
            if (session.nextResult()) {
                return extractRelease(session);
            } else {
                return null;
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Read a resource id.
     * 
     * @param name
     *            A resource name <code>String</code>.
     * @param version
     *            A resource version <code>String</code>.
     * @param checksum
     *            A resource checksum <code>String</code>.
     * @return A resource id <code>Long</code>.
     */
    public Long readResourceId(final String name, final String version,
            final String checksum) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_RESOURCE_ID);
            session.setString(1, name);
            session.setString(2, version);
            session.setString(3, checksum);
            session.executeQuery();
            if (session.nextResult()) {
                return session.getLong("RESOURCE_ID");
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * Read a release's resources.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param productUniqueId
     *            A product unique id <code>UUID</code>.
     * @param releaseName
     *            A release name.
     * @return A <code>List</code> of <code>Resource</code>s.
     */
    public List<Resource> readResources(final String productName,
            final String releaseName, final OS os) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_RESOURCES);
            session.setString(1, productName);
            session.setString(2, releaseName);
            session.setString(3, os.name());
            session.executeQuery();
            final List<Resource> resources = new ArrayList<Resource>();
            while (session.nextResult()) {
                resources.add(extractResource(session));
            }
            return resources;
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Add a resource to a release.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param release
     *            A <code>Release</code>.
     * @param resource
     *            A <code>Resource</code>.
     * @param streamSize
     *            The stream size <code>Long</code>.
     */
    private void addResource(final HypersonicSession session,
            final Release release, final Resource resource) {
        session.prepareStatement(SQL_CREATE_RELEASE_RESOURCE_REL);
        session.setLong(1, release.getId());
        session.setLong(2, resource.getId());
        session.setString(3, resource.getPath());
        if (1 != session.executeUpdate())
            throw new HypersonicException("Could not create release resource relationship.");
    }

    /**
     * Add a resource os relationship.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param resource
     *            A <code>Resource</code>.
     * @param os
     *            An <code>OS</code>.
     */
    private void addResource(final HypersonicSession session,
            final Resource resource, final OS os) {
        session.prepareStatement(SQL_CREATE_RESOURCE_OS);
        session.setLong(1, resource.getId());
        session.setString(2, resource.getOs().name());
        if (1 != session.executeUpdate())
            throw new HypersonicException("Could not create release.");
    }

    /**
     * Extract a product from a session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return A <code>Product</code>.
     */
    private Product extractProduct(final HypersonicSession session) {
        final Product product = new Product();
        product.setId(session.getLong("PRODUCT_ID"));
        product.setName(session.getString("PRODUCT_NAME"));
        return product;
    }

    /**
     * Extract a release from a session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return A <code>Release</code>.
     */
    private Release extractRelease(final HypersonicSession session) {
        final Release release = new Release();
        release.setChecksum(null);
        release.setDate(session.getCalendar("RELEASE_DATE"));
        release.setId(session.getLong("RELEASE_ID"));
        release.setName(session.getString("RELEASE_NAME"));
        release.setOs(OS.valueOf(session.getString("RELEASE_OS")));
        release.setProduct(readProduct("PRODUCT_NAME"));
        return release;
    }

    /**
     * Extract a resource from a session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return A <code>Resource</code>.
     */
    private Resource extractResource(final HypersonicSession session) {
        final Resource resource = new Resource();
        resource.setChecksum(session.getString("RESOURCE_CHECKSUM"));
        resource.setId(session.getLong("RESOURCE_ID"));
        resource.setName(session.getString("RESOURCE_NAME"));
        resource.setOs(OS.valueOf(session.getString("RESOURCE_OS")));
        resource.setPath(session.getString("RESOURCE_PATH"));
        resource.setSize(session.getLong("RESOURCE_SIZE"));
        resource.setVersion(session.getString("RESOURCE_VERSION"));
        return resource;
    }
}
