/*
 * Feb 20, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.system;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ProcessQueue extends AbstractHandler {

	/**
	 * Create a ProcessOfflineQueue.
	 * 
	 */
	public ProcessQueue() { super("system:processqueue"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        processQueue(readJabberId("userId"));
    }

    /**
     * Process the offline queue of messages for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    private void processQueue(final JabberId userId) {
        getQueueModel().processQueue(userId);
    }
}
