/*
 * Created On: Aug 29, 2006 9:11:55 AM
 */
package com.thinkparity.desdemona.wildfire.handler.profile;

import com.thinkparity.codebase.jabber.JabberId;

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
public final class ResetPassword extends AbstractHandler {

    /**
     * Create ResetPassword.
     *
     */
    public ResetPassword() {
        super("profile:resetpassword");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        logger.logApiId();
        writer.writeString("password", resetCredentials(provider,
                reader.readJabberId("userId"),
                reader.readString("securityAnswer")));
    }

    /**
     * Reset a user's credentials.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param securityAnswer
     *            A security question answer <code>String</code>.
     */
    private String resetCredentials(final ServiceModelProvider provider,
            final JabberId userId, final String securityAnswer) {
        return provider.getProfileModel().resetPassword(userId, securityAnswer);
    }
}
