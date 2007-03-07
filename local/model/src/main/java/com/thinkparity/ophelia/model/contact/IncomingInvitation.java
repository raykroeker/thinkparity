/*
 * Created On: Aug 14, 2006 10:48:55 AM
 */
package com.thinkparity.ophelia.model.contact;

import com.thinkparity.codebase.email.EMail;

import com.thinkparity.codebase.model.user.User;

/**
 * <b>Title:</b>thinkParity OpheliaModel Incoming Contact Invitation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class IncomingInvitation extends ContactInvitation {

    /** The invitation e-mail address. */
    private EMail invitedAs;

    /** A the inviting user. */
    private User invitedBy;

    /** Create IncomingInvitation. */
    public IncomingInvitation() {
        super();
    }

    /**
     * Obtain the invitedAs
     *
     * @return The String.
     */
    public EMail getInvitedAs() {
        return invitedAs;
    }

    /**
     * Obtain the userId
     *
     * @return The Long.
     */
    public User getInvitedBy() {
        return invitedBy;
    }

    /**
     * Determine if the invited as e-mail address is set.
     * 
     * @return True if the invited as e-mail address is set.
     */
    public Boolean isSetInvitedAs() {
        return null != invitedAs;
    }

    /**
     * Set invitedAs.
     *
     * @param invitedAs The String.
     */
    public void setInvitedAs(final EMail invitedAs) {
        this.invitedAs = invitedAs;
    }

    /**
     * Set userId.
     *
     * @param userId The Long.
     */
    public void setInvitedBy(final User invitedBy) {
        this.invitedBy = invitedBy;
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactInvitation#toString()
     */
    @Override
    public String toString() {
        return new StringBuffer(getClass().getName()).append("//")
                .append(id)
                .toString();
    }
}
