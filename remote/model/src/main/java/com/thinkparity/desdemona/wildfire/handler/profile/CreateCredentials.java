/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.profile;

import java.util.Calendar;

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
public final class CreateCredentials extends AbstractHandler {

    /**
     * Create CreateToken.
     *
     */
    public CreateCredentials() {
        super("profile:createcredentials");
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
        writer.write("credentials", createCredentials(provider,
                reader.readString("profileKey"),
                reader.readString("securityAnswer"),
                reader.readCalendar("createdOn")));
    }

    private TemporaryCredentials createCredentials(
            final ServiceModelProvider context, final String profileKey,
            final String securityAnswer, final Calendar createdOn) {
        return context.getProfileModel().createCredentials(profileKey,
                securityAnswer, createdOn);
    }
}
