/*
 * Created On:  Wed May 03 11:08:17 PDT 2006
 * $Id$
 */
package com.thinkparity.model.parity.model.audit.event;

import com.thinkparity.model.parity.model.audit.AuditEventType;
import com.thinkparity.model.xmpp.user.User;

/**
 * The audit event created when a team member is added to a document and
 * confirmed.
 *
 * @author raykroeker@gmail.com
 * @version 1.1
 */
public class AddTeamMemberConfirmEvent extends AuditEvent {

    /** The team member. */
    private User teamMember;

	/** Create AddTeamMemberConfirmEvent. */
	public AddTeamMemberConfirmEvent() {
        super(AuditEventType.ADD_TEAM_MEMBER_CONFIRM);
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
