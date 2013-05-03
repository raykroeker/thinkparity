/*
 * Created On: Aug 14, 2006 10:49:04 AM
 */
package com.thinkparity.codebase.model.contact;

import com.thinkparity.common.StringUtil;

import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity OpheliaModel Outgoing User Invitation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class OutgoingUserInvitation extends OutgoingInvitation {

    /** An invitation <code>User</code>. */
    private User invitationUser;

    /**
     * Create OutgoingInvitation.
     *
     */
    public OutgoingUserInvitation() {
        super();
    }

    public User getInvitationUser() {
        return invitationUser;
    }

    public void setInvitationUser(final User invitationUser) {
        this.invitationUser = invitationUser;
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactInvitation#toString()
     * 
     */
    @Override
    public String toString() {
        return StringUtil.toString(getClass(), "createdBy", createdBy,
                "createdOn", createdOn, "id", id, "invitationUser",
                invitationUser);
    }
}
