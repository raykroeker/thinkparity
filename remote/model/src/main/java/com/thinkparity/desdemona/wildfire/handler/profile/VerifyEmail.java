/*
 * Created On: Aug 27, 2006 1:29:56 PM
 */
package com.thinkparity.desdemona.wildfire.handler.profile;

import com.thinkparity.codebase.email.EMail;
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
public final class VerifyEmail extends AbstractHandler {

    /**
     * Create VerifyEmail.
     *
     */
    public VerifyEmail() {
        super("profile:verifyemail");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    @Override
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        logger.logApiId();
        verifyEMail(provider, reader.readJabberId("userId"), reader
                .readEMail("email"), reader.readString("key"));
    }

    /**
     * Verify an email in the user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     * @param key
     *            An email verification key <code>String</code>.
     */
    private void verifyEMail(final ServiceModelProvider provider,
            final JabberId userId, final EMail email, final String key) {
        provider.getProfileModel().verifyEmail(userId, email, key);
    }
}
