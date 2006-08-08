/*
 * Feb 20, 2006
 */
package com.thinkparity.server.handler.queue;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;

import com.thinkparity.server.handler.IQAction;
import com.thinkparity.server.handler.IQHandler;
import com.thinkparity.server.model.ParityServerModelException;
import com.thinkparity.server.model.queue.QueueModel;
import com.thinkparity.server.model.session.Session;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ProcessOfflineQueue extends IQHandler {

	/**
	 * Create a ProcessOfflineQueue.
	 * 
	 */
	public ProcessOfflineQueue() { super(IQAction.PROCESSOFFLINEQUEUE); }

	/**
	 * @see com.thinkparity.server.handler.IQHandler#handleIQ(org.xmpp.packet.IQ, com.thinkparity.server.model.session.Session)
	 * 
	 */
	public IQ handleIQ(final IQ iq, final Session session)
			throws ParityServerModelException, UnauthorizedException {
        logApiId();
		final QueueModel queueModel = getQueueModel(session);
		queueModel.processOfflineQueue();
		return createResult(iq);
	}
}
