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
public class RemoveTeamMember extends AbstractHandler {

    /** Create RemoveTeamMember. */
    public RemoveTeamMember() { super("artifact:removeteammember"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        removeTeamMember(readJabberId("userId"),
                readJabberIds("team", "teamMember"), readUUID("uniqueId"),
                readJabberId("teamMemberId"));
    }

    /**
     * Remove a user from a team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
    private void removeTeamMember(final JabberId userId,
            final List<JabberId> team, final UUID uniqueId,
            final JabberId teamMemberId) {
        getArtifactModel().removeTeamMember(userId, team, uniqueId,
                teamMemberId);
    }
}
