/*
 * Mar 1, 2006
 */
package com.thinkparity.desdemona.wildfire.handler.artifact;

import java.util.List;
import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.desdemona.wildfire.handler.AbstractHandler;

/**
 * Read the team member user ids.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class ReadTeamIds extends AbstractHandler {

	/**
	 * Create ReadTeamIds.
	 * 
	 */
	public ReadTeamIds() { super("artifact:readteamids"); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        final List<JabberId> teamIds =
            logVariable("teamIds", readTeamIds(readUUID("uniqueId")));
        writeJabberIds("teamIds", "teamIds", teamIds);
    }

    /**
     * Read the artifact team.
     * 
     * @param uniqueId
     *            The artifact unique id.
     * @return A list of the artifact team member ids <code>JabberId</code>.
     */
    private List<JabberId> readTeamIds(final UUID uniqueId) {
        return getArtifactModel().readTeamIds(uniqueId);
    }
}
