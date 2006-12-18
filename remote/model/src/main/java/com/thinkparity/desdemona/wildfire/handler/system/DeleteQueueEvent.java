/*
 * Feb 20, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.system;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DeleteQueueEvent extends AbstractHandler {

	/**
	 * Create a DeleteQueueEvent.
	 * 
	 */
	public DeleteQueueEvent() {
        super("system:deletequeueevent");
	}

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider,
     *      com.thinkparity.desdemona.util.service.ServiceRequestReader,
     *      com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     * 
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        logger.logApiId();
        deleteEvent(provider, reader.readJabberId("userId"),
                reader.readString("eventId"));
    }

    private void deleteEvent(final ServiceModelProvider context,
            final JabberId userId, final String eventId) {
        context.getQueueModel().deleteEvent(userId, eventId);
    }
}
