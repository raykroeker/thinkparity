/*
 * Feb 14, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.artifact;

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
public final class ReadKeyHolder extends AbstractHandler {

    /**
     * Create ReadKeyHolder.
     *
     */
	public ReadKeyHolder() {
        super("artifact:readkeyholder");
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
        final JabberId keyHolder = readKeyHolder(provider,
                reader.readJabberId("userId"), reader.readUUID("uniqueId"));
        if (null != keyHolder) {
            writer.writeJabberId("keyHolder", keyHolder);
        }
    }

    /**
     * Read the key holder.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @return A key holder jabber id.
     */
	private JabberId readKeyHolder(final ServiceModelProvider provider,
            final JabberId userId, final UUID uniqueId) {
	    return provider.getArtifactModel().readKeyHolder(userId, uniqueId);
	}
}
