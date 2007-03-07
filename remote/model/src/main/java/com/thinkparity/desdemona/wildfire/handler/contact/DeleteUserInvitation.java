/*
 * Created On: Aug 29, 2006 3:00:18 PM
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
public final class DeleteUserInvitation extends AbstractHandler {

    /**
     * Create DeleteUserInvitation.
     *
     */
    public DeleteUserInvitation() { super("contact:deleteuserinvitation"); }

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
        deleteUserInvitation(provider, reader.readJabberId("userId"),
                reader.readJabberId("invitedAs"),
                reader.readCalendar("deletedOn"));
    }

    /**
     * @see ContactModel#deleteInvitation(JabberId, EMail, Calendar)
     */
    private void deleteUserInvitation(final ServiceModelProvider context,
            final JabberId userId, final JabberId invitedAs,
            final Calendar deletedOn) {
        context.getContactModel().deleteUserInvitation(userId, invitedAs,
                deletedOn);
    }
}
