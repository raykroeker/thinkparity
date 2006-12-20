/*
 * Created On: Aug 7, 2006 11:47:07 AM
 */
package com.thinkparity.desdemona.wildfire.handler.container;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class Publish extends AbstractHandler {

    /**
     * Create Publish.
     *
     */
    public Publish() {
        super("container:publish");
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
        publish(provider, reader.readUUID("uniqueId"),
                reader.readLong("versionId"), reader.readString("name"),
                reader.readString("comment"),
                reader.readInteger("artifactCount"),
                reader.readJabberIds("team", "team-element"),
                reader.readJabberId("publishedBy"),
                reader.readJabberIds("publishedTo", "publishedTo"),
                reader.readCalendar("publishedOn"));
    }

    private void publish(final ServiceModelProvider context,
            final UUID uniqueId, final Long versionId, final String name,
            final String comment, final Integer artifactCount,
            final List<JabberId> team, final JabberId publishedBy,
            final List<JabberId> publishedTo, final Calendar publishedOn) {
        context.getContainerModel().publish(uniqueId, versionId, name, comment,
                artifactCount, team, publishedBy, publishedTo, publishedOn);
    }
}
