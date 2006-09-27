/*
 * Created On: Sep 16, 2006 2:55:45 PM
 */
package com.thinkparity.desdemona.wildfire.handler.archive;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class ReadTeamIds extends AbstractHandler {

    /** Create ReadTeamIds. */
    public ReadTeamIds() {
        super("archive:readteamids");
    }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        final List<JabberId> teamIds = readTeamIds(readJabberId("userId"), readUUID("uniqueId"));
        writeJabberIds("teamIds", "teamIds", teamIds);
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
    private List<JabberId> readTeamIds(final JabberId userId,
            final UUID uniqueId) {
        return getArchiveModel().readTeam(userId, uniqueId);
    }
}
