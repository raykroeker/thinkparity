/*
 * Created On:  14-Nov-06 8:45:35 PM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.ophelia.model.util.xmpp.event.XMPPEventListener;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
final class XMPPEventDispatcher {

    /**
     * A list of <code>XMPPEventListener</code>s organized by the event's
     * class.
     */
    private final Map<Class, List<? extends XMPPEventListener<? extends XMPPEvent>>> listeners;

    /** An apache logger wrapper. */
    private final Log4JWrapper logger;

    /** The <code>XMPPCore</code> interface. */
    private final XMPPCore xmppCore;

    /**
     * Create XMPPEventDispatcher.
     * 
     * @param xmppCore
     *            The <code>XMPPCore</code>.
     */
    XMPPEventDispatcher(final XMPPCore xmppCore) {
        super();
        this.listeners = new HashMap<Class, List<? extends XMPPEventListener<? extends XMPPEvent>>>();
        this.logger = new Log4JWrapper(getClass());
        this.xmppCore = xmppCore;
    }

    /**
     * Add an xmpp event listener.
     * 
     * @param <T>
     *            An <code>XMPPEvent</code> type.
     * @param eventClass
     *            An event <code>Class</code>.
     * @param listener
     *            An <code>XMPPEventListener</code>.
     * @return True if the list was modified as a result.
     */
    <T extends XMPPEvent> boolean addListener(final Class<T> eventClass,
            final XMPPEventListener<T> listener) {
        synchronized (eventClass) {
            final List<XMPPEventListener<T>> listeners = getListeners(eventClass);
            return listeners.add(listener);
        }
    }

    /**
     * Clear the list of listeners.
     *
     */
    void clearListeners() {
        for (final Class eventClass : listeners.keySet()) {
            synchronized (eventClass) {
                getListeners(eventClass).clear();
            }
        }
    }

    /**
     * Handle an xmpp event.
     * 
     * @param <T>
     *            An <code>XMPPEvent</code> type.
     * @param xmppEvent
     *            An <code>XMPPEvent</code>.
     */
    <T extends XMPPEvent> void handleEvent(final T xmppEvent) {
        logger.logVariable("xmppEvent", xmppEvent);
        synchronized (xmppEvent.getClass()) {
            final List<XMPPEventListener<T>> listeners = getListeners(xmppEvent.getClass());
            final List<XMPPEventListener<T>> copy = new ArrayList<XMPPEventListener<T>>();
            copy.addAll(listeners);
            for (final XMPPEventListener<T> listener : copy) {
                try {
                    listener.handleEvent(xmppEvent);
                } catch (final Throwable t) {
                    try {
                        xmppCore.handleError(t);
                    } catch (final Throwable t2) {
                        logger.logError(t2, "An error occured while handling error {0}.", t);
                    }
                }
            }
        }
    }

    /**
     * Remove an event listener for a given event.
     * 
     * @param <T>
     *            An <code>XMPPEvent</code> type.
     * @param eventClass
     *            An event <code>Class</code>.
     * @param listener
     *            An <code>XMPPEventListener</code>.
     * @return True if the list of listeners was modified as a result.
     */
    <T extends XMPPEvent> boolean removeListener(final Class<T> eventClass,
            final XMPPEventListener<T> listener) {
        synchronized (eventClass) {
            final List<XMPPEventListener<T>> listeners = getListeners(eventClass);
            return listeners.remove(listener);
        }
    }

    /**
     * Grab a list of event listeners for an event class.
     * 
     * @param <T>
     *            An <code>XMPPEvent</code> type.
     * @param eventClass
     *            An event <code>Class</code>.
     * @return A list of <code>XMPPEventListener</code>s.
     */
    @SuppressWarnings("unchecked")
    private <T extends XMPPEvent> List<XMPPEventListener<T>> getListeners(
            final Class eventClass) {
        List<XMPPEventListener<T>> listeners =
            (List<XMPPEventListener<T>>) this.listeners.get(eventClass);
        if (null == listeners) {
            listeners = new ArrayList<XMPPEventListener<T>>();
        }
        this.listeners.put(eventClass, listeners);
        return listeners;
    }
}
