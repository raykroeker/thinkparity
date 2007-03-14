/*
 * Created On: Aug 14, 2006 10:49:04 AM
 */
package com.thinkparity.codebase.model.contact;

import com.thinkparity.codebase.StringUtil;
import com.thinkparity.codebase.email.EMail;

/**
 * <b>Title:</b>thinkParity OpheliaModel Outgoing Contact EMail Invitation<br>
 * <b>Description:</b><br>
 * 
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public final class OutgoingEMailInvitation extends OutgoingInvitation {

    /** An invitation <code>EMail</code> address. */
    private EMail invitationEMail;

    /**
     * Create OutgoingEMailInvitation.
     *
     */
    public OutgoingEMailInvitation() {
        super();
    }

    public EMail getInvitationEMail() {
        return invitationEMail;
    }

    public void setInvitationEMail(final EMail invitationEMail) {
        this.invitationEMail = invitationEMail;
    }

    /**
     * @see com.thinkparity.ophelia.model.contact.ContactInvitation#toString()
     * 
     */
    @Override
    public String toString() {
        return StringUtil.toString(getClass(), "createdBy", createdBy,
                "createdOn", createdOn, "id", id, "invitationEMail",
                invitationEMail);
    }
}
