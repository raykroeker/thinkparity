/*
 * Created On:  9-Mar-07 2:05:11 PM
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;

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
public final class ReadOutgoingEMailInvitations extends AbstractHandler {

    /**
     * Create ReadIncomingInvitations.
     *
     */
    public ReadOutgoingEMailInvitations() {
        super("contact:readoutgoingemailinvitations");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        final List<OutgoingEMailInvitation> invitations =
            readOutgoingEMailInvitations(provider, reader.readJabberId("userId"));
        writer.writeOutgoingEMailInvitations("invitations", invitations);
    }

    private List<OutgoingEMailInvitation> readOutgoingEMailInvitations(
            final ServiceModelProvider provider, final JabberId userId) {
        return provider.getContactModel().readOutgoingEMailInvitations(userId);
    }
}
