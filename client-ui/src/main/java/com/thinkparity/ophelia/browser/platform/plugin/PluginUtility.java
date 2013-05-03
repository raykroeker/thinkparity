/*
 * Created On: Sep 23, 2006 11:59:32 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.io.IOException;
import java.io.InputStream;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.log4j.Log4JWrapper;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class PluginUtility {

    /** An apache logger. */
    protected final Log4JWrapper logger;

    /** Create PluginUtility. */
    protected PluginUtility() {
        super();
        this.logger = new Log4JWrapper();
    }

    /**
     * Read the meta info from the input stream.
     * 
     * @param inputStream
     *            An input stream.
     * @return The <code>PluginMetaInfo</code>.
     * @throws IOException
     */
    protected PluginMetaInfo readMetaInfo(final InputStream inputStream)
            throws IOException {
        final PluginMetaInfo metaInfo = new PluginMetaInfo();
        metaInfo.readMetaInfo(inputStream);
        return metaInfo;
    }

    /**
     * Translate an error into a parity unchecked error.
     * 
     * @param t
     *            An error.
     */
    protected PluginException translateError(final Throwable t) {
        if (PluginException.class.isAssignableFrom(t.getClass())) {
            return (PluginException) t;
        } else {
            final String errorId = new ErrorHelper().getErrorId(t);
            logger.logError(t, errorId);
            return PluginException.translate(errorId, t);
        }
    }
}
