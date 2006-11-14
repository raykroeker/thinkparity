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

    /**
     * Create XMPPSystem.
     *
     * @param core
     *      The <code>XMPPCore</code> interface.
     */
    XMPPSystem(final XMPPCore core) {
        super(core);
    }

    /**
     * @see com.thinkparity.ophelia.model.util.xmpp.AbstractXMPP#addEventHandlers()
     *
     */
    @Override
    protected void registerEventHandlers() {}

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
        final XMPPMethod processOfflineQueue = new XMPPMethod("system:readqueueevents");
        processOfflineQueue.setParameter("userId", userId);
        final List<XMPPEvent> events =
            execute(processOfflineQueue, Boolean.TRUE).readResultEvents("events");
        for (final XMPPEvent event : events) {
            logger.logVariable("event", event);
            notifyHandler(event);
            deleteQueueEvent(userId, event.getId());
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
