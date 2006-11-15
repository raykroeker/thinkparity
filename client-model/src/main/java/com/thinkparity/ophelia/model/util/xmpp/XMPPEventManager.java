/*
 * Created On:  10-Nov-06 2:09:21 PM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.HashMap;
import java.util.Map;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.ophelia.model.util.xmpp.event.XMPPEventHandler;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class XMPPEventManager {

    /** All instances of <code>XMPPEventManager</code>s. */
    private static final Map<XMPPCore, XMPPEventManager> INSTANCES;

    static {
        INSTANCES = new HashMap<XMPPCore, XMPPEventManager>();
    }

    /**
     * Obtain an instance of an xmpp event manager.
     * 
     * @param xmppCore
     *            An <code>XMPPCore</code> reference.
     * @return An <code>XMPPEventManager</code>.
     */
    static XMPPEventManager getInstance(final XMPPCore xmppCore) {
        synchronized (INSTANCES) {
            if (INSTANCES.containsKey(xmppCore)) {
                return INSTANCES.get(xmppCore);
            } else {
                INSTANCES.put(xmppCore, new XMPPEventManager());
                return getInstance(xmppCore);
            }
        }
    }

    /** The <code>XMPPEventHandler</code> registry. */
    private final Map<Class, XMPPEventHandler> registry;

    /** An apache logger wrapper. */
    private final Log4JWrapper logger;

    /**
     * Create XMPPEventManager.
     *
     */
    private XMPPEventManager() {
        super();
        this.registry = new HashMap<Class, XMPPEventHandler>();
        this.logger = new Log4JWrapper();
    }

    /**
     * Notify the handler for the event.
     * 
     * @param <T>
     *            An <code>XMPPEvent</code> type.
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    @SuppressWarnings("unchecked")
    <T extends XMPPEvent> void notifyHandler(final T event) {
        final XMPPEventHandler<T> handler = registry.get(event.getClass());
        if (null == handler) {
            logger.logWarning("No handler has been registered for event {0}.",
                    event.getClass());
        } else {
            handler.handleEvent(event);
        }
    }

    /**
     * Register an event handler.
     * 
     * @param <T>
     *            An <code>XMPPEvent</code>.
     * @param eventHandler
     *            An <code>XMPPEventHandler</code> for <code>E</code>.
     */
    <T extends XMPPEvent> void registerHandler(final Class<T> eventClass,
            final XMPPEventHandler<T> handler) {
        synchronized (registry) {
            Assert.assertNotTrue(registry.containsKey(eventClass),
                    "Event handler for event {0} has already been registered.",
                    eventClass.getName());
            registry.put(eventClass, handler);
        }
    }

    void clearHandlers() {
        synchronized (registry) {
            // TODO check if this is enough to free up all handler memory
            registry.clear();
        }
    }
}
