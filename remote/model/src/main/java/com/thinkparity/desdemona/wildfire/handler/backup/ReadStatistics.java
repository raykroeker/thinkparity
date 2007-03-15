/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.backup;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.backup.Statistics;

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
public final class ReadStatistics extends AbstractHandler {

    /**
     * Create ReadStatistics.
     *
     */
    public ReadStatistics() {
        super("backup:readstatistics");
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
        writer.write("statistics", readStatistics(provider,
                reader.readJabberId("userId")));
    }

    private Statistics readStatistics(final ServiceModelProvider context,
            final JabberId userId) {
        return context.getBackupModel().readStatistics(userId);
    }
}
