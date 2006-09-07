/*
 * Created On: Aug 21, 2006 8:54:27 AM
 */
package com.thinkparity.model.xmpp;

import java.text.MessageFormat;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.Assertion;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JHelper;

import com.thinkparity.model.parity.model.io.xmpp.XMPPMethod;
import com.thinkparity.model.parity.model.io.xmpp.XMPPMethodResponse;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class AbstractXMPP {

    /** The xmpp core functionality. */
    protected final XMPPCore xmppCore;

    /** An apache logger. */
    private final Logger logger;

    /** Create AbstractXMPP. */
    protected AbstractXMPP(final XMPPCore xmppCore) {
        super();
        this.logger = Logger.getLogger(getClass());
        this.xmppCore = xmppCore;
    }

    /**
     * @param response
     */
    protected void assertContainsResult(final XMPPMethodResponse response) {
        xmppCore.assertContainsResult("XMPP RESPONSE IS EMPTY", response);
    }

    /**
     * Assert that the user id matched that of the authenticated user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @see #isAuthenticatedUser(JabberId)
     */
    protected void assertIsAuthenticatedUser(final JabberId userId) {
        Assert.assertTrue("USER DOES NOT MATCH AUTHENTICATED USER",
                isAuthenticatedUser(userId));
    }

    /**
     * Execute an xmpp method.
     * 
     * @param method
     *            An xmpp method.
     * @return An xmpp method response.
     */
    protected XMPPMethodResponse execute(final XMPPMethod method) {
        return execute(method, Boolean.FALSE);
    }

    /**
     * Execute an xmpp method.
     * 
     * @param method
     *            An xmpp method.
     * @param expectedResponse
     *            A <code>Boolean</code> flag indicating an expected response.
     * @return An xmpp method response.
     */
    protected XMPPMethodResponse execute(final XMPPMethod method,
            final Boolean expectedResponse) {
        final XMPPMethodResponse response = method.execute(xmppCore.getConnection());
        if (expectedResponse) {
            assertContainsResult(response);
        }
        return response;
    }

    /**
     * Determine if the user id is the authenticated user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True if the user id matches the currently authenticated user.
     */
    protected Boolean isAuthenticatedUser(final JabberId userId) {
        return xmppCore.getJabberId().equals(userId);
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
