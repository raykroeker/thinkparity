/*
 * Created On:  21-Nov-06 7:58:45 AM
 */
package com.thinkparity.desdemona.model.queue;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;

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
