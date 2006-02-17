/*
 * Dec 1, 2005
 */
package com.thinkparity.server.model.queue;

import java.sql.SQLException;
import java.util.Collection;

import org.xmpp.packet.IQ;
import org.xmpp.packet.JID;

import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityErrorTranslator;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.io.sql.queue.QueueSql;
import com.thinkparity.server.model.session.Session;

/**
 * The queue model is used to persistantly store text for jabber ids. The text
 * is limitless; however large items will degrade the queue performance.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1.2.1
 */
class QueueModelImpl extends AbstractModelImpl {

	/**
	 * Handle to the parity queue sql interface.
	 */
	private final QueueSql queueSql;

	/**
	 * Create a QueueModelImpl.
	 * 
	 * @param session
	 *            The user session.
	 */
	QueueModelImpl(final Session session) {
		super(session);
		this.queueSql = new QueueSql();
	}

	/**
	 * Dequeue a previously enqueued item from the persistant store.
	 * 
	 * @param queueItem
	 *            The queue item to remove.
	 * @throws ParityServerModelException
	 */
	void dequeue(final QueueItem queueItem) throws ParityServerModelException {
		logger.info("dequeue(QueueItem)");
		logger.debug(queueItem);
		try {
			final Integer queueId = queueItem.getQueueId();
			queueSql.delete(queueId);
		}
		catch(SQLException sqlx) {
			logger.error("dequeue(QueueItem)", sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
		catch(RuntimeException rx) {
			logger.error("dequeue(QueueItem)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Enqueue a message for a jabber id to persistant storage.
	 * 
	 * @param jid
	 *            The message recipient.
	 * @param iq
	 *            The iq message.
	 * @return The enqueued item.
	 * @throws ParityServerModelException
	 */
	QueueItem enqueue(final JID jid, final IQ iq)
			throws ParityServerModelException {
		logger.info("enqueue(JID,String)");
		logger.debug(jid);
		logger.debug(iq);
		try {
			final String username = jid.getNode();
			final String message = iq.toXML();
			final Integer queueId = queueSql.insert(username, message);
			return queueSql.select(queueId);
		}
		catch(SQLException sqlx) {
			logger.error("enqueue(JID,String)", sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
		catch(RuntimeException rx) {
			logger.error("enqueue(JID,String)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}

	/**
	 * Obtain a list of queued items for the jabber id.
	 * 
	 * @return A list of queued items.
	 * @throws ParityServerModelException
	 */
	Collection<QueueItem> list() throws ParityServerModelException {
		logger.info("list(JID)");
		try {
			final String username = session.getJID().getNode();
			return queueSql.select(username);
		}
		catch(SQLException sqlx) {
			logger.error("list(JID)", sqlx);
			throw ParityErrorTranslator.translate(sqlx);
		}
		catch(RuntimeException rx) {
			logger.error("list(JID)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}
}
