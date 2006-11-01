/*
 * Created On: Sep 26, 2006 1:33:02 PM
 */
package com.thinkparity.codebase.log4j;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.ErrorHelper;
import com.thinkparity.codebase.StackUtil;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class Log4JWrapper {

    /** A default stack filter that will remove this log4j wrapper. */
    private static final StackUtil.Filter LOG4J_STACK_FILTER;

    static {
        LOG4J_STACK_FILTER = new StackUtil.Filter() {
            public Boolean accept(final StackTraceElement stackElement) {
                return !stackElement.getClassName().equals(
                        Log4JWrapper.class.getName()) &&
                        !stackElement.getMethodName().equals("logApiId");
            }
        };
    }

    /** An apache logger. */
    private final Logger logger;

    /**
     * Create Log4JWrapper.  This will create a logger for the instantiator
     * of this class.
     * 
     * @see StackUtil#getCallerClassName()
     * @see Logger#getLogger(String)
     */
    public Log4JWrapper() {
        this(Logger.getLogger(StackUtil.getCallerClassName()));
    }

    /**
     * Create Log4JWrapper.
     * 
     * @param clasz
     *            The class to use when creating the logger.
     * @see Logger#getLogger(Class)
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
     * Create Log4JWrapper.
     * 
     * @param name
     *            A logger name.
     * 
     * @see Logger#getLogger(String)
     */
    public Log4JWrapper(final String name) {
        this(Logger.getLogger(name));
    }

    public final String getErrorId(final String errorPattern,
            final Object... errorArguments) {
        return new ErrorHelper().getErrorId(logger, errorPattern, errorArguments);
    }

    /**
     * Obtain the apache logger.
     * 
     * @return An apache logger.
     */
    public Logger getLogger() {
        return logger;
    }

    /**
     * Log an api id. (A stack-trace format of the caller's caller).
     *
     */
    public final void logApiId() {
        if (logger.isInfoEnabled()) {
            logStackId(Level.INFO, "{0}.{1}({2}:{3})", StackUtil.getFrame(LOG4J_STACK_FILTER));
        }
    }

    /**
     * Log a filtered api id. (A stack-trace format of the caller's caller).
     * 
     * @param filter
     *            A stack util filter used to remove certain elements of the
     *            stack.
     */
    public final void logApiId(final StackUtil.Filter filter) {
        if (logger.isInfoEnabled()) {
            final List<StackUtil.Filter> filters = new ArrayList<StackUtil.Filter>();
            filters.add(LOG4J_STACK_FILTER);
            filters.add(filter);
            logStackId(Level.INFO, "{0}.{1}({2}:{3}) (filtered)", StackUtil.getFrame(filters));
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
    public final void logDebug(final String debugPattern,
            final Object... debugArguments) {
        if (logger.isDebugEnabled()) {
            logger.debug(Log4JHelper.renderAndFormat(logger, debugPattern,
                    debugArguments));
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
    public final <E extends Throwable> E logError(final E e,
            final String errorPattern, final Object... errorArguments) {
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(Log4JHelper.renderAndFormat(logger, errorPattern,
                    errorArguments), e);
        }
        return e;
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
    public final <F extends Throwable> F logFatal(final F f,
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
    public final void logFatal(final String fatalPattern,
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

    public final void logTrace(final String tracePattern,
            final Object... traceArguments) {
        if (logger.isTraceEnabled()) {
            logger.trace(Log4JHelper.renderAndFormat(logger, tracePattern,
                    traceArguments));
        }
    }

    /**
     * Log a trace id. (A stack-trace format of the caller's caller).
     * 
     */
    public final void logTraceId() {
        logTraceId(1);
    }

    /**
     * Log a trace id. (A stack-trace format of the caller's caller).
     * 
     */
    public final void logTraceId(final Integer frameCount) {
        if (logger.isTraceEnabled()) {
            final StackTraceElement[] frames = StackUtil.getFrames(LOG4J_STACK_FILTER);
            for (int i = 0; i < frameCount && i < frames.length; i++) {
                if (0 == i)
                    logStackId(Level.TRACE, "{0}.{1}({2}:{3})", frames[i]);
                else
                    logStackId(Level.TRACE, "{0} - {1}.{2}({3}:{4})", i, frames[i]);
            }
        }
    }

    /**
     * Log a filtered trace id. (A stack-trace format of the caller's caller).
     * 
     * @param filter
     *            A stack util filter used to remove certain elements of the
     *            stack.
     */
    public final void logTraceId(final StackUtil.Filter filter) {
        if (logger.isTraceEnabled()) {
            final List<StackUtil.Filter> filters = new ArrayList<StackUtil.Filter>();
            filters.add(LOG4J_STACK_FILTER);
            filters.add(filter);
            logStackId(Level.TRACE, "{0}.{1}({2}:{3}) (filtered)", StackUtil.getFrame(LOG4J_STACK_FILTER));
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
    public final <V> V logVariable(final String name, final V value) {
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
    public final void logWarning(final String warningPattern,
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
    public final <W extends Throwable> W logWarning(final W w,
            final String warningPattern, final Object... warningArguments) {
        if (logger.isEnabledFor(Level.WARN)) {
            logger.warn(Log4JHelper.renderAndFormat(logger, warningPattern,
                    warningArguments), w);
        }
        return w;
    }

    /**
     * Log a stack trace id at a given level.
     * 
     * @param level
     *            An apache logging <code>Level</code>.
     * @param stackPattern
     *            A pattern to use for the
     *            <ol>
     *            <li>Class name
     *            <li>Method name
     *            <li>File name
     *            <li>Line number
     *            </ol>
     *            of the stack trace element.
     * @param stackTraceElement
     *            A <code>StackTraceElement</code>.
     */
    private final void logStackId(final Level level, final String stackPattern,
            final Integer stackIndex, final StackTraceElement stackElement) {
        if (null == stackElement) {
            logger.log(level, "No stack available.");
        } else {
            logger.log(level, Log4JHelper.renderAndFormat(logger, stackPattern,
                    stackIndex, stackElement.getClassName(),
                    stackElement.getMethodName(), stackElement.getFileName(),
                    stackElement.getLineNumber()));
        }
    }
    /**
     * Log a stack trace id at a given level.
     * 
     * @param level
     *            An apache logging <code>Level</code>.
     * @param stackPattern
     *            A pattern to use for the
     *            <ol>
     *            <li>Class name
     *            <li>Method name
     *            <li>File name
     *            <li>Line number
     *            </ol>
     *            of the stack trace element.
     * @param stackTraceElement
     *            A <code>StackTraceElement</code>.
     */
    private final void logStackId(final Level level, final String stackPattern,
            final StackTraceElement stackElement) {
        if (null == stackElement) {
            logger.log(level, "No stack available.");
        } else {
            logger.log(level, Log4JHelper.renderAndFormat(logger, stackPattern,
                    stackElement.getClassName(), stackElement.getMethodName(),
                    stackElement.getFileName(), stackElement.getLineNumber()));
        }
    }
}
