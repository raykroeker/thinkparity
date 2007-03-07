/*
 * Created On: Aug 14, 2006 10:49:04 AM
 */
package com.thinkparity.ophelia.model.contact;

import com.thinkparity.codebase.StringUtil;

import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity OpheliaModel Outgoing User Invitation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class OutgoingUserInvitation extends OutgoingInvitation {

    /** A <code>User</code>. */
    private User user;

    /**
     * Create OutgoingInvitation.
     *
     */
    public OutgoingUserInvitation() {
        super();
    }

    /**
     * Obtain the user.
     * 
     * @return A <code>User</code>.
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the user id.
     * 
     * @param userId
     *            The user id <code>JabberId</code>.
     */
    public void setUser(final User user) {
        this.user = user;
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactInvitation#toString()
     * 
     */
    @Override
    public String toString() {
        return StringUtil.toString(getClass(), "id", id, "user", user);
    }
}
