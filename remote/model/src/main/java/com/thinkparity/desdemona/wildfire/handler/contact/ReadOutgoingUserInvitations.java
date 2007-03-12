/*
 * Created On:  9-Mar-07 2:05:11 PM
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;

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
public final class ReadOutgoingUserInvitations extends AbstractHandler {

    /**
     * Create ReadIncomingInvitations.
     *
     */
    public ReadOutgoingUserInvitations() {
        super("contact:readoutgoinguserinvitations");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        final List<OutgoingUserInvitation> invitations =
            readOutgoingUserInvitations(provider, reader.readJabberId("userId"));
        writer.writeOutgoingUserInvitations("invitations", invitations);
    }

    private List<OutgoingUserInvitation> readOutgoingUserInvitations(
            final ServiceModelProvider provider, final JabberId userId) {
        return provider.getContactModel().readOutgoingUserInvitations(userId);
    }
}
