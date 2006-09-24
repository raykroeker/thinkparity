/*
 * Created On: Sep 23, 2006 11:59:32 AM
 */
package com.thinkparity.ophelia.browser.platform.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;

import org.apache.log4j.Logger;

import com.thinkparity.codebase.StackUtil;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class PluginUtility {

    private final Logger logger;

    protected PluginUtility() {
        super();
        this.logger = Logger.getLogger(getClass());
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
            final Object errorId = getErrorId(t);
            logger.error(errorId, t);
            return PluginException.translate(errorId.toString(), t);
        }
    }

    /**
     * Obtain the error id for an error.
     * 
     * @param t
     *            An error <code>Throwable</code>.
     * @return An error id.s
     */
    private Object getErrorId(final Throwable t) {
        return MessageFormat.format("{0} {1} {2} {3}",
                StackUtil.getFrameClassName(2),
                StackUtil.getFrameMethodName(2),
                StackUtil.getFrameFileName(2),
                StackUtil.getFrameLineNumber(2),
                t.getLocalizedMessage());
    }
}
