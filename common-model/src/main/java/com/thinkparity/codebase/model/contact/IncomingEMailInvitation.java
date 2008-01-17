/*
 * Created On: Aug 14, 2006 10:48:55 AM
 */
package com.thinkparity.codebase.model.contact;

import com.thinkparity.common.StringUtil;

import com.thinkparity.codebase.email.EMail;

/**
 * <b>Title:</b>thinkParity OpheliaModel Incoming Contact Invitation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class IncomingEMailInvitation extends IncomingInvitation {

    /** The invitation <code>EMail</code> address. */
    private EMail invitationEMail;

    /**
     * Create IncomingEMailInvitation.
     *
     */
    public IncomingEMailInvitation() {
        super();
    }

    public EMail getInvitationEMail() {
        return invitationEMail;
    }

    public void setInvitationEMail(final EMail invitationEMail) {
        this.invitationEMail = invitationEMail;
    }

    /**
     * @see com.thinkparity.codebase.model.contact.ContactInvitation#toString()
     *
     */
    @Override
    public String toString() {
        return StringUtil.toString(getClass(), "createdBy", createdBy,
                "createdOn", createdOn, "id", id, "extendedBy", extendedBy,
                "invitationEMail", invitationEMail);
    }
}
