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
public final class AddTeamMember extends AbstractHandler {

    /**
     * Create AddTeamMember.
     *
     */
    public AddTeamMember() {
        super("artifact:addteammember");
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
        addTeamMember(provider, reader.readJabberId("userId"),
                reader.readJabberIds("team", "teamMember"),
                reader.readUUID("uniqueId"), reader.readJabberId("teamMemberId"));
    }

    /**
     * Add a team member for an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A user jabber id.
     */
    private void addTeamMember(final ServiceModelProvider context,
            final JabberId userId, final List<JabberId> team,
            final UUID uniqueId, final JabberId teamMemberId) {
        context.getArtifactModel().addTeamMember(userId, team, uniqueId,
                teamMemberId);
    }
}
