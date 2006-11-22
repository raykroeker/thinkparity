/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.model.queue;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.Context;
import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.session.Session;


/**
 * The queue model is used to persistantly store text for jabber ids. The text
 * is limitless; however large items will degrade the queue performance.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class QueueModel extends AbstractModel<QueueModelImpl> {

    /**
     * Obtain an internal thinkParity queue model.
     * 
     * @param context
     *            A thinkParity <code>Context</code>.
     * @param session
     *            A user's <code>Session</code>.
     * @return An <code>InternalQueueModel</code>.
     */
    public static InternalQueueModel getInternalModel(final Context context,
            final Session session) {
        return new InternalQueueModel(context, session);
    }

    /**
	 * Obtain a handle to a queue model.
	 * 
	 * @param session
	 *            The user session.
	 * @return The queue model.
	 */
	public static QueueModel getModel(final Session session) {
		final QueueModel queueModel = new QueueModel(session);
		return queueModel;
	}

	/**
     * Create QueueModel.
     * 
     * @param session
     *            A user's <code>Session</code>.
     */
	protected QueueModel(final Session session) {
		super(new QueueModelImpl(session));
	}

    public void createEvent(final JabberId userId, final JabberId eventUserId,
            final XMPPEvent event) {
        synchronized (getImplLock()) {
            getImpl().createEvent(userId, eventUserId, event);
        }
    }

	public void deleteEvent(final JabberId userId, final String eventId) {
        synchronized (getImplLock()) {
            getImpl().deleteEvent(userId, eventId);
        }
    }

	public List<XMPPEvent> readEvents(final JabberId userId) {
        synchronized (getImplLock()) {
            return getImpl().readEvents(userId);
        }
    }
}
