/*
 * Feb 20, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.queue;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ProcessOfflineQueue extends AbstractHandler {

	/**
	 * Create a ProcessOfflineQueue.
	 * 
	 */
	public ProcessOfflineQueue() { super("system:processofflinequeue"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        processOfflineQueue(readJabberId("userId"));
    }

    /**
     * Process the offline queue of messages for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    private void processOfflineQueue(final JabberId userId) {
        getQueueModel().processOfflineQueue(userId);
    }
}
