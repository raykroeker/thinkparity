/*
 * Created On: Jul 19, 2006 2:31:39 PM
 */
package com.thinkparity.desdemona.wildfire.handler.profile;

import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.profile.ProfileEMail;

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
public final class ReadEmails extends AbstractHandler {

    /**
     * Create ReadEmails.
     *
     */
    public ReadEmails() {
        super("profile:reademails");
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
        writer.writeProfileEMails("emails", readEMails(provider, reader
                .readJabberId("userId")));
    }

    /**
     * Read the profile's email addresses.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>List&lt;EMail&gt;</code>.
     */
    private List<ProfileEMail> readEMails(final ServiceModelProvider provider,
            final JabberId userId) {
        return provider.getProfileModel().readEMails(userId);
    }
}
