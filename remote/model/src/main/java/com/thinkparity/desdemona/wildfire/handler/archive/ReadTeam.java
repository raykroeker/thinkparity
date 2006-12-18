/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.archive;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.codebase.model.user.TeamMember;

import com.thinkparity.desdemona.util.service.ServiceModelProvider;
import com.thinkparity.desdemona.util.service.ServiceRequestReader;
import com.thinkparity.desdemona.util.service.ServiceResponseWriter;
import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * <b>Title:</b>thinkParity Archive Read Team Handler<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class ReadTeam extends AbstractHandler {

    /**
     * Create ReadTeam.
     * 
     */
    public ReadTeam() {
        super("archive:readteam");
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
        final List<TeamMember> team = readTeam(provider,
                reader.readJabberId("userId"), reader.readUUID("uniqueId"));
        writer.writeTeam("team", "teamMember", team);
    }

    /**
     * Read a list of document versions from the archive.
     * 
     * @param userId
     *            A user id <code>JabberId</code>.
     * @param uniqueId
     *            A container unique id <code>UUID</code>.
     * @return A <code>List&lt;JabberId&gt;</code>.
     */
    private List<TeamMember> readTeam(final ServiceModelProvider provider,
            final JabberId userId, final UUID uniqueId) {
        return provider.getArchiveModel().readTeam(userId, uniqueId);
    }
}
