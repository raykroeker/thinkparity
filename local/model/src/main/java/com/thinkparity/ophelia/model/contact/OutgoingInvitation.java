/*
 * Created On: Aug 14, 2006 10:49:04 AM
 */
package com.thinkparity.ophelia.model.contact;

import com.thinkparity.codebase.email.EMail;


/**
 * @author raymond@thinkparity.com
 * @version 1.1.2.1
 */
public class OutgoingInvitation extends ContactInvitation {

    /** An e-mail address. */
    private EMail email;

    /** Create OutgoingInvitation. */
    public OutgoingInvitation() {
        super();
    }

    /**
     * Obtain the email
     *
     * @return The String.
     */
    public EMail getEmail() {
        return email;
    }

    /**
     * Set email.
     *
     * @param email The String.
     */
    public void setEmail(final EMail email) {
        this.email = email;
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
