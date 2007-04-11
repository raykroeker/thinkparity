/*
 * Created On: Thu Sep 28 09:24:29 PDT 2006
 */
package com.thinkparity.ophelia.model.util.xmpp;

import java.util.Calendar;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;
import com.thinkparity.ophelia.model.io.xmpp.XMPPMethod;
import com.thinkparity.ophelia.model.util.ProcessMonitor;
import com.thinkparity.ophelia.model.util.xmpp.event.SystemListener;
import com.thinkparity.ophelia.model.util.xmpp.monitor.ProcessEventQueue;

final class XMPPSystem extends AbstractXMPP<SystemListener> {

    /** The event queue lock. */
    private final Object queueLock;

    /**
     * Create XMPPSystem.
     *
     * @param core
     *      The <code>XMPPCore</code> interface.
     */
    XMPPSystem(final XMPPCore core) {
        super(core);
        this.queueLock = new Object();
    }

    /**
     * Process the remote event queue. Read the events; notify the local event
     * handlers then delete the remote event.
     * 
     * @param monitor
     *            A <code>ProcessMonitor</code>.
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    void processEventQueue(final ProcessMonitor monitor, final JabberId userId) {
        logger.logApiId();
        logger.logVariable("userId", userId);
        synchronized (queueLock) {
            final XMPPMethod readQueueEvents = xmppCore.createMethod("system:readqueueevents");
            readQueueEvents.setParameter("userId", userId);
            final List<XMPPEvent> queue =
                execute(readQueueEvents, Boolean.TRUE).readResultEvents("events");
            notifyDetermine(monitor, queue.size());
            for (final XMPPEvent event : queue) {
                notifyStepBegin(monitor, ProcessEventQueue.HANDLE_EVENT, event);
                xmppCore.handleEvent(event);
                deleteQueueEvent(userId, event.getId());
                notifyStepEnd(monitor, ProcessEventQueue.HANDLE_EVENT);
            }
        }
    }

    /**
     * Read the remote date/time.
     * 
     * @return A <code>Calendar</code>.
     */
    Calendar readDateTime(final JabberId userId) {
        assertIsAuthenticatedUser(userId);
        final XMPPMethod remoteDateTime = xmppCore.createMethod("system:readdatetime");
        remoteDateTime.setParameter("userId", userId);
        return execute(remoteDateTime).readResultCalendar("datetime");
    }

    /**
     * Obtain the size of the event queue.
     * 
     * @return The size of the event queue.
     */
    Integer readEventQueueSize(final JabberId userId) {
        final XMPPMethod readQueueEvents = xmppCore.createMethod("system:readqueueevents");
        readQueueEvents.setParameter("userId", userId);
        return execute(readQueueEvents, Boolean.TRUE).readResultEvents("events").size();
    }

    /**
     * Read the thinkParity remote version.
     *
     * @return The version.
     */
    String readVersion() {
        logger.logApiId();
        final XMPPMethod readVersion = xmppCore.createMethod("system:readversion");
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
        final XMPPMethod deleteEvent = xmppCore.createMethod("system:deletequeueevent");
        deleteEvent.setParameter("userId", userId);
        deleteEvent.setParameter("eventId", eventId);
        execute(deleteEvent);
    }
}
