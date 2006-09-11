/*
 * Created On: Aug 14, 2006 10:48:55 AM
 */
package com.thinkparity.ophelia.model.contact;

import com.thinkparity.codebase.email.EMail;
import com.thinkparity.codebase.jabber.JabberId;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class IncomingInvitation extends ContactInvitation {

    /** The invitation e-mail address. */
    private EMail invitedAs;

    /** A the inviting user. */
    private JabberId invitedBy;

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
    public JabberId getInvitedBy() {
        return invitedBy;
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
    public void setInvitedBy(final JabberId invitedBy) {
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
