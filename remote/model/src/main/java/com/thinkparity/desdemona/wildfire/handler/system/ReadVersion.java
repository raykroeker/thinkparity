/*
 * Created On: Sep 28, 2006 8:41:26 AM
 */
package com.thinkparity.desdemona.wildfire.handler.system;

import java.text.MessageFormat;

import com.thinkparity.desdemona.model.Version;
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
public final class ReadVersion extends AbstractHandler {

    /**
     * Create ReadVersion.
     *
     */
    public ReadVersion() {
        super("system:readversion");
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
        writer.writeString("version", MessageFormat.format("{0} - {1} - {2}",
                Version.getName(), Version.getMode(), Version.getBuildId()));
    }
}
