/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.OutgoingUserInvitation;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Create Outgoing User Invitation
 * Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CreateOutgoingUserInvitation extends AbstractHandler {

    /**
     * Create CreateOutgoingUserInvitation.
     *
     */
	public CreateOutgoingUserInvitation() {
        super("contact:createoutgoinguserinvitation");
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
        createInvitation(provider, reader.readJabberId("userId"),
                reader.readOutgoingUserInvitation("invitation"));
    }

    private void createInvitation(final ServiceModelProvider context,
            final JabberId userId, final OutgoingUserInvitation invitation) {
        context.getContactModel().createInvitation(userId, invitation);
    }
}
