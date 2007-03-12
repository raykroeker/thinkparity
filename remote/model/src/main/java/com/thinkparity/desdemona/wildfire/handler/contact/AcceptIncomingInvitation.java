/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.IncomingInvitation;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Accept Incoming Invitation Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class AcceptIncomingInvitation extends AbstractHandler {

	/**
     * Create AcceptIncomingInvitation.
     *
	 */
	public AcceptIncomingInvitation() {
        super("contact:acceptincominginvitation");
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
        acceptIncomingInvitation(provider, reader.readJabberId("userId"),
                reader.readIncomingInvitation("invitation"),
                reader.readCalendar("acceptedOn"));
    }

    private void acceptIncomingInvitation(final ServiceModelProvider context,
            final JabberId userId, final IncomingInvitation invitation,
            final Calendar acceptedOn) {
        context.getContactModel().acceptIncomingInvitation(userId, invitation,
                acceptedOn);
    }
}
