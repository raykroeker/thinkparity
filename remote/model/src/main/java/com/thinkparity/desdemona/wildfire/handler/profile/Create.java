/*
 * Created On:  30-Mar-07 8:57:26 AM
 */
package com.thinkparity.desdemona.wildfire.handler.profile;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.profile.EMailReservation;
import com.thinkparity.codebase.model.profile.Profile;
import com.thinkparity.codebase.model.profile.UsernameReservation;
import com.thinkparity.codebase.model.session.Credentials;

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
public final class Create extends AuthenticatedHandler {

    /**
     * Create Create.
     *
     */
    public Create() {
        super("profile:create");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        create(provider, reader.readJabberId("userId"),
                reader.readUsernameReservation("usernameReservation"),
                reader.readEMailReservation("emailReservation"),
                reader.readCredentials("credentials"),
                reader.readProfile("profile"), reader.readEMail("email"),
                reader.readString("securityQuestion"),
                reader.readString("securityAnswer"));
    }

    private void create(final ServiceModelProvider provider,
            final JabberId userId,
            final UsernameReservation usernameReservation,
            final EMailReservation emailReservation,
            final Credentials credentials, final Profile profile,
            final EMail email, final String securityQuestion,
            final String securityAnswer) {
        provider.getProfileModel().create(userId, usernameReservation,
                emailReservation, credentials, profile, email,
                securityQuestion, securityAnswer);
    }
}
