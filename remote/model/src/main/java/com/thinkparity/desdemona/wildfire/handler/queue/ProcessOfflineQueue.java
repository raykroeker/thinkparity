/*
 * Feb 20, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.queue;

import org.jivesoftware.messenger.auth.UnauthorizedException;
import org.xmpp.packet.IQ;


import com.thinkparity.desdemona.model.ParityServerModelException;
import com.thinkparity.desdemona.model.queue.QueueModel;
import com.thinkparity.desdemona.model.session.Session;
import com.thinkparity.desdemona.wildfire.handler.IQAction;
import com.thinkparity.desdemona.wildfire.handler.IQHandler;

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
	 * @see com.thinkparity.desdemona.wildfire.handler.IQHandler#handleIQ(org.xmpp.packet.IQ, com.thinkparity.desdemona.model.session.Session)
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
