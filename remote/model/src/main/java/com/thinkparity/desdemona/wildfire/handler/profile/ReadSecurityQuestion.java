/*
 * Created On: Aug 29, 2006 11:08:50 AM
 */
package com.thinkparity.desdemona.wildfire.handler.profile;

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
public final class ReadSecurityQuestion extends AbstractHandler {

    /**
     * Create ReadSecurityQuestion.
     *
     */
    public ReadSecurityQuestion() {
        super("profile:readsecurityquestion");
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
        writer.writeString("securityQuestion", readSecurityQuestion(provider,
                reader.readString("profileKey")));
    }

    /**
     * Read the security question for the user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A security question <code>String</code>.
     */
    private String readSecurityQuestion(final ServiceModelProvider provider,
            final String profileKey) {
        return provider.getProfileModel().readSecurityQuestion(profileKey);
    }
}
