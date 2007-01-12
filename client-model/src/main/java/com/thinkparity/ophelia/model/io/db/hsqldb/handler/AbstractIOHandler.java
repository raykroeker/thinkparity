/*
 * Feb 8, 2006
 */
package com.thinkparity.ophelia.model.io.db.hsqldb.handler;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.model.io.db.hsqldb.HypersonicException;
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

    /**
     * The meta data io. Since the IO handler is an abstract io handler; it is
     * obtained using a lazy create pattern.
     * 
     * @see #getMetaDataIO()
     */
    private MetaDataIOHandler metaDataIO;

	/** A thinkParity hypersonic <code>SessionManager</code>. */
    private final SessionManager sessionManager;

    /**
     * Create AbstractIOHandler.
     * 
     */
    protected AbstractIOHandler() {
        super();
        this.logger = new Log4JWrapper();
        this.sessionManager = new SessionManager();
    }

    protected void checkpoint() {
        final Session session = openSession();
        try {
            session.execute("CHECKPOINT");
        } finally {
            session.close();
        }
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
	protected Session openSession() {
        return sessionManager.openSession(StackUtil.getCaller());
	}

    protected HypersonicException translateError(final Session session, final Throwable t) {
        session.rollback();
        if (t.getClass().isAssignableFrom(HypersonicException.class)) {
            return (HypersonicException) t;
        } else {
            return new HypersonicException(t);
        }
    }

    /**
     * 
     * @param errorId
     * @return
     */
    protected HypersonicException translateError(final String errorPattern,
            final Object... errorArguments) {
        final String errorId = logger.getErrorId(errorPattern, errorArguments);
        return HypersonicException.translate(errorId);
    }

    /**
     * Translate an error into a hypersonic error.
     * 
     * @param t
     *            An error.
     * @return A hypersonic error.
     */
    protected HypersonicException translateError(final Throwable t) {
        if (HypersonicException.class.isAssignableFrom(t.getClass())) {
            return (HypersonicException) t;
        } else {
            final String errorId = new ErrorHelper().getErrorId(t);
            return HypersonicException.translate(errorId, t);
        }
    }
}
