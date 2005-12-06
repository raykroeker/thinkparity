/*
 * Dec 1, 2005
 */
package com.thinkparity.server.model.queue;

import java.util.Collection;

import org.xmpp.packet.JID;

import com.thinkparity.server.model.AbstractModel;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.session.Session;

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
	 * Dequeue a previously enqueued item from the persistant store.
	 * 
	 * @param queueItem
	 *            The queue item to remove.
	 * @throws ParityServerModelException
	 */
	public void dequeue(final QueueItem queueItem)
			throws ParityServerModelException {
		synchronized(implLock) { impl.dequeue(queueItem); }
	}

	/**
	 * Add a message to the parity queue for the given jabber id.
	 * 
	 * @param jid
	 *            The jid to add the message for.
	 * @param message
	 *            The message to add.
	 * @return The created queue item.
	 * @throws ParityServerModelException
	 */
	public QueueItem enqueue(final JID jid, final String message)
			throws ParityServerModelException {
		synchronized(implLock) { return impl.enqueue(jid, message); }
	}

	/**
	 * Obtain a list of queued items for the jabber id.
	 * 
	 * @param jid
	 *            The jabber id.
	 * @return A list of queued items.
	 * @throws ParityServerModelException
	 */
	public Collection<QueueItem> list(final JID jid)
			throws ParityServerModelException {
		synchronized(implLock) { return impl.list(jid); }
	}
}
