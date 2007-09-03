/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.model.io.sql;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.OS;

import com.thinkparity.codebase.model.migrator.Error;
import com.thinkparity.codebase.model.migrator.Feature;
import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;
import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.model.io.hsqldb.HypersonicSession;
import com.thinkparity.desdemona.model.migrator.ResourceOpener;

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
        new StringBuilder("insert into TPSD_PRODUCT_RELEASE_ERROR ")
        .append("(RELEASE_ID,USER_ID,ERROR_DATE,ERROR_XML) ")
        .append("values (?,?,?,?) ")
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
        .append("(RESOURCE_CHECKSUM,RESOURCE_CHECKSUM_ALGORITHM,")
        .append("RESOURCE_SIZE,RESOURCE) ")
        .append("values (?,?,?,?)")
        .toString();

    /** Sql to delete a release. */
    private static final String SQL_DELETE_RELEASE =
        new StringBuilder("delete from TPSD_PRODUCT_RELEASE ")
        .append("where RELEASE_ID=?")
        .toString();

    /** Sql to delete release errors. */
    private static final String SQL_DELETE_RELEASE_ERRORS =
        new StringBuilder("delete from TPSD_PRODUCT_RELEASE_ERROR ")
        .append("where RELEASE_ID=?")
        .toString();

    /** Delete all resource relationships where the resource is specified. */
    private static final String SQL_DELETE_RESOURCE =
        new StringBuilder("delete from TPSD_PRODUCT_RELEASE_RESOURCE ")
        .append("where RESOURCE_ID=?")
        .toString();

    /** Delete all resource relationships where the resource is specified. */
    private static final String SQL_DELETE_RESOURCE_REL =
        new StringBuilder("delete from TPSD_PRODUCT_RELEASE_RESOURCE_REL ")
        .append("where RESOURCE_ID=?")
        .toString();

    /** Sql to determine release existence. */
    private static final String SQL_DOES_EXIST_RELEASE_BY_NAME =
        new StringBuilder("select count(R.RELEASE_ID) \"RELEASE_COUNT\" ")
        .append("from TPSD_PRODUCT_RELEASE R ")
        .append("where R.PRODUCT_ID=? and R.RELEASE_NAME=? and R.RELEASE_OS=?")
        .toString();

    /** Sql to determine resource existence by unique key. */
    private static final String SQL_DOES_EXIST_RESOURCE_UK =
        new StringBuilder("select count(PRRR.RESOURCE_ID) \"RESOURCE_COUNT\" ")
        .append("from TPSD_PRODUCT_RELEASE_RESOURCE_REL PRRR ")
        .append("where PRRR.RELEASE_ID=? and PRRR.RESOURCE_PATH=?")
        .toString();

    /** Sql to open a resource by its unique key. */
    private static final String SQL_OPEN_RESOURCE_PK =
        new StringBuilder("select R.RESOURCE ")
        .append("from TPSD_PRODUCT_RELEASE_RESOURCE R ")
        .append("where R.RESOURCE_ID=?")
        .toString();

    /** Sql to read a release. */
    private static final String SQL_READ_LATEST_RELEASE_NAME =
        new StringBuilder("select RELEASE_NAME ")
        .append("from TPSD_PRODUCT_RELEASE R ")
        .append("inner join TPSD_PRODUCT P on R.PRODUCT_ID=P.PRODUCT_ID ")
        .append("where P.PRODUCT_NAME=? and R.RELEASE_OS=? ")
        .append("order by R.RELEASE_DATE desc")
        .toString();

    /** Sql to read a release. */
    private static final String SQL_READ_PREVIOUS_RELEASES =
        new StringBuilder("select R.RELEASE_ID,R.RELEASE_NAME,R.RELEASE_OS,")
        .append("R.RELEASE_DATE,P.PRODUCT_NAME ")
        .append("from TPSD_PRODUCT_RELEASE R ")
        .append("inner join TPSD_PRODUCT P on R.PRODUCT_ID=P.PRODUCT_ID ")
        .append("where P.PRODUCT_NAME=? and R.RELEASE_OS=? ")
        .append("and R.RELEASE_DATE<? ")
        .append("order by R.RELEASE_DATE desc")
        .toString();

    /** Sql to read a product feature by its unique key. */
    private static final String SQL_READ_PRODUCT_FEATURE_UK =
        new StringBuilder("select PF.PRODUCT_ID,PF.FEATURE_ID,PF.FEATURE_NAME ")
        .append("from TPSD_PRODUCT_FEATURE PF ")
        .append("inner join TPSD_PRODUCT P on P.PRODUCT_ID=PF.PRODUCT_ID ")
        .append("where P.PRODUCT_ID=? and PF.FEATURE_NAME=?")
        .toString();

    /** Sql to read a product feature list. */
    private static final String SQL_READ_PRODUCT_FEATURES =
        new StringBuilder("select PF.PRODUCT_ID,PF.FEATURE_ID,PF.FEATURE_NAME ")
        .append("from TPSD_PRODUCT_FEATURE PF ")
        .append("inner join TPSD_PRODUCT P on P.PRODUCT_ID=PF.PRODUCT_ID ")
        .append("where P.PRODUCT_NAME=?")
        .toString();

    /** Sql to read a product by its unique key. */
    private static final String SQL_READ_PRODUCT_UK =
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
        .append("where P.PRODUCT_NAME=? and R.RELEASE_NAME=? ")
        .append("and R.RELEASE_OS=?")
        .toString();

    /** Sql to read a release error count. */
    private static final String SQL_READ_RELEASE_ERROR_COUNT =
        new StringBuilder("select count(PRE.ERROR_ID) \"ERROR_COUNT\" ")
        .append("from TPSD_PRODUCT_RELEASE_ERROR PRE ")
        .append("where PRE.RELEASE_ID=?")
        .toString();

    /** Read a release resource by its unique key. */
    private static final String SQL_READ_RELEASE_RESOURCE_UK =
        new StringBuilder("select PRR.RESOURCE_CHECKSUM,")
        .append("PRR.RESOURCE_CHECKSUM_ALGORITHM,PRR.RESOURCE_ID,")
        .append("PRR.RESOURCE_SIZE,PRRR.RESOURCE_PATH,PR.RELEASE_NAME ")
        .append("from TPSD_PRODUCT_RELEASE_RESOURCE PRR ")
        .append("inner join TPSD_PRODUCT_RELEASE_RESOURCE_REL PRRR ")
        .append("on PRRR.RESOURCE_ID=PRR.RESOURCE_ID ")
        .append("inner join TPSD_PRODUCT_RELEASE PR ")
        .append("on PR.RELEASE_ID=PRRR.RELEASE_ID ")
        .append("where PRRR.RELEASE_ID=? and PRRR.RESOURCE_PATH=?")
        .toString();

    /** Sql to read resources by the release primary key. */
    private static final String SQL_READ_RELEASE_RESOURCES_PK =
        new StringBuilder("select PRR.RESOURCE_CHECKSUM,")
        .append("PRR.RESOURCE_CHECKSUM_ALGORITHM,PRR.RESOURCE_ID,")
        .append("PRRR.RESOURCE_PATH,PRR.RESOURCE_SIZE,PR.RELEASE_NAME ")
        .append("from TPSD_PRODUCT_RELEASE_RESOURCE PRR ")
        .append("inner join TPSD_PRODUCT_RELEASE_RESOURCE_REL PRRR on ")
        .append("PRRR.RESOURCE_ID=PRR.RESOURCE_ID ")
        .append("inner join TPSD_PRODUCT_RELEASE PR on  ")
        .append("PR.RELEASE_ID=PRRR.RELEASE_ID ")
        .append("where PR.RELEASE_ID=?")
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
            // create release/resource relationship
            addResource(session, release, resource);
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    public void createError(final User user, final Release release,
            final InputStream errorStream, final Long errorLength,
            final Integer bufferSize, final Error error) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_CREATE_ERROR);
            session.setLong(1, release.getId());
            session.setLong(2, user.getLocalId());
            session.setCalendar(3, error.getOccuredOn());
            session.setAsciiStream(4, errorStream, errorLength, bufferSize);
            if (1 != session.executeUpdate())
                throw panic("Could not create error.");
            error.setId(session.getIdentity("TPSD_PRODUCT_RELEASE_ERROR"));

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
                throw panic("Could not create release.");
            release.setId(session.getIdentity("TPSD_PRODUCT_RELEASE"));

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
    public void createResource(final Resource resource,
            final InputStream stream, final Integer bufferSize) {
        final HypersonicSession session = openSession();
        try {
            // create resource
            session.prepareStatement(SQL_CREATE_RESOURCE);
            session.setString(1, resource.getChecksum());
            session.setString(2, resource.getChecksumAlgorithm());
            session.setLong(3, resource.getSize());
            session.setBinaryStream(4, stream, resource.getSize(), bufferSize);
            if (1 != session.executeUpdate())
                throw panic("Could not create release.");
            resource.setId(session.getIdentity("TPSD_PRODUCT_RELEASE_RESOURCE"));
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Delete a release.
     * 
     * @param release
     *            A <code>Release</code>.
     */
    public void delete(final Release release) {
        final HypersonicSession session = openSession();
        try {
            final List<Resource> resources = readResources(release);
            deleteResources(resources);

            deleteReleaseErrors(session, release);

            session.prepareStatement(SQL_DELETE_RELEASE);
            session.setLong(1, release.getId());
            if (1 != session.executeUpdate())
                throw panic("Could not delete release.");
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * Delete all resources not referenced by the latest release.
     * 
     * @param resources
     *            A <code>List</code> of <code>Resource</code>.
     */
    public void deleteResources(final List<Resource> resources) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DELETE_RESOURCE_REL);
            for (final Resource resource : resources) {
                session.setLong(1, resource.getId());
                session.executeUpdate();
            }
            session.prepareStatement(SQL_DELETE_RESOURCE);
            for (final Resource resource : resources) {
                session.setLong(1, resource.getId());
                if (1 != session.executeUpdate())
                    throw panic("Could not delete resource {0}:{1}.",
                            resource.getReleaseName(), resource.getPath());
            }
            session.commit();
        } catch (final Throwable t) {
            throw translateError(session, t);
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
                throw panic("Could not determine release existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Determine if a resource exists.
     * 
     * @return True if the release exists.
     */
    public Boolean doesExistResource(final Release release, final String path) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_DOES_EXIST_RESOURCE_UK);
            session.setLong(1, release.getId());
            session.setString(2, path);
            session.executeQuery();
            if (session.nextResult()) {
                final int resourceCount = session.getInteger("RESOURCE_COUNT");
                if (0 == resourceCount) {
                    return Boolean.FALSE;
                } else if (1 == resourceCount) {
                    return Boolean.TRUE;
                } else {
                    throw panic("Could not determine resource existence.");
                }
            } else {
                throw panic("Could not determine resource existence.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Open a resource.
     * 
     * @param resource
     *            A <code>Resource</code>.
     * @param opener
     *            A <code>ResourceOpener</code>.
     */
    public void openResource(final Resource resource,
            final ResourceOpener opener) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_OPEN_RESOURCE_PK);
            session.setLong(1, resource.getId());
            session.executeQuery();
            if (session.nextResult()) {
                final InputStream stream = session.getBlob("RESOURCE");
                try {
                    opener.open(stream);
                } finally {
                    stream.close();
                }
            } else {
                opener.open(null);
            }
        } catch (final Throwable t) {
            throw translateError(session, t);
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
    public String readLatestReleaseName(final String productName, final OS os) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_LATEST_RELEASE_NAME);
            session.setString(1, productName);
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
     * Read the previous release.
     * 
     * @param productName
     *            An existing product name <code>String</code>.
     * @param releaseName
     *            An existing release name <code>String</code>.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>Release</code>.
     */
    public Release readPreviousRelease(final String productName,
            final Calendar releaseDate, final OS os) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PREVIOUS_RELEASES);
            session.setString(1, productName);
            session.setString(2, os.name());
            session.setCalendar(3, releaseDate);
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
     * Read the previous release.
     * 
     * @param productName
     *            An existing product name <code>String</code>.
     * @param releaseName
     *            An existing release name <code>String</code>.
     * @param os
     *            An <code>OS</code>.
     * @return A <code>List<Release></code>.
     */
    public List<Release> readPreviousReleases(final String productName,
            final Calendar releaseDate, final OS os) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PREVIOUS_RELEASES);
            session.setString(1, productName);
            session.setString(2, os.name());
            session.setCalendar(3, releaseDate);
            session.executeQuery();
            final List<Release> releases = new ArrayList<Release>();
            while (session.nextResult()) {
                releases.add(extractRelease(session));
            }
            return releases;
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
     * Read a product feature.
     * 
     * @param product
     *            A <code>Product</code>.
     * @param name
     *            A feature name <code>String</code>.
     * @return A <code>Feature</code>.
     */
    public Feature readProductFeature(final Product product, final String name) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PRODUCT_FEATURE_UK);
            session.setLong(1, product.getId());
            session.setString(2, name);
            session.executeQuery();
            if (session.nextResult()) {
                return extractProductFeature(session);
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
     * Read a list of product features.
     * 
     * @param name
     *            A product name <code>String</code>.
     * @return A <code>List</code> of <code>Feature</code>s.
     */
    public List<Feature> readProductFeatures(final String name) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_PRODUCT_FEATURES);
            session.setString(1, name);
            session.executeQuery();
            final List<Feature> features = new ArrayList<Feature>();
            while (session.nextResult()) {
                features.add(extractProductFeature(session));
            }
            return features;
        } catch (final Throwable t) {
            throw translateError(session, t);
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
    public Release readRelease(final String productName, final String name,
            final OS os) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_RELEASE);
            session.setString(1, productName);
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

    public Resource readResource(final Release release, final String path) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_RELEASE_RESOURCE_UK);
            session.setLong(1, release.getId());
            session.setString(2, path);
            session.executeQuery();
            if (session.nextResult()) {
                return extractResource(session);
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
     * Read a release's resources.
     * 
     * @param release
     *            A <code>Release</code>.
     * @return A <code>List</code> of <code>Resource</code>s.
     */
    public List<Resource> readResources(final Release release) {
        final HypersonicSession session = openSession();
        try {
            session.prepareStatement(SQL_READ_RELEASE_RESOURCES_PK);
            session.setLong(1, release.getId());
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
            throw panic("Could not create release resource relationship.");
    }

    /**
     * Delete all of the release errors.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param release
     *            A <code>Release</code>.
     */
    private void deleteReleaseErrors(final HypersonicSession session,
            final Release release) {
        final int errors = readReleaseErrorCount(session, release).intValue();
        session.prepareStatement(SQL_DELETE_RELEASE_ERRORS);
        session.setLong(1, release.getId());
        if (errors != session.executeUpdate()) {
            throw panic("Could not delete release errors.");
        }
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
     * Extract a product feature from the session.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @return A <code>Feature</code>.
     */
    private Feature extractProductFeature(final HypersonicSession session) {
        final Feature feature = new Feature();
        feature.setFeatureId(session.getLong("FEATURE_ID"));
        feature.setName(session.getString("FEATURE_NAME"));
        feature.setProductId(session.getLong("PRODUCT_ID"));
        return feature;
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
        release.setDate(session.getCalendar("RELEASE_DATE"));
        release.setId(session.getLong("RELEASE_ID"));
        release.setName(session.getString("RELEASE_NAME"));
        release.setOs(OS.valueOf(session.getString("RELEASE_OS")));
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
        resource.setChecksumAlgorithm(session.getString("RESOURCE_CHECKSUM_ALGORITHM"));
        resource.setId(session.getLong("RESOURCE_ID"));
        resource.setPath(session.getString("RESOURCE_PATH"));
        resource.setReleaseName(session.getString("RELEASE_NAME"));
        resource.setSize(session.getLong("RESOURCE_SIZE"));
        return resource;
    }

    /**
     * Read the release error count.
     * 
     * @param session
     *            A <code>HypersonicSession</code>.
     * @param release
     *            A <code>Release</code>.
     * @return A <code>Integer</code>.
     */
    private Integer readReleaseErrorCount(final HypersonicSession session,
            final Release release) {
        session.prepareStatement(SQL_READ_RELEASE_ERROR_COUNT);
        session.setLong(1, release.getId());
        session.executeQuery();
        if (session.nextResult()) {
            return session.getInteger("ERROR_COUNT");
        } else {
            return null;
        }
    }
}
