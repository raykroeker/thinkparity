/*
 * Feb 20, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.system;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReadQueueEvents extends AbstractHandler {

	/**
	 * Create a ReadQueueEvents.
	 * 
	 */
	public ReadQueueEvents() { super("system:readqueueevents"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        writeEvents("events", "event-element", readEvents(readJabberId("userId")));
    }

    /**
     * Process the offline queue of messages for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    private List<XMPPEvent> readEvents(final JabberId userId) {
        return getQueueModel().readEvents(userId);
    }
}
