/*
 * Dec 1, 2005
 */
package com.thinkparity.desdemona.wildfire.handler.artifact;

import java.util.Calendar;
import java.util.UUID;

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
public final class CreateArtifact extends AbstractHandler {

    /**
     * Create CreateArtifact.
     *
     */
	public CreateArtifact() {
        super("artifact:create"); 
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
        create(provider, reader.readJabberId("userId"),
                reader.readUUID("uniqueId"), reader.readCalendar("createdOn"));
    }

    /**
     * Create an artifact.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            An artifact unique id <code>UUID</code>.
     */
    private void create(final ServiceModelProvider context,
            final JabberId userId, final UUID uniqueId, final Calendar createdOn) {
        context.getArtifactModel().create(userId, uniqueId, createdOn);
    }
}
