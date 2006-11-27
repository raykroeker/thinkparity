/*
 * Created On: Thu Sep 28 09:24:29 PDT 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.util.xmpp.event.SystemListener;

final class XMPPSystem extends AbstractXMPP<SystemListener> {

    /** The event queue lock. */
    private final Object queueLock;

    /** The event queue size. */
    private Integer queueSize;

    /**
     * Create XMPPSystem.
     *
     * @param core
     *      The <code>XMPPCore</code> interface.
     */
    XMPPSystem(final XMPPCore core) {
        super(core);
        this.queueLock = new Object();
        this.queueSize = 0;
    }

    /**
     * Obtain the size of the event queue.
     * 
     * @return The size of the event queue.
     */
    Integer getQueueSize() {
        return queueSize;
    }

    /**
     * Process the remote event queue. Read the events; notify the local event
     * handlers then delete the remote event.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    void processEventQueue(final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        logger.logVariable("queueSize", queueSize);
        synchronized (queueLock) {
            final XMPPMethod processOfflineQueue = new XMPPMethod("system:readqueueevents");
            processOfflineQueue.setParameter("userId", userId);
            final List<XMPPEvent> queue =
                execute(processOfflineQueue, Boolean.TRUE).readResultEvents("events");
            queueSize = queue.size();
            logger.logVariable("queueSize", queueSize);
            for (final XMPPEvent queueEvent : queue) {
                xmppCore.handleEvent(queueEvent);
                deleteQueueEvent(userId, queueEvent.getId());
                logger.logVariable("queueSize", --queueSize);
            }
        }
    }

    /**
     * Read the thinkParity remote version.
     *
     * @return The version.
     */
    String readVersion() {
        logger.logApiId();
        final XMPPMethod readVersion = new XMPPMethod("system:readversion");
        return execute(readVersion, Boolean.TRUE).readResultString("version");
    }

    /**
     * Delete an xmpp event.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param eventId
     *            An event id <code>String</code>.
     */
    private void deleteQueueEvent(final JabberId userId, final String eventId) {
        final XMPPMethod deleteEvent = new XMPPMethod("system:deletequeueevent");
        deleteEvent.setParameter("userId", userId);
        deleteEvent.setParameter("eventId", eventId);
        execute(deleteEvent);
    }
}
