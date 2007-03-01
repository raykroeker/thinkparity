/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.rules;

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
public final class IsPublishRestricted extends AbstractHandler {

    /**
     * Create IsPublishRestricted.
     *
     */
    public IsPublishRestricted() {
        super("rules:ispublishrestricted");
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
        writer.writeBoolean("isRestricted", isPublishRestricted(provider,
                reader.readJabberId("userId"),
                reader.readJabberId("publishFrom"),
                reader.readJabberId("publishTo")));
    }

    private Boolean isPublishRestricted(final ServiceModelProvider context,
            final JabberId userId, final JabberId publishFrom, final JabberId publishTo) {
        return context.getRulesModel().isPublishRestricted(userId, publishFrom, publishTo);
    }
}
