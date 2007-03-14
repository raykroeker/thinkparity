/*
 * Created On: Aug 29, 2006 3:00:18 PM
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;

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
public final class DeleteOutgoingEMailInvitation extends AbstractHandler {

    /**
     * Create DeleteOutgoingEMailInvitation.
     *
     */
    public DeleteOutgoingEMailInvitation() {
        super("contact:deleteoutgoingemailinvitation");
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
        deleteInvitation(provider, reader.readJabberId("userId"),
                reader.readOutgoingEMailInvitation("invitation"),
                reader.readCalendar("deletedOn"));
    }

    private void deleteInvitation(
            final ServiceModelProvider context, final JabberId userId,
            final OutgoingEMailInvitation invitation, final Calendar deletedOn) {
        context.getContactModel().deleteInvitation(userId,
                invitation, deletedOn);
    }
}
