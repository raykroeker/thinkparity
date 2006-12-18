/*
 * Created On: Apr 8, 2006
 * $Id$
 */
package com.thinkparity.desdemona.wildfire.handler.artifact;

import java.util.Calendar;
import java.util.List;
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
public final class ConfirmReceipt extends AbstractHandler {

    /**
     * Create ConfirmReceipt.
     *
     */
    public ConfirmReceipt() {
        super("artifact:confirmreceipt");
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
        confirmReceipt(provider, reader.readJabberId("userId"),
                reader.readUUID("uniqueId"), reader.readLong("versionId"),
                reader.readJabberId("publishedBy"), 
                reader.readCalendar("publishedOn"),
                reader.readJabberIds("publishedTo", "publishedToId"),
                reader.readJabberId("receivedBy"),
                reader.readCalendar("receivedOn"));
    }

    private void confirmReceipt(final ServiceModelProvider context,
            final JabberId userId, final UUID uniqueId, final Long versionId,
            final JabberId publishedBy, final Calendar publishedOn,
            final List<JabberId> publishedTo, final JabberId receivedBy,
            final Calendar receivedOn) {
        context.getArtifactModel().confirmReceipt(userId, uniqueId, versionId,
                publishedBy, publishedOn, publishedTo, receivedBy, receivedOn);
    }
}
