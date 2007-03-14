/*
 * Created On: Sep 17, 2006 2:28:00 PM
 */
package com.thinkparity.desdemona.wildfire.handler.backup;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b>thinkParity Backup Is Online Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class IsOnline extends AbstractHandler {

    /**
     * Create Restore.
     *
     */
    public IsOnline() {
        super("backup:isonline");
    }

    /**
     * @see com.thinkparity.desdemona.wildfire.handler.AbstractHandler#service(com.thinkparity.desdemona.util.service.ServiceModelProvider, com.thinkparity.desdemona.util.service.ServiceRequestReader, com.thinkparity.desdemona.util.service.ServiceResponseWriter)
     *
     */
    protected void service(final ServiceModelProvider provider,
            final ServiceRequestReader reader,
            final ServiceResponseWriter writer) {
        writer.writeBoolean("online", isBackupOnline(provider,
                reader.readJabberId("userId")));
    }

    private Boolean isBackupOnline(final ServiceModelProvider provider,
            final JabberId userId) {
        return provider.getBackupModel().isBackupOnline(userId);
    }
}
