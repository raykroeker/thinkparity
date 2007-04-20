/*
 * Created On: Aug 29, 2006 9:11:55 AM
 */
package com.thinkparity.desdemona.wildfire.handler.profile;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.session.TemporaryCredentials;

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
public final class UpdateForgottenPassword extends AbstractHandler {

    /**
     * Create ResetPassword.
     *
     */
    public UpdateForgottenPassword() {
        super("profile:updateforgottenpassword");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        updatePassword(provider, reader.readJabberId("userId"),
                reader.readTemporaryCredentials("credentials"),
                reader.readString("newPassword"));            
    }

    private void updatePassword(final ServiceModelProvider provider,
            final JabberId userId, final TemporaryCredentials credentials,
            final String newPassword) {
        provider.getProfileModel().updatePassword(userId, credentials, newPassword);
    }
}
