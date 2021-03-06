/*
 * Created On:  Wed May 03 11:08:17 PDT 2006
 * $Id$
 */
package com.thinkparity.ophelia.model.audit.event;

import com.thinkparity.codebase.model.user.User;

import com.thinkparity.ophelia.model.audit.AuditEventType;

/**
 * The audit event created when a team member is added to a document.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AddTeamMemberEvent extends AuditEvent {

    /** The team member. */
    private User teamMember;

	/** Create AddTeamMemberEvent. */
	public AddTeamMemberEvent() {
        super(AuditEventType.ADD_TEAM_MEMBER);
    }

    /**
     * Obtain the team member.
     *
     * @return The team member.
     */
    public User getTeamMember() { return teamMember; }

    /**
     * Set the team member.
     *
     * @param teamMember
     *      The team member
     */
    public void setTeamMember(final User teamMember) {
        this.teamMember = teamMember;
    }
}
