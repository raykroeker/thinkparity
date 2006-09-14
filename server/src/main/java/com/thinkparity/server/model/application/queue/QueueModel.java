/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.model.queue;

import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.desdemona.model.AbstractModel;
import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.session.Session;


/**
 * The queue model is used to persistantly store text for jabber ids. The text
 * is limitless; however large items will degrade the queue performance.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
public class QueueModel extends AbstractModel {

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
	 * Queue model implementation.
	 */
	private final QueueModelImpl impl;

	/**
	 * Queue model synchronization lock.
	 */
	private final Object implLock;

	/**
	 * Create a QueueModel.
	 */
	private QueueModel(final Session session) {
		super();
		this.impl = new QueueModelImpl(session);
		this.implLock = new Object();
	}

	/**
	 * Add a message to the parity queue for the given jabber id.
	 * 
	 * @param jid
	 *            The jabber user to enqueue the message for.
	 * @param iq
	 *            The query to add.
	 * @return The created queue item.
	 * @throws ParityServerModelException
	 */
	public QueueItem enqueue(final JID jid, final IQ iq) {
		synchronized(implLock) { return impl.enqueue(jid, iq); }
	}

	/**
	 * Process all pending queue items for the given session.
	 * 
	 * @throws ParityServerModelException
	 */
	public void processOfflineQueue() throws ParityServerModelException {
		synchronized(implLock) { impl.processOfflineQueue(); }
	}
}
