/*
 * Created On: Aug 30, 2006 1:31:58 PM
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
public class Update extends AbstractHandler {

    /**
     * Create Update.
     *
     */
    public Update() {
        super("profile:update");
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
        update(provider, reader.readJabberId("userId"),
                reader.readString("name"),
                reader.readString("organization"), reader.readString("title"));
    }

    /**
     * Update the user's profile.
     * 
     * @param provider
     *            A <code>ServiceModelProvider</code>.
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param name
     *            A user's name <code>String</code>.
     * @param organization
     *            A user's organization <code>String</code>.
     * @param title
     *            A user's title <code>String</code>.
     */
    private void update(final ServiceModelProvider provider,
            final JabberId userId, final String name,
            final String organization, final String title) {
        provider.getProfileModel().update(userId, name, organization, title);
    }
}
