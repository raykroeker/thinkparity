/*
 * Created On: Aug 14, 2006 10:48:55 AM
 */
package com.thinkparity.codebase.model.contact;

import com.thinkparity.codebase.StringUtil;

import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity OpheliaModel Incoming Contact Invitation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class IncomingUserInvitation extends IncomingInvitation {

    /** An invitation <code>User</code>. */
    private User invitationUser;

    /**
     * Create IncomingUserInvitation.
     * 
     */
    public IncomingUserInvitation() {
        super();
    }

    public User getInvitationUser() {
        return invitationUser;
    }

    public void setInvitationUser(final User invitationUser) {
        this.invitationUser = invitationUser;
    }

    /**
     * @see com.thinkparity.codebase.model.contact.ContactInvitation#toString()
     *
     */
    @Override
    public String toString() {
        return StringUtil.toString(getClass(), "createdBy", createdBy,
                "createdOn", createdOn, "id", id, "extendedBy", extendedBy,
                "invitationUser", invitationUser);
    }
}
