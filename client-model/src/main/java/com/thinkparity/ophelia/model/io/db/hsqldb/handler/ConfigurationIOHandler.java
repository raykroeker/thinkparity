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
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ConfigurationIOHandler extends AbstractIOHandler implements
		com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler {

    /** Sql to create a configuration entry. */
    private static final String SQL_CREATE =
            new StringBuffer("insert into CONFIGURATION ")
            .append("(CONFIGURATION_KEY,META_DATA_ID) ")
            .append("values (?,?)")
            .toString();

    /** Sql to delete a configuration entry. */
    private static final String SQL_DELETE =
            new StringBuffer("insert into CONFIGURATION ")
            .append("values (CONFIGURATION_KEY,META_DATA_ID) ")
            .append("(?,?)")
            .toString();

    /** Sql to read a configuration entry. */
    private static final String SQL_READ =
            new StringBuffer("select C.CONFIGURATION_KEY,MD.META_DATA_ID,")
            .append("MD.META_DATA_TYPE_ID,MD.META_DATA_KEY,MD.META_DATA_VALUE ")
            .append("from CONFIGURATION C ")
            .append("inner join META_DATA MD on C.META_DATA_ID=MD.META_DATA_ID ")
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
     * @see com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler#create(java.lang.String, java.lang.String)
     */
    public void create(final String key, final String value) throws HypersonicException {
        final Session session = openSession();
        try {
            final Long metaDataId = createMetaData(session, key, value);

            session.prepareStatement(SQL_CREATE);
            session.setString(1, key);
            session.setLong(2, metaDataId);
            if(1 != session.executeUpdate())
                throw new HypersonicException("Could not create configuration entry.");
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler#delete(java.lang.String)
     */
    public void delete(final String key) throws HypersonicException {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ);
            session.setString(1, key);
            session.executeQuery();
            if(session.nextResult()) {
                final MetaData metaData = extractMetaData(session, metaDataIO);
                metaDataIO.delete(session, metaData.getId());

                session.prepareStatement(SQL_DELETE);
                session.setString(1, key);
                if(1 != session.executeUpdate())
                    throw new HypersonicException("Could not delete configuration entry.");
            }
            else {
                throw new HypersonicException("Could not find configuration entry.");
            }
        } finally {
            session.close();
        }
    }

    /**
     * @see com.thinkparity.ophelia.model.io.handler.ConfigurationIOHandler#read(java.lang.String)
     */
    public String read(final String key) throws HypersonicException {
        final Session session = openSession();
        try {
            session.prepareStatement(SQL_READ);
            session.setString(1, key);
            session.executeQuery();
            if(session.nextResult()) { return extractMetaDataValue(session); }
            else { return null; }
        }
        finally { session.close(); }
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
     * Create the configuration meta data entry.
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
            final String key, final String value) {
        metaDataIO.update(session, metaDataId, MetaDataType.STRING, key, value);
    }
}
