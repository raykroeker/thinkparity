/*
 * Feb 8, 2006
 */
package com.thinkparity.model.parity.model.io.db.hsqldb.handler;

import org.apache.log4j.Logger;

import com.thinkparity.model.LoggerFactory;
import com.thinkparity.model.parity.model.io.db.hsqldb.Session;
import com.thinkparity.model.parity.model.io.db.hsqldb.SessionManager;
import com.thinkparity.model.parity.model.io.md.MetaData;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
abstract class AbstractIOHandler {

	/**
     * A log id.
     * 
     * @param io
     *            The io category.
     * @return The io log id.
     */
    protected static StringBuffer getIOId(final String io) {
        return new StringBuffer("[LMODEL] [IO] ").append(io);
    }

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

    /**
     * The configuration io. Since the IO handler is an abstract io handler; it is
     * obtained using a lazy create pattern.
     * 
     * @see #getConfigurationIO()
     */
    private ConfigurationIOHandler configurationIO;

    /**
     * The meta data io. Since the IO handler is an abstract io handler; it is
     * obtained using a lazy create pattern.
     * 
     * @see #getMetaDataIO()
     */
    private MetaDataIOHandler metaDataIO;

	/**
	 * Create a AbstractIOHandler.
	 * 
	 */
	protected AbstractIOHandler() {
		super();
		this.logger = LoggerFactory.getLogger(getClass());
	}

	/**
	 * Extract the meta data from the session.
	 * 
	 * @param session
	 *            The database session.
	 * @return The meta data object.
	 */
	protected MetaData extractMetaData(final Session session) {
		final MetaData metaData = new MetaData();
		metaData.setId(session.getLong("META_DATA_ID"));
		metaData.setKey(session.getString("KEY"));
		metaData.setType(session.getMetaDataTypeFromInteger("META_DATA_TYPE_ID"));
		metaData.setValue(getMetaDataIO().extractValue(session, metaData.getType(), "VALUE"));
		return metaData;
	}

    /**
     * Obtain the meta data io handler.
     * 
     * @return The meta data io handler.
     */
    protected ConfigurationIOHandler getConfigurationIO() {
        if(null == configurationIO) {
            configurationIO = new ConfigurationIOHandler();
        }
        return configurationIO;
    }

    /**
     * Obtain the meta data io handler.
     * 
     * @return The meta data io handler.
     */
    protected MetaDataIOHandler getMetaDataIO() {
        if(null == metaDataIO) {
            metaDataIO = new MetaDataIOHandler();
        }
        return metaDataIO;
    }

    /**
	 * Open a database session.
	 * 
	 * @return The database session.
	 */
	protected Session openSession() { return SessionManager.openSession(); }
}
