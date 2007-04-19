/*
 * Created On:  30-Mar-07 8:57:26 AM
 */
package com.thinkparity.desdemona.wildfire.handler.profile;

import java.util.Calendar;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.profile.UsernameReservation;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AuthenticatedHandler;

/**
 * <b>Title:</b>thinkParity DesdemonaModel Create Profile Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class CreateUsernameReservation extends AuthenticatedHandler {

    /**
     * Create CreateReservation.
     *
     */
    public CreateUsernameReservation() {
        super("profile:createusernamereservation");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        writer.write("reservation", createUsernameReservation(provider,
                reader.readJabberId("userId"), reader.readString("username"),
                reader.readCalendar("reservedOn")));
    }

    private UsernameReservation createUsernameReservation(
            final ServiceModelProvider provider, final JabberId userId,
            final String username, final Calendar reservedOn) {
        return provider.getProfileModel().createUsernameReservation(userId,
                username, reservedOn);
    }
}
