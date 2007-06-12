/*
 * Created On:  21-Nov-06 7:58:45 AM
 */
package com.thinkparity.desdemona.model.queue;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

/**
 * <b>Title:</b>thinkParity Desdemona Internal Queue Model<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public interface InternalQueueModel extends QueueModel {

    /**
     * Delete all queue events for the model user.
     * 
     */
    void deleteEvents();

    /**
     * Delete all queue events for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    void deleteEvents(JabberId userId);

    /**
     * Enqueue a default priority event for the model user.
     * 
     * @param event
     *            An <code>XMPPEvent</code>.
     */
    void enqueueEvent(XMPPEvent event);

    /**
     * Enqueue an event for the model user.
     * 
     * @param event
     *            An <code>XMPPEvent</code>.
     * @param priority
     *            The event <code>Priority</code>.
     */
    void enqueueEvent(XMPPEvent event, XMPPEvent.Priority priority);

    /**
     * Flush the queue for the model user.
     *
     */
    void flush();
}
