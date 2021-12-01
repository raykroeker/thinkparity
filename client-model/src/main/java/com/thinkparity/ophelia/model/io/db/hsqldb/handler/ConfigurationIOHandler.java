/*
 * Feb 21, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;


import javax.sql.DataSource;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.md.MetaData;
import com.thinkparity.ophelia.model.io.md.MetaDataType;

/**
 * @author raymond@raykroeker.com
 * @version 1.1
 */
public class ConfigurationIOHandler extends AbstractIOHandler implements
		com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler {

    /** Sql to create a configuration entry. */
    private static final String SQL_CREATE =
        new StringBuilder("insert into CONFIGURATION ")
        .append("(CONFIGURATION_KEY,META_DATA_ID) ")
        .append("values (?,?)")
        .toString();

    /** Sql to delete a configuration entry. */
    private static final String SQL_DELETE =
        new StringBuilder("delete from CONFIGURATION ")
        .append("where CONFIGURATION_KEY=?")
        .toString();

    /** Sql to read a configuration entry. */
    private static final String SQL_READ =
        new StringBuffer("select C.CONFIGURATION_KEY,MD.META_DATA_ID,")
        .append("MD.META_DATA_TYPE_ID,MD.META_DATA_KEY,MD.META_DATA_VALUE ")
        .append("from CONFIGURATION C ")
        .append("inner join META_DATA MD on C.META_DATA_ID=MD.META_DATA_ID ")
        .append("where C.CONFIGURATION_KEY=?")
        .toString();

    /** Sql to read a key count. */
    private static final String SQL_READ_KEY_COUNT =
        new StringBuilder("select count(CONFIGURATION_KEY) \"KEY_COUNT\" ")
        .append("from CONFIGURATION C ")
        .append("where C.CONFIGURATION_KEY=?")
        .toString();

    /** The meta data io interface. */
    private final MetaDataIOHandler metaDataIO;

    /**
     * Create ConfigurationIOHandler.
     * 
     * @param dataSource
     *            An sql <code>DataSource</code>.
     */
    public ConfigurationIOHandler(final DataSource dataSource) {
        super(dataSource);
        this.metaDataIO = new MetaDataIOHandler(dataSource);
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler#create(java.lang.String, java.lang.Integer)
     *
     */
    @Override
    public void create(final String key, final Integer value) {
        final Session session = openSession();
        try {
            create(session, createMetaData(session, key, value), key);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler#create(java.lang.String, java.lang.String)
     * 
     */
    @Override
    public void create(final String key, final String value) throws HypersonicException {
        final Session session = openSession();
        try {
            create(session, createMetaData(session, key, value), key);
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler#delete(java.lang.String)
     * 
     */
    @Override
    public void delete(final String key) throws HypersonicException {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ);
            session.setString(1, key);
            session.executeQuery();
            if (session.nextResult()) {
                final MetaData metaData = extractMetaData(session, metaDataIO);

                session.prepareStatement(SQL_DELETE);
                session.setString(1, key);
                if(1 != session.executeUpdate())
                    throw new HypersonicException("Could not delete configuration entry.");

                metaDataIO.delete(session, metaData.getId());
            }
            else {
                throw new HypersonicException("Could not find configuration entry.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler#isSet(java.lang.String)
     *
     */
    @Override
    public Boolean isSet(final String key) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ_KEY_COUNT);
            session.setString(1, key);
            session.executeQuery();
            if (session.nextResult()) {
                final int keyCount = session.getInteger("KEY_COUNT");
                if (0 == keyCount) {
                    return Boolean.FALSE;
                } else if (1 == keyCount) {
                    return Boolean.TRUE;
                } else {
                    throw new HypersonicException("Could not determine key count.");
                }
            } else {
                throw new HypersonicException("Could not determine key count.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler#read(java.lang.String)
     * 
     */
    @Override
    public String read(final String key) throws HypersonicException {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ);
            session.setString(1, key);
            session.executeQuery();
            if (session.nextResult()) {
                return extractMetaDataValue(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler#readInteger(java.lang.String)
     *
     */
    @Override
    public Integer readInteger(final String key) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ);
            session.setString(1, key);
            session.executeQuery();
            if (session.nextResult()) {
                return extractMetaDataIntegerValue(session);
            } else {
                return null;
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler#update(java.lang.String, java.lang.Integer)
     *
     */
    @Override
    public void update(final String key, final Integer value) {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ);
            session.setString(1, key);
            session.executeQuery();
            if (session.nextResult()) {
                final MetaData metaData = extractMetaData(session, metaDataIO);
                updateMetaData(session, metaData.getId(), key, value);
            } else {
                throw new HypersonicException("[CANNOT FIND CONFIGURATION]");
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler#update(java.lang.String, java.lang.String)
     */
    public void update(final String key, final String value) throws HypersonicException {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ);
            session.setString(1, key);
            session.executeQuery();
            if (session.nextResult()) {
                final MetaData metaData = extractMetaData(session, metaDataIO);
                updateMetaData(session, metaData.getId(), key, value);
            } else {
                throw new HypersonicException("[CANNOT FIND CONFIGURATION]");
            }
        } finally {
            session.close();
        }
    }

    /**
     * Create the configuration link to the meta data entry.
     * 
     * @param session
     *            A <code>Session</code>.
     * @param id
     *            A <code>Long</code>.
     * @param key
     *            A <code>String</code>.
     */
    private void create(final Session session, final Long id, final String key) {
        session.prepareStatement(SQL_CREATE);
        session.setString(1, key);
        session.setLong(2, id);
        if (1 != session.executeUpdate()) {
            throw new HypersonicException("Could not create configuration entry.");
        }
    }

    /**
     * Create an integer configuration meta data entry.
     * 
     * @param session
     *            The database session.
     * @param key
     *            The meta data key.
     * @param value
     *            The meta data value.
     * @return The meta data id.
     */
    private Long createMetaData(final Session session, final String key,
            final Integer value) {
        return metaDataIO.create(session, MetaDataType.INTEGER, key, value);
    }

    /**
     * Create a string configuration meta data entry.
     * 
     * @param session
     *            The database session.
     * @param key
     *            The meta data key.
     * @param value
     *            The meta data value.
     * @return The meta data id.
     */
    private Long createMetaData(final Session session, final String key,
            final String value) {
        return metaDataIO.create(session, MetaDataType.STRING, key, value);
    }

    /**
     * Extract the meta data value from the database session.
     * 
     * @param session
     *            The database session.
     * @return The configuration value.
     */
    private Integer extractMetaDataIntegerValue(final Session session) {
        return (Integer) extractMetaData(session, metaDataIO).getValue();
    }

    /**
     * Extract the meta data value from the database session.
     * 
     * @param session
     *            The database session.
     * @return The configuration value.
     */
    private String extractMetaDataValue(final Session session) {
        return (String) extractMetaData(session, metaDataIO).getValue();
    }

    /**
     * Update the configuration meta data.
     * 
     * @param session
     *            The database session.
     * @param metaDataId
     *            The meta data id.
     * @param key
     *            The meta data key.
     * @param value
     *            The meta data value.
     */
    private void updateMetaData(final Session session, final Long metaDataId,
            final String key, final Integer value) {
        metaDataIO.update(session, metaDataId, MetaDataType.INTEGER, key, value);
    }

    /**
     * Update the configuration meta data.
     * 
     * @param session
     *            The database session.
     * @param metaDataId
     *            The meta data id.
     * @param key
     *            The meta data key.
     * @param value
     *            The meta data value.
     */
    private void updateMetaData(final Session session, final Long metaDataId,
            final String key, final String value) {
        metaDataIO.update(session, metaDataId, MetaDataType.STRING, key, value);
    }
}
