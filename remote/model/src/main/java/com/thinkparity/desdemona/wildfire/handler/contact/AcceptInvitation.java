/*
 * Feb 28, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.contact;

import java.util.Calendar;

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
public final class AcceptInvitation extends AbstractHandler {

	/**
     * Create AcceptInvitation.
     *
	 */
	public AcceptInvitation() {
        super("contact:acceptinvitation");
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
        logger.logApiId();
        acceptInvitation(provider, reader.readJabberId("userId"),
                reader.readJabberId("invitedBy"),
                reader.readCalendar("acceptedOn"));
    }

    /**
     * @see ContactModel#acceptInvitation(JabberId, JabberId, Calendar)
     */
    private void acceptInvitation(final ServiceModelProvider context,
            final JabberId userId, final JabberId invitedBy,
            final Calendar acceptedOn) {
        context.getContactModel().acceptInvitation(userId, invitedBy, acceptedOn);
    }
}
