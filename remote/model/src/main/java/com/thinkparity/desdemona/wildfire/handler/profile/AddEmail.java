/*
 * Created On: Aug 26, 2006 10:32:39 AM
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
public final class AddEmail extends AbstractHandler {

    /**
     * Create AddEmail.
     *
     */
    public AddEmail() { super("profile:addemail"); }

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
        addEmail(provider, reader.readJabberId("userId"),
                reader.readEMail("email"));
    }

    /**
     * Add an email to the user's profile.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param email
     *            An <code>EMail</code>.
     */
    private void addEmail(final ServiceModelProvider provider,
            final JabberId userId, final EMail email) {
        provider.getProfileModel().addEmail(userId, email);
    }
}
