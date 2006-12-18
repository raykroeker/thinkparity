/*
 * Feb 20, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.system;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.util.xmpp.event.XMPPEvent;

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
public final class ReadQueueEvents extends AbstractHandler {

	/**
	 * Create a ReadQueueEvents.
	 * 
	 */
	public ReadQueueEvents() {
        super("system:readqueueevents");
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
        writer.writeEvents("events", "event-element", readEvents(provider,
                reader.readJabberId("userId")));
    }

    /**
     * Process the offline queue of messages for a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     */
    private List<XMPPEvent> readEvents(final ServiceModelProvider provider,
            final JabberId userId) {
        return provider.getQueueModel().readEvents(userId);
    }
}
