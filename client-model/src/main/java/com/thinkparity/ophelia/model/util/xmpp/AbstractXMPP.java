/*
 * Created On: Aug 21, 2006 8:54:27 AM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.ArrayList;
import java.util.List;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.event.EventListener;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.ophelia.model.util.xmpp.event.XMPPEventHandler;

import com.thoughtworks.xstream.XStream;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class AbstractXMPP<T extends EventListener> {

    /** An apache logger. */
    protected static final Log4JWrapper logger;

    static {
        logger = new Log4JWrapper();
    }

    /** The xmpp core functionality. */
    protected final XMPPCore xmppCore;

    /** An xstream xml serializer. */
    protected final XStream xstream;

    /** The xmpp interfact implementation's listeners. */
    private final List<T> listeners;

    /** An <code>XMPPEventManager</code>. */
    private final XMPPEventManager xmppEventManager;

    /** Create AbstractXMPP. */
    protected AbstractXMPP(final XMPPCore xmppCore) {
        super();
        this.listeners = new ArrayList<T>();
        this.xmppCore = xmppCore;
        this.xmppEventManager = XMPPEventManager.getInstance(xmppCore);
        this.xstream = new XStream();
    }

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
                logger.logTraceId();
                return listeners.add(listener);
            }
        }
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
        logger.logApiId();
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
        return xmppCore.execute(method);
    }

    /**
     * Execute an xmpp method.
     * 
     * @param method
     *            An xmpp method.
     * @param assertResponse
     *            A <code>Boolean</code> flag indicating an expected response.
     * @return An xmpp method response.
     */
    protected XMPPMethodResponse execute(final XMPPMethod method,
            final Boolean assertResponse) {
        return xmppCore.execute(method, assertResponse);
    }

    /**
     * Determine if the user id is the authenticated user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return True if the user id matches the currently authenticated user.
     */
    protected Boolean isAuthenticatedUser(final JabberId userId) {
        return xmppCore.getUserId().equals(userId);
    }

    /**
     * Notify the correct event handler for the xmpp remote event.
     * 
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    protected void notifyHandler(final XMPPEvent event) {
        xmppEventManager.notifyHandler(event);
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
                try {
                    notifier.notifyListener(listener);
                } catch (final Throwable t) {
                    logger.logError(t,
                            "Could not handle remote event for listener {0}.",
                            listener);
                }
            }
        }
        logger.logTraceId();
    }

    /**
     * Register an xmpp event handler.
     * 
     * @param <U>
     *            An <code>XMPPEvent</code> type.
     * @param eventClass
     *            The event <code>Class</code>.
     * @param handler
     *            An <code>XMPPEventHandler</code>.
     */
    protected final <U extends XMPPEvent> void registerEventHandler(
            final Class<U> eventClass, final XMPPEventHandler<U> handler) {
        xmppEventManager.registerHandler(eventClass, handler);
    }

    /**
     * Clear all xmpp event handlers.
     *
     */
    protected final void clearEventHandlers() {
        xmppEventManager.clearHandlers();
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
    protected abstract void registerEventHandlers();

    /**
     * Remove an xmpp event listener.
     * 
     * @param listener
     *            An <code>XMPPEventListener</code>.
     */
    protected boolean removeListener(final T listener) {
        logger.logTraceId();
        synchronized (listeners) {
            if (listeners.contains(listener)) {
                logger.logTraceId();
                return listeners.remove(listener);
            } else {
                logger.logTraceId();
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
        return xmppCore.translateError(t);
    }
}
