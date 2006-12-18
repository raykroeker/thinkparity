/*
 * Created On: Sep 8, 2006 4:43:34 PM
 */
package com.thinkparity.desdemona.wildfire.handler.user;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Read extends AbstractHandler {

    /**
     * Create Read.
     *
     */
    public Read() {
        super("user:read");
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
        final User user = read(provider, reader.readJabberId("userId"));
        logger.logVariable("user", user);
        if (null != user) {
            writer.writeJabberId("id", user.getId());
            writer.writeString("name", user.getName());
            if (user.isSetOrganization())
                writer.writeString("organization", user.getOrganization());
            if (user.isSetTitle())
                writer.writeString("title", user.getTitle());
        }
    }

    /**
     * Read a user.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @return A <code>User</code>.
     */
    private User read(final ServiceModelProvider context, final JabberId userId) {
        return context.getUserModel().read(userId);
    }
}
