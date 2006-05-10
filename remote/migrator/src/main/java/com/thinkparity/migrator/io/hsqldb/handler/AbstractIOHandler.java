/*
 * Feb 8, 2006
 */
package com.thinkparity.migrator.io.hsqldb.handler;

import org.apache.log4j.Logger;

import com.thinkparity.migrator.LoggerFactory;
import com.thinkparity.migrator.io.hsqldb.HypersonicSession;
import com.thinkparity.migrator.io.hsqldb.HypersonicSessionManager;
import com.thinkparity.migrator.io.md.MetaData;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public abstract class AbstractIOHandler {

	/**
	 * An apache logger.
	 * 
	 */
	protected final Logger logger;

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
	protected MetaData extractMetaData(final HypersonicSession session) {
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
	protected HypersonicSession openSession() { return HypersonicSessionManager.openSession(); }
}
