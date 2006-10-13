/*
 * Created On: Aug 21, 2006 8:54:27 AM
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.Packet;

import com.thinkparity.codebase.assertion.Assert;
import com.thinkparity.codebase.event.EventListener;
import com.thinkparity.codebase.event.EventNotifier;
import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.log4j.Log4JWrapper;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethodResponse;
import com.thinkparity.ophelia.model.util.smackx.packet.AbstractThinkParityIQ;

import com.thoughtworks.xstream.XStream;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
abstract class AbstractXMPP<T extends EventListener> {

    /** An apache logger. */
    protected final Log4JWrapper logger;

    /** The xmpp core functionality. */
    protected final XMPPCore xmppCore;

    /** An xstream xml serializer. */
    protected final XStream xstream;

    /** The xmpp interfact implementation's listeners. */
    private final List<T> listeners;

    /** Create AbstractXMPP. */
    protected AbstractXMPP(final XMPPCore xmppCore) {
        super();
        this.listeners = new ArrayList<T>();
        this.logger = new Log4JWrapper();
        this.xmppCore = xmppCore;
        this.xstream = new XStream();
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
        xmppCore.addPacketListener(
                new PacketListener() {
                    @SuppressWarnings("unchecked")
                    public void processPacket(final Packet packet) {
                        logger.logInfo("Remote event {0}.", packet.getClass().getName());
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
        return xmppCore.getJabberId().equals(userId);
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
        return xmppCore.translateError(t);
    }

    protected interface XMPPEventHandler<T extends AbstractThinkParityIQ> {
        public void handleEvent(final T query);
    }
}
