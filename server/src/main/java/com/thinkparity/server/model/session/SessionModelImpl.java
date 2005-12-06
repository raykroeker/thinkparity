/*
 * Dec 2, 2005
 */
package com.thinkparity.server.model.session;

import com.thinkparity.server.model.AbstractModelImpl;
import com.thinkparity.server.model.ParityServerModelException;

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
	void sendQueuedMessages() throws ParityServerModelException {
		
	}
}
