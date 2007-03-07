/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import java.util.Calendar;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.model.contact.ContactModel;
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
public final class DeclineEMailInvitation extends AbstractHandler {

    /**
     * Create DeclineEMailInvitation.
     *
     */
	public DeclineEMailInvitation() {
        super("contact:declineemailinvitation");
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
        declineInvitation(provider, reader.readJabberId("userId"),
                reader.readJabberId("invitedBy"), reader.readEMail("invitedAs"),
                reader.readCalendar("declinedOn"));
    }

    /**
     * @see ContactModel#declineInvitation(JabberId, JabberId, EMail, Calendar)
     * 
     */
    private void declineInvitation(final ServiceModelProvider context,
            final JabberId userId, final JabberId invitedBy,
            final EMail invitedAs, final Calendar declinedOn) {
        context.getContactModel().declineEMailInvitation(userId, invitedBy,
                invitedAs, declinedOn);
    }
}
