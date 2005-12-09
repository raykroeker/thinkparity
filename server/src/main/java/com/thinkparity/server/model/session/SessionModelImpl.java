/*
 * Dec 2, 2005
 */
package com.thinkparity.server.model.session;

import org.dom4j.Document;
import org.dom4j.DocumentException;

import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityErrorTranslator;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.queue.QueueItem;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
class SessionModelImpl extends AbstractModelImpl {

	/**
	 * Create a SessionModelImpl.
	 * 
	 * @param session
	 *            The user session.
	 */
	SessionModelImpl(final Session session) { super(session); }

	/**
	 * Send all queued messages for the user.
	 * 
	 * @throws ParityServerModelException
	 */
	void send(final QueueItem queueItem) throws ParityServerModelException {
		logger.info("send(QueueItem)");
		logger.debug(queueItem);
		try {
			final Document messageDocument = read(queueItem.getQueueMessage());
			// TODO:  Need to figure out how to reconstruct an IQ from xml.
//			final IQ iq = new IQ(messageDocument);
//			route(queueItem);
		}
		catch(DocumentException dx) {
			logger.error("send(QueueItem)", dx);
			throw ParityErrorTranslator.translate(dx);
		}
		catch(RuntimeException rx) {
			logger.error("send(QueueItem)", rx);
			throw ParityErrorTranslator.translate(rx);
		}
	}
}
