/*
 * Created On: Aug 12, 2006 12:55:35 PM
 */
package com.thinkparity.browser.platform;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.thinkparity.codebase.log4j.Log4JHelper;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public abstract class AbstractFactory {

    /** An apache logger. */
    private final Logger logger;

    /** Create AbstractFactory. */
    protected AbstractFactory() {
        super();
        this.logger = Logger.getLogger(getClass());
    }

    /**
     * Log a warning.
     * 
     * @param pattern
     *            A message format pattern.
     * @param arguments
     *            A list of message arguments.
     */
    protected void logWarning(final Object message) {
        if(Level.WARN.isGreaterOrEqual(logger.getEffectiveLevel())) {
            logger.warn(Log4JHelper.render(logger, message));
        }
    }
}
