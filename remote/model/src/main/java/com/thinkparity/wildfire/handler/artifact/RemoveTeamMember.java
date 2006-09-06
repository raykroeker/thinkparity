/*
 * Created On: Aug 2, 2006 1:19:39 PM
 */
package com.thinkparity.wildfire.handler.artifact;

import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.Constants.Xml;

import com.thinkparity.wildfire.handler.AbstractHandler;

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
