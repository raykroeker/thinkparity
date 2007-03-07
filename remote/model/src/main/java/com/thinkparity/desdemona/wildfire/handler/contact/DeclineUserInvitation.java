/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import java.util.Calendar;

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
public final class DeclineUserInvitation extends AbstractHandler {

    /**
     * Create DeclineUserInvitation.
     *
     */
	public DeclineUserInvitation() {
        super("contact:declineuserinvitation");
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
        declineUserInvitation(provider, reader.readJabberId("userId"),
                reader.readJabberId("invitedBy"),
                reader.readCalendar("declinedOn"));
    }

    private void declineUserInvitation(final ServiceModelProvider context,
            final JabberId userId, final JabberId invitedBy,
            final Calendar declinedOn) {
        context.getContactModel().declineUserInvitation(userId, invitedBy,
                declinedOn);
    }
}
