/*
 * Created On:  9-Mar-07 2:05:11 PM
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.IncomingInvitation;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Read Incoming Invitations Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadIncomingInvitations extends AbstractHandler {

    /**
     * Create ReadIncomingInvitations.
     *
     */
    public ReadIncomingInvitations() {
        super("contact:readincominginvitations");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        final List<IncomingInvitation> invitations =
            readIncomingInvitations(provider, reader.readJabberId("userId"));
        writer.writeIncomingInvitations("invitations", invitations);
    }

    private List<IncomingInvitation> readIncomingInvitations(
            final ServiceModelProvider provider, final JabberId userId) {
        return provider.getContactModel().readIncomingInvitations(userId);
    }
}
