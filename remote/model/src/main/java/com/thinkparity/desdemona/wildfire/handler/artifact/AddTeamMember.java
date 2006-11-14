/*
 * Created On: Aug 2, 2006 1:19:39 PM
 */
package com.thinkparity.desdemona.wildfire.handler.artifact;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class AddTeamMember extends AbstractHandler {

    /** Create AddTeamMember. */
    public AddTeamMember() { super("artifact:addteammember"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        addTeamMember(readJabberId("userId"), readJabberIds("team",
                "teamMember"), readUUID("uniqueId"), readJabberId("teamMemberId"));
    }

    /**
     * Add a team member for an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A user jabber id.
     */
    private void addTeamMember(final JabberId userId,
            final List<JabberId> team, final UUID uniqueId,
            final JabberId teamMemberId) {
        getArtifactModel().addTeamMember(userId, team, uniqueId, teamMemberId);
    }
}
