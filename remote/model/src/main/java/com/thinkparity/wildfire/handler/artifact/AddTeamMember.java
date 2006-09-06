/*
 * Created On: Aug 2, 2006 1:19:39 PM
 */
package com.thinkparity.wildfire.handler.artifact;

import java.util.UUID;

import com.thinkparity.codebase.jabber.JabberId;

import com.thinkparity.model.Constants.Xml.Service;

import com.thinkparity.wildfire.handler.AbstractHandler;

/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class AddTeamMember extends AbstractHandler {

    /** Create AddTeamMember. */
    public AddTeamMember() { super(Service.Artifact.ADD_TEAM_MEMBER); }

    /**
     * @see com.thinkparity.codebase.wildfire.handler.AbstractHandler#service()
     */
    @Override
    public void service() {
        logApiId();
        addTeamMember(readUUID("uniqueId"), readJabberId("jabberId"));
    }

    /**
     * Add a team member for an artifact.
     * 
     * @param uniqueId
     *            An artifact unique id.
     * @param jabberId
     *            A user jabber id.
     */
    private void addTeamMember(final UUID uniqueId, final JabberId jabberId) {
        getArtifactModel().addTeamMember(uniqueId, jabberId);
    }
}
