/*
 * Created On: Sep 26, 2006 1:33:02 PM
 */
package com.thinkparity.codebase.log4j;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.thinkparity.codebase.StackUtil;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Log4JWrapper {

    /** An apache logger. */
    private final Logger logger;

    /**
     * Create Log4JWrapper.  This will create a logger for the instantiator
     * of this class.
     * 
     */
    public Log4JWrapper() {
        this(Logger.getLogger(StackUtil.getCallerClassName()));
    }

    /**
     * Create Log4JWrapper.
     * 
     * @param clasz
     *            The class to use when creating the logger.
     */
    public Log4JWrapper(final Class clasz) {
        this(Logger.getLogger(clasz));
    }

    /**
     * Create Log4JWrapper.
     * 
     * @param logger
     *            An apache <code>Logger</code>.
     */
    public Log4JWrapper(final Logger logger) {
        super();
        this.logger = logger;
    }

    /**
     * Log an api id. (A stack-trace format of the caller's caller).
     *
     */
    public final void logApiId() {
        if (logger.isInfoEnabled()) {
            // get out of the logApiId hierarchy
            int index = 1;
            StackTraceElement element = StackUtil.getFrame(index);
            while (null != element && "logApiId".equals(element.getMethodName())) {
                element = StackUtil.getFrame(++index);
            }
            if (null == element) {
                logger.info(Log4JHelper.renderAndFormat(logger, "No stack available."));
            } else {
                logger.info(Log4JHelper.renderAndFormat(logger, "{0}.{1}({2}:{3})",
                        element.getClassName(), element.getMethodName(),
                        element.getFileName(), element.getLineNumber()));
            }
        }
    }

    /**
     * Log a debug.
     * 
     * @param debugPattern
     *            A debug pattern.
     * @param debugArguments
     *            Debug arguments.
     */
    public void logDebug(final String debugPattern,
            final Object... debugArguments) {
        if (logger.isDebugEnabled()) {
            logger.debug(Log4JHelper.renderAndFormat(logger, debugPattern,
                    debugArguments));
        }
    }

    /**
     * Log an error.
     * 
     * @param errorPattern
     *            The error format pattern.
     * @param errorArguments
     *            The error format arguments.
     */
    public final void logError(final String errorPattern,
            final Object... errorArguments) {
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(Log4JHelper.renderAndFormat(logger, errorPattern,
                    errorArguments));
        }
    }

    /**
     * Log an error.
     * 
     * @param <T>
     *            The error type.
     * @param t
     *            The error.
     * @param errorPattern
     *            The error format pattern.
     * @param errorArguments
     *            The error format arguments.
     * @return The error.
     */
    public <T extends Throwable> T logError(final T t,
            final String errorPattern, final Object... errorArguments) {
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(Log4JHelper.renderAndFormat(logger, errorPattern,
                    errorArguments), t);
        }
        return t;
    }

    /**
     * Log a fatality.
     * 
     * @param <F>
     *            A fatal error type.
     * @param f
     *            An fatal error.
     * @param fatalPattern
     *            A fatal pattern.
     * @param fatalArguments
     *            Fatal pattern arguments.
     * @param The
     *            fatal error.
     */
    public <F extends Throwable> F logFatal(final F f,
            final String fatalPattern, final Object... fatalArguments) {
        if (logger.isEnabledFor(Level.FATAL)) {
            logger.fatal(Log4JHelper.renderAndFormat(logger, fatalPattern,
                    fatalArguments), f);
        }
        return f;
    }

    /**
     * Log a fatality.
     * 
     * @param fatalPattern
     *            A fatal pattern.
     * @param fatalArguments
     *            Fatal pattern arguments.
     */
    public void logFatal(final String fatalPattern,
            final Object... fatalArguments) {
        if (logger.isEnabledFor(Level.FATAL)) {
            logger.fatal(Log4JHelper.renderAndFormat(logger, fatalPattern,
                    fatalArguments));
        }
    }

    /**
     * Log an informational statement.
     * 
     * @param infoPattern
     *            An info statement pattern.
     * @param infoArguments
     *            Info statement arguments.
     */
    public final void logInfo(final String infoPattern,
            final Object... infoArguments) {
        if (logger.isInfoEnabled()) {
            logger.info(Log4JHelper.renderAndFormat(logger, infoPattern,
                    infoArguments));
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
    public <V> V logVariable(final String name, final V value) {
        if (logger.isDebugEnabled()) {
            logger.debug(Log4JHelper.renderAndFormat(logger, "{0}:{1}", name,
                    value));
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
    public void logWarning(final String warningPattern,
            final Object... warningArguments) {
        if (logger.isEnabledFor(Level.WARN)) {
            logger.warn(Log4JHelper.renderAndFormat(logger, warningPattern,
                    warningArguments));
        }
    }

    /**
     * Log a warning.
     * 
     * @param <W>
     *            A warning error type.
     * @param w
     *            A warning error.
     * @param warningPattern
     *            A warning pattern.
     * @param warningArguments
     *            Warning pattern arguments.
     */
    public <W extends Throwable> W logWarning(final W w,
            final String warningPattern, final Object... warningArguments) {
        if (logger.isEnabledFor(Level.WARN)) {
            logger.warn(Log4JHelper.renderAndFormat(logger, warningPattern,
                    warningArguments), w);
        }
        return w;
    }
}
