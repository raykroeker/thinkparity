/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.archive;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;
import com.thinkparity.codebase.model.user.TeamMember;

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
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        final List<TeamMember> team = readTeam(readJabberId("userId"), readUUID("uniqueId"));
        writeTeam("team", "teamMember", team);
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
    private List<TeamMember> readTeam(final JabberId userId,
            final UUID uniqueId) {
        return getArchiveModel().readTeam(userId, uniqueId);
    }
}
