/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.contact.OutgoingEMailInvitation;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Create Outgoing EMail Invitation
 * Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CreateOutgoingEMailInvitation extends AbstractHandler {

    /**
     * Create CreateOutgoingEMailInvitation.
     *
     */
	public CreateOutgoingEMailInvitation() {
        super("contact:createoutgoingemailinvitation");
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
        createOutgoingEMailInvitation(provider, reader.readJabberId("userId"),
                reader.readOutgoingEMailInvitation("invitation"));
    }

    private void createOutgoingEMailInvitation(
            final ServiceModelProvider context, final JabberId userId,
            final OutgoingEMailInvitation invitation) {
        context.getContactModel().createOutgoingEMailInvitation(userId,
                invitation);
    }
}
