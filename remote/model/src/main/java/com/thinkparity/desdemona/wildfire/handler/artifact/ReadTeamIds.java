/*
 * Mar 1, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.artifact;

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
public final class ReadTeamIds extends AbstractHandler {

	/**
	 * Create ReadTeamIds.
	 * 
	 */
	public ReadTeamIds() {
        super("artifact:readteamids");
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
        final List<JabberId> teamIds =
            logger.logVariable("teamIds", readTeamIds(provider,
                    reader.readUUID("uniqueId")));
        writer.writeJabberIds("teamIds", "teamIds", teamIds);
    }

    /**
     * Read the artifact team.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @return A list of the artifact team member ids <code>JabberId</code>.
     */
    private List<JabberId> readTeamIds(final ServiceModelProvider provider,
            final UUID uniqueId) {
        return provider.getArtifactModel().readTeamIds(uniqueId);
    }
}
