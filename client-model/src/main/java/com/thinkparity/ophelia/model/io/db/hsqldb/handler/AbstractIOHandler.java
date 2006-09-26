/*
 * Feb 8, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.model.io.db.hsqldb.Session;
import com.thinkparity.ophelia.model.io.db.hsqldb.SessionManager;
import com.thinkparity.ophelia.model.io.md.MetaData;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractIOHandler {

	/** An apache logger. */
	protected final Log4JWrapper logger;

    /**
     * The configuration io. Since the IO handler is an abstract io handler; it is
     * obtained using a lazy create pattern.
     * 
     * @see #getConfigurationIO()
     */
    private ConfigurationIOHandler configurationIO;

    /** A hypersonic <code>SessionManager</code>. */
    private final SessionManager sessionManager;

	/**
     * The meta data io. Since the IO handler is an abstract io handler; it is
     * obtained using a lazy create pattern.
     * 
     * @see #getMetaDataIO()
     */
    private MetaDataIOHandler metaDataIO;

    /**
     * Create AbstractIOHandler.
     * 
     * @param sessionManager
     *            A hypersonic <code>SessionManager</code>.
     */
	protected AbstractIOHandler(final SessionManager sessionManager) {
		super();
		this.logger = new Log4JWrapper();
        this.sessionManager = sessionManager;
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
            configurationIO = new ConfigurationIOHandler(sessionManager);
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
            metaDataIO = new MetaDataIOHandler(sessionManager);
        }
        return metaDataIO;
    }

    /**
	 * Open a database session.
	 * 
	 * @return The database session.
	 */
	protected Session openSession() {
        return sessionManager.openSession(StackUtil.getCaller());
	}
}
