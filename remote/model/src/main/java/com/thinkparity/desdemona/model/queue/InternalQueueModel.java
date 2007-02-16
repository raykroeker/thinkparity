/*
 * Created On:  21-Nov-06 7:58:45 AM
 */
package com.thinkparity.desdemona.model.queue;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.desdemona.model.session.Session;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class InternalQueueModel extends QueueModel {

    /**
     * Create InternalQueueModel.
     *
     */
    InternalQueueModel(final Context context, final Session session) {
        super(session);
    }

    /**
     * Create an event for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param eventUserId
     *            The event target user id <code>JabberId</code>.
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    public void createEvent(final JabberId userId, final JabberId eventUserId,
            final XMPPEvent event) {
        synchronized (getImplLock()) {
            getImpl().createEvent(userId, eventUserId, event);
        }
    }

    /**
     * Create an event for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param eventUserId
     *            The event target user id <code>JabberId</code>.
     * @param event
     *            An <code>XMPPEvent</code>.
     * @param priority
     *            The event <code>Priority</code>.
     */
    public void createEvent(final JabberId userId, final JabberId eventUserId,
            final XMPPEvent event, final XMPPEvent.Priority priority) {
        synchronized (getImplLock()) {
            getImpl().createEvent(userId, eventUserId, event, priority);
        }
    }

    /**
     * Delete all queue events for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    public void deleteEvents(final JabberId userId) {
        synchronized (getImplLock()) {
            getImpl().deleteEvents(userId);
        }
    }
}
