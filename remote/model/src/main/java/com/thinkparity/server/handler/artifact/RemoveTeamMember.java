/*
 * Created On: Aug 2, 2006 1:19:39 PM
 */
package com.thinkparity.server.handler.artifact;

import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.server.Constants.Xml;
import com.thinkparity.server.handler.AbstractController;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class RemoveTeamMember extends AbstractController {

    /** Create RemoveTeamMember. */
    public RemoveTeamMember() { super("artifact:removeteammember"); }

    /**
     * @see com.thinkparity.codebase.controller.AbstractController#service()
     */
    @Override
    public void service() {
        logApiId();
        removeTeamMember(
                readUUID(Xml.Artifact.UNIQUE_ID),
                readJabberId(Xml.User.JABBER_ID));
    }

    /**
     * Remove a user from a team.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A jabber id.
     */
    private void removeTeamMember(final UUID uniqueId, final JabberId jabberId) {
        getArtifactModel().removeTeamMember(uniqueId, jabberId);
    }
}
