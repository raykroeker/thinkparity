/*
 * Apr 5, 2006
 */
package com.thinkparity.ophelia.model.util.smackx.packet.document;

import java.util.LinkedList;
import java.util.List;

import com.thinkparity.codebase.jabber.JabberId;


/**
 * An xmpp internet query result.
 * 
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class IQHandleReactivate extends IQDocument {

    /** The document team. */
    private final List<JabberId> team;

    /** Create IQHandleReactivate. */
    public IQHandleReactivate() {
        super(null);
        this.team = new LinkedList<JabberId>();
    }

    /**
     * Add a team member.
     * 
     * @param jabberId
     *            A jabber id.
     * @return True if the team was modified as a result of calling
     *         addTeamMember.
     */
    public boolean addTeamMember(final JabberId jabberId) {
        return team.add(jabberId);
    }

    /**
     * Clear the team.
     *
     */
    public void clearTeam() { team.clear(); }

    /**
     * @see com.thinkparity.ophelia.model.util.smackx.packet.IQParity#getChildElementXML()
     * 
     */
    public String getChildElementXML() { return null; }

    /**
     * Obtain the team.
     * 
     * @return A list of jabber ids.
     */
    public List<JabberId> getTeam() { return team; }

    /**
     * Remove a team member.
     * 
     * @param jabberId
     *            A jabber id.
     * @return True if the team was modified as a result of calling
     *         removeTeamMember.
     */
    public boolean remoteTeamMember(final JabberId jabberId) {
        return team.remove(jabberId);
    }
}
