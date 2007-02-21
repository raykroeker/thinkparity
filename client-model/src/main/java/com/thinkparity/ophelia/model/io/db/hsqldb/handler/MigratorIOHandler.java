/*
 * Created On: Jun 28, 2006 8:49:07 PM
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

import com.thinkparity.codebase.OS;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.migrator.Product;
import com.thinkparity.codebase.model.migrator.Release;
import com.thinkparity.codebase.model.migrator.Resource;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.md.MetaData;
import com.thinkparity.ophelia.model.io.md.MetaDataType;

/**
 * <b>Title:</b>thinkParity Migrator IO Handler Implementation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class MigratorIOHandler extends AbstractIOHandler implements
        com.thinkparity.ophelia.model.io.handler.MigratorIOHandler {

    /** Sql to create a migrator meta data reference. */
    private static final String SQL_CREATE_META_DATA =
        new StringBuffer("insert into MIGRATOR_META_DATA ")
        .append("(META_DATA_ID) ")
        .append("values (?)")
        .toString();

    /** Sql to create a migrator meta data reference. */
    private static final String SQL_DELETE_META_DATA =
        new StringBuffer("delete from MIGRATOR_META_DATA where META_DATA_ID=?")
        .toString();

    /** Sql to read a meta data item. */
    private static final String SQL_READ_META_DATA =
        new StringBuffer("select MD.META_DATA_ID,MD.META_DATA_KEY,MD.META_DATA_TYPE_ID,")
        .append("MD.META_DATA_VALUE ")
        .append("from MIGRATOR_META_DATA MMD ")
        .append("inner join META_DATA MD on MMD.META_DATA_ID=MD.META_DATA_ID ")
        .append("where MD.META_DATA_KEY=? and MD.META_DATA_TYPE_ID=?")
        .toString();

    /** Select a tree of meta data entries. */
    private static final String SQL_READ_META_DATA_TREE =
        new StringBuffer("select ")
        .append("from META_DATA MD ")
        .append("inner join MIGRATOR_META_DATA MMD on MD.META_DATA_ID=MMD.META_DATA_ID ")
        .append("where MD.META_DATA_KEY like ?")
        .toString();

    /** An instance of <code>MetaDataIO</code>. */
    private final MetaDataIOHandler metaDataIO;

    /**
     * Create MigratorIOHandler.
     * 
     * @param dataSource
     *            An sql <code>DataSource</code>.
     */
    public MigratorIOHandler(final DataSource dataSource) {
        super(dataSource);
        this.metaDataIO = new MetaDataIOHandler(dataSource);
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#createInstalledRelease(com.thinkparity.codebase.model.migrator.Release)
     *
     */
    public void createInstalledRelease(final Release release,
            final List<Resource> resources) {
        final Session session = openSession();
        try {
            createMetaDataCalendar(session, "InstalledRelease.date", release.getDate());
            createMetaDataString(session, "InstalledRelease.name", release.getName());
            createMetaDataOs(session, "InstalledRelease.os", release.getOs());
            createMetaDataInteger(session, "InstalledRelease.resources-size", resources.size());
            for (int i = 0; i < resources.size(); i++) {
                addResource(session, "InstalledRelease", resources, i);
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#createLatestRelease(com.thinkparity.codebase.model.migrator.Release)
     *
     */
    public void createLatestRelease(final Release release, final List<Resource> resources) {
        final Session session = openSession();
        try {
            createMetaDataCalendar(session, "LatestRelease.date", release.getDate());
            createMetaDataString(session, "LatestRelease.name", release.getName());
            createMetaDataOs(session, "LatestRelease.os", release.getOs());
            createMetaDataInteger(session, "LatestRelease.resources-size", resources.size());
            for (int i = 0; i < resources.size(); i++) {
                addResource(session, "LatestRelease", resources, i);
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#createProduct(com.thinkparity.codebase.model.migrator.Product)
     *
     */
    public void createProduct(final Product product) {
        final Session session = openSession();
        try {
            createMetaDataUserId(session, "Product.createdBy", product.getCreatedBy());
            createMetaDataCalendar(session, "Product.createdOn", product.getCreatedOn());
            createMetaDataString(session, "Product.name", product.getName());
            createMetaDataUniqueId(session, "Product.uniqueId", product.getUniqueId());
            createMetaDataUserId(session, "Product.updatedBy", product.getUpdatedBy());
            createMetaDataCalendar(session, "Product.updatedOn", product.getUpdatedOn());
        } catch (final Throwable t) {
            throw translateError(session, t);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#deleteLatestRelease()
     *
     */
    public void deleteLatestRelease() {
        final Session session = openSession();
        try {
            deleteMetaDataTree(session, "LatestRelease");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#readInstalledRelease()
     *
     */
    public Release readInstalledRelease() {
        final Session session = openSession();
        try {
            final Release release = new Release();
            release.setDate(readMetaDataCalendar(session, "InstalledRelease.date"));
            release.setName(readMetaDataString(session, "InstalledRelease.name"));
            release.setOs(readMetaDataOs(session, "InstalledRelease.os"));
            release.setProduct(readProduct(session));
            return release;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#readInstalledResources()
     *
     */
    public List<Resource> readInstalledResources() {
        final Session session = openSession();
        try {
            final Integer resourcesSize = readMetaDataInteger(session, "InstalledRelease.resources-size");
            final List<Resource> resources = new ArrayList<Resource>(resourcesSize);
            for (int i = 0; i < resourcesSize; i++) {
                resources.add(readResource(session, "InstalledRelease", i));
            }
            return resources;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#readLatestRelease()
     *
     */
    public Release readLatestRelease() {
        final Session session = openSession();
        try {
            final Release release = new Release();
            release.setDate(readMetaDataCalendar(session, "LatestRelease.date"));
            release.setName(readMetaDataString(session, "LatestRelease.name"));
            release.setOs(readMetaDataOs(session, "LatestRelease.os"));
            release.setProduct(readProduct(session));
            return release;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#readLatestResources()
     *
     */
    public List<Resource> readLatestResources() {
        final Session session = openSession();
        try {
            final Integer resourcesSize = readMetaDataInteger(session, "LatestRelease.resources-size");
            final List<Resource> resources = new ArrayList<Resource>(resourcesSize);
            for (int i = 0; i < resourcesSize; i++) {
                resources.add(readResource(session, "LatestRelease", i));
            }
            return resources;
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.MigratorIOHandler#readProduct()
     *
     */
    public Product readProduct() {
        final Session session = openSession();
        try {
            return readProduct(session);
        } finally {
            session.close();
        }
    }

    /**
     * Add a resource.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param releaseKey
     *            A release key <code>String</code>.
     * @param resources
     *            A <code>List</code> of <code>Resource</code>s.
     * @param index
     *            An <code>Integer</code> index within the list.
     */
    private void addResource(final Session session, final String releaseKey,
            final List<Resource> resources, final Integer index) {
        final Resource resource = resources.get(index);
        createMetaDataString(session, getKey(releaseKey, index, "checksum"), resource.getChecksum());
        createMetaDataString(session, getKey(releaseKey, index, "name"), resource.getName());
        createMetaDataString(session, getKey(releaseKey, index, "path"), resource.getPath());
        createMetaDataString(session, getKey(releaseKey, index, "version"), resource.getVersion());
        createMetaDataOs(session, getKey(releaseKey, index, "os"), resource.getOs());
        createMetaDataLong(session, getKey(releaseKey, index, "size"), resource.getSize());
    }

    /**
     * Create a meta data reference for the migrator.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param metaDataId
     *            A meta data id <code>Long</code>.
     */
    private void createMetaData(final Session session, final Long metaDataId) {
        session.prepareStatement(SQL_CREATE_META_DATA);
        session.setLong(1, metaDataId);
        if (1 != session.executeUpdate())
            throw new HypersonicException("Could not create migrator meta data.");
    }

    /**
     * Create a meta data reference for a calendar
     * 
     * @param session
     *            A <code>Session</code>.
     * @param key
     *            A meta data key <code>String</code>.
     * @param value
     *            A <code>Calendar</code> value.
     */
    private void createMetaDataCalendar(final Session session, final String key,
            final Calendar value) {
        createMetaData(session, metaDataIO.create(MetaDataType.CALENDAR, key, value));
    }

    /**
     * Create a meta data reference for a string
     * 
     * @param session
     *            A <code>Session</code>.
     * @param key
     *            A meta data key <code>String</code>.
     * @param value
     *            A <code>String</code> value.
     */
    private void createMetaDataInteger(final Session session, final String key,
            final Integer value) {
        createMetaData(session, metaDataIO.create(MetaDataType.INTEGER, key, value));
    }

    /**
     * Create a meta data reference for a string
     * 
     * @param session
     *            A <code>Session</code>.
     * @param key
     *            A meta data key <code>String</code>.
     * @param value
     *            A <code>String</code> value.
     */
    private void createMetaDataLong(final Session session, final String key,
            final Long value) {
        createMetaData(session, metaDataIO.create(MetaDataType.LONG, key, value));
    }

    /**
     * Create a meta data reference for a string
     * 
     * @param session
     *            A <code>Session</code>.
     * @param key
     *            A meta data key <code>String</code>.
     * @param value
     *            A <code>String</code> value.
     */
    private void createMetaDataOs(final Session session, final String key,
            final OS value) {
        createMetaData(session, metaDataIO.create(MetaDataType.STRING, key, value.name()));
    }

    /**
     * Create a meta data reference for a string
     * 
     * @param session
     *            A <code>Session</code>.
     * @param key
     *            A meta data key <code>String</code>.
     * @param value
     *            A <code>String</code> value.
     */
    private void createMetaDataString(final Session session, final String key,
            final String value) {
        createMetaData(session, metaDataIO.create(MetaDataType.STRING, key, value));
    }

    /**
     * Create a meta data reference for a unique id.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param key
     *            A meta data key <code>String</code>.
     * @param value
     *            A <code>String</code> value.
     */
    private void createMetaDataUniqueId(final Session session, final String key,
            final UUID value) {
        createMetaData(session, metaDataIO.create(MetaDataType.UNIQUE_ID, key, value));
    }

    /**
     * Create a meta data reference for a user id.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param key
     *            A meta data key <code>String</code>.
     * @param value
     *            A <code>JabberId</code> value.
     */
    private void createMetaDataUserId(final Session session, final String key,
            final JabberId value) {
        createMetaData(session, metaDataIO.create(MetaDataType.JABBER_ID, key, value));
    }

    /**
     * Delete a tree of meta data items.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param keyPrefix
     *            A key prefix <code>String</code>
     */
    private void deleteMetaDataTree(final Session session,
            final String keyPrefix) {
        final List<Long> metaDataIds = readMetaDataTree(session, keyPrefix);
        session.prepareStatement(SQL_DELETE_META_DATA);
        for (final Long metaDataId : metaDataIds) {
            session.setLong(1, metaDataId);
            if (1 != session.executeUpdate())
                throw new HypersonicException("Could not delete meta data tree item.");

            metaDataIO.delete(metaDataId);
        }
    }

    /**
     * Obtain the key for a release resource.
     * 
     * @param releaseKey
     *            A release key <code>String</code>.
     * @param resourceIndex
     *            A resource index <code>Integer</code>.
     * @param key
     *            A value key <code>String</code>.
     * @return A meta data composite key <code>String</code>.
     */
    private String getKey(final String releaseKey, final Integer resourceIndex,
            final String key) {
        // "InstalledRelease.resource-0.checksum"
        return MessageFormat.format("{0}.resource-{1}.{2}",
                releaseKey, String.valueOf(resourceIndex), key);
    }

    /**
     * Read meta data for a given key.
     * 
     * @param key
     *            A meta data key.
     * @return An instance of <code>MetaData</code>.
     */
    private MetaData readMetaData(final Session session, final String key,
            final MetaDataType type) {
        session.prepareStatement(SQL_READ_META_DATA);
        session.setString(1, key);
        session.setTypeAsInteger(2, type);
        session.executeQuery();
        if (session.nextResult()) {
            return extractMetaData(session, metaDataIO);
        } else {
            return null;
        }
    }

    /**
     * Read a calendar meta data value.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param key
     *            A meta data key <code>String</code>.
     * @return A <code>Calendar</code> value.
     */
    private Calendar readMetaDataCalendar(final Session session, final String key) {
        return (Calendar) readMetaData(session, key, MetaDataType.CALENDAR).getValue();
    }

    /**
     * Read a long meta data value.
     * @param session
     *            A <code>Session</code>.
     * @param key
     *            A meta data key <code>String</code>.
     * @return A <code>Integer</code> value.
     */
    private Integer readMetaDataInteger(final Session session, final String key) {
        return (Integer) readMetaData(session, key, MetaDataType.INTEGER).getValue();
    }

    /**
     * Read a long meta data value.
     * @param session
     *            A <code>Session</code>.
     * @param key
     *            A meta data key <code>String</code>.
     * @return A <code>Long</code> value.
     */
    private Long readMetaDataLong(final Session session, final String key) {
        return (Long) readMetaData(session, key, MetaDataType.LONG).getValue();
    }

    /**
     * Read an operating system meta data value.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param key
     *            A meta data key <code>String</code>.
     * @return An <code>OS</code> value.
     */
    private OS readMetaDataOs(final Session session, final String key) {
        return OS.valueOf((String) readMetaData(session, key, MetaDataType.STRING).getValue());
    }

    /**
     * Read a string meta data value.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param key
     *            A meta data key <code>String</code>.
     * @return A <code>String</code> value.
     */
    private String readMetaDataString(final Session session, final String key) {
        return (String) readMetaData(session, key, MetaDataType.STRING).getValue();
    }

    /**
     * Read a tree of meta data elements.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param keyPrefix
     *            A key prefix <code>String</code>.
     * @return A <code>List</code> of <code>Long</code>s.
     */
    private List<Long> readMetaDataTree(final Session session,
            final String keyPrefix) {
        session.prepareStatement(SQL_READ_META_DATA_TREE);
        session.setString(1, new StringBuffer(keyPrefix).append("%").toString());
        session.executeQuery();
        final List<Long> metaDataIds = new ArrayList<Long>();
        while (session.nextResult()) {
            metaDataIds.add(session.getLong("META_DATA_ID"));
        }
        return metaDataIds;
    }

    /**
     * Read a unique id meta data value.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param key
     *            A meta data key <code>String</code>.
     * @return A <code>UUID</code> value.
     */
    private UUID readMetaDataUniqueId(final Session session, final String key) {
        return (UUID) readMetaData(session, key, MetaDataType.UNIQUE_ID).getValue();
    }

    /**
     * Read a jabber id meta data value.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param key
     *            A meta data key <code>String</code>.
     * @return A <code>JabberId</code> value.
     */
    private JabberId readMetaDataUserId(final Session session, final String key) {
        return (JabberId) readMetaData(session, key, MetaDataType.JABBER_ID).getValue();
    }

    /**
     * Read the product.
     * 
     * @param session
     *            A <code>Session</code>.
     * @return A <code>Product</code>.
     */
    private Product readProduct(final Session session) {
        final Product product = new Product();
        product.setCreatedBy(readMetaDataUserId(session, "Product.createdBy"));
        product.setCreatedOn(readMetaDataCalendar(session, "Product.createdOn"));
        product.setName(readMetaDataString(session, "Product.name"));
        product.setUniqueId(readMetaDataUniqueId(session, "Product.uniqueId"));
        product.setUpdatedBy(readMetaDataUserId(session, "Product.updatedBy"));
        product.setUpdatedOn(readMetaDataCalendar(session, "Product.updatedOn"));
        return product;
    }

    /**
     * Add a resource.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param releaseKey
     *            A release key <code>String</code>.
     * @param index
     *            An <code>Integer</code> index within the list.
     */
    private Resource readResource(final Session session,
            final String releaseKey, final Integer index) {
        final Resource resource = new Resource();
        resource.setChecksum(readMetaDataString(session, getKey(releaseKey, index, "checksum")));
        resource.setName(readMetaDataString(session, getKey(releaseKey, index, "name")));
        resource.setPath(readMetaDataString(session, getKey(releaseKey, index, "path")));
        resource.setVersion(readMetaDataString(session, getKey(releaseKey, index, "version")));
        resource.setOs(readMetaDataOs(session, getKey(releaseKey, index, "os")));
        resource.setSize(readMetaDataLong(session, getKey(releaseKey, index, "size")));
        return resource;
    }
}
