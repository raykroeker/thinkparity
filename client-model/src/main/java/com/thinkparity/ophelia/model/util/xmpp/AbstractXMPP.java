/*
 * Created On: Aug 21, 2006 8:54:27 AM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;

import com.thinkparity.codebase.StackUtil;
import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.assertion.Assertion;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JHelper;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.ophelia.model.util.EventListener;
import com.thinkparity.ophelia.model.util.EventNotifier;
import com.thinkparity.ophelia.model.util.smackx.packet.AbstractThinkParityIQ;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class AbstractXMPP<T extends EventListener> {

    /** The xmpp core functionality. */
    protected final XMPPCore xmppCore;

    /** The xmpp interfact implementation's listeners. */
    private final List<T> listeners;

    /** An apache logger. */
    private final Logger logger;

    /** Create AbstractXMPP. */
    protected AbstractXMPP(final XMPPCore xmppCore) {
        super();
        this.listeners = new ArrayList<T>();
        this.logger = Logger.getLogger(getClass());
        this.xmppCore = xmppCore;
    }

    /**
     * Add an event handler for a remote xmpp event.
     * 
     * @param <U>
     *            The internet query (event) type.
     * @param eventHandler
     *            An event handler.
     * @param queryType
     *            The query type.
     */
    protected <U extends AbstractThinkParityIQ> void addEventHandler(
            final XMPPEventHandler<U> eventHandler,
            final Class<? extends U> queryType) {
        xmppCore.getConnection().addPacketListener(
                new PacketListener() {
                    @SuppressWarnings("unchecked")
                    public void processPacket(final Packet packet) {
                        try {
                            eventHandler.handleEvent((U) packet);
                        } catch (final Throwable t) {
                            throw translateError(t);
                        }
                    }
                },
                new PacketTypeFilter(queryType));
    }

    /**
     * Add event handlers for the xmpp implementation.
     *
     */
    protected void addEventHandlers() {}

    /**
     * Add an xmpp event listener.
     * 
     * @param listener
     *            An <code>XMPPEventListener</code>.
     */
    protected boolean addListener(final T listener) {
        synchronized (listeners) {
            if (listeners.contains(listener)) {
                return false;
            } else {
                return listeners.add(listener);
            }
        }
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
     * Clear all xmpp event listeners.
     *
     */
    protected void clearListeners() {
        synchronized (listeners) {
            listeners.clear();
        }
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
            logger.info(MessageFormat.format("{0} {1}#{2}",
                    xmppCore.getJabberId().getUsername(),
                    StackUtil.getCallerClassName(),
                    StackUtil.getCallerMethodName()));
        }
    }

    protected void logError(final Object message) {
        if (logger.isEnabledFor(Level.ERROR)) {
            logger.error(MessageFormat.format("{0} {1}",
                    xmppCore.getJabberId().getUsername(),
                    Log4JHelper.render(logger, message)));
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
    protected <V> V logVariable(final String name, final V value) {
        if (logger.isDebugEnabled()) {
            logger.debug(MessageFormat.format("{0} {1}:{2}",
                    xmppCore.getJabberId().getUsername(), name,
                    Log4JHelper.render(logger, value)));
        }
        return value;
    }

    /**
     * Log a warning message.
     * 
     * @param message A warning message.
     */
    protected void logWarning(final Object message) {
        if (Level.WARN.isGreaterOrEqual(logger.getEffectiveLevel())) {
            logger.warn(MessageFormat.format("{0} {1}",
                    xmppCore.getJabberId().getUsername(),
                    Log4JHelper.render(logger, message)));
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
            logger.warn(MessageFormat.format("{0} {1}",
                    xmppCore.getJabberId().getUsername(),
                    Log4JHelper.render(logger, message)), t);
        }
    }

    /**
     * Notify all event listeners.
     * 
     * @param notifier
     *            A thinkParity <code>EventNotifier</code>.
     */
    protected void notifyListeners(final EventNotifier<T> notifier) {
        synchronized (listeners) {
            for (final T listener : listeners) {
                notifier.notifyListener(listener);
            }
        }
    }

    /**
     * Remove an xmpp event listener.
     * 
     * @param listener
     *            An <code>XMPPEventListener</code>.
     */
    protected boolean removeListener(final T listener) {
        synchronized (listeners) {
            if (listeners.contains(listener)) {
                return listeners.remove(listener);
            } else {
                return false;
            }
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
        if (XMPPUncheckedException.class.isAssignableFrom(t.getClass())) {
            return (XMPPUncheckedException) t;
        } else if (Assertion.class.isAssignableFrom(t.getClass())) {
            final Object errorId = MessageFormat.format("{0}#{1} - {2}",
                    StackUtil.getCallerClassName(),
                    StackUtil.getCallerMethodName(), t.getLocalizedMessage());
            logger.error(errorId, t);
            return (Assertion) t;
        } else {
            final Object errorId = MessageFormat.format("{0}#{1} - {2}",
                    StackUtil.getCallerClassName(),
                    StackUtil.getCallerMethodName(), t.getLocalizedMessage());
            logger.error(errorId, t);
            return XMPPErrorTranslator.translateUnchecked(xmppCore, errorId, t);
        }
    }

    protected interface XMPPEventHandler<T extends AbstractThinkParityIQ> {
        public void handleEvent(final T query);
    }
}
