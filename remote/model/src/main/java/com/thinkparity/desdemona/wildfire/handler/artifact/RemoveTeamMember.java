/*
 * Created On: Aug 2, 2006 1:19:39 PM
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
public final class RemoveTeamMember extends AbstractHandler {

    /**
     * Create RemoveTeamMember.
     *
     */
    public RemoveTeamMember() {
        super("artifact:removeteammember");
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
        removeTeamMember(provider, reader.readJabberId("userId"),
                reader.readJabberIds("team", "teamMember"),
                reader.readUUID("uniqueId"),
                reader.readJabberId("teamMemberId"));
    }

    /**
     * Remove a user from a team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
    private void removeTeamMember(final ServiceModelProvider provider,
            final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final JabberId teamMemberId) {
        provider.getArtifactModel().removeTeamMember(userId, team, uniqueId,
                teamMemberId);
    }
}
