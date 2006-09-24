/*
 * Created On: Sep 24, 2006 11:24:06 AM
 */
package com.thinkparity.ophelia.browser.application.browser;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.log4j.Log4JHelper;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class BrowserHelper {

    /** The thinkParity <code>Browser</code>. */
    protected final Browser browserApplication;

    /** An apache logger. */
    private final Logger logger;

    /**
     * Create BrowserHelper.
     * 
     * @param browserApplication
     *            The thinkParity <code>Browser</code>.
     */
    BrowserHelper(final Browser browserApplication) {
        super();
        this.browserApplication = browserApplication;
        this.logger = Logger.getLogger(getClass());
    }

    /**
     * Log an api id.
     *
     */
    protected void logApiId() {
        if (logger.isInfoEnabled()) {
            logger.info(Log4JHelper.renderAndFormat(logger, "{0}.{1}({2}:{3})",
                    StackUtil.getCallerClassName(),
                    StackUtil.getCallerMethodName(),
                    StackUtil.getCallerFileName(),
                    StackUtil.getCallerLineNumber()));
        }
    }

    /**
     * Log a error.
     * 
     * @param errorPattern
     *            A error pattern.
     * @param errorArguments
     *            Warning error arguments.
     */
    protected void logError(final String errorPattern,
            final Object... errorArguments) {
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(Log4JHelper.renderAndFormat(logger, errorPattern,
                    errorArguments));
        }
    }

    /**
     * Log an error.
     * 
     * @param t
     *            An error.
     * @param errorPattern
     *            An error pattern.
     * @param errorArguments
     *            Error pattern arguments.
     */
    protected void logError(final Throwable t, final String errorPattern,
            final Object... errorArguments) {
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(Log4JHelper.renderAndFormat(logger, errorPattern,
                    errorArguments), t);
        }
    }

    /**
     * Log a fatality.
     * 
     * @param fatalPattern
     *            A fatal pattern.
     * @param fatalArguments
     *            Fatal pattern arguments.
     */
    protected void logFatal(final String fatalPattern,
            final Object... fatalArguments) {
        if (logger.isEnabledFor(Level.FATAL)) {
            logger.fatal(Log4JHelper.renderAndFormat(logger, fatalPattern,
                    fatalArguments));
        }
    }

    /**
     * Log a fatality.
     * 
     * @param t
     *            An error.
     * @param fatalPattern
     *            A fatal pattern.
     * @param fatalArguments
     *            Fatal pattern arguments.
     */
    protected void logFatal(final Throwable t, final String fatalPattern,
            final Object... fatalArguments) {
        if (logger.isEnabledFor(Level.FATAL)) {
            logger.fatal(Log4JHelper.renderAndFormat(logger, fatalPattern,
                    fatalArguments), t);
        }
    }

    /**
     * Log a variable.
     * 
     * @param <V>
     *            The variable type.
     * @param name
     *            The variable name.
     * @param value
     *            The variable.
     * @return The variable.
     */
    protected <V> V logVariable(final String name, final V value) {
        if (logger.isDebugEnabled()) {
            logger.debug(Log4JHelper.renderAndFormat(logger, "{0}:{1}",
                    name, value));
        }
        return value;
    }

    /**
     * Log a warning.
     * 
     * @param warningPattern
     *            A warning pattern.
     * @param warningArguments
     *            Warning pattern arguments.
     */
    protected void logWarning(final String warningPattern,
            final Object... warningArguments) {
        if (logger.isEnabledFor(Level.WARN)) {
            logger.warn(Log4JHelper.renderAndFormat(logger, warningPattern,
                    warningArguments));
        }
    }

    /**
     * Log a warning.
     * 
     * @param t
     *            A warning error.
     * @param warningPattern
     *            A warning pattern.
     * @param warningArguments
     *            Warning pattern arguments.
     */
    protected void logWarning(final Throwable t, final String warningPattern,
            final Object... warningArguments) {
        if (logger.isEnabledFor(Level.WARN)) {
            logger.warn(Log4JHelper.renderAndFormat(logger, warningPattern,
                    warningArguments), t);
        }
    }
}
