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
 * <b>Title:</b><br>
 * <b>Description:</b><br>
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class DeclineIncomingInvitation extends AbstractHandler {

    /**
     * Create DeclineIncomingInvitation.
     *
     */
	public DeclineIncomingInvitation() {
        super("contact:declineincominginvitation");
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
        declineIncomingInvitation(provider, reader.readJabberId("userId"),
                reader.readIncomingInvitation("invitation"),
                reader.readCalendar("declinedOn"));
    }

    private void declineIncomingInvitation(final ServiceModelProvider context,
            final JabberId userId, final IncomingInvitation invitation,
            final Calendar declinedOn) {
        context.getContactModel().declineIncomingInvitation(userId, invitation,
                declinedOn);
    }
}
