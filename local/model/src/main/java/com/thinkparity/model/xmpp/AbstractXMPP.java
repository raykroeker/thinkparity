/*
 * Created On: Aug 21, 2006 8:54:27 AM
 */
package com.thinkparity.model.xmpp;

import java.text.MessageFormat;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.assertion.Assertion;
import com.thinkparity.codebase.log4j.Log4JHelper;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class AbstractXMPP {

    /** An apache logger. */
    private final Logger logger;

    /** The xmpp core functionality. */
    protected final XMPPCore xmppCore;

    /** Create AbstractXMPP. */
    protected AbstractXMPP(final XMPPCore xmppCore) {
        super();
        this.logger = Logger.getLogger(getClass());
        this.xmppCore = xmppCore;
    }

    /**
     * Log the api id of the caller.
     *
     */
    protected void logApiId() {
        if (logger.isInfoEnabled()) {
            logger.info(MessageFormat.format("{0}#{1}",
                    StackUtil.getCallerClassName(),
                    StackUtil.getCallerMethodName()));
        }
    }

    /**
     * Debug a variable. Note that only the variable value will be rendered.
     * 
     * @param name
     *            The variable name.
     * @param value
     *            The variable value.
     */
    protected void logVariable(final String name, final Object value) {
        if (logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format("{0}:{1}",
                    name,
                    Log4JHelper.render(logger, value)));
        }
    }

    /**
     * Log a warning message.
     * 
     * @param message A warning message.
     */
    protected void logWarning(final Object message) {
        if (Level.WARN.isGreaterOrEqual(logger.getEffectiveLevel())) {
            logger.warn(Log4JHelper.render(logger, message));
        }
    }

    /**
     * Log a warning with a error.
     * 
     * @param message
     *            A warning message.
     * @param t
     *            An error.
     */
    protected void logWarning(final Object message, final Throwable t) {
        if (Level.WARN.isGreaterOrEqual(logger.getEffectiveLevel())) {
            logger.warn(Log4JHelper.render(logger, message), t);
        }
    }

    /**
     * Translate an error into a runtime exception logging it if thinkParity
     * does not already know about the error.
     * 
     * @param t
     *            An error <code>Throwable</code>
     * @return A <code>RuntimeException</code>
     */
    protected RuntimeException translateError(final Throwable t) {
        if (Assertion.class.isAssignableFrom(t.getClass())) {
            return (Assertion) t;
        }
        else {
            final Object errorId = MessageFormat.format("{0}#{1} - {2}",
                        StackUtil.getCallerClassName(),
                        StackUtil.getCallerMethodName(), t.getLocalizedMessage());
            logger.error(errorId, t);
            return XMPPErrorTranslator.translateUnchecked(xmppCore, errorId, t);
        }
    }
}
