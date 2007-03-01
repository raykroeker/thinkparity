/*
 * Created On: Jul 19, 2006 2:31:39 PM
 */
package com.thinkparity.desdemona.wildfire.handler.profile;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.profile.Profile;

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
public final class Read extends AbstractHandler {

    /**
     * Create Read.
     *
     */
    public Read() {
        super("profile:read");
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
        final Profile profile =
            logger.logVariable("profile", read(provider, reader.readJabberId("userId")));

        if(null != profile) {
            writer.writeFeatures("features", profile.getFeatures());
            writer.writeJabberId("id", profile.getId());
            writer.writeString("name", profile.getName());
            writer.writeString("organization", profile.getOrganization());
            writer.writeString("title", profile.getTitle());
            writer.writeVCard("vcard", profile.getVCard());
        }
    }

    /**
     * Read the profile.
     * 
     * @param jabberId
     *            A jabber id.
     * @return A profile.
     */
    private Profile read(final ServiceModelProvider context,
            final JabberId jabberId) {
        return context.getProfileModel().read(jabberId);
    }
}
