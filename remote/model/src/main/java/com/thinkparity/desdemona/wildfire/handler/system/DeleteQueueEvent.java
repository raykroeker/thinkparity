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
public class DeleteQueueEvent extends AbstractHandler {

	/**
	 * Create a DeleteQueueEvent.
	 * 
	 */
	public DeleteQueueEvent() { super("system:deletequeueevent"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        deleteEvent(readJabberId("userId"), readString("eventId"));
    }

    private void deleteEvent(final JabberId userId, final String eventId) {
        getQueueModel().deleteEvent(userId, eventId);
    }
}
